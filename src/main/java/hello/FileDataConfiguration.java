package hello;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="data")
public class FileDataConfiguration {

    String source;
    String persistType;
    boolean important;
    long fileSizeThreshold;

    public String getSource(){
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPersistType() {
        return persistType;
    }

    public void setPersistType(String persistType) {
        this.persistType = persistType;
    }

    public boolean isImportant(){
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
    public long getFileSizeThreshold() {
        return fileSizeThreshold;
    }

    public void setFileSizeThreshold(long fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }
}
