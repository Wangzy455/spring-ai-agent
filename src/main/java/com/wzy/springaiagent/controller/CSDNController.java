package com.wzy.springaiagent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wzy.springaiagent.fcalling.csdn.infrastructure.client.CSDNServiceClient;
import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.ICSDNService;
import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.dto.ArticleRequestDTO;
import com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.dto.ArticleResponseDTO;
import java.io.IOException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 通过AI发布CSDN文章的接口
 */
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/func/")
@Slf4j
public class CSDNController {

    private final ICSDNService csdnService;

    private final ChatClient chatClient;
    public CSDNController(ChatClient.Builder builder) {
        CSDNServiceClient client = new CSDNServiceClient();
        this.csdnService = client.getCsdnService();
        this.chatClient = builder.build();
    }



    @RequestMapping(value ="csdn",method = RequestMethod.GET)
    public Flux<ChatResponse> pushText(@RequestParam String message) throws IOException {

        String prompt = "请用 JSON 格式生成一篇关于“" + message + "”的简短 CSDN 技术文章，要求：title 不超过 20 字，description 不超过 30 字，正文 markdown 和 html 长度合计不超过 500 字。输出格式如下：" +
            "{\"title\": \"\", \"description\": \"\", \"markdown\": \"\", \"html\": \"\"}";

        String content = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

        log.info("大模型生成的内容:{}",content);
        String json = content.replaceAll("(?s)^```json\\s*|\\s*```$", "");
        JSONObject jsonObject = JSON.parseObject(json);
        // 1. 构建请求对象
        ArticleRequestDTO request = new ArticleRequestDTO();
        request.setTitle(jsonObject.getString("title"));
        request.setMarkdowncontent(jsonObject.getString("markdown"));
        request.setContent(jsonObject.getString("html"));
        request.setReadType("public");
        request.setLevel("0");
        request.setTags("全栈,AI");
        request.setStatus(0);
        request.setCategories("技术");
        request.setType("original");
        request.setOriginal_link("");
        request.setAuthorized_status(true);
        request.setDescription(jsonObject.getString("description"));
        request.setResource_url("");
        request.setNot_auto_saved("0");
        request.setSource("pc_mdeditor");
        request.setCover_images(Collections.emptyList());
        request.setCover_type(0);
        request.setIs_new(1);
        request.setVote_id(0);
        request.setResource_id("");
        request.setPubStatus("draft");
        request.setSync_git_code(0);

        // 2. 调用接口
        String cookie = "";
        Call<ArticleResponseDTO> call = csdnService.saveArticle(request, cookie);
        Response<ArticleResponseDTO> response = call.execute();

        log.info("\r\n测试结果" + JSON.toJSONString(response));

        // 3. 验证结果
        if (response.isSuccessful()) {
            ArticleResponseDTO articleResponseDTO = response.body();
            log.info("发布文章成功 {}", articleResponseDTO);
        }

        ArticleResponseDTO articleResponseDTO = response.body();
        String res = articleResponseDTO.toString();

        return chatClient.prompt("提取url,title和description")
            .user(res)
            .stream()
            .chatResponse();
    }




}
