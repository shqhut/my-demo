package com.shq.demo.pg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PgInfoMapper {

    @Select("SELECT version()")
    String getVersion();

    @Select("SELECT current_database()")
    String getCurrentDatabase();

}
