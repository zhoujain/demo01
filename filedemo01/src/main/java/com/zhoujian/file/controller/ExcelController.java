package com.zhoujian.file.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zhoujian.file.domain.User;
import com.zhoujian.file.domain.vo.Result;
import com.zhoujian.file.execl.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * execl测试
 * * 原文链接（不定时增加新功能）: https://zyqok.blog.csdn.net/article/details/121994504
 *
 * @author zhoujian on 2023/1/3
 */
@RestController
@Slf4j
@RequestMapping(value = "/excel")
@Api(tags = "Excel案例")
@ApiSort(1)
public class ExcelController {

    @ApiOperation(value = "导入excel")
    @PostMapping(value = "/import")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(name = "file",value = "批量签名文件导入",required = true,dataType="MultipartFile",allowMultiple = true,paramType = "query")
    public Result<JSONArray> uploadWay2(@RequestParam("file") MultipartFile[] file,
                                HttpServletRequest request) {
        Result<JSONArray> result = new Result<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file2 = multipartRequest.getFile("file");

        JSONArray jsonArray = null;
        try {
            jsonArray = ExcelUtils.readMultipartFile(file2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(jsonArray.toJSONString());
        result.setSuccess(true);
        result.setResult(jsonArray);
        return result;
    }

    @ApiOperation(value = "导入excel转为对象")
    @PostMapping(value = "/importToClass")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(name = "file",value = "批量签名文件导入",required = true,dataType="MultipartFile",allowMultiple = true,paramType = "query")
    public Result<List<User>> importToClass(@RequestParam("file") MultipartFile[] file,
                                        HttpServletRequest request) {
        Result<List<User>> result = new Result<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file2 = multipartRequest.getFile("file");

        List<User> users = new ArrayList<>(18);
        try {
            users = ExcelUtils.readMultipartFile(file2, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.stream().forEach(System.out::println);
        result.setSuccess(true);
        result.setResult(users);
        return result;
    }

    @ApiOperation(value = "导入excel多个sheet转为")
    @PostMapping(value = "/importToMany")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(name = "file",value = "批量签名文件导入",required = true,dataType="MultipartFile",allowMultiple = true,paramType = "query")
    public Result<?> importToMany(@RequestParam("file") MultipartFile[] file,
                                            HttpServletRequest request) {
        Result<List<User>> result = new Result<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file2 = multipartRequest.getFile("file");

        try {
            Map<String, JSONArray> map = ExcelUtils.readFileManySheet(file2);
            map.forEach((key,value) ->{
                System.out.println("Sheet名称:" + key);
                System.out.println("Sheet数据:" + value);
                System.out.println("--------------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setSuccess(true);
        return result;
    }


    @ApiOperation(value = "导出excel")
    @GetMapping(value = "/export")
    @ApiOperationSupport(order = 4)
    public void export(HttpServletResponse response) {
        Result<?> result = new Result<>();
        // 表头数据
        List<Object> head = Arrays.asList("姓名","年龄","性别","头像");
        // 用户1数据
        List<Object> user1 = new ArrayList<>();
        user1.add("诸葛亮");
        user1.add(60);
        user1.add("男");
        user1.add("https://profile.csdnimg.cn/A/7/3/3_sunnyzyq");
        // 用户2数据
        List<Object> user2 = new ArrayList<>();
        user2.add("大乔");
        user2.add(28);
        user2.add("女");
        user2.add("https://profile.csdnimg.cn/6/1/9/0_m0_48717371");
        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        sheetDataList.add(user1);
        sheetDataList.add(user2);
        // 导出数据
        ExcelUtils.export(response,"用户表", sheetDataList);

    }
}