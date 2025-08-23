package com.luckyb.domain.checkin.service;

import com.luckyb.domain.checkin.dto.CheckinResponse;
import com.luckyb.domain.checkin.entity.Checkin;
import com.luckyb.domain.checkin.repository.CheckinRepository;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.shelter.repository.ShelterRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.global.exception.ShelterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class CheckinServiceTest {

    @Mock
    private CheckinRepository checkinRepository;

    @Mock
    private ShelterRepository shelterRepository;

    @InjectMocks
    private CheckinService checkinService;

    private User testUser;
    private Shelter testShelter;
    private Checkin testCheckin;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@example.com")
                .nickname("테스트유저")
                .build();

        testShelter = Shelter.builder()
                .user(testUser)
                .name("테스트 대피소")
                .capacity(100)
                .build();

        testCheckin = Checkin.builder()
                .user(testUser)
                .shelter(testShelter)
                .build();
        // 테스트를 위해 ID 설정 (실제로는 JPA가 자동 설정)
        testCheckin = new Checkin(testUser, testShelter);
    }

    @Test
    @DisplayName("정상적인 체크인이 성공해야 한다")
    void checkin_Success() {
        // given
        String shelterId = "test-shelter-id";
        when(shelterRepository.findById(shelterId)).thenReturn(Optional.of(testShelter));
        when(checkinRepository.findActiveCheckinByUserAndShelter(testUser, shelterId))
                .thenReturn(Optional.empty());
        
        // Mock Checkin with ID and checkinTime using thenAnswer
        when(checkinRepository.save(any(Checkin.class))).thenAnswer(invocation -> {
            Checkin checkin = invocation.getArgument(0);
            // Reflection을 사용하여 ID와 checkinTime 설정
            try {
                java.lang.reflect.Field idField = Checkin.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(checkin, 1L);
                
                java.lang.reflect.Field checkinTimeField = Checkin.class.getDeclaredField("checkinTime");
                checkinTimeField.setAccessible(true);
                checkinTimeField.set(checkin, java.time.LocalDateTime.now());
            } catch (Exception e) {
                // Reflection 실패 시 무시
            }
            return checkin;
        });

        // when
        CheckinResponse response = checkinService.checkin(testUser, shelterId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCheckinId()).isEqualTo("1");
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 대피소에 체크인 시 예외가 발생해야 한다")
    void checkin_WithNonExistentShelter_ThrowsException() {
        // given
        String nonExistentShelterId = "non-existent-shelter";
        when(shelterRepository.findById(nonExistentShelterId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> checkinService.checkin(testUser, nonExistentShelterId))
                .isInstanceOf(ShelterNotFoundException.class);
    }

    @Test
    @DisplayName("이미 체크인된 상태에서 중복 체크인 시 예외가 발생해야 한다")
    void checkin_WithDuplicateCheckin_ThrowsException() {
        // given
        String shelterId = "test-shelter-id";
        when(shelterRepository.findById(shelterId)).thenReturn(Optional.of(testShelter));
        when(checkinRepository.findActiveCheckinByUserAndShelter(testUser, shelterId))
                .thenReturn(Optional.of(testCheckin));

        // when & then
        assertThatThrownBy(() -> checkinService.checkin(testUser, shelterId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 체크인된 상태입니다.");
    }
} 