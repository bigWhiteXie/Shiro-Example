package com.codexie.service.Impl;

import com.codexie.mapper.UserMapper;
import com.codexie.pojo.User;
import com.codexie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper mapper;
    @Override
    public List<String> getRolesService(String pricipal) {
        return mapper.selRolesMapper(pricipal);
    }

    @Override
    public List<String> getPermissionsService(String pricipal) {
        return mapper.selPsMapper(pricipal);
    }

    @Override
    public User selUserService(String uname) {
        return mapper.selUserMapper(uname);
    }

    @Override
    public List<User> selAllUserService() {
        return mapper.selectAll();
    }
}
