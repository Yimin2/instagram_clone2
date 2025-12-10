package com.example.instagramapi.repository;

import com.example.instagramapi.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long PostId);
    boolean existsByUserIdAndPostId(Long UserId, Long postId);
    long countByPostId(Long postId);
}
