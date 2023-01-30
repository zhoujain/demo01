package com.zhoujian.aspectdemo.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.zhoujian.aspectdemo.aspect.annotation.AutoLog;
import com.zhoujian.aspectdemo.constant.CommonConstant;
import com.zhoujian.aspectdemo.domain.LogDTO;
import com.zhoujian.aspectdemo.utils.IpUtils;
import com.zhoujian.aspectdemo.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @ClassName AutoLogAspect
 * @Description
 * @Author zhoujian
 * @Time 2023/1/30 10:14
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Pointcut("@annotation(com.zhoujian.aspectdemo.aspect.annotation.AutoLog)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(joinPoint, time, result);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object result) {
        // 签名可以有方法也可以是类
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        LogDTO logDTO = new LogDTO();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        if (autoLog != null){
            String content = autoLog.value();
            // 设置内容
            logDTO.setLogContent(content);
            // 设置日志类型
            logDTO.setLogType(autoLog.logType());
            // 操作方式
            logDTO.setOperateType(autoLog.operateType());
        }
        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();
        // 设置ip
        logDTO.setIp(IpUtils.getIpAddr(httpServletRequest));
        // 设置用户id
        logDTO.setUserid("111");
        logDTO.setUsername("zhoujian1");
        // 设置方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        logDTO.setMethod(className + "." + methodName + "()");

        // 设置操作类型

        // 设置请求参数
        logDTO.setRequestParam(getRequestParams(httpServletRequest, joinPoint));
        //
        logDTO.setCostTime(time);
        logDTO.setCreateTime(new Date());

        log.info(logDTO.toString());
    }


    /**
     * @Description: 获取请求参数
     * @author: scott
     * @date: 2020/4/16 0:10
     * @param request:  request
     * @param joinPoint:  joinPoint
     * @Return: java.lang.String
     */
    private String getRequestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        String params = "";
        if (CommonConstant.HTTP_POST.equals(httpMethod) || CommonConstant.HTTP_PUT.equals(httpMethod) || CommonConstant.HTTP_PATCH.equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            Object[] arguments  = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof BindingResult || paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            PropertyFilter profilter = new PropertyFilter() {
                @Override
                public boolean apply(Object o, String name, Object value) {
                    int length = 500;
                    if(value!=null && value.toString().length()>length){
                        return false;
                    }
                    return true;
                }
            };
            params = JSONObject.toJSONString(arguments, profilter);
            //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params += "  " + paramNames[i] + ": " + args[i];
                }
            }
        }
        return params;
    }
}
