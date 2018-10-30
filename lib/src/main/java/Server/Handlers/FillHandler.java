package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Server.Services.Fill.Fill;
import Server.Services.Fill.FillResponse;

public class FillHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //get data from exchange somehow

        //talk to service
        Fill fillService = new Fill();
        FillResponse response = fillService.fill("username", 0); //fix later

        //send response
        Gson gson = new Gson();
        exchange.sendResponseHeaders(200, 0);
        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        System.out.println("response body: " + gson.toJson(response));


    }
}
