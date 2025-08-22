package com.luckyb.domain.checkin;

import com.luckyb.global.util.QRCodeGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class QRCodeIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    private MockMvc mockMvc;

    @Test
    @DisplayName("QR 코드 생성 API가 정상적으로 동작해야 한다")
    void generateQRCode_Success() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String testShelterId = "test-shelter-123";
        
        mockMvc.perform(get("/api/v1/qr/generate/{shelterId}", testShelterId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string("Content-Disposition", 
                        "inline; filename=\"qr-code-" + testShelterId + ".png\""));
    }

    @Test
    @DisplayName("QR 코드 디코딩 API가 정상적으로 동작해야 한다")
    void decodeQRContent_Success() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String testShelterId = "test-shelter-456";
        String qrContent = "luckyb://checkin/" + testShelterId;
        
        mockMvc.perform(post("/api/v1/qr/decode")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(qrContent))
                .andExpect(status().isOk())
                .andExpect(content().string(testShelterId));
    }

    @Test
    @DisplayName("잘못된 QR 코드 내용 디코딩 시 에러가 발생해야 한다")
    void decodeQRContent_InvalidContent_ThrowsError() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        String invalidQrContent = "invalid-qr-content";
        
        mockMvc.perform(post("/api/v1/qr/decode")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(invalidQrContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("QR 코드 유틸리티가 정상적으로 동작해야 한다")
    void qrCodeGenerator_UtilityMethods_Success() {
        String testShelterId = "test-shelter-789";
        
        // QR 코드 생성 테스트
        byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(testShelterId);
        assertThat(qrCodeBytes).isNotNull();
        assertThat(qrCodeBytes.length).isGreaterThan(0);
        
        // QR 코드 내용 디코딩 테스트
        String qrContent = "luckyb://checkin/" + testShelterId;
        String decodedShelterId = qrCodeGenerator.decodeQRContent(qrContent);
        assertThat(decodedShelterId).isEqualTo(testShelterId);
    }

    @Test
    @DisplayName("QR 코드 파일 저장이 정상적으로 동작해야 한다")
    void saveQRCodeToFile_Success() {
        String testShelterId = "test-shelter-file";
        String filePath = "test-qr-code.png";
        
        // 파일 저장 테스트
        qrCodeGenerator.saveQRCodeToFile(testShelterId, filePath);
        
        // 파일이 생성되었는지 확인
        java.io.File file = new java.io.File(filePath);
        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isGreaterThan(0);
        
        // 테스트 후 파일 삭제
        file.delete();
    }
} 