package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.PostCreateRequest;
import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.PostLikeRepository;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .user(user)
                .build();

        return PostResponse.from(postRepository.save(post));
    }

    public List<PostResponse> findAll(Long currentUserId) {
        List<Post> posts = postRepository.findAllWithUser();
        return posts.stream()
                .map(post -> toPostResponseWithStats(post, currentUserId))
                .toList();
    }

    public PostResponse findById(Long postId, Long currentUserId) {
        Post post = getPostByIdOrThrow(postId);

        return toPostResponseWithStats(post, currentUserId);
    }

    public List<PostResponse> findByUsername(String username, Long currentUserId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Post> posts = postRepository.findByUserIdWithUser(user.getId());

        return posts.stream()
                .map(post -> toPostResponseWithStats(post, currentUserId))
                .toList();
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = getPostByIdOrThrow(postId);

        if(!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }

        postRepository.delete(post);
    }

    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private PostResponse toPostResponseWithStats(Post post, Long currentUserId) {
        boolean liked = currentUserId != null && postLikeRepository.existsByUserIdAndPostId(currentUserId, post.getId());
        long likeCount = postLikeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, liked, likeCount, commentCount);
    }
}
