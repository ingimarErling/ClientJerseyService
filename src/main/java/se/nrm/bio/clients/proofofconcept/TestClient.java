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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * java -Xms512m -Xmx15G
 *
 * @author ingimar
 */
public class TestClient {

    private final static Logger logger = Logger.getLogger(TestClient.class);

    public static void main(String[] args) throws IOException {

        TestClient c = new TestClient();
        int healthOfServer = 404;
        healthOfServer = c.healthOfServer();
        System.out.println("Main is " + healthOfServer);

        if (healthOfServer == 200) {
            System.out.println("Server is running \n");
//            c.other();
           c.testing();
        } else {
            System.out.println("Server is not running ");
        }
    }

    // 2019-11-13, fungerar, har alltför många dependencies i pom.xml -kolla igenom
    private int healthOfServer() throws IOException {
        int status = 404;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/JerseyServer/rest/upload/hello");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        status = response.getStatusLine().getStatusCode();
        return status;

    }

    /**
     * the uploaded file does not have the same md5sum !?
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void png() throws FileNotFoundException, IOException {
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

        String fileName = "testbild-svt-666.png";
        String filePath = "/tmp/".concat(fileName);
        System.out.println("File ".concat(filePath));

        int i = 0;

        FormDataMultiPart form = new FormDataMultiPart();
        form.field("owner", "ingimar");
        form.field("fileName", fileName);
        form.field("workgroupId", "XXX");
        form.field("userId", Integer.toString(i));
        InputStream content = new FileInputStream(new File(filePath));
        FormDataBodyPart fdp = new FormDataBodyPart("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        form.bodyPart(fdp);

        final WebTarget target = client.target("http://localhost:8080/JerseyServer/rest/upload/png");
        final Response response = target.request().post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
        content.close();
        System.out.println("response " + response.getStatus());
    }

    private void other() throws FileNotFoundException, IOException {
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        System.out.println("Testclient OTHER");

        String fileName = "testbild-svt-666.png";
//        String fileName = "500mb.zip";
//        String fileName = "1000mb.zip";
//        String fileName = "2GB.zip";
        String filePath = "/tmp/".concat(fileName);

        System.out.println("File ".concat(filePath));

        int i = 0;

        FormDataMultiPart form = new FormDataMultiPart();
        form.field("owner", "ingimar");
        form.field("fileName", fileName);
        form.field("workgroupId", "XXX");
        form.field("userId", Integer.toString(i));
        InputStream content = new FileInputStream(new File(filePath));
        FormDataBodyPart fdp = new FormDataBodyPart("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        form.bodyPart(fdp);

        final WebTarget target = client.target("http://localhost:8080/JerseyServer/rest/upload/other");
        final Response response = target.request().post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
        content.close();
        System.out.println("response " + response.getStatus());
        System.out.println("response " + response.getStatusInfo());
    }

    private void testing() throws FileNotFoundException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String fileName = "testbild-svt-666.png";
        String filePath = "/tmp/".concat(fileName);
        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("owner", "ingimar")
                .addTextBody("fileName", fileName).addTextBody("workgroupId", "XXX").addBinaryBody("content", new File(filePath)).build();

        HttpPost httpPost = new HttpPost("http://localhost:8080/JerseyServer/rest/upload/other");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity result = response.getEntity();
    }
}