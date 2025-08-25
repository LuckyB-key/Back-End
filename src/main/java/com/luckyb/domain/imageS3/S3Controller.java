  package com.luckyb.domain.imageS3;

  import lombok.RequiredArgsConstructor;
  import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;
  import org.springframework.web.multipart.MultipartFile;

  @RestController
  @RequiredArgsConstructor
  @RequestMapping("/api/v1")
  @ConditionalOnProperty(name = "cloud.aws.credentials.access-key", havingValue = "true", matchIfMissing = false)
  public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("multipartFile") MultipartFile file) {
      return ResponseEntity.ok(s3Service.uploadAndGetUrl(file));
    }
  }
