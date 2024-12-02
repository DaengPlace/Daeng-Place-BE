package com.mycom.backenddaengplace.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String refresh;
    private String expiration;

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    public void setRefresh(String refresh) {
        if (refresh == null || refresh.length() < 10) {
            throw new IllegalArgumentException("Refresh token is invalid");
        }
        this.refresh = refresh;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}