package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.net.HttpURLConnection;
import java.nio.file.*;

import java.io.IOException;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePathStr = exchange.getRequestURI().getPath();
        File testFile = new File(filePathStr);
        String fullPath;
        if(filePathStr.equals("/") || filePathStr.contains("index.html")) { //or index.html
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            fullPath = "lib/src/web/index.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
        else if (filePathStr.contains("main.css")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            fullPath = "lib/src/web/css/main.css";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();

        }
        else if (filePathStr.contains("404.html")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            fullPath = "lib/src/web/HTML/404.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();

        }
        else if (filePathStr.contains("favicon.ico")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            fullPath = "lib/src/web/favicon.ico";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();

        }
        else if (filePathStr.contains("favicon.jpg")) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            fullPath = "lib/src/web/favicon.jpg";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();

        }
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            fullPath = "lib/src/web/index.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }

    }
}

/*
getRequestURI().getPath();

for each thing in file:
if filepath contains thing-
assign another string to the whole path
then do
 Path filePath = FileSystems.getDefault().getPath(filePathStr);
  Files.copy(filePath, exchange.getResponseBody());

else give index
then close response body


take url, make into file objects
file.exists()
file.canread()
if both, then do the thing
else, doesn't exist, send 404
 */