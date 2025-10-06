package com.example.ExploRun.service;

import com.example.ExploRun.dto.TokenPair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtProviderService {
  /**
   * Generates a short-lived Access Token based on the username. The Access Token is used for
   * authorizing protected resources.
   *
   * @param username The username to be placed in the token's 'subject' claim.
   * @return The generated JWT token as a String.
   */
  String generateAccessToken(String username);

  /**
   * Generates a long-lived Refresh Token. This token contains an additional claim identifying it as
   * a refresh token. It is used to obtain a new Access Token after the previous one expires.
   *
   * @param username The username to be placed in the token's 'subject' claim.
   * @return The generated JWT token as a String.
   */
  String generateRefreshToken(String username);

  /**
   * Validates the JWT token. Checks the signature validity, expiration date, and verifies that the
   * username contained in the token matches the provided UserDetails object.
   *
   * @param token The token to be validated.
   * @param userDetails An object containing the expected user data (e.g., username).
   * @return true if the token is valid and belongs to the specified user; false otherwise.
   */
  boolean isValidToken(String token, UserDetails userDetails);

  /**
   * Checks if the given token is a Refresh Token, based on the existence and value of the
   * 'tokenType' claim (expected value: "refresh_token").
   *
   * @param token The token to check.
   * @return true if the token is a refresh token; false otherwise.
   */
  boolean isRefreshToken(String token);

  /**
   * Extracts the username ('subject' claim) from the JWT token.
   *
   * @param token The token from which the username is to be extracted.
   * @return The username as a String.
   */
  String extractUsernameFromToken(String token);

  /**
   * Generates a complete pair of tokens (Access Token and Refresh Token) after successful
   * authentication.
   *
   * @param authentication The Authentication object containing the authenticated user's data.
   * @return A TokenPair object containing the Access Token and Refresh Token.
   */
  TokenPair generateTokenPair(Authentication authentication);

  Long getAccessTokenExpirationInSeconds();

  String extractJtiFromToken(String token);
}
