package Server.Services.Login;

import javax.xml.crypto.Data;

import Server.DAO.AuthDAO;
import Server.DAO.UserDAO;
import Exceptions.DatabaseException;
import Server.Model.AuthToken;
import Server.Model.User;
import java.util.UUID;


/**
 * Service that handles all login requests.
 */
public class Login {

    /**
     * Checks if the given username is in the users table and that the password is correct.
     * It also checks if the user is already logged in.
     * if the credentials are valid and the user is not logged in already, it will add a new authToken to the AuthToken table.
     *
     * @param request: a LoginRequest object that contains a username and password.
     *
     * @return a LoginResponse object that will contain an error message on an unsuccessful login,
     * or an authToken, username and personID on a successful login.
     */
    public LoginResponse login (LoginRequest request) { //check if already logged in
        /*
        THINGS TO CHECK:
        Does the username exist?
        Does the password match the username?
        Is the user already logged in?
         */
        LoginResponse response = new LoginResponse();
        if (!validUsername(request.userName)) {
            response.message = "Invalid username.";
            response.success = false;
            return response;
        }
        if (!correctPassword(request)) {
            response.message = "Incorrect password.";
            response.success = false;
            return response;
        }
        if (loggedIn(request.userName)) {
            response.message = "Already logged in.";
            response.success = false;
            return response;
        }

        AuthDAO authDAO = new AuthDAO();
        AuthToken newToken = new AuthToken();
        newToken.userName = request.userName;
        newToken.authToken = UUID.randomUUID().toString().substring(0, 8);
        try {
            authDAO.insert(newToken); //this does not work.
        }
        catch(DatabaseException e) {
            System.out.println(e.toString());
        }

        //put things in response
        response.authToken = newToken.authToken;
        response.userName = newToken.userName;
        UserDAO userDAO = new UserDAO();
        User currentUser = new User();
        currentUser = userDAO.getUser(newToken.userName);

        response.personID = currentUser.personID;
        response.success = true;
        return response;
    }

    public boolean loggedIn(String userName) {
        AuthDAO authDAO = new AuthDAO();
        AuthToken userToken = authDAO.get(userName);

        if (userToken.authToken == null && userToken.userName == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean correctPassword(LoginRequest request) {
        UserDAO userDAO = new UserDAO();
        User requestUser = userDAO.getUser(request.userName);

        if (requestUser.password.equals(request.password)) {
            return true;
        }
        return false;
    }

    public boolean validUsername(String userName) {
        UserDAO userDAO = new UserDAO();
        User requestUser = new User();
        requestUser = userDAO.getUser(userName);

        if (requestUser.userName == null) {
            return false;
        }
        return true;
    }


}
