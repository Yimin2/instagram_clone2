package com.example.instagramapi.service;

import com.example.instagramapi.dto.request.LoginRequest;
import com.example.instagramapi.dto.request.SignupRequest;
import com.example.instagramapi.dto.response.KakaoTokenResponse;
import com.example.instagramapi.dto.response.KakaoUserResponse;
import com.example.instagramapi.dto.response.TokenResponse;
import com.example.instagramapi.dto.response.UserResponse;
import com.example.instagramapi.entity.AuthProvider;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.UserRepository;
import com.example.instagramapi.security.JwtProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final KakaoOauthService kakaoOauthService;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        String token = jwtProvider.createToken(user.getUsername());

        return TokenResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional
    public TokenResponse kakaoLogin(String code) {
        KakaoTokenResponse tokenResponse = kakaoOauthService.getToken(code);
        KakaoUserResponse userInfo = kakaoOauthService.getUserInfo(tokenResponse.getAccessToken());
        User user = userRepository.findByProviderAndProviderId(AuthProvider.KAKAO, userInfo.getId())
                .orElseGet(() -> createKakaoUser(userInfo));
        user.updateOAuthProfile(
                userInfo.getKakaoAccount().getProfile().getNickname(),
                userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        String token = jwtProvider.createToken(user.getUsername());
        return TokenResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    // 카카오 회원가입
    private User createKakaoUser(KakaoUserResponse userInfo) {
        String username = "kakao_" + userInfo.getId();
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(username))
                .name(userInfo.getKakaoAccount().getProfile().getNickname())
                .provider(AuthProvider.KAKAO)
                .providerId(userInfo.getId())
                .build();
        return userRepository.save(user);
    }
}
