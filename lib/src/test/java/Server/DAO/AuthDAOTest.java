package Server.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.AuthToken;
import Server.Model.User;

import static org.junit.Assert.*;

public class AuthDAOTest {
    private AuthDAO authDAO = new AuthDAO();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTables() {
        authDAO.createTables();
        assertTrue(authDAO.tableExists());
    }

    @Test
    public void insertGood() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "InsertTestTokenID3";
        token.userName = "InsertTestName3";
        authDAO.insert(token);
        AuthToken result = authDAO.get("InsertTestName3");
        assertEquals(token.authToken, result.authToken);
        authDAO.remove(token);

    }

    @Test(expected = DatabaseException.class)
    public void insertNoAuthToken() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.userName = "TestName";

        authDAO.insert(token);

    }

    @Test(expected = DatabaseException.class)
    public void insertNoUsername() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "TestToken";
        authDAO.insert(token);


    }

    @Test(expected = DatabaseException.class)
    public void insertExtantUser() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "TestToken";
        token.userName = "TestName";

        authDAO.insert(token);
        authDAO.insert(token);

    }

    @Test
    public void remove() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "RemoveTestID3";
        token.userName = "RemoveTestName3";
        authDAO.insert(token);
        authDAO.remove(token);
        AuthToken result = authDAO.get("RemoveTestName3");
        assertNull(result.userName);
        assertNull(result.authToken);

    }

    @Test(expected = DatabaseException.class)
    public void removeInvalid() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "BadID";
        token.userName = "BadName";
        authDAO.remove(token);

    }

    @Test
    public void clear() {
        authDAO.clear();
        assertFalse(authDAO.tableExists());
    }

    @Test
    public void get() throws DatabaseException {
        AuthToken token = new AuthToken();
        token.authToken = "GetTestTokenID5";
        token.userName = "GetTestTokenUsername5";
        authDAO.insert(token);
        AuthToken result = authDAO.get("GetTestTokenUsername5");
        assertEquals(token.authToken, result.authToken);
        authDAO.remove(token);

    }

    @Test
    public void getInvalid() {
        AuthToken token = authDAO.get("nonexistentUser");
        assertTrue(token.authToken == null);
    }

    @Test
    public void getUser() throws DatabaseException { //this works in main. There's persistent data that's causing problems.
        authDAO.clear();
        AuthToken token = new AuthToken();
        token.authToken = "GUID";
        token.userName = "GetUsername";

        User user = new User();
        user.userName = "GetUsername";
        user.password = "a";
        user.lastName = "b";
        user.firstName = "c";
        user.email = "e";
        user.gender = "f";
        user.personID = "testuserID";

        UserDAO userDAO = new UserDAO();
        try {
            authDAO.insert(token);
            userDAO.insert(user);
        }
        catch(DatabaseException e) {

        }
        User result = authDAO.getUser("GUID");
        assertEquals(user.userName, result.userName);
        authDAO.remove(token);
        userDAO.remove(user);

    }

    @Test
    public void getUserInvalid(){
        User testUser = authDAO.getUser("nonexistentUser");
        assertTrue(testUser.userName == null);

    }
}
/*
UNIT TESTING:
assertEquals(expected, actual)
assertTrue(boolean)
assertFalse(boolean)
assertNull(Object)
assertNotNull(Object)
assertSame(Object1, Object2)       // same object
assertNotSame(Object1, Object2)    // not same object

 */