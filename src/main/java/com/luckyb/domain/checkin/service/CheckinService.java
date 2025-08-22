package com.luckyb.domain.checkin.service;

import com.luckyb.domain.checkin.dto.CheckinResponse;
import com.luckyb.domain.checkin.dto.MyCheckinListRequest;
import com.luckyb.domain.checkin.dto.MyCheckinListResponse;
import com.luckyb.domain.checkin.entity.Checkin;
import com.luckyb.domain.checkin.repository.CheckinRepository;
import com.luckyb.domain.shelter.entity.Shelter;
import com.luckyb.domain.shelter.repository.ShelterRepository;
import com.luckyb.domain.user.entity.User;
import com.luckyb.global.exception.ErrorCode;
import com.luckyb.global.exception.ShelterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final ShelterRepository shelterRepository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional
    public CheckinResponse checkin(User user, String shelterId) {
        // 대피소 존재 여부 확인
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new ShelterNotFoundException(ErrorCode.SHELTER_NOT_FOUND));

        // 이미 체크인된 상태인지 확인
        checkinRepository.findActiveCheckinByUserAndShelter(user, shelterId)
                .ifPresent(existingCheckin -> {
                    throw new IllegalStateException("이미 체크인된 상태입니다.");
                });

        // 새로운 체크인 생성
        Checkin checkin = Checkin.builder()
                .user(user)
                .shelter(shelter)
                .build();

        Checkin savedCheckin = checkinRepository.save(checkin);

        return CheckinResponse.builder()
                .checkinId(savedCheckin.getId().toString())
                .timestamp(savedCheckin.getCheckinTime().format(ISO_FORMATTER))
                .build();
    }

    public MyCheckinListResponse getMyCheckins(User user, MyCheckinListRequest request) {
        // 페이징 설정
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        // 사용자의 체크인 기록 조회
        Page<Checkin> checkinPage = checkinRepository.findByUserOrderByCheckinTimeDesc(user, pageable);

        List<MyCheckinListResponse.CheckinRecord> records = checkinPage.getContent().stream()
                .map(this::convertToCheckinRecord)
                .collect(Collectors.toList());

        return MyCheckinListResponse.builder()
                .data(records)
                .build();
    }

    private MyCheckinListResponse.CheckinRecord convertToCheckinRecord(Checkin checkin) {
        return MyCheckinListResponse.CheckinRecord.builder()
                .checkinId(checkin.getId().toString())
                .shelterId(checkin.getShelter().getShelterId())
                .shelterName(checkin.getShelter().getName())
                .checkinTime(checkin.getCheckinTime().format(ISO_FORMATTER))
                .checkoutTime(checkin.getCheckoutTime() != null ? 
                        checkin.getCheckoutTime().format(ISO_FORMATTER) : null)
                .build();
    }

    public long getActiveCheckinCount(String shelterId) {
        return checkinRepository.countActiveCheckinsByShelter(shelterId);
    }
} 