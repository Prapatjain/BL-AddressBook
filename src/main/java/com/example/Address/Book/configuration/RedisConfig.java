package com.example.Address.Book.configuration;

import com.example.Address.Book.dto.ContactDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ContactDTO> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ContactDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Set string serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Set JSON serializer for values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, List<ContactDTO>> contactListRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<ContactDTO>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Set string serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Set JSON serializer for values (works better for lists)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
