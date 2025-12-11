package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.ProfileUpdateRequest;
import com.example.instagramapi.dto.response.UserProfileResponse;
import com.example.instagramapi.dto.response.UserResponse;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.FollowRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    // TODO: PostRepository, FollowRepository 추가 후 주입

    public UserProfileResponse getProfile(String username, Long currentUserId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        long postCount = postRepository.countByUserId(user.getId());
        long followerCount = followRepository.countByFollowingId(user.getId());
        long followingCount = followRepository.countByFollowerId(user.getId());
        boolean isFollowing = currentUserId != null && followRepository.existsByFollowerIdAndFollowingId(currentUserId, user.getId());

        return UserProfileResponse.of(user, postCount, followerCount, followingCount, isFollowing);
    }

    @Transactional
    public UserResponse updateProfile(String username, Long currentUserId, ProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.NOT_PROFILE_OWNER);
        }

        user.updateProfile(request.getName(), request.getBio(), request.getProfileImageUrl());
        return UserResponse.from(user);
    }
}
