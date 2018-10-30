package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Server.Services.Login.Login;
import Server.Services.Login.LoginRequest;
import Server.Services.Login.LoginResponse;

public class LoginHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //get input data
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        System.out.println(reqData);
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(reqData, LoginRequest.class);

        //talk to service
        Login loginService = new Login();
        LoginResponse response = loginService.login(request);

        //send response
        exchange.sendResponseHeaders(200, 0);
        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        System.out.println("response body: " + gson.toJson(response));


    }
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
