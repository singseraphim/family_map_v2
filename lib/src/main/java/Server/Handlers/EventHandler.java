package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Server.Services.Event.Event;
import Server.Services.Event.EventResponse;

public class EventHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //get input data
        String auth = exchange.getRequestHeaders().get("Authorization").get(0);
        String path = exchange.getRequestURI().getPath();
        path = path.substring(1);
        String[] args = path.split("/");
        String eventID = args[1];

        //talk to service
        Event eventService = new Event();
        EventResponse response = eventService.getEvent(eventID, auth);

        //send response
        Gson gson = new Gson();
        exchange.sendResponseHeaders(200, 0);
        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        System.out.println("response body: " + gson.toJson(response));
    }
}
