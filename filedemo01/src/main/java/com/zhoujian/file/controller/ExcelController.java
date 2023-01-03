package com.zhoujian.file.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zhoujian.file.domain.vo.Result;
import com.zhoujian.file.execl.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * execl测试
 *
 * @author zhoujian on 2023/1/3
 */
@RestController
@Slf4j
@RequestMapping(value = "/excel")
@Api(tags = "Excel案例")
@ApiSort(1)
public class ExcelController {

    @ApiOperation(value = "导入execl")
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
}