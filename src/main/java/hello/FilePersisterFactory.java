package hello;

public interface FilePersisterFactory {

    S3Helper getFilePersister(String type);
}

