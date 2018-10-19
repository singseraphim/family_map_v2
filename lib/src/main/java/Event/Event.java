package Event;

/**
 * Service that handles all event requests.
 */
public class Event {

    /**
     * Returns all events for all family members of the given user.
     * User is determined by the given authToken.
     * @param authToken: Authorization token for the current user.
     * @return an EventsResponse object that either contains a list of events or an error message if there is a problem.
     */
    public EventsResponse getEvents(String authToken) {
        return null;
    }

    /**
     * Returns an event object associated with the given event ID.
     * Checks that the eventID is valid, that the authToken is valid, and that the event belongs to the user corresponding
     * to the authToken.
     * @param eventID: ID of the event to be returned.
     * @param authToken: authorization token of the current user.
     * @return an EventResponse object that either contains an event object or an error message if there is a problem.
     */
    public EventResponse getEvent(String eventID, String authToken) {
        return null;
    }
}
