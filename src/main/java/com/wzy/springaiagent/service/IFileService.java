package com.wzy.springaiagent.service;

import com.wzy.springaiagent.common.pojo.entity.File;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    List<File> show(String fileType);
    void upload(MultipartFile multipartFile);
    void download(String fileName, HttpServletResponse response);
    boolean remove(String fileName);
}
