package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.FollowResponse;
import com.example.instagramapi.dto.response.UserResponse;
import com.example.instagramapi.entity.Follow;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.FollowRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowerService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private FollowResponse getFollowCounts(Long userId, boolean isFollowing) {
        long followerCount = followRepository.countByFollowingId(userId);
        long followingCount = followRepository.countByFollowerId(userId);

        return FollowResponse.of(isFollowing, followerCount, followingCount);
    }

    @Transactional
    public FollowResponse follow(String username, Long followerId) {
        User following = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        if (following.getId()
                .equals(follower.getId())) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId())) {
            throw new CustomException(ErrorCode.ALREADY_FOLLOWING);
        }

        followRepository.save(follow);

        return getFollowCounts(followerId, true);
    }

    @Transactional
    public FollowResponse unfollow(String username, Long followerId) {
        User following = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, following.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        followRepository.delete(follow);
        return getFollowCounts(follow.getId(), false);
    }

    public List<UserResponse> getFollowers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowersByFollowingId(user.getId())
                .stream()
                .map(follow -> UserResponse.from(follow.getFollower()))
                .toList();
    }

    public List<UserResponse> getFollowings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowingsByFollowerId(user.getId())
                .stream()
                .map(follow -> UserResponse.from(follow.getFollowing()))
                .toList();
    }
}
