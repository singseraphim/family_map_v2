package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.nio.file.*;

import java.io.IOException;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //how to get the file path from this? Where to put index.html?
        String filePathStr = exchange.getRequestURI().getPath();
        String fullPath;
        if (filePathStr.contains("main.css")) {
            fullPath = "lib/src/web/css/main.css";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        else if (filePathStr.contains("404.html")) {
            fullPath = "lib/src/web/HTML/404.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        else if (filePathStr.contains("favicon.ico")) {
            fullPath = "lib/src/web/favicon.ico";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        else if (filePathStr.contains("favicon.jpg")) {
            fullPath = "lib/src/web/favicon.jpg";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        else {
            fullPath = "lib/src/web/index.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
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
 */