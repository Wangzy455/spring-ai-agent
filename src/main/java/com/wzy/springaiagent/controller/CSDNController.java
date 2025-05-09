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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public String pushText(@RequestParam String message) throws IOException {

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
        String cookie = "cf_clearance=EiCcmTE6ROBRZYJBmRxPv1W9En.kXor5.apEdCgfyj8-1716559516-1.0.1.1-sP0d2JKdUimXTHJmUARaGEjaXvRWphjic5NDiH_y7gOcqou0pFrPcnnlo8F2Og275.biPrbo9cx4oOjaCvqzkw; fid=20_45233079488-1723517596226-544035; UN=qq_62127264; c_dl_prid=-; c_dl_rid=1725093239565_768531; c_dl_fref=https://blog.csdn.net/m0_52861000/article/details/125820297; c_dl_fpage=/download/m0_52861000/86237022; c_dl_um=-; __gads=ID=ce8c882bf79555a4:T=1716812220:RT=1734421455:S=ALNI_MYwGruYTDCgVqVoqcJT9249SzDwFA; __gpi=UID=00000e304de686d8:T=1716812220:RT=1734421455:S=ALNI_MZnyD19nhxtBpQ6OZOGyF_mLw8TgQ; __eoi=ID=3ffd8e2d791a453c:T=1733105663:RT=1734421455:S=AA-AfjbhE902VQLtxGzDh6odx8bS; uuid_tt_dd=10_18950653270-1736932106297-393018; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1741185243; _clck=uhd957%7C2%7Cfty%7C0%7C1605; dc_session_id=10_1744005182684.639535; c_pref=default; c_first_ref=default; c_first_page=https%3A//blog.csdn.net/qq_62127264%3Fspm%3D1011.2480.3001.10640; c_dsid=11_1744005183425.000434; c_segment=10; dc_sid=45c93e6e24de9a5e5805b02c971627ac; SESSION=a2da7887-4d82-44ca-8ffc-b3a7b62e6cc0; loginbox_strategy=%7B%22taskId%22%3A317%2C%22abCheckTime%22%3A1744005183881%2C%22version%22%3A%22ExpA%22%2C%22nickName%22%3A%22%E4%B8%80%E5%BF%B5%E8%AF%A4%E5%BF%83%22%2C%22blog-threeH-dialog-expa%22%3A1743130942497%7D; hide_login=1; UserName=qq_62127264; UserInfo=7dfc0a36114e4a319ff49b817c3b40e4; UserToken=7dfc0a36114e4a319ff49b817c3b40e4; UserNick=%E4%B8%80%E5%BF%B5%E8%AF%A4%E5%BF%83; AU=987; BT=1744005232219; p_uid=U010000; c_page_id=default; creativeSetApiNew=%7B%22toolbarImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20230921102607.png%22%2C%22publishSuccessImg%22%3A%22https%3A//img-home.csdnimg.cn/images/20240229024608.png%22%2C%22articleNum%22%3A12%2C%22type%22%3A2%2C%22oldUser%22%3Atrue%2C%22useSeven%22%3Afalse%2C%22oldFullVersion%22%3Atrue%2C%22userName%22%3A%22qq_62127264%22%7D; csdn_newcert_qq_62127264=1; c_ref=https%3A//blog.csdn.net/qq_62127264%3Fspm%3D1011.2480.3001.10640; creative_btn_mp=3; log_Id_pv=4; log_Id_view=65; log_Id_click=6; dc_tos=suc35k";
        Call<ArticleResponseDTO> call = csdnService.saveArticle(request, cookie);
        Response<ArticleResponseDTO> response = call.execute();

        log.info("\r\n测试结果" + JSON.toJSONString(response));

        // 3. 验证结果
        if (response.isSuccessful()) {
            ArticleResponseDTO articleResponseDTO = response.body();
            log.info("发布文章成功 {}", articleResponseDTO);
        }

        return JSON.toJSONString(response);
    }




}
