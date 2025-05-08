package com.wzy.springaiagent.fcalling.csdn.domin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.wzy.springaiagent.fcalling.csdn.types.utils.MarkdownConverter;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFunctionRequest {

    @JsonProperty(required = true, value = "title")
    @JsonPropertyDescription("文章标题")
    private String title;

    @JsonProperty(required = true, value = "markdowncontent")
    @JsonPropertyDescription("文章内容")
    private String markdowncontent;

    @JsonProperty(required = true, value = "tags")
    @JsonPropertyDescription("文章标签，英文逗号隔开")
    private String tags;

    @JsonProperty(required = true, value = "description")
    @JsonPropertyDescription("文章简述")
    private String description;

    public String getContent() {
        return MarkdownConverter.convertToHtml(markdowncontent);
    }

}