package com.example.ExploRun.service.impl;

import com.example.ExploRun.dto.RefreshTokenMetadata;
import com.example.ExploRun.dto.TokenPair;
import com.example.ExploRun.request.LoginRequestDTO;
import com.example.ExploRun.response.AuthenticationResponse;
import com.example.ExploRun.service.AuthService;
import com.example.ExploRun.service.JwtProviderService;
import com.example.ExploRun.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtProviderService jwtProviderService;
  private final JwtTokenService jwtTokenService;

  @Override
  public AuthenticationResponse login(LoginRequestDTO loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    TokenPair tokenPair = jwtProviderService.generateTokenPair(authentication);

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String rtJti = jwtProviderService.extractJtiFromToken(tokenPair.getRefreshToken()); // [1]

    long rtExpiry = jwtTokenService.getRefreshTokenExpirationMs();
    long currentTimestamp = System.currentTimeMillis();

    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String ipAddress = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");

    RefreshTokenMetadata metadata =
        new RefreshTokenMetadata(
            currentTimestamp + rtExpiry, currentTimestamp, userAgent, ipAddress);

    jwtTokenService.storeSession(userDetails.getUsername(), rtJti, metadata);

    String scope =
        userDetails
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    return new AuthenticationResponse(
        tokenPair.getAccessToken(),
        "bearer",
        tokenPair.getRefreshToken(),
        jwtProviderService.getAccessTokenExpirationInSeconds(),
        scope);
  }

  @Override
  public void logoutSession(String refreshToken) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String rtJti = jwtProviderService.extractJtiFromToken(refreshToken);

    if (rtJti != null) {
      jwtTokenService.revokeSession(userDetails.getUsername(), rtJti);
    }
  }

  @Override
  public void logout() {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    jwtTokenService.revokeAllSessions(userDetails.getUsername());
  }

  @Override
  public ResponseEntity<?> refreshToken(String oldRefreshToken) {

    if (!jwtProviderService.isRefreshToken(oldRefreshToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    String username = jwtProviderService.extractUsernameFromToken(oldRefreshToken);
    String oldRtJti = jwtProviderService.extractJtiFromToken(oldRefreshToken);

    if (oldRtJti == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Invalid refresh token (missing JTI)");
    }

    Optional<RefreshTokenMetadata> sessionMetadataOptional =
        jwtTokenService.getSessionMetadata(username, oldRtJti);

    if (sessionMetadataOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session invalid or expired");
    }

    jwtTokenService.revokeSession(username, oldRtJti);

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    Authentication authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    TokenPair newTokenPair = jwtProviderService.generateTokenPair(authToken);
    String newRtJti = jwtProviderService.extractJtiFromToken(newTokenPair.getRefreshToken());

    long rtExpiry = jwtTokenService.getRefreshTokenExpirationMs();
    long currentTimestamp = System.currentTimeMillis();

    RefreshTokenMetadata newMetadata = sessionMetadataOptional.get();
    newMetadata.setExpirationTimestamp(currentTimestamp + rtExpiry);
    newMetadata.setIssuedAtTimestamp(currentTimestamp);

    jwtTokenService.storeSession(username, newRtJti, newMetadata);

    String scope =
        userDetails
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    return ResponseEntity.ok(
        new AuthenticationResponse(
            newTokenPair.getAccessToken(),
            "bearer",
            newTokenPair.getRefreshToken(),
            jwtProviderService.getAccessTokenExpirationInSeconds(),
            scope));
  }
}
