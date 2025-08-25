package com.luckyb.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QRCodeGeneratorTest {

  private QRCodeGenerator qrCodeGenerator;

  @BeforeEach
  void setUp() {
    qrCodeGenerator = new QRCodeGenerator();
  }

  @Test
  @DisplayName("QR 코드가 정상적으로 생성되어야 한다")
  void generateQRCode_Success() {
    // given
    String shelterId = "test-shelter-123";

    // when
    byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(shelterId);

    // then
    assertThat(qrCodeBytes).isNotNull();
    assertThat(qrCodeBytes.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("다양한 크기의 QR 코드가 정상적으로 생성되어야 한다")
  void generateQRCode_WithDifferentSizes_Success() {
    // given
    String shelterId = "test-shelter-456";
    int[] sizes = {100, 200, 300, 500};

    // when & then
    for (int size : sizes) {
      byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(shelterId, size);
      assertThat(qrCodeBytes).isNotNull();
      assertThat(qrCodeBytes.length).isGreaterThan(0);
    }
  }

  @Test
  @DisplayName("QR 코드 내용이 정상적으로 디코딩되어야 한다")
  void decodeQRContent_Success() {
    // given
    String shelterId = "test-shelter-789";
    String qrContent = "luckyb://checkin/" + shelterId;

    // when
    String decodedShelterId = qrCodeGenerator.decodeQRContent(qrContent);

    // then
    assertThat(decodedShelterId).isEqualTo(shelterId);
  }

  @Test
  @DisplayName("잘못된 QR 코드 내용 디코딩 시 예외가 발생해야 한다")
  void decodeQRContent_InvalidContent_ThrowsException() {
    // given
    String invalidQrContent = "invalid-qr-content";

    // when & then
    assertThatThrownBy(() -> qrCodeGenerator.decodeQRContent(invalidQrContent))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("유효하지 않은 QR 코드 내용입니다: " + invalidQrContent);
  }

  @Test
  @DisplayName("빈 QR 코드 내용 디코딩 시 예외가 발생해야 한다")
  void decodeQRContent_EmptyContent_ThrowsException() {
    // given
    String emptyQrContent = "";

    // when & then
    assertThatThrownBy(() -> qrCodeGenerator.decodeQRContent(emptyQrContent))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("유효하지 않은 QR 코드 내용입니다: " + emptyQrContent);
  }

  @Test
  @DisplayName("null QR 코드 내용 디코딩 시 예외가 발생해야 한다")
  void decodeQRContent_NullContent_ThrowsException() {
    // when & then
    assertThatThrownBy(() -> qrCodeGenerator.decodeQRContent(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("유효하지 않은 QR 코드 내용입니다: null");
  }

  @Test
  @DisplayName("QR 코드 파일 저장이 정상적으로 동작해야 한다")
  void saveQRCodeToFile_Success() {
    // given
    String testShelterId = "test-shelter-file";
    String filePath = "test-qr-code.png";

    // when
    qrCodeGenerator.saveQRCodeToFile(testShelterId, filePath);

    // then
    java.io.File file = new java.io.File(filePath);
    assertThat(file.exists()).isTrue();
    assertThat(file.length()).isGreaterThan(0);

    // cleanup - 주석 처리하여 파일 유지
    // file.delete();
  }
} 