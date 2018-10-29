package Server.Services.Event;

import java.util.ArrayList;

/**
 * A response object that will either contain a list of events or an error message.
 * Used by the Server.Services.Event class when the getEvents() method is called.
 */
public class EventsResponse {
    String message;
    ArrayList<Server.Model.Event> data = new ArrayList<>();
    boolean success = true;
}
