package com.zhoujian.file.domain;

import com.zhoujian.file.execl.ExcelImport;
import lombok.Data;

/**
 * 用户
 *
 * @author zhoujian on 2022/12/23
 */
@Data
public class User {

    // 行号
    private int rowNum;

    // 必需 唯一性
    @ExcelImport(value = "姓名", required = true, unique = true)
    private String name;

    @ExcelImport("年龄")
    private Integer age;

    @ExcelImport(value = "性别", kv = "1-男;2-女")
    private String sex;

    // 设置最大长度
    @ExcelImport(value = "电话", maxLength = 11, required = true)
    private String tel;

    @ExcelImport("城市")
    private String city;

    @ExcelImport("头像")
    private String avatar;

    // 获取原数据
    private String rowData;

    // 获取错误提示
    private String rowTips;
}