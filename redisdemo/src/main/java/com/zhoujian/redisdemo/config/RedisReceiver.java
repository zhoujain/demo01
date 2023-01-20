package com.zhoujian.redisdemo.config;


import cn.hutool.core.util.ObjectUtil;
import com.zhoujian.redisdemo.constant.GlobalConstants;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zyf
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(Map params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        MyRedisListener messageListener = SpringContextHolder.getHandler(handlerName.toString(), MyRedisListener.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
