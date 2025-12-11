package com.example.instagramapi.controller;

import com.example.instagramapi.dto.request.ProfileUpdateRequest;
import com.example.instagramapi.dto.response.ApiResponse;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.dto.response.UserProfileResponse;
import com.example.instagramapi.dto.response.UserResponse;
import com.example.instagramapi.security.CustomUserDetails;
import com.example.instagramapi.service.PostService;
import com.example.instagramapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;
    // TODO: PostService, FollowService 추가 후 주입

    @Operation(summary = "프로필 조회")
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @Parameter(description = "사용자명") @PathVariable String username,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails != null ? userDetails.getId() : null;
        UserProfileResponse response = userService.getProfile(username, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Parameter(description = "사용자명") @PathVariable String username,
            @AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody ProfileUpdateRequest request) {
        UserResponse response = userService.updateProfile(username, userDetails.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // TODO: 사용자 게시물 조회 API 추가
    // GET /api/users/{username}/posts
    @GetMapping("/{username}/posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getUserPosts(@PathVariable String username,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<PostResponse> response = postService.findByUsername(username, userDetails.getId());
        return ResponseEntity.ok((ApiResponse.success(response)));
    }

    // TODO: 팔로워 목록 조회 API 추가
    // GET /api/users/{username}/followers

    // TODO: 팔로잉 목록 조회 API 추가
    // GET /api/users/{username}/following
}
