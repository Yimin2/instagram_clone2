package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.PostResponse;
import com.example.instagramapi.dto.response.SliceResponse;
import com.example.instagramapi.entity.Post;
import com.example.instagramapi.repository.CommentRepository;
import com.example.instagramapi.repository.FollowRepository;
import com.example.instagramapi.repository.PostLikeRepository;
import com.example.instagramapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    public SliceResponse<PostResponse> getFeed(Long userId, Pageable pageable) {
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);

        followingIds.add(userId);

        Slice<Post> posts = postRepository.findByUserIdsWithUserPaging(followingIds, pageable);

        List<PostResponse> content = posts.getContent()
                .stream()
                .map(post -> toPostResponseWithStats(post, userId))
                .toList();

        return SliceResponse.from(posts, content);
    }

    public SliceResponse<PostResponse> getExplore(Long userId, Pageable pageable) {
        Slice<Post> posts = postRepository.findAllWithUserPaging(pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> toPostResponseWithStats(post,userId))
                .toList();
        return SliceResponse.from(posts, content);
    }

    private PostResponse toPostResponseWithStats(Post post, Long currentUserId) {
        boolean liked = currentUserId != null && postLikeRepository.existsByUserIdAndPostId(currentUserId, post.getId());
        long likeCount = postLikeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, liked, likeCount, commentCount);
    }
}
