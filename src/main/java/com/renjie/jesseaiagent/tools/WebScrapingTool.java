package com.renjie.jesseaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页抓取工具
 */
public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page and return cleaned text (URLs removed)")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            // 提取纯文本，Jsoup 会自动去除 HTML 标签和链接
            String text = doc.body().text();
            // 额外清理：移除残留的 URL
            text = text.replaceAll("https?://[^\\s]+", "[链接已移除]");
            // 截断过长内容，保留前 3000 字
            if (text.length() > 3000) {
                text = text.substring(0, 3000) + "...(内容已截断)";
            }
            return text;
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
