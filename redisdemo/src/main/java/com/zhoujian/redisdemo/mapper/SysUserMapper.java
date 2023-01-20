package com.zhoujian.redisdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoujian.redisdemo.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SysUserMapper
 * @Description
 * @Author zhoujian
 * @Time 2023/1/19 16:35
 * @Version 1.0
 */
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
}
