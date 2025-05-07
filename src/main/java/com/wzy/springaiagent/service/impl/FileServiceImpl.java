package com.wzy.springaiagent.service.impl;

import com.wzy.springaiagent.common.pojo.entity.File;
import com.wzy.springaiagent.config.MinioConfig;
import com.wzy.springaiagent.mapper.IFileMapper;
import com.wzy.springaiagent.service.IFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 根据业务进行进一步的封装
 */
@Service
public class FileServiceImpl implements IFileService {

    @Resource
    private MinioConfig minioConfig;
    @Resource
    private IFileMapper fileDao;


    /**
     * 文件展示 - 获取默认存储桶中的所有文件名
     * 根据不同的文件类型进行展示
     * @return 文件名列表
     */
    @Override
    public List<File> show(String FileType) {
        try {
            List<File> file = new ArrayList<>();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件上传 - 将文件上传到默认存储桶并更新DTO信息
     * @param multipartFile 文件上传
     * @return 更新后的文件上传DTO，包含文件URL
     */
    @Override
    public void upload(MultipartFile multipartFile) {
        try {
           //执行上传到minio的操作
           String fileUrl = minioConfig.putObject(multipartFile);
           //构造存储到mysql的数据
            String fileName = multipartFile.getOriginalFilename();
            //构造文件类型
            int lastDotIndex = multipartFile.getOriginalFilename().lastIndexOf('.');
            String fileType = fileName.substring(lastDotIndex + 1);

            File file = File.builder()
                .fileName(fileName)
                .fileType(fileType)
                .fileUrl(fileUrl)
                .build();
            // 存储到mysql
            fileDao.insertFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件下载 - 从默认存储桶下载指定文件
     * @param fileName 文件名
     * @param response HTTP响应对象
     */
    @Override
    public void download(String fileName, HttpServletResponse response) {
        try {
            minioConfig.download(fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件删除 - 从默认存储桶删除指定文件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    @Override
    public boolean remove(String fileName) {
        try {
            // 从mysql中删除
            fileDao.deleteFile(fileName);
            return minioConfig.removeObject(minioConfig.getBucket(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
