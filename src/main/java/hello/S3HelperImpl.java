package hello;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class S3HelperImpl implements S3Helper {
    private AmazonS3 s3ClientUsEast = null;


    public void persistFile(String bucketName, String key,File file)throws IOException {
        getS3ClientUsEast().putObject(bucketName, key, file);
    }

    public void persistFileStream(String bucketName, String key,InputStream input) throws IOException {
        ObjectMetadata meta = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(input);
        meta.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, byteArrayInputStream, meta);
        getS3ClientUsEast().putObject(putObjectRequest);
    }

    public AmazonS3 getS3ClientUsEast() {

        if (s3ClientUsEast == null)
        {
            try {

                s3ClientUsEast = AmazonS3ClientBuilder
                        .standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build();
            }
            catch (Exception ex){

                s3ClientUsEast = new AmazonS3Client(new ProfileCredentialsProvider("default"));

            }
        }

        return s3ClientUsEast;
    }
}
