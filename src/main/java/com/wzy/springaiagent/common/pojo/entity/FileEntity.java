package com.wzy.springaiagent.common.pojo.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件元数据实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    // 主键
    private Integer id;
    // 文件名
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件存储路径
    private String fileUrl;
    // 文件上传日期
    private Date date;
    // 标签
    private String tag;
}
