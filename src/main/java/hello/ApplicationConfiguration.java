package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfiguration  {

    @Value("${file_persist_options.type}")
    private String persistType;


    //private DataprotectionConfiguration dataprotectionConfiguration;

    @Bean
    public S3Helper getS3Helper(){
        S3Helper helper = getFilePersisterFactory().getFilePersister(persistType);
        return helper;
    }

    @Bean
    public FilePersisterFactory getFilePersisterFactory(){
       return new FilePersisterFactoryImpl();
    }

}