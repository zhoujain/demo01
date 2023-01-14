package com.zhoujian.httpdemo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @ClassName MyRestErrorHandler
 * @Description 自定义错误处理 否则调用Defalult会抛出错误，影响下面的过程
 * @Author zhoujian
 * @Time 2023/1/13 16:04
 * @Version 1.0
 */
public class MyRestErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return statusCode != null ? this.hasError(statusCode) : this.hasError(rawStatusCode);
    }
    protected boolean hasError(HttpStatus statusCode) {
        return statusCode.isError();
    }

    protected boolean hasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return series == CLIENT_ERROR || series == SERVER_ERROR;
    }
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println("错误不做处理");
    }
}
