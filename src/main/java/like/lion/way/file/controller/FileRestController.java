package like.lion.way.file.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;
import like.lion.way.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
public class FileRestController {

    private final S3Service s3Service;

    //s3 이미지 보여주기
    @GetMapping("/display")
    public ResponseEntity<StreamingResponseBody> display(@RequestParam("filename") String filename) {
        if (!StringUtils.hasText(filename)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        InputStream inputStream = s3Service.downloadFile(filename);

        StreamingResponseBody responseBody = outputStream -> {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };

        return ResponseEntity.ok()
                .body(responseBody);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            //파일저장방식 변경
            String uuid = UUID.randomUUID().toString(); //uuid자동으로발급
            String datePath = LocalDate.now().toString().replace("-", "/");//년월일 생성
            String key = datePath + "/" + uuid;//년월일+/uuid

            s3Service.uploadFile(file);//버킷에저장하는 서비스호출

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }
}
