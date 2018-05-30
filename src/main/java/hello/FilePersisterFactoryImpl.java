package hello;


public class FilePersisterFactoryImpl implements FilePersisterFactory {

    public S3Helper getFilePersister(String type){

        switch (type){

            case "s3":
                System.out.println("TEST");
                return new S3HelperImpl();
            default:
                System.out.println("Default");
                return new S3HelperImpl();
        }
    }
}
