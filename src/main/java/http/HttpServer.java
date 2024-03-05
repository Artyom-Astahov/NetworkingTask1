package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.Salary;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            var socket = serverSocket.accept();
            procesSocket(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private Salary parsingJson(String json) {
        String[] parts = json.split("\r\n\r\n", 2);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Salary salary = objectMapper.readValue(parts[1], Salary.class);
            return salary;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void procesSocket(Socket socket) {
        try (socket;
             var inputStream = new DataInputStream(socket.getInputStream());
             var outputStream = new DataOutputStream(socket.getOutputStream())) {
            String jsonByte = new String(inputStream.readNBytes(520));
            var salary = parsingJson(jsonByte);
            String body = Files.readString(Path.of("src/main/resources/receipt.html"));
            int totalIncome = salary.totalIncome();
            int totalTax = salary.totalTax();
            int totalProfit = salary.totalProfit();
            body = body.replace("${total_income}", Integer.toString(totalIncome));
            body = body.replace("${total_tax}", Integer.toString(totalTax));
            body = body.replace("${total_profit}", Integer.toString(totalProfit));
            outputStream.write("""
                    HTTP/1.1 200 OK
                    content-type: text/html
                    content-type: %s
                    """.formatted(body.getBytes().length).getBytes());
            outputStream.write(System.lineSeparator().getBytes());
            outputStream.write(body.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
