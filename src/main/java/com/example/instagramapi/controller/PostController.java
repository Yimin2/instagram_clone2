package com.example.instagramapi.controller;

import com.example.instagramapi.dto.request.CommentCreateRequest;
import com.example.instagramapi.dto.request.PostCreateRequest;
import com.example.instagramapi.dto.response.ApiResponse;
import com.example.instagramapi.dto.response.CommentResponse;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.security.CustomUserDetails;
import com.example.instagramapi.service.CommentService;
import com.example.instagramapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody @Valid PostCreateRequest request) {
        PostResponse response = postService.create(userDetails.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> findAll() {
        List<PostResponse> posts = postService.findAll();

        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<PostResponse>> findById(@PathVariable Long id) {
        PostResponse response = postService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.delete(id, userDetails.getId());
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> creatComment(@PathVariable Long id,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @Valid @RequestBody CommentCreateRequest request) {
        CommentResponse response = commentService.create(id, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}/username")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long id) {
        List<CommentResponse> response = commentService.findByPostId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.delete(commentId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
