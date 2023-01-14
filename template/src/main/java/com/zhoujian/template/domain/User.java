package com.zhoujian.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 *
 * @author zhoujian on 2022/12/23
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
}