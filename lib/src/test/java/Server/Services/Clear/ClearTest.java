package Server.Services.Clear;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Server.DAO.AuthDAO;
import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;

import static org.junit.Assert.*;

public class ClearTest {
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    Clear clearService = new Clear();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void clear() {
        clearService.clear();
        assertFalse(userDAO.tableExists());
        assertFalse(personDAO.tableExists());
        assertFalse(eventDAO.tableExists());
        assertFalse(authDAO.tableExists());
    }
}