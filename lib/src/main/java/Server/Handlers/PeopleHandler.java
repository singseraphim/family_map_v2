package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Server.Services.Person.PeopleResponse;
import Server.Services.Person.Person;

public class PeopleHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //get authtoken data somehow

        //talk to service
        Person personService = new Person();
        PeopleResponse response = personService.getPeople("foo");

        //send response
        Gson gson = new Gson();
        exchange.sendResponseHeaders(200, 0);
        Writer out = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, out);
        out.close();
        System.out.println("response body: " + gson.toJson(response));


    }
}
