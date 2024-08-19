package like.lion.way.file.service;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    void uploadFile(MultipartFile file, String key);
    InputStream downloadFile(String key);
}
