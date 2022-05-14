package com.codexie.mapper;

import com.codexie.pojo.File;
import java.util.List;

public interface FileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    File selectByPrimaryKey(Integer id);

    List<File> selectAll();

    int updateByPrimaryKey(File record);
}