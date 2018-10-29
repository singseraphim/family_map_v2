package Server.Services.Event;

import javax.xml.crypto.Data;

import Server.DAO.AuthDAO;
import Server.DAO.EventDAO;
import Exceptions.DatabaseException;
import Server.Model.User;

/**
 * Service that handles all event requests.
 */
public class Event {
    private EventDAO eventDAO = new EventDAO();

    /**
     * Returns all events for all family members of the given user.
     * User is determined by the given authToken.
     * @param authToken: Authorization token for the current user.
     * @return an EventsResponse object that either contains a list of events or an error message if there is a problem.
     */
    public EventsResponse getEvents(String authToken) {
        EventsResponse response = new EventsResponse();
        AuthDAO authDAO = new AuthDAO();
        try {
            eventDAO.createTables();
            User currentUser = authDAO.getUser(authToken);
            if (currentUser == null) {
                response.message = "Authtoken doesn't exist";
                response.success = false;
                return response;
            }
            response.data = eventDAO.getEvents(currentUser.userName);
            response.success = true;
            return response;
        }
        catch(DatabaseException e) {
            response.message = "getEvents failed";
            response.success = false;
            return response;
        }
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
        EventResponse response = new EventResponse();
        eventDAO.createTables();
        if (validToken(eventID, authToken)) {
            Server.Model.Event event = eventDAO.getEvent(eventID);
            response.eventID = event.eventID;
            response.personID = event.person;
            response.latitude = event.latitude;
            response.longitude = event.longitude;
            response.country = event.country;
            response.city = event.city;
            response.eventType = event.city;
            response.year = event.year;
            response.success = true;
            return response;
        }
        response.message = "Invalid token";
        return response;
    }

    /**
     * Checks if the eventID exists, the authToken exists and that the username corresponding to the authToken is the same user
     * who is associated with the eventID.
     * @param eventID event ID given in request
     * @param authToken authToken given in request
     * @return true if the eventID and authToken are valid and the users they are both associated with match.
     */
    public boolean validToken(String eventID, String authToken) {
        //get user from authtoken: make sure authtoken is valid
        //get user from eventID: make sure eventID is valid
        //compare the users, if identical then true.
        User currentUser = getAuthUser(authToken);
        if (currentUser == null) { //authToken doesn't exist
            return false;
        }
        User eventDescendant = getEventUser(eventID);
        if (eventDescendant == null) { //eventID is invalid
            return false;
        }
        if (eventDescendant.equals(currentUser)) { //if the current user matches the descendant of the event
            return true;
        }
        return false;
    }

    /**
     * Returns user associated with the given authToken
     * @param authToken the authToken associated with the returned user
     * @return a user associated with the given authToken, or null if the authToken doesn't exist in the table.
     */
    public User getAuthUser(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        User currentUser = new User();
        authDAO.createTables();
        currentUser = authDAO.getUser(authToken);

        return currentUser;
    }

    /**
     * Returns user associated with the given eventID
     * @param eventID the eventID to find the user for
     * @return user object associated with the eventID, or null if the eventID doesn't exist in the table.
     */
    public User getEventUser(String eventID) {
        EventDAO eventDAO = new EventDAO();
        User eventUser = new User();
        try {
            eventUser = eventDAO.getUser(eventID);
        }
        catch (DatabaseException e) {

        }
        return eventUser;
    }
}

