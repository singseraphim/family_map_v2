package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Server.Services.Register.Register;
import Server.Services.Register.RegisterRequest;
import Server.Services.Register.RegisterResponse;
import jdk.nashorn.api.scripting.JSObject;
import java.lang.Object;

public class RegisterHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                Gson gson = new Gson();
                RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                Register registerService = new Register();
                RegisterResponse response = registerService.register(request);
                //how to send response back?

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

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