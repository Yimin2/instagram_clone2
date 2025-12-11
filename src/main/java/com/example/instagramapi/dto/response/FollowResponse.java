package com.example.instagramapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponse {

    private boolean following;
    private long followerCount;
    private long followingCount;

    public static FollowResponse of(boolean following, long followerCount, long followingCount) {
        return FollowResponse.builder()
                .following(following)
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }
}
