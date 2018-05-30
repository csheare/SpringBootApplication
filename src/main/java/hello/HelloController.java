package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@RestController
public class HelloController {

    @Autowired
    private S3Helper s3Helper;

    @RequestMapping("/")
    public String index() {return "Hello World!";}

    @PostMapping("/addFile/{fileName:.+}")
    public ResponseEntity addFile(@PathVariable String fileName) {
        try {
            s3Helper.persistFile("acom-emp-work-878616621923-us-east-1"
                    ,String.format("cshearer/%s", fileName)
                    ,new File("assets/" + fileName));
        }catch(Exception e){

            return new ResponseEntity("Exception", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
}

    @PostMapping("/streamFile")
    public ResponseEntity streamFile(@RequestParam("filename") MultipartFile file){
        try {
            s3Helper.persistFileStream("acom-emp-work-878616621923-us-east-1"
                    ,String.format("cshearer/%s", file.getOriginalFilename())
                    ,file.getInputStream());

        }catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}