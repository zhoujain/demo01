package com.zhoujian.redisdemo.config;


import java.util.Map;

/**
 * @Description: 自定义消息监听
 * @author: scott
 * @date: 2020/01/01 16:02
 */
public interface MyRedisListener {
    /**
     * 接受消息
     *
     * @param message
     */
    void onMessage(Map message);

}
