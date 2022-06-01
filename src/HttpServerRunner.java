import hhtp.servelets.server.HttpServer;

public class HttpServerRunner {
    public static void main(String[] args) {
        var httpServerTest = new HttpServer(8899,100);
        httpServerTest.run();
    }
}
