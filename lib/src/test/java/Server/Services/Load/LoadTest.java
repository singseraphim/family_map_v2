package Server.Services.Load;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.DatabaseException;
import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.Event;
import Server.Model.Person;
import Server.Model.User;

import static org.junit.Assert.*;

public class LoadTest {

    private User user = new User();
    private User user2 = new User();
    private Person person = new Person();
    private Person person2 = new Person();
    private Event event = new Event();
    private Event event2 = new Event();
    private Load loadService = new Load();
    private LoadResponse response = new LoadResponse();
    private LoadRequest request = new LoadRequest();

    @Before
    public void setUp() throws Exception {

        user.userName = "a";
        user.password = "p";
        user.lastName = "l";
        user.firstName = "f";
        user.email = "e";
        user.gender = "f";
        user.personID = "p";

        user2.userName = "b";
        user2.password = "c";
        user2.lastName = "d";
        user2.firstName = "e";
        user2.email = "f";
        user2.gender = "f";
        user2.personID = "h";

        person.gender = "f";
        person.descendant = "d";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "i";

        person2.gender = "m";
        person2.descendant = "u";
        person2.lastName = "v";
        person2.firstName = "w";
        person2.father = "x";
        person2.mother = "y";
        person2.spouse = "z";
        person2.personID = "s";

        event.city = "c";
        event.country = "d";
        event.descendant = "e";
        event.eventID = "f";
        event.eventType = "g";
        event.latitude = 1;
        event.longitude = 2;
        event.year = 3;
        event.person = "h";

        event2.city = "s";
        event2.country = "t";
        event2.descendant = "u";
        event2.eventID = "v";
        event2.eventType = "w";
        event2.latitude = 11;
        event2.longitude = 12;
        event2.year = 13;
        event2.person = "x";

        request.events.add(event);
        request.events.add(event2);
        request.people.add(person);
        request.people.add(person2);
        request.users.add(user);
        request.users.add(user2);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void load() throws DatabaseException {
        response = loadService.load(request);
        assertTrue(response.success);
        UserDAO userDAO = new UserDAO();
        EventDAO eventDAO = new EventDAO();
        PersonDAO personDAO = new PersonDAO();
        User resultUser = userDAO.getUser("a");
        Person resultPerson = personDAO.getPerson("i");
        Event resultEvent = eventDAO.getEvent("f");
        assertEquals(user.userName, resultUser.userName);
        assertEquals(event.eventID, resultEvent.eventID);
        assertEquals(person.personID, resultPerson.personID);


    }

    @Test
    public void loadInvalidData() {
        request.users.clear();
        user.gender = "z";
        user2.userName = "";
        request.users.add(user);
        request.users.add(user2);
        LoadResponse response = loadService.load(request);
        assertTrue(response.message == "Loading threw a database exception, invalid information.");
    }
}