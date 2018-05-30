package hello;

import com.amazonaws.services.s3.AmazonS3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface S3Helper {
    void persistFile(String bucketName, String key,File file) throws IOException;
    void persistFileStream(String bucketName, String key,InputStream file ) throws IOException;
    AmazonS3 getS3ClientUsEast();
}
