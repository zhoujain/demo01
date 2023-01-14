package com.zhoujian.httpdemo.service;

import com.zhoujian.httpdemo.config.MyRestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName HttpServiceImpl
 * @Description
 * @Author zhoujian
 * @Time 2023/1/14 9:42
 * @Version 1.0
 */
@Service
public class HttpServiceImpl {
    @Autowired
    private RestTemplate restTemplate;


    public void test01() {
        String url = "http://192.168.1.2:6363/template/template/test1";
        ResponseEntity<String> forEntity = null;
        try {
            forEntity = restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println("响应码：-------------------" + forEntity.getStatusCode());
        System.out.println("响应码：-------------------" + forEntity.getStatusCodeValue());
        if (forEntity.getStatusCode().is2xxSuccessful()){
            System.out.println(forEntity.getBody());
        }
    }

    public void test02() {

        String url = "http://192.168.1.2:6363/template/template/test1";
        ResponseEntity<String> forEntity = null;
        try {

            forEntity = restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println("响应码：-------------------" + forEntity.getStatusCode());
        System.out.println("响应码：-------------------" + forEntity.getStatusCodeValue());
        if (forEntity.getStatusCode().is2xxSuccessful()){
            System.out.println(forEntity.getBody());
        }
    }
}
