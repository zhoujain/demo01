package com.zhoujian.httpdemo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zhoujian.httpdemo.domain.User;
import com.zhoujian.httpdemo.domain.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模板控制器
 *
 * @author zhoujian on 2022/12/23
 */
@RestController
@Slf4j
@RequestMapping(value = "/template")
@Api(tags = "模板控制器案例")
@ApiSort(1)
public class TemplateController {

    @ApiOperation(value = "GET请求不带参数")
    @GetMapping(value = "/test")
    @ApiOperationSupport(order = 1)
    public Result test() {
        return Result.OK("测试成功");
    }

    @ApiOperation(value = "GET请求带参数")
    @GetMapping(value = "/test1")
    @ApiOperationSupport(order = 2)
    public Result test1(@RequestParam String name) {
        return Result.OK("GET测试成功:" + name);
    }

    @ApiOperation(value = "POST请求带参数")
    @PostMapping(value = "/test2")
    @ApiOperationSupport(order = 3)
    public Result test2(@RequestParam String name) {
        return Result.OK("POST测试成功：" + name);
    }

    @PostMapping(value = "/test3")
    @ApiOperation(value = "POST请求带请求体")
    @ApiOperationSupport(order = 4)
    public Result test3(@RequestBody @ApiParam("用户") User user) {
        return Result.OK("POST测试成功：" + user.toString());
    }
}