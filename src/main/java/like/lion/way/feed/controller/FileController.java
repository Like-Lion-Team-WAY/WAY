package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import like.lion.way.file.service.S3Service;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
public class FileController {

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
}

