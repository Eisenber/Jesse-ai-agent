package com.renjie.jesseaiagent.agent;

import com.renjie.jesseaiagent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class YuManus extends ToolCallAgent {

    public YuManus(ToolCallback[] allTools, ChatModel chatModel) {
        super(allTools, chatModel);
        this.setName("yuManus");
        this.setSystemPrompt("""
                你是 YuManus，一个全能型 AI 助手。用中文回答，主动使用工具完成任务。

                回答规则：
                - 禁止输出任何 http:// https:// www. 开头的链接
                - 引用来源用纯文字注明，如"来源：腾讯云开发者社区"
                """);
        this.setNextStepPrompt("""
                如果用户有明确任务且尚未调用工具：调用合适的工具（如 searchWeb 搜索、scrapeWebPage 抓取网页等）。
                如果工具已返回结果：用中文将结果总结成清晰易读的回答呈现给用户，不要在这一步调用任何工具。
                如果已经完成回答且用户没有新需求：调用 doTerminate 结束。
                注意：绝对不要输出任何 http:// https:// 开头的链接。
                """);
        this.setMaxSteps(20);
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
