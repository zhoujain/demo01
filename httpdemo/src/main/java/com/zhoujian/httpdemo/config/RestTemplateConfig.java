package com.zhoujian.httpdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName RestTemplateConfig
 * @Description http配置
 * @Author zhoujian
 * @Time 2023/1/14 9:42
 * @Version 1.0
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //ms毫秒
        factory.setReadTimeout(5000);
        //ms毫秒
        factory.setConnectTimeout(15000);
        return factory;
    }

    @Bean("restTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        /**
         * 可以自定义切换底层客户端
         */
        RestTemplate restTemplate = new RestTemplate(factory);

        //restTemplate.
        // 加入自定义信息转换
        //restTemplate.getMessageConverters().add(new MyMappingJackson2HttpMessageConverter());
        //restTemplate.getInterceptors().add(new LoggingInterceptor());
        /**
         * 设置自定义的错误处理 就算放在外面方法里，在调用一次之后也变成全局了
         */
        restTemplate.setErrorHandler(new MyRestErrorHandler());
        return restTemplate;
    }
}
