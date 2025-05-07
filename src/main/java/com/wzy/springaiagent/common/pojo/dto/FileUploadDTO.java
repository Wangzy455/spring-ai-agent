package com.wzy.springaiagent.common.pojo.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO {
    // 文件名
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件存储路径
    private String fileUrl;
    // 文件上传日期
    private Date date;

}
