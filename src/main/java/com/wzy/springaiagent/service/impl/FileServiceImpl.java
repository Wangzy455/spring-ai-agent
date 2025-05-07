package com.wzy.springaiagent.service.impl;

import com.wzy.springaiagent.common.constants.Constant;
import com.wzy.springaiagent.common.pojo.entity.File;
import com.wzy.springaiagent.config.MinioConfig;
import com.wzy.springaiagent.mapper.IFileMapper;
import com.wzy.springaiagent.service.IFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 根据业务进行进一步的封装
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Resource
    private MinioConfig minioConfig;
    @Resource
    private IFileMapper fileDao;


    /**
     * 文件展示
     * 根据不同的文件类型进行展示
     * @return 文件名列表
     */
    @Override
    public List<File> show(String FileTag) {
        try {
            return fileDao.getByFileTag(FileTag);
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
            //构造文件标签
            String tag = getTag(fileType);
            log.info("插入的标签为:{}",tag);

            File file = File.builder()
                .fileName(fileName)
                .fileType(fileType)
                .fileUrl(fileUrl)
                .tag(tag)
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

    /**
     * 根据文件类型获取文件标签的方法
     * @param fileType
     * @return
     */
    private String getTag(String fileType){
        if(fileType.equals("txt")||fileType.equals("pdf")||fileType.equals("doc")){
            return Constant.FILE_DOC;
        }else if(fileType.equals("jpg")||fileType.equals("png")||fileType.equals("jpeg")){
            return Constant.FILE_JPG;
        }else if(fileType.equals("mp4")||fileType.equals("avi")||fileType.equals("wmv")){
            return Constant.FILE_MV;
        }else if(fileType.equals("zip")||fileType.equals("rar")){
            return Constant.FILE_ZIP;
        }else{
            return Constant.FILE_UNKNOWN;
        }
    }
}
