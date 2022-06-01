import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final int port;
    private final ExecutorService pool;
    private boolean stopped;

    public HttpServer(int port, int poolSize) {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            while (!stopped) {
                var socket = server.accept();
                System.out.println("Socket accepted");
                pool.submit(() -> processSocket(socket));
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void processSocket(Socket socket) {
        try (socket;
             var inputStream = new DataInputStream(socket.getInputStream());
             var outputStream = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("REQUEST " + new String(inputStream.readNBytes(400)));

            Thread.sleep(10000);

            var body = Files.readAllBytes(Path.of("resources", "example.html"));
            var headers = """
                    HTTP/1.1 200 OK
                    content-type: text/html
                    content-length: %s
                    """.formatted(body.length).getBytes();
            outputStream.write(headers);
            outputStream.write(System.lineSeparator().getBytes());
            outputStream.write(body);

        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
