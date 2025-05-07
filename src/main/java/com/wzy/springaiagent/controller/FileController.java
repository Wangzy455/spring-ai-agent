package com.wzy.springaiagent.controller;


import com.wzy.springaiagent.common.constants.Response;
import com.wzy.springaiagent.common.pojo.entity.File;
import com.wzy.springaiagent.service.IFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通过minio实现网盘的文件增删改查操作
 * 将文本元数据存储到pgvstore
 */

@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/file/")
@Slf4j
public class FileController {

    @Resource
    private IFileService fileService;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping(value="upload",method = RequestMethod.POST)
    public Response<String> upload(@RequestParam MultipartFile file){
        try{
            log.info("开始上传文件，文件名称:{}",file.getOriginalFilename());
            fileService.upload(file);
            return Response.<String>builder().code("200").info("上传成功").build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.<String>builder().code("500").info("上传失败").build();
        }
    }

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    @RequestMapping(value="delete",method = RequestMethod.POST)
    public Response<String> remove(String fileName){
        try{
            log.info("开始删除文件:{}",fileName);
            fileService.remove(fileName);
            return Response.<String>builder().code("200").info("删除成功").build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.<String>builder().code("500").info("删除失败").build();
        }
    }

    /**
     * 下载文件
     * @param fileName
     * @param response
     * @return
     */
    @RequestMapping(value="download",method = RequestMethod.GET)
    public Response<String> download(String fileName,HttpServletResponse response){
        log.info("开始下载文件,文件名:{}",fileName);
        fileService.download(fileName,response);
        log.info("下载完成");
        return Response.<String>builder().code("200").info("下载成功").build();
    }

    /**
     * 根据文件标签展示对应标签文件
     * @param tag
     * @return
     */
    @RequestMapping(value="show",method = RequestMethod.POST)
    public Response<List<File>> show(@RequestParam String tag){
        log.info("根据文件标签展示所有文件:{}",tag);
        List<File> files = fileService.show(tag);
        log.info("文件：{}",files);
        return Response.<List<File>>builder().code("200").info("展示成功").data(files).build();
    }

}
