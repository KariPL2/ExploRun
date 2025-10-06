package com.example.ExploRun.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
  RUNNER(Set.of(
      "PLAN_ROUTE_BASIC",
      "SAVE_ROUTE",
      "VIEW_OWN_ROUTES"
  )),

  PREMIUM_RUNNER(Set.of(
      "PLAN_ROUTE_BASIC",
      "PLAN_ROUTE_ADVANCED",
      "SAVE_ROUTE",
      "VIEW_OWN_ROUTES",
      "CREATE_PUBLIC_ROUTE",
      "VIEW_PREMIUM_POIS"
  )),

  ADMIN(Set.of(
      "PLAN_ROUTE_BASIC",
      "PLAN_ROUTE_ADVANCED",
      "SAVE_ROUTE",
      "VIEW_ALL_ROUTES",
      "MANAGE_USERS",
      "MANAGE_POIS"
  ));

  private final Set<String> permissions;

  Role(Set<String> permissions) {
    this.permissions = permissions;
  }

  /**
   * Konwertuje zestaw uprawnień (String) na format akceptowany przez Spring Security
   * (SimpleGrantedAuthority), z prefiksem "ROLE_".
   *
   * @return Zbiór uprawnień i samej roli w formacie Spring Security.
   */
  public Set<GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = permissions.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    return authorities;
  }
}
