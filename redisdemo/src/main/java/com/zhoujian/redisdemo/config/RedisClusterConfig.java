package com.zhoujian.redisdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonMap;


@Slf4j

@Configuration
@Profile("cluster")
public class RedisClusterConfig {

    @Autowired
    RedisProperties redisProperties;

    /**
     * GenericObjectPoolConfig ???????????????
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        return genericObjectPoolConfig;
    }


    @Primary
    @Bean("clusterLettuceConnectionFactory")
    public LettuceConnectionFactory clusterLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        redisClusterConfiguration.setPassword(redisProperties.getPassword());
        redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());

        LettucePoolingClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(redisProperties.getTimeout())
                .build();
        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }
    /**
     * RedisTemplate??????
     * @param lettuceConnectionFactory
     * @return
     */
    @Primary
    @Bean(name = "commonRedisTemple")
    @Resource(name = "clusterLettuceConnectionFactory")
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory clusterLettuceConnectionFactory) {
        log.info(" --- redis config init --- ");
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        // ??????RedisTemplate<String, Object>??????
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(clusterLettuceConnectionFactory);//???????????????
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();//???????????????

        // key?????????
        redisTemplate.setKeySerializer(stringSerializer);
        // value?????????
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key?????????
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value?????????
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * ????????????????????????
     * @return
     */
    @SuppressWarnings("all")
    private Jackson2JsonRedisSerializer jacksonSerializer() {
        // ??????Jackson2JsonRedisSerializer???????????????
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // ???????????????????????????field,get???set,????????????????????????ANY???????????????private???public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // ????????????????????????????????????????????????final????????????final?????????????????????String,Integer???????????????
        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }


//	@Bean
//	public RedisClusterConfiguration redisClusterConfiguration() {
//		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
//		String[] hosts = host.split(",");
//		Set<RedisNode> nodeList = new HashSet<RedisNode>();
//		for (String hostAndPort : hosts) {
//			String[] hostOrPort = hostAndPort.split(":");
//			nodeList.add(new RedisNode(hostOrPort[0], Integer.parseInt(hostOrPort[1])));
//		}
//		redisClusterConfiguration.setClusterNodes(nodeList);
////		redisClusterConfiguration.setMaxRedirects();
//		return redisClusterConfiguration;
//	}

//	@Bean
//	@Resource(name = "clusterLettuceConnectionFactory")
//	public CacheManager cacheManager(LettuceConnectionFactory clusterLettuceConnectionFactory) {
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
//		// ??????????????????????????????????????????,????????????????????????????????? 6??????
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(6)); //??????????????????
//		RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
//		//.disableCachingNullValues();
//
//
//		RedisCacheManager cacheManager = RedisCacheManager.builder(clusterLettuceConnectionFactory)
//				.cacheDefaults(redisCacheConfiguration)
//				.build();
//		return cacheManager;
//	}
}
