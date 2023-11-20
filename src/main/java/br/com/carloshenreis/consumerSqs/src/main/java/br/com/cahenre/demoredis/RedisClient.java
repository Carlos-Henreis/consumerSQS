package br.com.cahenre.demoredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisClient {

    @Autowired
    private StringRedisTemplate template;

    public String getValue(String key) {
        return template.opsForValue().get(key);
    }

    public boolean ping() {
        // The 'ping' command is not directly supported by Spring Data Redis, so we use the 'execute' method to send a raw Redis command
        return "PONG".equals(template.execute((RedisCallback<Object>) (connection) -> connection.ping()));
    }
}