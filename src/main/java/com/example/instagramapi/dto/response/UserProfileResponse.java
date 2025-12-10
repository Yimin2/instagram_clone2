package com.example.instagramapi.dto.response;

import com.example.instagramapi.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String bio;
    private String profileImageUrl;
    private long postCount;
    private long followerCount;
    private long followingCount;
    private boolean following;

    public static UserProfileResponse of(User user, long postCount, long followerCount,
                                         long followingCount, boolean following) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .profileImageUrl(user.getProfileImageUrl())
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .following(following)
                .build();
    }
}
