package Server.Services.Register;

import java.util.UUID;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.AuthToken;
import Server.Model.Person;
import Server.Model.User;

/**
 * Service that handles all register requests.
 */
public class Register {


    /**
     * Checks if the username is already in the User table. If it isn't, the function will
     * create a new User object and add it to the User table, and then create a new authToken object and add it to the
     * AuthToken table.
     * @param request: a register request object that contains a username, password,
     *                email, first name, last name and gender.
     * @return RegisterResponse: a register response object that will either contain
     *                an auth token, username and personID on a successful response
     *                  or an error message on a failed response.
     */
    public RegisterResponse register(RegisterRequest request) {
        /*
        THINGS TO CHECK FOR:
        username needs to be unique, not blank
        password needs to be not blank
        email, not blank
        names, not blank
        gender needs to be m or f or nb
         */
        RegisterResponse response = new RegisterResponse();
        if (!uniqueUsername(request.newUser.userName)) {
            response.message = "Username taken";
            response.success = false;
            return response;
        }
        if (request.newUser.userName == "" || request.newUser.userName == null) {
            response.success = false;
            response.message = "Username blank";
            return response;
        }
        if (request.newUser.password == "" || request.newUser.password == null) {
            response.message = "Password blank";
            response.success = false;

            return response;
        }
        if (request.newUser.email == "" || request.newUser.email == null) {
            response.message = "Email blank";
            response.success = false;

            return response;

        }
        if (request.newUser.firstName == "" || request.newUser.firstName == null) {
            response.message = "First name blank";
            response.success = false;

            return response;

        }
        if (request.newUser.lastName == "" || request.newUser.lastName == null) {
            response.message = "Last name blank";
            response.success = false;

            return response;

        }
        if (request.newUser.gender != "m" && request.newUser.gender != "f") {
            response.message = "Invalid gender";
            response.success = false;

            return response;

        }

        //DO THE LOGGING IN THING
        /*
        Insert into users table
        Insert into persons table
        Insert into auth table
         */

        UserDAO userDAO = new UserDAO();
        PersonDAO personDAO = new PersonDAO();
        AuthDAO authDAO = new AuthDAO();

        User newUser = request.newUser;
        newUser.personID = UUID.randomUUID().toString().substring(0, 8);

        Person newPerson = new Person();
        newPerson.personID = newUser.personID;
        newPerson.firstName = request.newUser.firstName;
        newPerson.lastName = request.newUser.lastName;
        newPerson.descendant = request.newUser.userName;
        newPerson.gender = request.newUser.gender;

        AuthToken newToken = new AuthToken();
        newToken.userName = request.newUser.userName;
        newToken.authToken = UUID.randomUUID().toString().substring(0, 8);

        try {
            userDAO.insert(request.newUser);
            personDAO.insert(newPerson);
            authDAO.insert(newToken);

        }
        catch (DatabaseException e) {
            response.message = "error inserting into tables";
            response.success = false;
            return response;

        }

        response.authToken = newToken.authToken;
        response.personID = newPerson.personID;
        response.userName = request.newUser.userName;
        response.success = true;
        return response;
    }

    public boolean uniqueUsername(String attemptedName) {
        UserDAO userDAO = new UserDAO();
        User currentUser = new User();
        currentUser = userDAO.getUser(attemptedName);

        if (currentUser.userName == null) {
            return true;
        }
        return false;
    }
}
