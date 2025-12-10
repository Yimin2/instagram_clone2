package com.example.instagramapi.repository;

import com.example.instagramapi.entity.Comment;
import com.example.instagramapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH  c.user WHERE c.post.id = :postId")
    List<Comment> findByPostIdWithUser(@Param("postId") Long postId);

    long countByPostId(Long PostId);

    Long post(Post post);
}
