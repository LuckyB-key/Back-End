package com.luckyb.domain.checkin.controller;

import com.luckyb.global.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeGenerator qrCodeGenerator;

    /**
     * 대피소 ID를 기반으로 QR 코드를 생성합니다.
     * 
     * @param shelterId 대피소 ID
     * @return QR 코드 이미지
     */
    @GetMapping("/generate/{shelterId}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable String shelterId) {
        byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(shelterId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeBytes.length);
        headers.set("Content-Disposition", "inline; filename=\"qr-code-" + shelterId + ".png\"");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCodeBytes);
    }

    /**
     * QR 코드 내용을 디코딩합니다 (테스트용).
     * 
     * @param qrContent QR 코드 내용
     * @return 디코딩된 대피소 ID
     */
    @PostMapping("/decode")
    public ResponseEntity<String> decodeQRContent(@RequestBody String qrContent) {
        try {
            String shelterId = qrCodeGenerator.decodeQRContent(qrContent);
            return ResponseEntity.ok(shelterId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 테스트용 QR 코드를 파일로 저장합니다.
     * 
     * @param shelterId 대피소 ID
     * @return 저장 결과 메시지
     */
    @PostMapping("/save/{shelterId}")
    public ResponseEntity<String> saveQRCodeToFile(@PathVariable String shelterId) {
        try {
            String filePath = "qr-code-" + shelterId + ".png";
            qrCodeGenerator.saveQRCodeToFile(shelterId, filePath);
            return ResponseEntity.ok("QR 코드가 파일에 저장되었습니다: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("QR 코드 저장에 실패했습니다: " + e.getMessage());
        }
    }
} 