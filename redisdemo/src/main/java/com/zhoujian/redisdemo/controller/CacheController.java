package com.zhoujian.redisdemo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zhoujian.redisdemo.domain.SysUser;
import com.zhoujian.redisdemo.domain.vo.Result;
import com.zhoujian.redisdemo.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CacheController
 * @Description
 * @Author zhoujian
 * @Time 2023/1/19 16:27
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/template")
@Api(tags = "缓存案例")
@ApiSort(2)
public class CacheController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "@Cacheable测试")
    @GetMapping(value = "/cacheableTest")
    @ApiOperationSupport(order = 1)
    public Result test(@RequestParam String userId) {
        SysUser sysUser = sysUserService.getUser(userId);
        log.info(sysUser.toString());
        return Result.OK("");
    }

    @ApiOperation(value = "CachePut测试")
    @PostMapping(value = "/CachePutTest")
    @ApiOperationSupport(order = 2)
    public Result test2(@RequestParam String realName,String userId) {
        sysUserService.updateName(realName,userId);
        return Result.OK("测试成功");
    }

    @ApiOperation(value = "CacheEvic测试")
    @PostMapping(value = "/CacheEvicTest")
    @ApiOperationSupport(order = 3)
    public Result test3(@RequestParam String userId) {
        sysUserService.deleterById(userId);
        return Result.OK("测试成功");
    }
}
