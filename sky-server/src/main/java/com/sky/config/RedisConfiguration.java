package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/26
 * @since 1.0.0
 */
//TODO redis配置类
@Configuration
@Slf4j
public class RedisConfiguration {
   @Bean
   public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
       log.info("开始创建redisTemplate");
       RedisTemplate redisTemplate = new RedisTemplate();
       //工厂类
       redisTemplate.setConnectionFactory(redisConnectionFactory);
       //key序列化
       redisTemplate.setKeySerializer(new StringRedisSerializer());
       redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
       return redisTemplate;
   }
}