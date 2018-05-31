package hello;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalHelperImpl implements FileHelper{


    public void printFile() {
        System.out.println("This is a Local File!");
    }

    public void persistFile(String bucketName, String key, MultipartFile file) throws IOException {
        OutputStream os = null;
        InputStream input = file.getInputStream();
        try{
            os = new FileOutputStream(String.format("/Users/cshearer/IdeaProjects/hello_world/local/%s",key));
            byte[] buffer = new  byte[1024];
            int length;
            while((length = input.read(buffer)) > 0){
                os.write(buffer,0,length);
            }
        }finally{
            os.close();
            input.close();
        }
    }
}
