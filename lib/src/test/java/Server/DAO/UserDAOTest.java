package Server.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.User;

import static org.junit.Assert.*;

public class UserDAOTest {
    private UserDAO userDAO = new UserDAO();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createTables() {
        userDAO.createTables();
        assertTrue(userDAO.tableExists());
    }

    @Test
    public void insert() throws DatabaseException {

        User user = new User();
        user.userName = "InsertUnitTestUser";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";
        userDAO.insert(user);
        User returnUser = userDAO.getUser("InsertUnitTestUser");
        assertTrue(user.equals(returnUser));
        userDAO.remove(user);

    }

    @Test
    public void getUser() throws DatabaseException {

        User user = new User();
        user.userName = "GetUserUnitTestUser";
        user.password = "b";
        user.lastName = "c";
        user.firstName = "d";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";
        userDAO.insert(user);
        User returnUser = userDAO.getUser("GetUserUnitTestUser");
        assertTrue(user.equals(returnUser));
        userDAO.remove(user);

    }

    @Test
    public void getInvalidUser() {
        User returnUser = userDAO.getUser("BadUser");
        assertTrue(returnUser.userName == null);
    }

    @Test
    public void clear() {
        userDAO.clear();
        assertFalse(userDAO.tableExists());
    }
}