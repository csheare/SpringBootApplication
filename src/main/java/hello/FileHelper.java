package hello;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHelper {

    void printFile();
    void persistFile(String bucketName, String key, MultipartFile input ) throws IOException;

}
