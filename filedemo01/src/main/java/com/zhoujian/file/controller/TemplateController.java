package com.zhoujian.file.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;

import com.zhoujian.file.domain.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @ApiOperation(value = "测试")
    @GetMapping(value = "/test")
    @ApiOperationSupport(order = 1)
    public Result test() {
        return Result.OK("测试成功");
    }
}