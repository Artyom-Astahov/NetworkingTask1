package http;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static java.net.http.HttpRequest.BodyPublishers.ofFile;

public class HttpClientRunner {
    public static void main(String[] args) throws IOException, InterruptedException {
        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083"))
                .header("content-type", "application/json")
                .POST(ofFile(Path.of("src/main/resources/send.json")))
                .build();
        var responseString = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/NewHTML.html"))){
            writer.write(responseString.body());
        }
        System.out.println(responseString.body());
    }
}
