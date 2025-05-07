package com.wzy.springaiagent.util;


import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

public class JSONTest {
    @Test
    public void test(){
        String json = "{\n"
            + "\t\"result\": {\n"
            + "\t\t\"metadata\": {\n"
            + "\t\t\t\"finishReason\": \"STOP\",\n"
            + "\t\t\t\"contentFilters\": [],\n"
            + "\t\t\t\"empty\": true\n"
            + "\t\t},\n"
            + "\t\t\"output\": {\n"
            + "\t\t\t\"messageType\": \"ASSISTANT\",\n"
            + "\t\t\t\"metadata\": {\n"
            + "\t\t\t\t\"finishReason\": \"STOP\",\n"
            + "\t\t\t\t\"id\": \"0e23e35f-e0de-9edc-9704-2102b9d02d49\",\n"
            + "\t\t\t\t\"role\": \"ASSISTANT\",\n"
            + "\t\t\t\t\"messageType\": \"ASSISTANT\",\n"
            + "\t\t\t\t\"reasoningContent\": \"\"\n"
            + "\t\t\t},\n"
            + "\t\t\t\"toolCalls\": [],\n"
            + "\t\t\t\"media\": [],\n"
            + "\t\t\t\"text\": \"```json\\n{\\n  \\\"english_keywords\\\": [\\n    \\\"Java programming tutorial\\\",\\n    \\\"Java documentation\\\",\\n    \\\"Java best practices\\\"\\n  ],\\n  \\\"chinese_keywords\\\": [\\n    \\\"Java编程教程\\\",\\n    \\\"Java官方文档\\\",\\n    \\\"Java最佳实践\\\"\\n  ]\\n}\\n``` \\n\\n这些关键词可以帮助你更有效地搜索与Java相关的资料。英文关键词涵盖了教程、官方文档和最佳实践，而中文关键词则对应了类似的主题，方便你在中英文资源中进行查找。\"\n"
            + "\t\t}\n"
            + "\t},\n"
            + "\t\"metadata\": {\n"
            + "\t\t\"id\": \"0e23e35f-e0de-9edc-9704-2102b9d02d49\",\n"
            + "\t\t\"model\": \"\",\n"
            + "\t\t\"rateLimit\": {\n"
            + "\t\t\t\"requestsRemaining\": 0,\n"
            + "\t\t\t\"requestsLimit\": 0,\n"
            + "\t\t\t\"tokensReset\": \"PT0S\",\n"
            + "\t\t\t\"tokensRemaining\": 0,\n"
            + "\t\t\t\"requestsReset\": \"PT0S\",\n"
            + "\t\t\t\"tokensLimit\": 0\n"
            + "\t\t},\n"
            + "\t\t\"usage\": {\n"
            + "\t\t\t\"completionTokens\": 0,\n"
            + "\t\t\t\"generationTokens\": 100,\n"
            + "\t\t\t\"nativeUsage\": null,\n"
            + "\t\t\t\"promptTokens\": 27,\n"
            + "\t\t\t\"totalTokens\": 127\n"
            + "\t\t},\n"
            + "\t\t\"promptMetadata\": [],\n"
            + "\t\t\"empty\": true\n"
            + "\t},\n"
            + "\t\"results\": [\n"
            + "\t\t{\n"
            + "\t\t\t\"metadata\": {\n"
            + "\t\t\t\t\"finishReason\": \"STOP\",\n"
            + "\t\t\t\t\"contentFilters\": [],\n"
            + "\t\t\t\t\"empty\": true\n"
            + "\t\t\t},\n"
            + "\t\t\t\"output\": {\n"
            + "\t\t\t\t\"messageType\": \"ASSISTANT\",\n"
            + "\t\t\t\t\"metadata\": {\n"
            + "\t\t\t\t\t\"finishReason\": \"STOP\",\n"
            + "\t\t\t\t\t\"id\": \"0e23e35f-e0de-9edc-9704-2102b9d02d49\",\n"
            + "\t\t\t\t\t\"role\": \"ASSISTANT\",\n"
            + "\t\t\t\t\t\"messageType\": \"ASSISTANT\",\n"
            + "\t\t\t\t\t\"reasoningContent\": \"\"\n"
            + "\t\t\t\t},\n"
            + "\t\t\t\t\"toolCalls\": [],\n"
            + "\t\t\t\t\"media\": [],\n"
            + "\t\t\t\t\"text\": \"```json\\n{\\n  \\\"english_keywords\\\": [\\n    \\\"Java programming tutorial\\\",\\n    \\\"Java documentation\\\",\\n    \\\"Java best practices\\\"\\n  ],\\n  \\\"chinese_keywords\\\": [\\n    \\\"Java编程教程\\\",\\n    \\\"Java官方文档\\\",\\n    \\\"Java最佳实践\\\"\\n  ]\\n}\\n``` \\n\\n这些关键词可以帮助你更有效地搜索与Java相关的资料。英文关键词涵盖了教程、官方文档和最佳实践，而中文关键词则对应了类似的主题，方便你在中英文资源中进行查找。\"\n"
            + "\t\t\t}\n"
            + "\t\t}\n"
            + "\t]\n"
            + "}";

        JSONObject resultJson = JSONObject.parseObject(json);

        JSONObject result = (JSONObject) resultJson.get("result");

        JSONObject output = (JSONObject) result.get("output");

        String text = (String) output.get("text");
        System.out.println(text);

    }



}
