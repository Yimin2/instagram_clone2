package com.example.instagramapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadResponse {

    private String url;

    public static FileUploadResponse of(String url) {
        return FileUploadResponse.builder()
                .url(url)
                .build();
    }
}
