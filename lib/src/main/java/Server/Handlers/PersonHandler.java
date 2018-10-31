package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Server.Services.Person.PeopleResponse;
import Server.Services.Person.Person;
import Server.Services.Person.PersonResponse;

public class PersonHandler  implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //get data somehow
        String auth = exchange.getRequestHeaders().get("Authorization").get(0);
        String path = exchange.getRequestURI().getPath();
        path = path.substring(1);
        String[] args = path.split("/");
        String personID = args[1];

        //talk to service:
        Person personService = new Person();
        PersonResponse response = personService.getPerson(personID, auth); //fix later

        //write data:
        Gson gson = new Gson();
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
