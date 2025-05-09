package com.wzy.springaiagent.fcalling.search;

import com.wzy.springaiagent.mapper.IDynamicSqlMapper;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 通过大模型生成sql然后执行
 */
@Service
@Slf4j
public class DynamicSqlService {

    @Resource
    private IDynamicSqlMapper IDynamicSqlMapper;

    private final ChatClient chatClient;
    public DynamicSqlService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public List<Map<String, Object>> generateAndExecute(String question) {
        // 1. 通过 Prompt 生成 SQL
        String prompt = "建表语句:DROP TABLE IF EXISTS `file_reo`;\n"
            + "CREATE TABLE `file_reo`  (\n"
            + "  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',\n"
            + "  `file_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件名',\n"
            + "  `file_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件类型',\n"
            + "  `tag` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标签(文本，图片，视频，压缩文件，回收站)',\n"
            + "  `file_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件存储链接',\n"
            + "  `date` date NULL DEFAULT NULL COMMENT '上传日期',\n"
            + "  PRIMARY KEY (`id` DESC) USING BTREE\n"
            + ") ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;\n"
            + "\n"
            + "SET FOREIGN_KEY_CHECKS = 1;"
            + "把这段自然语言转换为 SQL 查询（只允许 SELECT）：\n问题：" + question + "\nSQL:"
            + "参考上面的建表语句和我的示例：我想查询有多少张图片，查询：select * from file_reo where tag = '图片'"
            + "最后只返回sql就可以";
        String content = chatClient.prompt()
            .user(prompt)
            .call()
            .content();

        content = content.replaceAll("(?s)```sql\\s*(.*?)\\s*```", "$1").trim();

        log.info("生成的sql：{}", content);

        // 3. 执行 SQL
        return IDynamicSqlMapper.executeSelect(content);
    }

}
