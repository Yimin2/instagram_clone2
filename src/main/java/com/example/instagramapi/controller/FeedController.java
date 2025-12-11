package com.example.instagramapi.controller;


import com.example.instagramapi.dto.response.ApiResponse;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.dto.response.SliceResponse;
import com.example.instagramapi.security.CustomUserDetails;
import com.example.instagramapi.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/feed") // /api/feed?size=10&page=2
    public ResponseEntity<ApiResponse<SliceResponse<PostResponse>>> getFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        SliceResponse<PostResponse> response = feedService.getFeed(userDetails.getId(), pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/explore")
    public ResponseEntity<ApiResponse<SliceResponse<PostResponse>>> getExplore(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        SliceResponse<PostResponse> response = feedService.getExplore(userDetails.getId(), pageable);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}