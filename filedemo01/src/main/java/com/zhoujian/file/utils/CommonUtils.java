package com.zhoujian.file.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * @author zhoujian on 2022/12/23
 */
@Slf4j
public class CommonUtils {

    /**
     * 中文正则
     */
    private static Pattern ZHONGWEN_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 文件名 正则字符串
     * 文件名支持的字符串：字母数字中文.-_()（） 除此之外的字符将被删除
     */
    private static String FILE_NAME_REGEX = "[^A-Za-z\\.\\(\\)\\-（）\\_0-9\\u4e00-\\u9fa5]";


    /**
     * 判断文件名是否带盘符，重新处理
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        int unixSep = fileName.lastIndexOf("/");
        int winSep = fileName.lastIndexOf("\\");

        int pos = Math.max(winSep, unixSep);

        if (pos != -1){
            fileName = fileName.substring(pos + 1);
        }

        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","")
                .replace("#", "").replace("“", "").replace("”", "");
        //替换上传文件名字中的空格
        fileName=fileName.replaceAll("\\s","");
        //update-beign-author:taoyan date:20220302 for: /issues/3381 online 在线表单 使用文件组件时，上传文件名中含%，下载异常
        fileName = fileName.replaceAll(FILE_NAME_REGEX, "");
        //update-end-author:taoyan date:20220302 for: /issues/3381 online 在线表单 使用文件组件时，上传文件名中含%，下载异常
        return fileName;
    }
}