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
        if(filePathStr.equals("/")) {
            String fullPath = "lib/src/web/index.html";
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
        else if(testFile.exists() && testFile.canRead()) {
            String fullPath = testFile.getPath();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
        else {
            String fullPath = "lib/src/web/HTML/404.html";
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Path filePath = FileSystems.getDefault().getPath(fullPath);
            Files.copy(filePath, exchange.getResponseBody());
        }
       /* if (filePathStr.contains("main.css")) {
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
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();
        }
*/

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