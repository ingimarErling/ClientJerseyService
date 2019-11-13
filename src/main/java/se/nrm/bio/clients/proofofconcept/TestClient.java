package se.nrm.bio.clients.proofofconcept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * java -Xms512m -Xmx15G
 *
 * @author ingimar
 */
public class TestClient {

//    private final static Logger logger = Logger.getLogger(TestClient.class);

    public static void main(String[] args) throws IOException {

        TestClient c = new TestClient();
        int healthOfServer = 404;
        healthOfServer = c.healthOfServer();
        System.out.println("Main is " + healthOfServer);

        if (healthOfServer == 200) {
            System.out.println("Server is running \n");
            HttpResponse response = c.testing();
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Client posted and response was "+statusCode);
        } else {
            System.out.println("Server is not running ");
        }
    }

    private int healthOfServer() throws IOException {
        int status = 404;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/JerseyServer/rest/upload/hello");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        status = response.getStatusLine().getStatusCode();
        return status;

    }

    private HttpResponse testing() throws FileNotFoundException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String fileName = "testbild-svt-666.png";
//        String fileName = "2GB.zip";
        String fileName = "3GB.zip";
        String filePath = "/tmp/".concat(fileName);
        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("owner", "ingimar")
                .addTextBody("fileName", fileName).addTextBody("workgroupId", "XXX").addBinaryBody("content", new File(filePath)).build();

        HttpPost httpPost = new HttpPost("http://localhost:8080/JerseyServer/rest/upload/other");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);

//        HttpEntity result = response.getEntity();
        return response;
    }
}
