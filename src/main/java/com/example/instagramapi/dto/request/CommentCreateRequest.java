package com.example.instagramapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(max = 100, message = "댓글은 100자 이하만")
    private String content;
}
