package com.codexie.aspect;

import com.codexie.annotations.MyLog;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class LogAdvice {
    private Logger logger = LoggerFactory.getLogger(LogAdvice.class);

    @Pointcut("@annotation(com.codexie.annotations.MyLog)")
    public void mylog(){ }

    @Before(value = "mylog()")
    public void executeBefore(JoinPoint joinPoint){
        handLog(joinPoint,null);
    }

    @AfterReturning(value = "mylog()",returning = "result")
    public void executeAfter(Object result){
        handLog(null,result);
    }




    public void handLog(JoinPoint point,Object result){
        MyLog myLog = getAnnotation(point);
        Map<String, String> map = getControllerMethodDescription(myLog,point);
        if(point != null){
            logger.info("{},{} start to execute {}",map.get("title"),map.get("operatorType"),map.get("business"));
        }

        if(result != null){
            logger.info("{},{} execute {} successfully,return {}",map.get("title"),map.get("operatorType"),map.get("business"),result);
        }

    }

    /**
     * get annotation info
     * @param log
     * @param point
     * @return
     */
    private Map<String,String> getControllerMethodDescription(MyLog log,JoinPoint point){
        HashMap<String, String> map = new HashMap<>();
        map.put("title",log.title());
        if(!"".equals(log.bussiness())) {
            map.put("bussiness", log.bussiness());
        }else{
            String methodName = point.getSignature().getName();
            map.put("bussiness",methodName);
        }
        map.put("operatorType",log.operatorType());
        return map;
    }
    /**
     * get now time
     * @return
     */
    private String now(){
        Date date = new Date();
        return format.format(date);
    }
    /**
     * get MyLog annotation
     * @param joinPoint
     * @return
     */
    private MyLog getAnnotation(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        MyLog annotation = method.getAnnotation(MyLog.class);
        return annotation;
    }
}
