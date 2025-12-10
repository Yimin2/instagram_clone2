package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.LikeResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.PostLike;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.PostLikeRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse like(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        PostLike postLike  = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(postLike);

        return LikeResponse.of(true, postLikeRepository.countByPostId(postId));
    }

    @Transactional
    public LikeResponse unlike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        PostLike postLike = postLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_LIKED));

        postLikeRepository.delete(postLike);

        long likeCount = postLikeRepository.countByPostId(postId);
        return LikeResponse.of(false, likeCount);
    }

    public LikeResponse getLikeStatus(Long postId, Long userId) {
        if(!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        boolean liked = userId != null && postLikeRepository.existsByUserIdAndPostId(userId, postId);
        long likeCount = postLikeRepository.countByPostId(postId);

        return LikeResponse.of(liked,likeCount);
    }
}
