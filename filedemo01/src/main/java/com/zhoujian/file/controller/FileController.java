package com.zhoujian.file.controller;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zhoujian.file.domain.vo.Result;
import com.zhoujian.file.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 文件上传测试
 *
 * @author zhoujian on 2022/12/23
 */
@RestController
@Slf4j
@RequestMapping(value = "/file")
@Api(tags = "文件上传案例")
@ApiSort(2)
public class FileController {

    @Value(value = "${my.path.upload}")
    private String uploadPath;


    @ApiOperation(value = "测试")
    @PostMapping(value = "/upload")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(name = "file",value = "批量签名文件导入",required = true,dataType="MultipartFile",allowMultiple = true,paramType = "query")
    public Result<?> upload(@RequestParam("file")MultipartFile[] file) {
        return Result.OK("测试成功");
    }

    @ApiOperation(value = "上传文件入参方式2")
    @PostMapping(value = "/uploadWay2")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParam(name = "file",value = "批量签名文件导入",required = true,dataType="MultipartFile",allowMultiple = true,paramType = "query")
    public Result<?> uploadWay2(@RequestParam("file")MultipartFile[] file,
                                @RequestParam(value = "path",required = false)@ApiParam("文件上传路径") String path,
                                HttpServletRequest request) {
        Result<?> result = new Result<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file2 = multipartRequest.getFile("file");

        String savePath = this.uploadLocal(file2, path);
        if (StrUtil.isEmpty(savePath)){
            result.setMessage("上传失败！");
            result.setSuccess(false);
        }else {
            result.setMessage(savePath);
            result.setSuccess(true);
        }
        return result;
    }

    private String uploadLocal(MultipartFile mf,String path){

        try {
            String ctxPath = uploadPath;
            // 会直接在项目所在盘符下创建文件架
            //绝对路径：是一个完整的路径，以盘符开始（c： d：）c:\a.txt
            //相对路径：相对指的是相对于当前项目的根目录（可以省略项目的根目录）

            // 前缀没有/就是相对路径  有/ 会默认加添加盘符

            File file = new File(ctxPath + File.separator + path + File.separator);
            if (!file.exists()){
                file.mkdir();
            }

            // 获取文件名
            String originalFilename = mf.getOriginalFilename();
            originalFilename = CommonUtils.getFileName(originalFilename);
            String fileName = "";
            if (originalFilename.contains(".")){
                fileName = originalFilename.substring(0,originalFilename.lastIndexOf(".")) + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            }else {
                fileName = originalFilename + "_" + System.currentTimeMillis();
            }
            File saveFile = new File(file.getPath() + File.separator + fileName);
            System.out.println(saveFile.getCanonicalPath());
            System.out.println(saveFile.getAbsolutePath());

            FileCopyUtils.copy(mf.getBytes(),saveFile);
            String dbPath = "";
            if (StrUtil.isEmpty(path)){
                dbPath = path + File.separator + fileName;
            }else {
                dbPath = fileName;
            }

            if (dbPath.contains("\\")) {
                dbPath = dbPath.replace("\\", "/");
            }
            return dbPath;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }

}