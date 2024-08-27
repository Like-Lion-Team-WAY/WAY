package like.lion.way.file.service;

import java.io.InputStream;
import like.lion.way.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadFile(MultipartFile file);

    InputStream downloadFile(String key);

    void deleteFile(String file);

    ApiResponse<Object> apiUploadFile(MultipartFile file);

}
