package com.example.ExploRun.service.impl;

import com.example.ExploRun.dto.TokenPair;
import com.example.ExploRun.service.JwtProviderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JwtProviderServiceImpl implements JwtProviderService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  @Value("${jwt.refresh-expiration}")
  private long refreshExpirationMs;

  @Override
  public String generateAccessToken(String username) {
    return generateToken(username, jwtExpirationMs, new HashMap<>());
  }

  @Override
  public Long getAccessTokenExpirationInSeconds() {
    return jwtExpirationMs / 1000L;
  }

  @Override
  public String generateRefreshToken(String username) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("tokenType", "refresh_token");

    return generateToken(username, refreshExpirationMs, claims);
  }

  @Override
  public String extractJtiFromToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSignInKey())
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getId();
    } catch (Exception e) {
      log.error("Failed to extract JTI from token: {}", e.getMessage());
      return null;
    }
  }

  @Override
  public boolean isValidToken(String token, UserDetails userDetails) {
    final String username = extractUsernameFromToken(token);
    if (!username.equals(userDetails.getUsername())) {
      return false;
    }

    try {
      Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  @Override
  public boolean isRefreshToken(String token) {
    Claims claims =
        Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

    return claims.get("tokenType").equals("refresh_token");
  }

  @Override
  public String extractUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  @Override
  public TokenPair generateTokenPair(Authentication auth) {
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    String accessToken = generateAccessToken(userDetails.getUsername());
    String refreshToken = generateRefreshToken(userDetails.getUsername());
    return new TokenPair(accessToken, refreshToken);
  }

  private String generateToken(String username, long expirationMs, Map<String, Object> claims) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationMs);

    String jti = null;
    if (claims.containsKey("tokenType") && "refresh_token".equals(claims.get("tokenType"))) {
      jti = UUID.randomUUID().toString();
      claims.put("jti", jti);
    }

    return Jwts.builder()
        .subject(username)
        .claims(claims)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(getSignInKey())
        .compact();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
