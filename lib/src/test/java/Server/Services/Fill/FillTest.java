package Server.Services.Fill;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.DatabaseException;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.Person;
import Server.Model.User;
import Server.Services.Clear.Clear;

import static org.junit.Assert.*;

public class FillTest {
    Fill fillService = new Fill();
    UserDAO userDAO = new UserDAO();
    User user = new User();

    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.userName = "FillUser";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";

        PersonDAO personDAO = new PersonDAO();
        Person person = new Person();
        person.firstName = "d";
        person.lastName = "c";
        person.personID = "g";
        person.gender = "f";
        person.descendant = "FillUser";

        try {
            userDAO.insert(user);
            personDAO.insert(person);
        }
        catch (DatabaseException e) {

        }

    }

    @After
    public void tearDown() throws Exception {
        Clear clearService = new Clear();
        clearService.clear();
    }

        @Test
    public void fillOneGen() { //note: This fails because when I run my unit tests, my fill method can't find the json files with
                                // the random data. I talked to Kara and we couldn't figure out how to fix it. It works perfectly
                                // when I run it in main, and I passed it off fine.
        FillResponse response = fillService.fill("FillUser", 1);
        assertTrue(response.success);
        assertTrue(response.message == "Successfully added " + 2 + " persons" +
                " and " + 7 + " events to the database.\n");
    }

    @Test
    public void fillInvalidGen() {
        FillResponse response = fillService.fill("FillUser", -5);
        assertFalse(response.success);
        assertTrue(response.message == "Invalid number of generations");
    }

    @Test
    public void fillNoUsername() {
        FillResponse response = fillService.fill("", 3);
        assertFalse(response.success);
        assertTrue(response.message == "Empty username field");
    }

    @Test
    public void fillInvalidUsername() {
        FillResponse response = fillService.fill("bjork", 5);
        assertFalse(response.success);
        assertTrue(response.message == "User does not exist");
    }
}