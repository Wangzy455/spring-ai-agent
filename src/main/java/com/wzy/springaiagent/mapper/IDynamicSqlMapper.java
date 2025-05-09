package com.wzy.springaiagent.mapper;

import com.wzy.springaiagent.fcalling.search.DynamicSqlProvider;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface IDynamicSqlMapper {
    @Select("${sql}")
    List<Map<String, Object>> executeSelect(@Param("sql") String sql);
}
