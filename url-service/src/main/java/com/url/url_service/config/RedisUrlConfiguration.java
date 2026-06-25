package com.url.url_service.config;

import com.url.url_service.model.RedisUrlData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@Configuration
@EnableRedisRepositories(
        basePackages = "com.url.url_service.repository.RedisUrlRepo",
        redisTemplateRef = "urlTemplate"
)
public class RedisUrlConfiguration {

    @Value("${spring.redis.url.host}")
    private String host;

    @Value("${spring.redis.url.port}")
    private int port;

    @Value("${spring.redis.url.password}")
    private String password;

    @Bean(name = "urlConnectionFactory")
    public RedisConnectionFactory urlConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "urlTemplate")
    public RedisTemplate<String, RedisUrlData> redisUrlTemplate() {
        RedisTemplate<String, RedisUrlData> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(urlConnectionFactory());
        return redisTemplate;
    }
}

