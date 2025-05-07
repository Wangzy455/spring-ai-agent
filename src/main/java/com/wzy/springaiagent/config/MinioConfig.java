package com.wzy.springaiagent.config;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class MinioConfig implements InitializingBean {

    @Getter
    @Value("${minio.bucketName}")
    private String bucket;

    @Value("${minio.host}")
    private String host;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(host, "Minio host 为空");
        Assert.hasText(accessKey, "Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");

        this.minioClient = MinioClient.builder()
            .endpoint(this.host)
            .credentials(this.accessKey, this.secretKey)
            .build();
    }

    public String putObject(MultipartFile multipartFile) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        String fileName = multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build()
            );
        }

        return this.url + '/' + UriUtils.encode(fileName, StandardCharsets.UTF_8);
    }

    public void download(String fileName, HttpServletResponse response) {
        try (InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder().bucket(bucket).object(fileName).build())) {

            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder().bucket(bucket).object(fileName).build());

            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> listBucketNames() throws Exception {
        List<Bucket> bucketList = minioClient.listBuckets();
        List<String> bucketNames = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketNames.add(bucket.name());
        }
        return bucketNames;
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public boolean makeBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        }
        return false;
    }

    public boolean removeBucket(String bucketName) throws Exception {
        if (bucketExists(bucketName)) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                if (result.get().size() > 0) {
                    return false;
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return !bucketExists(bucketName);
        }
        return false;
    }

    public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
        if (bucketExists(bucketName)) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> objectNames = new ArrayList<>();
        if (bucketExists(bucketName)) {
            Iterable<Result<Item>> results = listObjects(bucketName);
            for (Result<Item> result : results) {
                objectNames.add(result.get().objectName());
            }
        }
        return objectNames;
    }

    public boolean removeObject(String bucketName, String objectName) throws Exception {
        if (bucketExists(bucketName)) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        if (bucketExists(bucketName)) {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .build()
            );
        }
        return null;
    }
}
