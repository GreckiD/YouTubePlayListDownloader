import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.net.URL;

import static io.restassured.RestAssured.given;

public class VideoConverter {

    private RequestSpecification requestSpec;
    private String projectDIR = System.getProperty("user.dir");

    public VideoConverter() {
        RestAssured.baseURI = "https://toquemp3.com/";
        requestSpec = new RequestSpecBuilder()
                .addHeader("content-type", "application/json; charset=UTF-8")
                .setRelaxedHTTPSValidation()
                .build();
    }

    private String getMP3DownloadUrl(String videoId) {
        Response response = given()
                .spec(requestSpec)
                .get("/en/@api/json/mp3/" + videoId);
        String downloadURL = JsonPath.parse(response.getBody().asString()).read("$.vidInfo.0.dloadUrl");
        return downloadURL;
    }

    public void downloadMP3File(String videoId, String title) {
        String downloadURL = getMP3DownloadUrl(videoId);
        title = title.replace("/", "");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL("https:" + downloadURL).openStream());
             FileOutputStream mp3File = new FileOutputStream(projectDIR + "/target/files/" + title + ".mp3")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                mp3File.write(data, 0, byteContent);
            }
            System.out.println("Downloaded: " + title);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
