package com.zhoujian.redisdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhoujian.redisdemo.domain.SysUser;

/**
 * @ClassName SysUserService
 * @Description
 * @Author zhoujian
 * @Time 2023/1/19 16:45
 * @Version 1.0
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getUser(String userId);

    void updateName(String realName,String userId);

    void deleterById(String userId);

}
