package com.renjie.jesseaiagent.agent;

import com.renjie.jesseaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ReAct 工具调用代理：think（AI 决定调用哪些工具）→ act（手动执行工具）
 * internalToolExecutionEnabled=false 防止框架自动消费工具调用请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    private final ToolCallback[] availableTools;
    private final ChatModel chatModel;
    private ChatResponse toolCallChatResponse;
    private String lastThoughtText = "";

    public ToolCallAgent(ToolCallback[] availableTools, ChatModel chatModel) {
        super();
        this.availableTools = availableTools;
        this.chatModel = chatModel;
    }

    @Override
    public boolean think() {
        try {
            List<Message> fullMessages = new ArrayList<>();
            if (getSystemPrompt() != null && !getSystemPrompt().isEmpty()) {
                fullMessages.add(new SystemMessage(getSystemPrompt()));
            }
            fullMessages.addAll(getMessageList());
            // nextStepPrompt 只加到本次请求，不持久化到历史（避免重复累积）
            if (getNextStepPrompt() != null && !getNextStepPrompt().isEmpty()) {
                fullMessages.add(new UserMessage(getNextStepPrompt()));
            }

            // 关键：internalToolExecutionEnabled(false) 让工具调用请求原样返回
            Prompt prompt = new Prompt(fullMessages,
                    ToolCallingChatOptions.builder()
                            .internalToolExecutionEnabled(false)
                            .toolCallbacks(List.of(availableTools))
                            .build());

            ChatResponse chatResponse = chatModel.call(prompt);
            this.toolCallChatResponse = chatResponse;
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            String text = assistantMessage.getText();
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();

            log.info(getName() + " 思考: {}", text);
            this.lastThoughtText = assistantMessage.getText();
            log.info(getName() + " 选择了 {} 个工具", toolCallList.size());
            toolCallList.forEach(tc ->
                    log.info("  → {}: {}", tc.name(), tc.arguments()));

            getMessageList().add(assistantMessage);
            return !toolCallList.isEmpty();
        } catch (Exception e) {
            log.error(getName() + " 思考异常: {}", e.getMessage());
            getMessageList().add(new AssistantMessage("思考异常: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (toolCallChatResponse == null || !toolCallChatResponse.hasToolCalls()) {
            return "没有工具调用";
        }
        AssistantMessage assistantMessage = toolCallChatResponse.getResult().getOutput();
        List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
        if (toolCalls.isEmpty()) {
            return "没有工具调用";
        }

        // 手动匹配工具名 → ToolCallback
        var toolMap = Arrays.stream(availableTools)
                .collect(Collectors.toMap(t -> t.getToolDefinition().name(), t -> t, (a, b) -> a));

        List<ToolResponseMessage.ToolResponse> responses = new ArrayList<>();
        for (AssistantMessage.ToolCall tc : toolCalls) {
            try {
                ToolCallback callback = toolMap.get(tc.name());
                if (callback == null) {
                    // 尝试忽略大小写
                    callback = Arrays.stream(availableTools)
                            .filter(t -> t.getToolDefinition().name().equalsIgnoreCase(tc.name()))
                            .findFirst().orElse(null);
                }
                if (callback == null) {
                    log.warn("未找到工具: {}", tc.name());
                    responses.add(new ToolResponseMessage.ToolResponse(
                            tc.id(), tc.name(), "错误: 未找到工具 " + tc.name()));
                } else {
                    String result = callback.call(tc.arguments());
                    // 全局过滤：移除所有工具输出中的 URL 链接
                    result = result.replaceAll("https?://[^\\s]+", "[链接已移除]");
                    log.info("工具 {} 执行完成: {}", tc.name(), result);
                    responses.add(new ToolResponseMessage.ToolResponse(tc.id(), tc.name(), result));
                    if ("doTerminate".equals(callback.getToolDefinition().name())) {
                        setState(AgentState.FINISHED);
                    }
                }
            } catch (Exception e) {
                log.error("工具 {} 执行失败: {}", tc.name(), e.getMessage());
                responses.add(new ToolResponseMessage.ToolResponse(
                        tc.id(), tc.name(), "错误: " + e.getMessage()));
            }
        }

        ToolResponseMessage toolResponseMessage = new ToolResponseMessage(responses, Map.of());
        getMessageList().add(toolResponseMessage);

        return responses.stream()
                .map(r -> "工具 " + r.name() + " → " + r.responseData())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String step() {
        boolean shouldAct = think();
        if (!shouldAct) {
            return lastThoughtText != null && !lastThoughtText.isEmpty()
                    ? lastThoughtText
                    : "思考完成";
        }
        return act();
    }

}
