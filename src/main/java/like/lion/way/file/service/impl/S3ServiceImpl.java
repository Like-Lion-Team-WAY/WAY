package like.lion.way.file.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;
import like.lion.way.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client; //AWS에서 지원해주는 인터페이스.

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            //파일저장방식 변경
            String uuid = UUID.randomUUID().toString(); //uuid자동으로발급
            String datePath = LocalDate.now().toString().replace("-", "/");//년월일 생성
            String key = datePath + "/" + uuid;//년월일+/uuid

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            return key;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    @Override
    public void deleteFile(String file) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)  // 버킷 이름을 지정합니다.
                    .key(file)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }
}
