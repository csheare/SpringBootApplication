package hello;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3HelperImpl implements FileHelper
{
    private AmazonS3 s3ClientUsEast = null;
    private long partSize;

    @Autowired
    private FileDataConfiguration fileDataConfiguration;



    public void printFile() {
        System.out.println("This is an S3 File Persister!");
    }

    public void persistFile(String bucketName, String key, MultipartFile file) throws IOException {
        partSize = fileDataConfiguration.getFileSizeThreshold();
        System.out.println(String.format("The file size is: %s, The threshold is: %s",file.getSize(),partSize));
        if(file.getSize() < partSize){
            System.out.println("Single File Upload");
            singleFileUpload(bucketName, key,file);
        }else{
            System.out.println("Multiple File Upload");
            multipleFileUpload(bucketName,key,file);
        }


    }

    private void singleFileUpload(String bucketName,String key,MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        ObjectMetadata meta = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(input);
        meta.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, byteArrayInputStream, meta);
        getS3ClientUsEast().putObject(putObjectRequest);
    }

    private void multipleFileUpload(String bucketName,String key,MultipartFile multiPartFile) throws IOException {
        File file = new File("/Users/cshearer/IdeaProjects/hello_world/assets/cat.jpg");
        long contentLength = file.length();
       try{
           //1) Get S3 Client : getS3ClientUSEast
           //2)Create a list of ETag objects.
           //You retrieve ETags for each object part uploaded, then, after each indic=vidual part has been uploaded, pass the list
           // of ETags tp the request to complete the upload

           List<PartETag> partETags = new ArrayList<PartETag>();

           //3) Initiate the multipart upload
           InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,key);
           InitiateMultipartUploadResult initResponse = getS3ClientUsEast().initiateMultipartUpload(initRequest);

           //4) Upload the file parts
           long filePosition = 0;
           for(int i = 1; filePosition < contentLength; i++){

               //Since the last part could be less than the threshold limit, adjust as needed
               partSize = Math.min(partSize, (contentLength - filePosition));

               //4a)Create the request to upload a part
               UploadPartRequest uploadRequest = new UploadPartRequest()
                       .withBucketName(bucketName)
                       .withKey(key)
                       .withUploadId(initResponse.getUploadId())
                       .withPartNumber(i)
                       .withFileOffset(filePosition)
                       .withFile(file)
                       .withPartSize(partSize);

               //4b)Upload the part and add the response's ETag to our list
               UploadPartResult uploadResult = getS3ClientUsEast().uploadPart(uploadRequest);
               partETags.add(uploadResult.getPartETag());

               filePosition += partSize;
           }

           //5) Complete the multipart Upload
           CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key, initResponse.getUploadId(), partETags);
           getS3ClientUsEast().completeMultipartUpload(compRequest);
       }catch(AmazonServiceException e){
           e.printStackTrace();
       }catch(SdkClientException e){
           e.printStackTrace();
       }
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
