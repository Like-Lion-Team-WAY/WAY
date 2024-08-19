package like.lion.way.file.service;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadFile(MultipartFile file);

    InputStream downloadFile(String key);
    void deleteFile(String file);
}
