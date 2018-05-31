package hello;


public class FilePersisterFactoryImpl implements FilePersisterFactory {

    private FileDataConfiguration fileDataConfiguration;

    public FilePersisterFactoryImpl(FileDataConfiguration fileDataConfiguration){

        this.fileDataConfiguration = fileDataConfiguration;
    }

    public FileHelper getFilePersister() throws Exception {
        FileHelper fileHelper = getFilePersister(fileDataConfiguration.persistType);
        return fileHelper;

    }

    public FileHelper getFilePersister(String type) throws Exception {

        FileHelper fileHelper = null;

        switch (type){

            case "s3":
                fileHelper = new S3HelperImpl();
                break;

            case "local":
                fileHelper = new LocalHelperImpl();
                break;

            default:
                System.out.println("Default");
                throw new Exception(String.format("%s is not a valid file persister type",type));
        }

        return fileHelper;
    }
}
