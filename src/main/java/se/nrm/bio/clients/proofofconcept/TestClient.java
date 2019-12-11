package se.nrm.bio.clients.proofofconcept;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
/**
 * java -jar -Xms512m -Xmx15G target/restfulClient.jar
 *
 * @author ingimar
 */
public class TestClient {

//    private final static Logger logger = Logger.getLogger(TestClient.class);
    public static void main(String[] args) throws IOException {

        TestClient c = new TestClient();
        int healthOfServer = 404;
        healthOfServer = c.healthOfServer();
        System.out.println("Health of the mediaserver  is " + healthOfServer);

        if (healthOfServer == 200) {
//        if (healthOfServer == 404) {
            System.out.println("Server is running \n");
//            HttpResponse response = c.testingOne(); 
            HttpResponse response = c.testing();
//            HttpResponse response = c.testingThree();
//            Response response = c.testing_Three();
//            Response response = c.testing_All();

        } else {
            System.out.println("Server is not running ");
        }
    }

    private int healthOfServer() throws IOException {
        int status = 404;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/MediaServerResteasy/rest/file/running");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        System.out.println(response.getStatusLine().getReasonPhrase());
        status = response.getStatusLine().getStatusCode();
        return status;

    }

    private HttpResponse testing() throws FileNotFoundException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
//        String filename = "testbild-svt-666.png";
        String filename = "1G.zip";

        String filePath = "/tmp/".concat(filename);
        File file = new File(filePath);
        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("owner", "ingimar")
                .addTextBody("filename", filename).addBinaryBody("content", new File(filePath), ContentType.DEFAULT_BINARY, filename).build();
        HttpPost httpPost = new HttpPost("http://localhost:8080/MediaServerResteasy/rest/file/upload");
        httpPost.setEntity(entity);
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String entityString = EntityUtils.toString(entity1);
        client.close();

        return response;
    }

    // fungerar ; fredagen, 15 Nov 2019.
//    private HttpResponse testingOne() throws FileNotFoundException, IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("owner", "ingimar").build();
//
//        HttpPost httpPost = new HttpPost("http://localhost:8080/MediaServerResteasy/media/vegatestOwner");
//        httpPost.setEntity(entity);
//        HttpResponse response = httpClient.execute(httpPost);
//        String reasonPhrase = response.getStatusLine().getReasonPhrase();
//        System.out.println("reasonPhrase ".concat(reasonPhrase));
//
//        HttpEntity entity1 = response.getEntity();
//        String entityString = EntityUtils.toString(entity1);
//        System.out.println("String from server is " + entityString);
//
//        return response;
//    }
//
//    private Response testing_Three() throws FileNotFoundException, IOException {
//        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
//        FormDataMultiPart form = new FormDataMultiPart();ponse = httpclient.execute(httpGet);
//        form.field("owner", "ingimar");
//        form.field("userid", "12");
//        form.field("fileName", "filnamn");
//
//        final WebTarget target = client.target("http://localhost:8080/MediaServerResteasy/media/vegatest2");
//        final Response response = target.request().post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
//        System.out.println("response " + response.getStatus());
//
//        return response;
//    }
//
//    private Response testing_All() throws FileNotFoundException, IOException {
//        String fileName = "testbild-svt-666.png";
//        String filePath = "/tmp/".concat(fileName);
//
//        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
//        FormDataMultiPart form = new FormDataMultiPart();
//        form.field("owner", "ingimar");
//        form.field("fileName", "mocking.jpg");
//        InputStream content = new FileInputStream(new File(filePath));
//        FormDataBodyPart fdp = new FormDataBodyPart("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
//        form.bodyPart(fdp);
//
////        final WebTarget target = client.target("http://localhost:8080/MediaServerResteasy/media/vega");
//        final WebTarget target = client.target();
//        final Response response = target.request().post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
//        System.out.println("response " + response.getStatus());
//
//        return response;
//    }
//
//    private HttpResponse testingThree() throws FileNotFoundException, IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        HttpEntity entity = MultipartEntityBuilder.create().
//                addTextBody("owner", "ingimar").
//                addTextBody("userid", "1").
//                addTextBody("fileName", "mock.jpg").build();
//
//        HttpPost httpPost = new HttpPost("http://localhost:8080/MediaServerResteasy/media/vegatest2");
//        httpPost.setEntity(entity);
//        HttpResponse response = httpClient.execute(httpPost);
//        String reasonPhrase = response.getStatusLine().getReasonPhrase();
//        System.out.println("reasonPhrase ".concat(reasonPhrase));
//
//        HttpEntity entity1 = response.getEntity();
//        String entityString = EntityUtils.toString(entity1);
//        System.out.println("String from server is " + entityString);
//
//        return response;
//    }
}
