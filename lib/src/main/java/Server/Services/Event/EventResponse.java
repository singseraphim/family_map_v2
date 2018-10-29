package Server.Services.Event;
import Server.Model.Event;

/**
 * A response object that will either contain an event or an error message.
 * Used by the Server.Services.Event class when the getEvent() method is called.
 */
public class EventResponse {
    public String message;
    public String eventID;
    public String personID;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String eventType;
    public int year;
    boolean success;

}
