package com.wzy.springaiagent.fcalling.search;

import org.apache.ibatis.annotations.Param;

public class DynamicSqlProvider {
    public static String build(@Param("sql") String sql) {
        return sql;
    }
}

