package Server.Handlers;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

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
        server.createContext(" /user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/[username]/{generations}", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/[personID]", new PersonHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event/[eventID]", new EventHandler());
        server.createContext("/event", new EventHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }

}
