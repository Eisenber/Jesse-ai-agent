package com.renjie.jesseaiagent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（基于内存的向量数据库初始化）
 */
@Configuration
@Slf4j
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;
    @Resource
    private MyKeywordEnricher myKeywordEnricher;
    @Bean
    VectorStore loveAppVectorStore(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel)
                .build();
        // 加载文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        log.info("RAG 文档加载完成，共 {} 个文档", documentList.size());
        // 自主切分
        //List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
        // 关键词增强（可选，启动时较慢，失败自动回退原始文档）
        List<Document> enrichedDocuments = documentList;
        // 暂时跳过关键词增强以加速启动，需要时取消注释
        // try {
        //     enrichedDocuments = myKeywordEnricher.enrichDocuments(documentList);
        //     log.info("RAG 关键词增强完成");
        // } catch (Exception e) {
        //     log.warn("RAG 关键词增强失败: {}", e.getMessage());
        // }
        // 逐条写入向量库（硅基流动 embedding 批量可能不兼容，逐条更稳定）
        int successCount = 0;
        for (Document doc : enrichedDocuments) {
            try {
                simpleVectorStore.add(List.of(doc));
                successCount++;
            } catch (Exception e) {
                log.warn("文档 '{}' 向量化失败，跳过: {}", doc.getId(), e.getMessage());
            }
        }
        log.info("RAG 内存向量库初始化完成，成功写入 {}/{} 条向量", successCount, enrichedDocuments.size());
        return simpleVectorStore;
    }

}
