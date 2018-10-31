package Server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import Server.Services.Register.RegisterRequest;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch(IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        System.out.println("Creating contexts");
        server.createContext("/user/register", new RegisterHandler()); //may need to make more handlers for events and persons
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new PeopleHandler());
        server.createContext("/event/", new EventHandler());
        server.createContext("/event", new EventsHandler());
        server.createContext("/", new RootHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }

}

/*
CAPTAIN'S LOG:
Root handler, what?
Go over what an HTTP exchange is and where the data is in it
How do I parse data from json that isn't packaged as a request
Am I understanding this whole thing correctly
Where do I put index.html and how do I make my program talk to it

 */
