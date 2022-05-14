package com.codexie.mapper;

import com.codexie.pojo.Permission;
import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer pid);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);
}