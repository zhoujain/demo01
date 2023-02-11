package com.zhoujian.httpdemo;

import com.zhoujian.httpdemo.utils.RestUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

@SpringBootTest
class HttpdemoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("111");
    }

    /**
     * 优点：只对当前的请求有效
     *
     * 适用场景：内部basic认证较多，账号密码不同
     */
    @Test
    void test01() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders() {{
//            String auth = username + ":" + password;
            String auth = "admim" + ":" + "admin";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        ResponseEntity<String> responseEntity = restTemplate.exchange
                ("http://localhost/basicdemo", HttpMethod.POST, new HttpEntity<String>(httpHeaders), String.class);
    }

    /**
     * 从Spring 5之后，支持BasicAuthenticationInterceptor，直接配置即可。
     *
     * 优点：一次配置，后续重复使用
     *
     * 适用场景：内部相同的basic认证账号和密码
     */
    @Test
    void test02() {
        // 方式一，手动配置BasicAuthenticationInterceptor
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new BasicAuthenticationInterceptor("username","password"));

        // 方式二，使用RestTemplateBuilder自动配置
        RestTemplate template = new RestTemplateBuilder()
                .basicAuthentication("username", "password")
                .build();
    }

    @Test
    void test03() {
        String url = "http://172.20.20.19:15673/api/queues";
//        String url = "http://172.20.22.91:6020/appsysdataregapi/v2/reg/orgBox";
        HttpHeaders httpHeaders = new HttpHeaders() {{

            String auth = "admin" + ":" + "admin1234";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        ResponseEntity<String> request = RestUtil.request(url, HttpMethod.GET, httpHeaders, null, null, String.class);
        System.out.println(request.getBody());
    }

}
