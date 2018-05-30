package hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class PersistFileResponse {

    private String message;
    private String path;

    public PersistFileResponse(String message){
        this.message = message;
    }

}
