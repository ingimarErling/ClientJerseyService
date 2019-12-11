package se.nrm.bio.clients.proofofconcept;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.json.JSONObject;

/**
 * 2019-12-11 , able to post the 
 * @author ingimar
 */
public class MimicIdasVegadareCall {

    public static void main(String[] args) throws Exception {

        MimicIdasVegadareCall call = new MimicIdasVegadareCall();
        call.saveZipFileToMediaServer();

    }

    private String saveZipFileToMediaServer() throws Exception {

        final String url = "https://api.nrm.se/mserver/media";
        String fileName = "testbild-svt-666.png";
        String filePath = "/tmp/".concat(fileName);

        String uuid = null;

        ab(url, filePath);

        return uuid;
    }

    private void ab(String url, String Filepath) throws FileNotFoundException {

        ResteasyClient client = new ResteasyClientBuilder().build();

        ResteasyWebTarget target = client.target(url);

        MultipartFormDataOutput upload = new MultipartFormDataOutput();
        upload.addFormData("content", new FileInputStream(new File(Filepath)), MediaType.APPLICATION_OCTET_STREAM_TYPE);
        upload.addFormData("owner", "ingimar", MediaType.WILDCARD_TYPE);
        upload.addFormData("filename", "test-bild.png", MediaType.WILDCARD_TYPE);
        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(upload) {
        };

        Response response = target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
        String jsonString = response.readEntity(String.class);
        String uuid="";
        JSONObject json = new JSONObject(jsonString);
        if (json != null && !json.isNull("uuid")) {
            uuid = json.getString("uuid");
        }
        System.out.println("UUID is " + uuid);

    }
}
