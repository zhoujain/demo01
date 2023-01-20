package com.zhoujian.redisdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhoujian.redisdemo.domain.SysUser;
import com.zhoujian.redisdemo.mapper.SysUserMapper;
import com.zhoujian.redisdemo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @ClassName SysUserServiceImpl
 * @Description
 * @Author zhoujian
 * @Time 2023/1/19 16:47
 * @Version 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * @Cacheable
     * 针对方法配置，能够根据方法的请求参数对其结果进行缓存
     * 不对比数据库
     * @param userId
     * @return
     */
    // cacheNames/value：指定缓存组件的名字，数组形式
    // key：缓存数据使用的key，确定缓存可以用唯一key进行指定；eg：编写SpEL； #id，参数id的值 ,，#a0(第一个参数)， #p0(和a0的一样的意义) ，#root.args[0]
    // keyGenerator：key的生成器；可以自己指定key的生成器的组件id（注意： key/keyGenerator：二选一使用;不能同时使用）
    // cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
    // condition：指定符合条件的情况下才缓存；使用SpEl表达式，eg：condition = "#a0>1"：第一个参数的值>1的时候才进行缓存
    // unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；eg：unless = "#a0！=2":如果第一个参数的值不是2，结果不缓存；
    // sync：是否使用异步模式
    @Override
    @Cacheable(value = {"user"} /*keyGenerator = "myKeyGenerator",key = "#userId",condition = "#a0>=1",unless = "#a0!=2"*/)
    public SysUser getUser(String userId) {
        return sysUserMapper.selectById(userId);
    }

    /**
     * @CachePut
     * 注解也是一个用来缓存的注解，不过缓存和@Cacheable有明显的区别是即调用方法，又更新缓存数据，
     * 也就是执行方法操作之后再来同步更新缓存，所以这个主键常用于更新操作，也可以用于查询，主
     * 键属性和@Cacheable有很多类似的，详情参看@link @CachePut源码
     *
     * 入参更新redis
     * @param realName
     * @param userId
     */
    @Override
    @CachePut(value = {"user"}, key = "#userId")
    public void updateName(String realName,String userId) {
        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysUser::getId, userId);
        lambdaUpdateWrapper.set(SysUser::getRealname, realName);
        sysUserMapper.update(null, lambdaUpdateWrapper);
    }

    /**
     * key：指定要清除的数据
     * allEntries = true：指定清除这个缓存中所有的数据
     * beforeInvocation = false：默认代表缓存清除操作是在方法执行之后执行
     * beforeInvocation = true：代表清除缓存操作是在方法运行之前执行
     * @param userId
     */
    @Override
    @CacheEvict(value = {"user"}, beforeInvocation = true,key="#id")
    public void deleterById(String userId) {
        sysUserMapper.deleteById(userId);
    }


}
