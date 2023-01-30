package com.zhoujian.aspectdemo.aspect.annotation;

import com.zhoujian.aspectdemo.constant.CommonConstant;
import com.zhoujian.aspectdemo.domain.ModuleType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName AutoLog
 * @Description
 * @Author zhoujian
 * @Time 2023/1/30 10:07
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    /**
     * 日志内容
     * @return
     */
    String value() default "";

    int logType() default CommonConstant.LOG_TYPE_2;

    int operateType() default 0;


    /**
     * 模块类型 默认为common
     * @return
     */
    ModuleType module() default ModuleType.COMMON;
}
