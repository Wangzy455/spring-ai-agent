package com.wzy.springaiagent.mapper;

import com.wzy.springaiagent.common.pojo.entity.FileEntity;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IFileMapper {

    /**
     * 根据文件类型查询文件
     * @param tag
     * @return
     */
    List<FileEntity> getByFileTag(String tag);

    /**
     * 上传文件的元数据信息插入
     * @param fileEntity
     */
    void insertFile(FileEntity fileEntity);

    /**
     * 删除对应的文件元数据信息
     * @param fileName
     */
    @Delete("delete from file_reo where file_name = #{fileName}")
    void deleteFile(String fileName);

    void updateTag(String fileName);

    List<String> getAllKnowledge();
}
