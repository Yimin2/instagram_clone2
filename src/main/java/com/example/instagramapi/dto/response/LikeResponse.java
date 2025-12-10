package com.example.instagramapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {
    private boolean liked;
    private long likeCount;

    public static LikeResponse of(boolean liked, long likeCount) {
        return LikeResponse.builder()
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }
}
