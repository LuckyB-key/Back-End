package com.luckyb.domain.imageS3;

import static com.luckyb.global.exception.ErrorCode.IMAGE_UPLOAD_FAIL;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.luckyb.global.exception.S3Exception;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cloud.aws.credentials.access-key", havingValue = "true", matchIfMissing = false)
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String uploadAndGetUrl(MultipartFile file) {

    String originalFilename = file.getOriginalFilename() + UUID.randomUUID();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try {
      amazonS3.putObject(bucket, originalFilename, file.getInputStream(), metadata);
    } catch (IOException e) {
      throw new S3Exception(IMAGE_UPLOAD_FAIL);
    }

    return amazonS3.getUrl(bucket, originalFilename).toString();
  }
}

