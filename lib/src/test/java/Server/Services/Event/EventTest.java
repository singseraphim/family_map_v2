package Server.Services.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.AuthToken;
import Server.Model.User;

import static org.junit.Assert.*;

public class EventTest {
    private Event eventService = new Event();
    private EventDAO eventDAO = new EventDAO();
    private Server.Model.Event event = new Server.Model.Event();
    private Server.Model.Event event2 = new Server.Model.Event();
    private Server.Model.Event event3 = new Server.Model.Event();
    private AuthToken authToken = new AuthToken();

    @Before
    public void setUp() throws Exception {

        event.city = "c";
        event.country = "d";
        event.descendant = "TestDescendant";
        event.eventID = "TestID1";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        event2.city = "s";
        event2.country = "t";
        event2.descendant = "TestDescendant";
        event2.eventID = "TestID2";
        event2.eventType = "w";
        event2.latitude = 11;
        event2.longitude = 12;
        event2.year = 13;
        event2.person = "x";

        event3.city = "a";
        event3.country = "b";
        event3.descendant = "TestDescendant2";
        event3.eventID = "TestID3";
        event3.eventType = "w";
        event3.latitude = 11;
        event3.longitude = 12;
        event3.year = 13;
        event3.person = "x";

        authToken.userName = "TestDescendant";
        authToken.authToken = "TestToken";

        User user = new User();
        user.userName = "TestDescendant";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";

        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.insert(user);

            eventDAO.insert(event);
            eventDAO.insert(event2);
            eventDAO.insert(event3);
            authDAO.insert(authToken);
        }
        catch (DatabaseException e) {

        }

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getEvents() {
        EventsResponse response = eventService.getEvents("TestToken");
        assertTrue(response.success);


    }

    @Test
    public void getEventsInvalid() {
        EventsResponse response = eventService.getEvents("blorp");
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    public void getEvent() {
        EventResponse response = eventService.getEvent("TestID1", "TestToken");
        assertTrue(response.success);
        assertNotNull(response.eventID);


    }

    @Test
    public void getPeopleInvalidID() {
        EventResponse response = eventService.getEvent("blorp", "TestToken");
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    public void getPeopleInvalidAuth() {
        EventResponse response = eventService.getEvent("TestID1", "bleorp");
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    public void getPeopleWrongDesc() {
        EventResponse response = eventService.getEvent("TestID3", "TestToken");
        assertFalse(response.success);
    }

}