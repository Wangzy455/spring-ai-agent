package com.wzy.springaiagent.service.impl;

import com.wzy.springaiagent.service.IWebSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class WebSearchServiceImpl implements IWebSearchService {

    private final ChatClient chatClient;
    private WebSearchServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public void search() {

    }
}
