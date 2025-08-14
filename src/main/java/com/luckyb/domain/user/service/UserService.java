package com.luckyb.domain.user.service;

import com.luckyb.domain.user.dto.UserMeResponse;
import com.luckyb.domain.user.dto.UserMeUpdateRequest;
import com.luckyb.domain.user.entity.User;
import com.luckyb.domain.user.repository.UserRepository;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 정보 조회
     */
    public UserMeResponse getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
        
        return UserMeResponse.from(user);
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public UserMeResponse updateUserInfo(String userId, UserMeUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

        // 닉네임 중복 체크 (변경하려는 닉네임이 현재 닉네임과 다른 경우에만)
        if (request.getNickname() != null && 
            !request.getNickname().equals(user.getNickname()) &&
            userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATE_NICKNAME.getMessage() + ": " + request.getNickname());
        }

        // 이메일 중복 체크 (변경하려는 이메일이 현재 이메일과 다른 경우에만)
        if (request.getEmail() != null && 
            !request.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(ErrorCode.DUPLICATE_EMAIL.getMessage() + ": " + request.getEmail());
        }

        user.updateInfo(request.getNickname(), request.getEmail(), request.getPreferences());
        User savedUser = userRepository.save(user);

        log.info("사용자 정보가 수정되었습니다. userId: {}", userId);
        return UserMeResponse.from(savedUser);
    }

    /**
     * 새 사용자 생성
     */
    @Transactional
    public User createUser() {
        try {
            User user = new User(User.UserRole.USER);
            
            User savedUser = userRepository.save(user);
            log.info("새 사용자가 생성되었습니다. userId: {}", savedUser.getUserId());
            
            return savedUser;
        } catch (Exception e) {
            log.error("사용자 생성 중 오류 발생", e);
            throw new RuntimeException("사용자 생성에 실패했습니다.", e);
        }
    }
} 