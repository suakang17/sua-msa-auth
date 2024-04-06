package com.sua.authserver.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * @param loginId              : key
     * @param refreshToken
     * @param refreshTokenTime
     */
    public void setRefreshToken(String loginId, String refreshToken, long refreshTokenTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
//        redisTemplate.opsForValue().set(loginId, refreshToken, minutes, TimeUnit.MINUTES);
    }

    public String getRefreshToken(String loginId) {
        return redisTemplate.opsForValue().get(loginId);
    }

    public void deleteRefreshToken(String loginId) {
        redisTemplate.delete(loginId);
    }

    public boolean hasKey(String loginId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(loginId));
    }

    public void setBlackList(String accessToken, String msg, Long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(msg.getClass()));
        redisTemplate.opsForValue().set(accessToken, msg, minutes, TimeUnit.MINUTES);
    }

    public String getBlackList(String loginId) {
        return redisTemplate.opsForValue().get(loginId);
    }

    public boolean deleteBlackList(String loginId) {
        return Boolean.TRUE.equals(redisTemplate.delete(loginId));
    }

    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
