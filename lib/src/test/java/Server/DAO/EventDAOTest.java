package Server.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.Event;
import Server.Model.User;

import static org.junit.Assert.*;

public class EventDAOTest {
    private EventDAO eventDAO = new EventDAO();
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTables() {
        eventDAO.createTables();
        assertTrue(eventDAO.tableExists());
    }

    @Test
    public void getEvents() throws DatabaseException {
        Event event = new Event();
        Event event2 = new Event();

        event.city = "c";
        event.country = "d";
        event.descendant = "GetEventsDescendant";
        event.eventID = "GetEventsTestID";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        event2.city = "s";
        event2.country = "t";
        event2.descendant = "GetEventsDescendant";
        event2.eventID = "GetEventsTestID2";
        event2.eventType = "w";
        event2.latitude = 11;
        event2.longitude = 12;
        event2.year = 13;
        event2.person = "x";

        UserDAO userDAO = new UserDAO();
        User user = new User();
        user.userName = "GetEventsDescendant";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";


        try {
            eventDAO.insert(event2);
            userDAO.insert(user);
            eventDAO.insert(event);

        }
        catch(DatabaseException e) {

        }
        ArrayList<Event> eventList = eventDAO.getEvents("GetEventsDescendant");
        //assertTrue(eventList.size() == 2);
        assertTrue(eventList.contains(event));
        assertTrue(eventList.contains(event2));


    }

    @Test(expected = DatabaseException.class)
    public void getInvalidUsernameEvents() throws DatabaseException {
        ArrayList<Event> eventList = eventDAO.getEvents("BadUsername");
    }

    @Test
    public void getEvent() throws DatabaseException {
        Event event = new Event();

        event.city = "c";
        event.country = "d";
        event.descendant = "GetEventDescendant33";
        event.eventID = "GetEventTestID33";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";
        eventDAO.insert(event);
        Event result = eventDAO.getEvent("GetEventTestID33");
        assertEquals(event.eventID, result.eventID);

    }

    @Test
    public void getEventInvalidID() {
        Event event = eventDAO.getEvent("badID");
        assertTrue(event.eventID == null);
    }

    @Test
    public void insert() throws DatabaseException {
        Event event = new Event();

        event.city = "c";
        event.country = "d";
        event.descendant = "e";
        event.eventID = "InsertTestID4455";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";
        eventDAO.insert(event);
        Event result = eventDAO.getEvent("InsertTestID4455");
        assertEquals(event.eventID, result.eventID);
        eventDAO.remove(event);
    }

    @Test(expected = DatabaseException.class)
    public void insertExtantID() throws DatabaseException {
        Event event = new Event();
        event.eventID = "eventID";
        event.person = "person";
        event.descendant = "descendant";
        eventDAO.insert(event);
        eventDAO.insert(event);
    }
    @Test
    public void remove() {
        Event event = new Event();
        event.city = "c";
        event.country = "d";
        event.descendant = "e";
        event.eventID = "RemoveTestID";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        try {
            eventDAO.insert(event);
            eventDAO.remove(event);
            Event result = eventDAO.getEvent("RemoveTestID");
            assertNull(result.eventID);
            assertNull(result.descendant);

        }
        catch(DatabaseException e) {

        }
    }

    @Test(expected = DatabaseException.class)
    public void invalidRemove() throws DatabaseException {
        Event event = new Event();
        event.descendant = "badDesc";
        event.person = "badPerson";
        event.eventID = "badID";
        eventDAO.remove(event);
    }
    @Test
    public void removeEvents() {
        Event event = new Event();
        Event event2 = new Event();

        event.city = "c";
        event.country = "d";
        event.descendant = "RemoveDescendant";
        event.eventID = "RemoveTestID1";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        event2.city = "s";
        event2.country = "t";
        event2.descendant = "RemoveDescendant";
        event2.eventID = "RemoveTestID2";
        event2.eventType = "w";
        event2.latitude = 11;
        event2.longitude = 12;
        event2.year = 13;
        event2.person = "x";

        try {
            eventDAO.insert(event);
            eventDAO.insert(event2);
            eventDAO.removeEvents("RemoveDescendant");
            ArrayList<Event> eventList = eventDAO.getEvents("RemoveDescendant");
            assertTrue(eventList.size() == 0);
        }
        catch (DatabaseException e) {

        }

    }

    @Test(expected = DatabaseException.class)
    public void removeInvalidUsernameEvents() throws DatabaseException {
        eventDAO.removeEvents("BadUsername");
    }

    @Test
    public void clear() {
        eventDAO.clear();
        assertFalse(eventDAO.tableExists());
    }

    @Test
    public void getUser() {
        Event event = new Event();
        event.city = "c";
        event.country = "d";
        event.descendant = "GetUserUsername";
        event.eventID = "GetUserID";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        User user = new User();
        user.userName = "GetUserUsername";
        user.password = "a";
        user.lastName = "b";
        user.firstName = "c";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";

        UserDAO userDAO = new UserDAO();

        try {
            eventDAO.insert(event);
            userDAO.insert(user);
            User result = eventDAO.getUser("GetUserID");
            assertEquals(user, result);
        }
        catch(DatabaseException e) {

        }
    }

    @Test(expected = DatabaseException.class)
    public void getInvalidID() throws DatabaseException {
        User user = eventDAO.getUser("badUser");
        assertTrue(user.personID == null);
    }

}