package com.wzy.springaiagent.fcalling.csdn.infrastructure.gateway.dto;

import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class ArticleRequestDTO {

    private String article_id = "";
    private String title;
    private String description;
    private String content;
    private String tags;
    private String categories = "";
    private String type = "original";
    private Integer status = 0;
    private String read_type = "public";
    private String reason = "";
    private String original_link = "";
    private Boolean authorized_status = false;
    private Boolean check_original = false;
    private String source = "pc_postedit";
    private String not_auto_saved = "1";
    private String creator_activity_id = "";
    private List<String> cover_images = Collections.emptyList();
    private Integer cover_type = 1;
    private Integer vote_id = 0;
    private String resource_id = "";
    private Integer scheduled_time = 0;
    private Integer is_new = 1;
    private Integer sync_git_code = 0;
    private String markdowncontent;
    private String level;
    private String pubStatus;
    private String resource_url;

    // 设置readType方法，实际上是设置read_type字段
    public void setReadType(String readType) {
        this.read_type = readType;
    }

    // 获取readType方法，实际上是获取read_type字段
    public String getReadType() {
        return this.read_type;
    }

}