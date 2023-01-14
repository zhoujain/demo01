package com.zhoujian.redisdemo.utils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName JedisRedisUtils
 * @Description
 * @Author zhoujian
 * @Time 2023/1/14 17:36
 * @Version 1.0
 */
public class JedisRedisUtils {
    private static String ip;
    private static int port;

    static {
        ip = "192.168.1.3";
        port = 6379;
    }

    public static void testRedis(){
        Jedis jedis = new Jedis(ip, port);
        jedis.auth("zhoujian");
        String str = jedis.get("zhoujian");
        System.out.println(str);
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        jedis.close();
    }

    /**
     * 连接池连接redis
     */
    public static void testRedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(18);
        JedisPool pool = new JedisPool(config,ip,port,2000,"zhoujian");
        Jedis jedis = pool.getResource();
        String str = jedis.get("zhoujian");
        System.out.println(str);
        jedis.close();
    }

    // 哨兵模式，连接的时候会通过哨兵寻找主节点，找到之后会连接主节点，
    // 而且在主节点宕机之后也可以通过监控重新连接新的主节点，这个过程是客户端就已经我们做的事情，
    // 我们也不需要关注哪个节点是主节点。
    public static void testSentinel() {
        String masterName = "mymaster";
        String password = "";
        // 设置参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);
        // 哨兵信息，注意填写哨兵的地址
        Set<String> sentinels = new HashSet<>(Arrays.asList("192.168.242.128:28021","192.168.242.128:28022","192.168.242.128:28023"));
        // 创建连接池
        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
        //JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels,jedisPoolConfig, password);
        // 获取客户端
        Jedis jedis = pool.getResource();
        // 执行两个命令
        jedis.set("okayjam", "www.okayjam.com");
        String value = jedis.get("okayjam");
        System.out.println(value);
    }


    public static void testCluster() {
        // 配置参数，根据服务器情况设置连接参数
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        // 添加集群master地址
        Set<HostAndPort> jedisClusterSet = new HashSet<>();
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8201));
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8202));
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8203));
        // 连接集群
        JedisCluster jedisCluster = new JedisCluster(jedisClusterSet, config);
        // r如果带密码
        //JedisCluster jedisCluster  = new JedisCluster(jedisClusterSet,1000, 1000, 5, password, config);
        // 设置key
        jedisCluster.set("okayjam", "www.okayjam.com");
        // 读取key
        System.out.println(jedisCluster.get("okayjam"));
    }

    public static void main(String[] args) {
        testRedisPool();
    }
}
