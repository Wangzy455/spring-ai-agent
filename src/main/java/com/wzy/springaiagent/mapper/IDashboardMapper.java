package com.wzy.springaiagent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IDashboardMapper {

      @Select("select count(*) from file_reo where tag = #{tag}")
      Integer getZData(String tag);

      @Select("select count(*) from file_reo")
      Integer getAll();
}
