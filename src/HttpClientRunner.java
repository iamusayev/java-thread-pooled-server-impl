import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class HttpClientRunner {
    public static void main(String[] args) throws Exception {
        var httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8899"))
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofFile(Path.of("resources", "first.json"))).build();
        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        var response2 = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        var response3 = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response3.get().body());
        System.out.println(response.get().body());
        System.out.println(response2.get().body());
    }
}
