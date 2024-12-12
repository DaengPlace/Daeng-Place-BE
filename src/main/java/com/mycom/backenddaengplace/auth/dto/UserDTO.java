package com.mycom.backenddaengplace.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String provider;
    private String providerId;
    private String nickname;
    private String profileImage;
    private String email;

    // Custom Setter for provider
    public void setProvider(String provider) {
        if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException("Provider cannot be null or empty");
        }
        this.provider = provider;
    }

    // Custom Setter for providerId
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    // Custom Setter for nickname
    public void setNickname(String nickname) {
        this.nickname = nickname.trim(); // Example: Automatically trims whitespace
    }

    // Custom Setter for profileImage
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // Custom Setter for email
    public void setEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }
}
