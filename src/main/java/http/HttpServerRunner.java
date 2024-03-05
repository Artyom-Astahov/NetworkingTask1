package http;

public class HttpServerRunner {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8083);
        httpServer.run();
    }
}
