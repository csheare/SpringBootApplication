package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HelloController {

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private FileDataConfiguration fileDataConfiguration;

    @RequestMapping("/")
    public String index() {
        String source = fileDataConfiguration.getSource() ;
        return source;
    }

    @PostMapping("/streamFile")
    public ResponseEntity streamFile(@RequestParam("filename") MultipartFile file){
        try {
            fileHelper.printFile();
            fileHelper.persistFile("acom-emp-work-878616621923-us-east-1"
                    ,String.format("cshearer/%s", file.getOriginalFilename())
                    ,file);

        }catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}