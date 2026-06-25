package com.url.url_service.config;

import com.url.url_service.model.RedisUrlData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCounterConfig {

    @Value("${spring.redis.counter.host}")
    private String host;

    @Value("${spring.redis.counter.port}")
    private int port;

    @Value("${spring.redis.counter.password}")
    private String password;

    @Bean(name = "counterConnectionFactory")
    public RedisConnectionFactory counterConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "counterTemplate")
    public RedisTemplate<String, Object> redisUrlTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(counterConnectionFactory());
        return redisTemplate;
    }
}
