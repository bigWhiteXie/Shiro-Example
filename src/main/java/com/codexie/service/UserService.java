package com.codexie.service;

import com.codexie.pojo.User;

import java.util.List;

public interface UserService {
    List<String> getRolesService(String pricipal);

    List<String> getPermissionsService(String pricipal);

    User selUserService(String uname);
}
