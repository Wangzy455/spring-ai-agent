package com.wzy.springaiagent.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 流式对话与非流式对话接口
 */
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/chat/")
public class ChatController {

    private final ChatClient chatClient;
    private ChatController (ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    /**
     * 非流式对话
     * @param message
     * @return
     */
    @RequestMapping(value = "generate",method = RequestMethod.GET)
    public ChatResponse generate(@RequestParam String message){
        return this.chatClient.prompt()
            .user(message)
            .call()
            .chatResponse();
    }

    /**
     * 流式对话
     * @param message
     * @return
     */
    @RequestMapping(value = "generate_stream",method = RequestMethod.GET)
    public Flux<ChatResponse> generateStream(@RequestParam String message){
        return this.chatClient.prompt()
                .user(message)
                .stream()
                .chatResponse();
    }

}
