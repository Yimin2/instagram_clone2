package com.example.instagramapi.config;

import com.example.instagramapi.entity.Post;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.repository.PostRepository;
import com.example.instagramapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 사용자 생성
        String encodedPassword = passwordEncoder.encode("test1234");

        User test = userRepository.save(
                User.builder()
                        .username("test")
                        .password(encodedPassword)
                        .name("test")
                        .email("test@test.com")
                        .build()
        );

        User admin = userRepository.save(
                User.builder()
                        .username("admin")
                        .password(encodedPassword)
                        .name("admin")
                        .email("admin@admin.com")
                        .build()
        );

        User user = userRepository.save(
                User.builder()
                        .username("user")
                        .password(encodedPassword)
                        .name("user")
                        .email("user@user.com")
                        .build()
        );

        // 게시물 생성
        Post post1 = postRepository.save(
                Post.builder()
                        .content("sample post1")
                        .imageUrl("/uploads/sample1.png")
                        .user(admin)
                        .build()
        );

        Post post2 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post3 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post4 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post5 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post6 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post7 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post8 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post9 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
        Post post10 = postRepository.save(
                Post.builder()
                        .content("sample post2")
                        .imageUrl("/uploads/sample2.jpg")
                        .user(test)
                        .build()
        );
    }
}