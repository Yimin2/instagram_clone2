package com.example.instagramapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String bio;

    private String profileImageUrl;

    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    private Long providerId;

    @Builder
    public User(String username, String email, String password, String name, AuthProvider provider, Long providerId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider != null ? provider : AuthProvider.LOCAL;
        this.providerId = providerId;
    }

    public void updateProfile(String name, String bio, String profileImageUrl) {
        if (name != null)
            this.name = name;
        if (bio != null)
            this.bio = bio;
        if (profileImageUrl != null)
            this.profileImageUrl = profileImageUrl;
    }

    public void updateOAuthProfile(String name, String profileImageUrl) {
        if (name != null)
            this.name = name;
        if (profileImageUrl != null)
            this.profileImageUrl = profileImageUrl;
    }
}
