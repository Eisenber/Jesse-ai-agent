package com.renjie.jesseaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://b0.bdstatic.com/1ae6eb73d9b9325e7c074178048cabba.jpg";
        String fileName = "雷姆.jpg";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
