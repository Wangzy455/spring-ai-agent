package com.wzy.springaiagent.controller;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.wzy.springaiagent.common.constants.Response;
import com.wzy.springaiagent.fcalling.search.DynamicSqlService;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过自然语言进行查询
 */
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/search/")
@Slf4j
public class SearchController {

    @Resource
    private DynamicSqlService dynamicSqlService;

    private final ChatClient chatClient;

    public SearchController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * 使用自然语言进行查询
     * @param message
     * @return
     */
    @RequestMapping(value = "query",method = RequestMethod.GET)
    public Response<String> search(@RequestParam String message) {
        // 返回查询的json
        List<Map<String, Object>> maps = dynamicSqlService.generateAndExecute(message);
        log.info("返回的json:{}",maps);
        String json = new Gson().toJson(maps);

        String prompt = "CREATE TABLE `file_reo`  (\n"
            + "  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',\n"
            + "  `file_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件名',\n"
            + "  `file_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件类型',\n"
            + "  `tag` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签(文本，图片，视频，压缩文件，回收站)',\n"
            + "  `file_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件存储链接',\n"
            + "  `date` date NULL DEFAULT NULL COMMENT '上传日期',\n"
            + "  PRIMARY KEY (`id` DESC) USING BTREE\n"
            + ") ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;"
            + "这个是建表语句，在把json转换成自然语言的时候参考这个建表语句,不要写推测的内容就直接给我返回语言"
            + "比如{image_count=1}，就返回系统中有1张图片";

        String content = chatClient.prompt(prompt)
            .user(json)
            .call()
            .content();

        return Response.<String>builder().code("200").info("查询成功").data(content).build();

    }
}
