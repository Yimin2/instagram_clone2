package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.CommentCreateRequest;
import com.example.instagramapi.dto.response.CommentResponse;
import com.example.instagramapi.entity.Comment;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long postId, Long userId, CommentCreateRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();

        return CommentResponse.from(commentRepository.save(comment));
    }

    public List<CommentResponse> findByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findByPostIdWithUser(postId);
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if(!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_COMMENT_OWNER);
        }

        commentRepository.delete(comment);
    }
}
