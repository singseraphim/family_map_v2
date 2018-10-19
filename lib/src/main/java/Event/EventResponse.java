package Event;
import Model.Event;

/**
 * A response object that will either contain an event or an error message.
 * Used by the Event class when the getEvent() method is called.
 */
public class EventResponse {
    String message;
    Event event;

}
