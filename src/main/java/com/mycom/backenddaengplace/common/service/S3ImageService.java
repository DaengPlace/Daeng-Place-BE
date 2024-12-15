package com.mycom.backenddaengplace.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    public String uploadImage(MultipartFile file, String dirName) {
        validateFile(file);
        validateFileExtension(file.getOriginalFilename());

        try {
            return uploadToS3(file, dirName);
        } catch (IOException e) {
            log.error("Failed to upload image to S3", e);
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new RuntimeException("파일이 비어있거나 파일명이 올바르지 않습니다.");
        }
    }

    private void validateFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new RuntimeException("파일 확장자가 없습니다.");
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 가능)");
        }
    }

    private String uploadToS3(MultipartFile file, String dirName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFileName = dirName + "/" + UUID.randomUUID() + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream fileInputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    storedFileName,
                    fileInputStream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);  // 파일 공개 읽기 권한 설정

            s3Client.putObject(putObjectRequest);
            return s3Client.getUrl(bucketName, storedFileName).toString();
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String fileName = imageUrl.substring(imageUrl.indexOf(bucketName) + bucketName.length() + 1);
            s3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to delete image from S3: {}", imageUrl, e);
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
    }

    // 각 도메인별 디렉토리 상수
    public static final String USER_PROFILE_DIR = "user-profiles";
    public static final String DOG_BREED_DIR = "dog-breeds";
    public static final String REVIEW_DIR = "reviews";
    public static final String PET_PROFILE_DIR = "pet-profiles";
}
