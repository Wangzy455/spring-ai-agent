package com.wzy.springaiagent.service.impl;

import com.wzy.springaiagent.common.pojo.entity.FileEntity;
import com.wzy.springaiagent.config.MinioConfig;
import com.wzy.springaiagent.mapper.IFileMapper;
import com.wzy.springaiagent.service.IRagService;
import com.wzy.springaiagent.util.CustomMultipartFile;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RagServiceImpl implements IRagService {
    @Resource
    private MinioConfig minioConfig;
    @Resource
    private IFileMapper fileDao;


    /**
     * 上传知识库
     * @param ragTag
     * @param fileTemplate
     * @throws IOException
     */
    @Override
    public void uploadKnowledge(String ragTag, String fileTemplate) throws IOException {

        File tempFile = new File(ragTag+".txt");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), fileTemplate.getBytes(StandardCharsets.UTF_8));

        MultipartFile multipartFile = new CustomMultipartFile(tempFile);

        try{
            String fileUrl = minioConfig.putObject(multipartFile);
            FileEntity fileEntity = FileEntity.builder()
                .fileName(ragTag)
                .fileType("txt")
                .fileUrl(fileUrl)
                .tag("知识库")
                .build();
            fileDao.insertFile(fileEntity);
            log.info("上传知识库成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error("上传知识库失败");
        }
    }

    /**
     * 解析知识库
     * @param ragTag
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    @Override
    public String chooseKnowledge(String ragTag)
        throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 构建文件流
        InputStream inputStream = minioConfig.getStream(ragTag);
        // 转换成字符串
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        reader.close();

        return stringBuilder.toString();
    }

    /**
     * 展示现有的知识库
     */
    @Override
    public List<String> showAllKnowledge() {
        return fileDao.getAllKnowledge();
    }

    /**
     * 删除指定的知识库
     * @param fileName
     */
    @Override
    public void deleteKnowledge(String fileName) throws Exception {
        minioConfig.removeObject(minioConfig.getBucket(), fileName+".txt");
        fileDao.deleteFile(fileName);
    }
}
