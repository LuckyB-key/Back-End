package com.luckyb.global.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class QRCodeGenerator {

    private static final int DEFAULT_SIZE = 300;
    private static final String DEFAULT_FORMAT = "PNG";

    /**
     * 대피소 ID를 포함한 QR 코드를 생성합니다.
     * 
     * @param shelterId 대피소 ID
     * @return QR 코드 이미지의 바이트 배열
     */
    public byte[] generateQRCode(String shelterId) {
        return generateQRCode(shelterId, DEFAULT_SIZE);
    }

    /**
     * 대피소 ID를 포함한 QR 코드를 생성합니다.
     * 
     * @param shelterId 대피소 ID
     * @param size QR 코드 크기
     * @return QR 코드 이미지의 바이트 배열
     */
    public byte[] generateQRCode(String shelterId, int size) {
        try {
            String qrContent = createQRContent(shelterId);
            BufferedImage qrImage = createQRCodeImage(qrContent, size);
            return convertToBytes(qrImage);
        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("QR 코드 생성에 실패했습니다.", e);
        }
    }

    /**
     * QR 코드를 파일로 저장합니다.
     * 
     * @param shelterId 대피소 ID
     * @param filePath 저장할 파일 경로
     */
    public void saveQRCodeToFile(String shelterId, String filePath) {
        try {
            String qrContent = createQRContent(shelterId);
            BufferedImage qrImage = createQRCodeImage(qrContent, DEFAULT_SIZE);
            File file = new File(filePath);
            ImageIO.write(qrImage, DEFAULT_FORMAT, file);
            log.info("QR 코드가 파일에 저장되었습니다: {}", filePath);
        } catch (Exception e) {
            log.error("QR 코드 파일 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("QR 코드 파일 저장에 실패했습니다.", e);
        }
    }

    /**
     * QR 코드 내용을 생성합니다.
     * 
     * @param shelterId 대피소 ID
     * @return QR 코드에 포함될 내용
     */
    private String createQRContent(String shelterId) {
        return String.format("luckyb://checkin/%s", shelterId);
    }

    /**
     * QR 코드 이미지를 생성합니다.
     * 
     * @param content QR 코드 내용
     * @param size QR 코드 크기
     * @return QR 코드 이미지
     */
    private BufferedImage createQRCodeImage(String content, int size) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(Color.BLACK);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        graphics.dispose();
        return image;
    }

    /**
     * BufferedImage를 바이트 배열로 변환합니다.
     * 
     * @param image 변환할 이미지
     * @return 바이트 배열
     */
    private byte[] convertToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, DEFAULT_FORMAT, baos);
        return baos.toByteArray();
    }

    /**
     * QR 코드 내용을 디코딩합니다 (테스트용).
     * 
     * @param qrContent QR 코드 내용
     * @return 대피소 ID
     */
    public String decodeQRContent(String qrContent) {
        if (qrContent == null || qrContent.trim().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 QR 코드 내용입니다: " + qrContent);
        }
        if (qrContent.startsWith("luckyb://checkin/")) {
            return qrContent.substring("luckyb://checkin/".length());
        }
        throw new IllegalArgumentException("유효하지 않은 QR 코드 내용입니다: " + qrContent);
    }
} 