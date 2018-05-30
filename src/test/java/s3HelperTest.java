import hello.S3Helper;
import hello.S3HelperImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertTrue;

public class s3HelperTest {
    String fileName;
    File file;
    InputStream stream;

    private S3Helper s3Helper;

    @Before
    public void setUp() throws IOException {
        System.out.println("SETTING UP...");
        s3Helper = new S3HelperImpl();
        UUID id = UUID.randomUUID();
        fileName = String.format("%s.txt",id.toString());
        file = new File(fileName);
        file.createNewFile();
        stream = new FileInputStream(file);
    }

    @After
    public void tearDown() {
        System.out.println("TEARING DOWN...");
        file.delete();
    }


    /***Integration Tests***/
    @Test
    public void s3PersistFileTest() throws IOException {
        s3Helper.persistFile("acom-emp-work-878616621923-us-east-1", String.format("cshearer/%s", file.toString()),file);
        String expected = String.format("S3Object [key=%s,bucket=%s]",String.format("cshearer/%s", file.toString()),"acom-emp-work-878616621923-us-east-1");
        String actual = s3Helper.getS3ClientUsEast().getObject("acom-emp-work-878616621923-us-east-1", String.format("cshearer/%s", file.toString())).toString();
        assertTrue(expected.equals(actual));
    }

   @Test
    public void s3PersistFileStreamTest() throws IOException{
        s3Helper.persistFileStream("acom-emp-work-878616621923-us-east-1", String.format("cshearer/%s", file.toString()),stream);
        assertTrue((String.format("S3Object [key=%s,bucket=%s]",String.format("cshearer/%s", file.toString()),"acom-emp-work-878616621923-us-east-1")).equals(
                (s3Helper.getS3ClientUsEast().getObject("acom-emp-work-878616621923-us-east-1", String.format("cshearer/%s", file.toString()))).toString()));

    }

    /***Unit Tests***/

    @Test
    public void testS3connection() throws Exception{

        //mocked  interface ...
        S3Helper s3Mock = mock(hello.S3Helper.class);

        doThrow(new RuntimeException("whoops")).when(s3Mock).persistFile(any(), any(), any());

        try {
            s3Mock.persistFile("","",null);
        }catch(Exception e){
            assertEquals("whoops",e.getMessage());
        }
    }

}