package com.renjie.jesseaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;

@SpringBootTest
@ActiveProfiles("local") // 激活 local 配置文件，加载邮箱配置
public class QQEmailSenderToolTest {

    @Autowired
    private QQEmailSenderTool emailSender; // 注入实例

    @Test
    void sendTextEmail() {
        try {
            emailSender.sendTextEmail(
                    "3209903373@qq.com",  // 替换为目标邮箱
                    "测试邮件",
                    "这是一封通过Java发送的测试邮件。"
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendHtmlEmail() {
        try {
            String html = "<h2>🎉 这是一封HTML测试邮件</h2>" +
                    "<p><b>加粗内容</b>，<a href='https://example.com'>点击链接</a></p>";
            emailSender.sendHtmlEmail("recipient@example.com", "HTML测试", html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}