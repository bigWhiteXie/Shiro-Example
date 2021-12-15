package com.codexie.config;

import com.codexie.matcher.MyMatcher;
import com.codexie.realm.MyRealm;
import net.sf.ehcache.CacheManager;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ShiroConfig {
    @Autowired
    MyRealm myRealm;

    @Autowired
    MyMatcher myMatcher;

    /**得到一个securityManager对象，该对象是全局单例的
     * 我们要为它设置自定义realm模块、rememberMeManager模块
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        //1.创建securManager对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //2.创建matcher
        myMatcher.setHashAlgorithmName("md5");
        myMatcher.setHashIterations(3);
        //3.将matcher设置到realm中
        myRealm.setCredentialsMatcher(myMatcher);


        //4.集中设置securityManager
        securityManager.setRealm(myRealm);
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager( ehCacheCacheManager());
        return securityManager;
    }

    @Bean
    public EhCacheManager ehCacheCacheManager(){
        //1.创建ehCacheManager
        EhCacheManager ehCacheManager = new EhCacheManager();

        //2.读取配置文件
        InputStream in = null;
        try {
            in = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache-shiro.xml");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.根据配置文件创建cacheManager对象
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(in);
        //4.将cacheManager对象传给ehCacheManager对象
        ehCacheManager.setCacheManager(cacheManager);

        return ehCacheManager;

    }

    /**
     * 目的：为Shiro的rememberMe模块设置自定义cookie
     * 主要是设置cookie的范围、存在时间等
     * @return
     */
    public SimpleCookie myCookie(){
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(60*60*24*10);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 创建CookieRememberMeManager对象，将自定义cookie传给它
     * @return
     */
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(myCookie());
        rememberMeManager.setCipherKey("1234567890987654".getBytes());
        return rememberMeManager;
    }

    /**
     * 初始化shiro各权限过滤器的范围
     * @return
     */
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        //将登录页面的请求设置成不需要认证
        chainDefinition.addPathDefinition("/userController/login","anon");
        //将用户登录的请求设置成不需要认证
        chainDefinition.addPathDefinition("/userController/userLogin","anon");
        //设置退出的过滤器
        chainDefinition.addPathDefinition("/loginOut","logout");
        //其它的所有请求设置成需要用户
        chainDefinition.addPathDefinition("/**","user");

        return chainDefinition;
    }
}
