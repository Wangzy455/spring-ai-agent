package com.wzy.springaiagent.controller;

import com.wzy.springaiagent.common.constants.Response;
import com.wzy.springaiagent.service.IRagService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 轻量级rag，可以创建提示词仓库进行管理
 * 可以上传，通过Redis和MySQL进行提示词管理
 * 通过标签实现一个轻量的rag
 */
@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping("/api/v1/rag/")
public class RagController {

    @Resource
    private IRagService ragService;

    private ChatClient chatClient;
    private RagController (ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    /**
     * 上传知识库
     * @param ragTag
     * @param file
     * @return
     */
    @RequestMapping(value="upload",method = RequestMethod.POST)
    public Response<String> uploadKnowledge(@RequestParam String ragTag, @RequestParam String file) throws IOException {
        log.info("上传知识库:{}",ragTag);
        ragService.uploadKnowledge(ragTag, file);
        log.info("上传知识库成功");
        return Response.<String>builder().code("200").info("上传知识库成功").build();
    }

    /**
     * 根据知识库进行针对的回答
     * @param ragTag
     * @param message
     * @return
     */
    @RequestMapping(value="chat",method = RequestMethod.GET)
    public Flux<ChatResponse> chatByTemplate(@RequestParam String ragTag,@RequestParam String message)
        throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String prompt = ragService.chooseKnowledge(ragTag);

        // 根据上传的知识库进行回答
        return chatClient.prompt(String.valueOf(prompt))
            .user(message)
            .stream()
            .chatResponse();
    }

    /**
     * 展示所有的知识库
     * @return
     */
    @RequestMapping(value="show",method = RequestMethod.POST)
    public Response<List<String>> showAllKnowledge(){
        List<String> ans = ragService.showAllKnowledge();
        return Response.<List<String>>builder().code("200").info("查询知识库成功").data(ans).build();
    }

    /**
     * 删除对应知识库
     * @param ragTag
     * @return
     */
    @RequestMapping(value="delete",method = RequestMethod.GET)
    public Response<String> deleteKnowledge(@RequestParam String ragTag) throws Exception {
        log.info("删除知识库：{}",ragTag);
        ragService.deleteKnowledge(ragTag);
        return Response.<String>builder().code("200").info("删除知识库成功").build();
    }
}
