package com.wzy.springaiagent.fcalling.csdn.domin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFunctionResponse {

    @JsonProperty(required = true, value = "code")
    @JsonPropertyDescription("code")
    private Integer code;

    @JsonProperty(required = true, value = "msg")
    @JsonPropertyDescription("msg")
    private String msg;

    @JsonProperty(value = "url")
    @JsonPropertyDescription("文章URL")
    private String url;

    @JsonProperty(value = "article_id")
    @JsonPropertyDescription("文章ID")
    private Long articleId;

}
