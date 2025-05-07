package com.wzy.springaiagent.mapper;

import com.wzy.springaiagent.common.pojo.entity.File;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IFileMapper {

    /**
     * 根据文件类型查询文件
     * @param fileType
     * @return
     */
    @Select("select * from file where file_type = #{fileType}")
    List<File> getByFileType(String fileType);

    /**
     * 上传文件的元数据信息插入
     * @param file
     */
    void insertFile(File file);

    /**
     * 删除对应的文件元数据信息
     * @param fileName
     */
    @Delete("delete from file where file_name = #{fileName}")
    void deleteFile(String fileName);
}
