package com.zhoujian.httpdemo;

import com.alibaba.fastjson.JSONObject;
import com.zhoujian.httpdemo.utils.RestUtil;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //https请求
    @Test
    void test04() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        String url = "https://58.23.12.98:8034/login.do";
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username","test");
        map.add("password","098f6bcd4621d373cade4e832627b4f6");
        map.add("loginType","NORMAL");

//        Map<String,String> map = new HashMap<>();
//        map.put("username","test");
//        map.put("password","098f6bcd4621d373cade4e832627b4f6");
//        map.put("loginType","NORMAL");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        System.out.println(map.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.POST, request, JSONObject.class);
        System.out.println(exchange.getBody().toJSONString());
    }


    @Test
    void test05() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        String url = "https://58.23.12.98:8034/login.do?username={username}&password={password}&loginType={loginType}";
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HashMap<String, String> map = new HashMap<>();
        map.put("username","test");
        map.put("password","098f6bcd4621d373cade4e832627b4f6");
        map.put("loginType","NORMAL");

//        Map<String,String> map = new HashMap<>();
//        map.put("username","test");
//        map.put("password","098f6bcd4621d373cade4e832627b4f6");
//        map.put("loginType","NORMAL");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//        System.out.println(map.toString());
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.POST, null, JSONObject.class,map);
        System.out.println(exchange.getBody().toJSONString());
    }

    @Test
    void test06() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        String url = "https://58.23.12.98:8034/login.do?username={username}&password={password}&loginType={loginType}";
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HashMap<String, String> map = new HashMap<>();
        map.put("username","test");
        map.put("password","098f6bcd4621d373cade4e832627b4f6");
        map.put("loginType","NORMAL");

        ResponseEntity<String> template = restTemplate.getForEntity(url, String.class, map);
        System.out.println(template.getBody().toString());
    }
}
