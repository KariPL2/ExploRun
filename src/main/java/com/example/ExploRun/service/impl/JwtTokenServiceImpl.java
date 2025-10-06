package com.example.ExploRun.service.impl;

import com.example.ExploRun.dto.RefreshTokenMetadata;
import com.example.ExploRun.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Service
public class JwtTokenServiceImpl  implements JwtTokenService {


  private final RedisTemplate<String, Object> redisTemplate;
  private final HashOperations<String, String, RefreshTokenMetadata> hashOperations;

  private static final String REFRESH_SESSION_KEY_PREFIX = "session:rt:";
  private static final String ACCESS_BLACKLIST_PREFIX = "blacklist:at:";

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-expiration}")
  private long refreshTokenExpiration;

  public JwtTokenServiceImpl(
      RedisTemplate<String, Object> redisTemplate,
      @Value("${jwt.expiration}") long jwtExpiration,
      @Value("${jwt.refresh-expiration}") long refreshTokenExpiration) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = redisTemplate.opsForHash();
    this.jwtExpiration = jwtExpiration;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }


  @Override
  public void blacklistAccessToken(String accessTokenJti, long expirationMs) {
    String key = ACCESS_BLACKLIST_PREFIX + accessTokenJti;
    redisTemplate.opsForValue().set(key, "blacklisted", expirationMs, TimeUnit.MILLISECONDS);
  }

  @Override
  public boolean isAccessTokenBlacklisted(String accessTokenJti) {
    String key = ACCESS_BLACKLIST_PREFIX + accessTokenJti;
    return redisTemplate.hasKey(key);
  }

  private String getSessionKey(String username) {
    return REFRESH_SESSION_KEY_PREFIX + username;
  }

  @Override
  public void storeSession(String username, String rtJti, RefreshTokenMetadata metadata) {
    String key = getSessionKey(username);
    hashOperations.put(key, rtJti, metadata);
  }

  @Override
  public void revokeSession(String username, String rtJti) {
    String key = getSessionKey(username);
    hashOperations.delete(key, rtJti);
  }

  @Override
  public void revokeAllSessions(String username) {
    String key = getSessionKey(username);
    redisTemplate.delete(key);
  }

  @Override
  public Optional<RefreshTokenMetadata> getSessionMetadata(String username, String rtJti) {
    String key = getSessionKey(username);
    RefreshTokenMetadata metadata = hashOperations.get(key, rtJti);

    if (metadata != null && metadata.getExpirationTimestamp() < System.currentTimeMillis()) {
      revokeSession(username, rtJti);
      return Optional.empty();
    }

    return Optional.ofNullable(metadata);
  }

  @Override
  public long getRefreshTokenExpirationMs() {
    return refreshTokenExpiration;
  }
}
