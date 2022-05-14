package com.codexie.matcher;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 该类的作用是对HashedCredentialsMatcher的功能进行封装，采用静态代理的设计模式
 * 具体功能：若用户在十分钟内连续输错三次则用户将被锁定十分钟
 * 思路：采用ehCacheManager的特点，设置一个数据钝化时间为10分钟的缓冲池
 *      当用户登录时判断用户的输错次数是否大于等于3，若是则不用进行数据库的比对，直接抛出异常
 *      若不是则跟数据库进行比对，比对成功则清空之前的记录，比对失败则将失败次数+1
 */
@Component
public class MyMatcher extends HashedCredentialsMatcher {
    private Ehcache passwordEhcache;

    public MyMatcher(EhCacheManager ehCacheManager) {
        Ehcache passwordRetryEhcache =  ehCacheManager.getCacheManager().getEhcache("passwordRetryEhcache");
        passwordEhcache =  passwordRetryEhcache;
    }

    public MyMatcher() {

    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        int i = 0;
        //1.得到uname
        String uname = token.getPrincipal().toString();
        //2.根据uname得到缓冲池中的密码
        Element element = passwordEhcache.get(uname);

        //3.若该用户名对应的元素为空则将其初始化并放入缓冲池
        if(element == null){
            element = new Element(uname,0);
            passwordEhcache.put(element);
        }else{ //4.否则比较该元素的值是否大于等于3，若是则抛异常
            if((Integer) element.getObjectValue() >= 3){
                throw new ExcessiveAttemptsException();
            }
        }

        //5.调用父级方法做信息核验
        boolean match = super.doCredentialsMatch(token, info);
        //6.若核验成功则移除之前的记录
        if(match){
            passwordEhcache.remove(uname);
        }else{
            //否则将失败记录+1
            Integer integer = (Integer) element.getObjectValue();
            integer++;

            passwordEhcache.put(new Element(uname,integer));
            System.out.println("失败次数："+integer);
        }
        return match;
    }

//    public static void main(String[] args) {
//        MyMatcher matcher = new MyMatcher();
//
//    }
}
