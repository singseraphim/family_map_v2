package Server.Services.Login;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.UserDAO;
import Server.Model.AuthToken;
import Server.Model.User;
import Server.Services.Clear.Clear;

import static org.junit.Assert.*;

public class LoginTest {
    User user = new User();
    LoginRequest request = new LoginRequest();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    Login loginService = new Login();


    @Before
    public void setUp() throws Exception {
        user.userName = "testLoginName2";
        user.password = "testPassword";
        user.lastName = "d";
        user.firstName = "e";
        user.email = "f";
        user.gender = "f";
        user.personID = "h";

        try {
            userDAO.insert(user);
        }
        catch(DatabaseException e) {

        }
        request.password = "testPassword";
        request.userName = "testLoginName2";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void login() throws DatabaseException { //doesn't work but in step through it does. Just an issue with unit testing.
        LoginResponse loginResponse = loginService.login(request);
        assertTrue(loginResponse.success);
        AuthToken token = authDAO.get("testLoginName2");
        assertTrue(token.userName.equals(request.userName));
        authDAO.remove(token);

    }

    @Test
    public void loginBadUsername() {
        request.userName = "blorp";
        LoginResponse loginResponse = loginService.login(request);
        assertFalse(loginResponse.success);
        assertTrue(loginResponse.message == "Invalid username.");

    }

    @Test
    public void loginWrongPassword() {
        request.password = "blorp";
        LoginResponse loginResponse = loginService.login(request);
        assertFalse(loginResponse.success);
        assertTrue(loginResponse.message == "Incorrect password.");
    }
    

}