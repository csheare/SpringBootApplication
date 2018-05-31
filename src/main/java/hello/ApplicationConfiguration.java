package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfiguration  {

    @Bean
    public FileHelper getFileHelper() throws Exception {
        FileHelper helper = getFilePersisterFactory().getFilePersister();
        return helper;
    }

    @Bean
    public FilePersisterFactory getFilePersisterFactory(){
        FilePersisterFactory filePersisterFactory = new FilePersisterFactoryImpl(getFileDataConfiguration());
       return filePersisterFactory;
    }

    @Bean
    public FileDataConfiguration getFileDataConfiguration(){
        FileDataConfiguration fileDataConfiguration = new FileDataConfiguration();
        return fileDataConfiguration;
    }

}