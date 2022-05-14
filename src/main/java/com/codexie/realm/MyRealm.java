package com.codexie.realm;

import com.codexie.pojo.User;
import com.codexie.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 该类的功能是重写授权和认证获取数据的方法
 */
@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService service;

    /**
     * 目的：向数据库中选取该用户的权限信息，若没使用缓冲池则每次授权操作都需要执行一次该方法
     * @param principalCollection
     * @return AuthorizationInfo 授权信息对象
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取身份信息
        String pricipal = principalCollection.getPrimaryPrincipal().toString();

        //2.根据身份获取、角色权限等信息
        List<String> roles = service.getRolesService(pricipal);
        List<String> permissions = service.getPermissionsService(pricipal);
        System.out.println("用户："+pricipal);
        System.out.println("角色："+roles);
        System.out.println("权限："+permissions);
        //3.将角色、权限添加到授权信息里面
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }

    /**
     * 情景：当用户进行登录认证的时候shiro底层会使用该方法获取认证信息，但shiro默认向shiro.ini文件获取
     * 目的：使得shiro向数据库获取认证信息，并且做一些自定义操作，比如设置加密的盐等等
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户的身份信息
        String uname = authenticationToken.getPrincipal().toString();
        //2.根据用户名查找用户
        User user = service.selUserService(uname);
        //3.判断用户是否存在
        if(user != null){
            //得到数据库密码
            String pwd = user.getPwd();
            //将身份信息与数据库中的密码还有盐存放到认证信息中以便于shiro的认证模块做比对
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), pwd, ByteSource.Util.bytes("codexie"), uname);
            return info;
        }
        return null;
    }
}
