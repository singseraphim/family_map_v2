package Server.Services.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.AuthToken;
import Server.Model.User;

import static org.junit.Assert.*;

public class PersonTest {
    private Person personService = new Person();
    private PersonDAO personDAO = new PersonDAO();
    private AuthToken authToken = new AuthToken();
    Server.Model.Person person = new Server.Model.Person();
    Server.Model.Person person2 = new Server.Model.Person();
    Server.Model.Person person3 = new Server.Model.Person();

    @Before
    public void setUp() throws Exception {

        person.gender = "f";
        person.descendant = "TestDescendant";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID1";

        person2.gender = "f";
        person2.descendant = "TestDescendant";
        person2.lastName = "v";
        person2.firstName = "w";
        person2.father = "x";
        person2.mother = "y";
        person2.spouse = "z";
        person2.personID = "TestID2";

        person3.gender = "f";
        person3.descendant = "TestDescendant2";
        person3.lastName = "b";
        person3.firstName = "c";
        person3.father = "d";
        person3.mother = "e";
        person3.spouse = "f";
        person3.personID = "TestID3";

        authToken.authToken = "TestToken";
        authToken.userName = "TestDescendant";

        User user = new User();
        user.userName = "TestDescendant";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";

        AuthDAO authDAO = new AuthDAO();
        PersonDAO personDAO = new PersonDAO();
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.insert(user);

            personDAO.insert(person);
            personDAO.insert(person2);
            personDAO.insert(person3);
            authDAO.insert(authToken);
        }
        catch (DatabaseException e) {

        }

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getPeople() {
        PeopleResponse response = personService.getPeople("TestToken");
        assertTrue(response.success);

    }

    @Test
    public void getPeopleInvalidToken() {
        PeopleResponse response = personService.getPeople("blorp");
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    public void getPerson() {
        PersonResponse personResponse = personService.getPerson("TestID1", "TestToken");
        assertTrue(personResponse.success);
    }

    @Test
    public void getPersonInvalidID() {
        PersonResponse personResponse = personService.getPerson("blorp", "TestToken");
        assertFalse(personResponse.success);
        assertNotNull(personResponse.message);
    }

    @Test
    public void getPersonInvalidAuth() {
        PersonResponse personResponse = personService.getPerson("TestID1", "bleorp");
        assertFalse(personResponse.success);
        assertNotNull(personResponse.message);
    }

    @Test
    public void getPersonWrongDesc() {
        PersonResponse personResponse = personService.getPerson("TestID3", "TestToken");
        assertFalse(personResponse.success);

    }





}