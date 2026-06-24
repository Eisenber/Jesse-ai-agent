package com.renjie.jesseaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
/**
 * 自定义基于阿里云知识库服务的RAG增强顾问
 * （已废弃：DashScope 云知识库已失效，仅在有 dashscope api-key 时启用）
 */
@ConditionalOnProperty(name = "spring.ai.dashscope.api-key")
class LoveAppRagCloudAdvisorConfig {

    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        log.warn("DashScope 云知识库已不可用，loveAppRagCloudAdvisor 不应被创建");
        return null;
    }
}
