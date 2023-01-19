package com.zhoujian.redisdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhoujian.redisdemo.constant.CacheConstant;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

import static java.util.Collections.singletonMap;

/**
 * @ClassName RedisConfig
 * @Description
 * @Author zhoujian
 * @Time 2023/1/17 17:33
 * @Version 1.0
 */
@Configuration
@Slf4j
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * RedisTemplate配置
     * @param lettuceConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        log.info(" --- redis config init --- ");
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory); // 设置连接池
        RedisSerializer<String> stringSerializer = new StringRedisSerializer(); //设置序列化

        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }



    /**
     * 缓存配置管理器 指定方法如何缓存
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        // 配置序列化（解决乱码的问题）,并且配置缓存默认有效期 6小时
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(6));
        // key value序列化
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        //.disableCachingNullValues();

        // 以锁写入的方式创建RedisCacheWriter对象
        //update-begin-author:taoyan date:20210316 for:注解CacheEvict根据key删除redis支持通配符*
        // 自定义的 可以去除
        RedisCacheWriter writer = new MyRedisCacheWriter(factory, Duration.ofMillis(50L));
        //RedisCacheWriter.lockingRedisCacheWriter(factory);
        // 创建默认缓存配置对象
        /* 默认配置，设置缓存有效期 1小时*/
        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        /* 自定义配置test:demo 的超时时间为 5分钟*/
        RedisCacheManager cacheManager = RedisCacheManager.builder(writer).cacheDefaults(redisCacheConfiguration)
                // .withInitialCacheConfigurations(singletonMap(CacheConstant.SYS_DICT_TABLE_CACHE,
                //         RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)).disableCachingNullValues()
                //                 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))))
                // .withInitialCacheConfigurations(singletonMap(CacheConstant.TEST_DEMO_CACHE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)).disableCachingNullValues()))
                // .withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_RANKING, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
                // .withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_PAGE_LIST, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
                // .transactionAware()
                .build();
        //update-end-author:taoyan date:20210316 for:注解CacheEvict根据key删除redis支持通配符*
        return cacheManager;
    }


    /**
     * redis 监听配置
     *
     * @param redisConnectionFactory redis 配置
     * @return
     */
    // @Bean
    // public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, RedisReceiver redisReceiver, MessageListenerAdapter commonListenerAdapter) {
    //     RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    //     container.setConnectionFactory(redisConnectionFactory);
    //     container.addMessageListener(commonListenerAdapter, new ChannelTopic(GlobalConstants.REDIS_TOPIC_NAME));
    //     return container;
    // }


    // @Bean
    // MessageListenerAdapter commonListenerAdapter(RedisReceiver redisReceiver) {
    //     MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver, "onMessage");
    //     messageListenerAdapter.setSerializer(jacksonSerializer());
    //     return messageListenerAdapter;
    // }
}
