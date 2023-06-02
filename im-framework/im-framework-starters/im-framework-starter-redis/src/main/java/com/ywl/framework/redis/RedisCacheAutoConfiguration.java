package com.ywl.framework.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ywl.framework.redis.annotation.ExpireRedisCacheWriter;
import com.ywl.framework.redis.aspect.CacheExpireAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RedisCache相关配置
 *
 * @author luozhan
 * @date 2022-3-18
 */
@EnableCaching
@Configuration
@Import({RedisClient.class, CacheExpireAspect.class})
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {
    @Value("${spring.application.name:unknown}")
    private String appName;

    private final GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;

    public RedisCacheAutoConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        objectMapper.registerModule(timeModule);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    }


    /**
     * 注入RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 指定序列化方式
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 注入RedisCacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        RedisCacheConfiguration config = RedisCacheConfiguration
                // 注意默认配置允许缓存null
                .defaultCacheConfig()
                // 接口缓存上指定了key的时候统一加服务名前缀
                .computePrefixWith(cacheName -> appName + ":" + cacheName + ":")
                // 可以根据业务需要设置统一过期时间，这里是为了强制使用@CacheExpire手动设置过期时间所以设置很短
                .entryTtl(Duration.ofMillis(100000))
                // 配置序列化和反序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
        return RedisCacheManager.builder(new ExpireRedisCacheWriter(factory)).cacheDefaults(config).build();
    }

    /**
     * 使用@Cachable生成的key的默认规则
     * 如果手动指定了key则不生效
     * <p>
     * 格式：入参1,入参2
     * 示例：1,2
     * 加上前缀后完整key格式：order_service:order:1,2
     * <p>
     * 建议使用@Cacheable的时候都指定key
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 配置使用@Cachable时在redis中序列化的方式
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json());
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
    }
}
