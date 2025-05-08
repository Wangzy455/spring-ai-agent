package com.wzy.springaiagent.service;

import com.wzy.springaiagent.common.pojo.entity.FileEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    List<FileEntity> show(String fileType);
    void upload(MultipartFile multipartFile);
    void download(String fileName, HttpServletResponse response);
    void remove(String fileName);
}
