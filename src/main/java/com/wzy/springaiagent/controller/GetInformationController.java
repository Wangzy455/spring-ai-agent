package com.wzy.springaiagent.controller;

import com.wzy.springaiagent.service.IWebSearchService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于自动获取信息
 */
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/")
public class GetInformationController {

    @Resource
    private IWebSearchService webSearchService;
    private final ChatClient chatClient;
    public GetInformationController (ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    /**
     * 获取联网信息并存储
     * @param message
     */
    @RequestMapping(value = "getInformation",method = RequestMethod.GET)
    public void getInformation(String message) {

    }
}
