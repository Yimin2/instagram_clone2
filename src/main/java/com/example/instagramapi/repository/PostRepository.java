package com.example.instagramapi.repository;

import com.example.instagramapi.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM  Post p JOIN FETCH p.user ORDER BY p.createdAt DESC")
    List<Post> findAllWithUser();

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE  p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE  p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Post> findByUserIdWithUser(@Param("userId") Long UserId);

    long countByUserId(Long userId);

    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC")
    Slice<Post> findAllWithUserPaging(Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    Slice<Post> findByUserIdsWithUserPaging(@Param("userIds")List<Long> userIds, Pageable pageable);

}
