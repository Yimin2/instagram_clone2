package com.example.instagramapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API 응답")
public class ApiResponse<T> {

    @Schema(description = "성공 여부")
    private boolean success;

    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "에러 정보")
    private ErrorDto error;

    @Getter
    @Builder
    public static class ErrorDto {
        private String code;
        private String message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> error(String code, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .error(ErrorDto.builder()
                        .code(code)
                        .message(message)
                        .build())
                .build();
    }
}
