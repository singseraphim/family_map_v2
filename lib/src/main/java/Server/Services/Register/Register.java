package Server.Services.Register;

import java.util.UUID;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.AuthToken;
import Server.Model.Event;
import Server.Model.Person;
import Server.Model.User;
import Server.Services.Fill.Fill;
import Server.Services.Fill.FillResponse;

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
        if (!uniqueUsername(request.userName)) {
            response.message = "Username taken";
            response.success = false;
            return response;
        }
        if (request.userName.equals("") || request.userName == null) {
            response.success = false;
            response.message = "Username blank";
            return response;
        }
        if (request.password.equals("") || request.password == null) {
            response.message = "Password blank";
            response.success = false;

            return response;
        }
        if (request.email.equals("") || request.email == null) {
            response.message = "Email blank";
            response.success = false;

            return response;

        }
        if (request.firstName.equals("") || request.firstName == null) {
            response.message = "First name blank";
            response.success = false;

            return response;

        }
        if (request.lastName.equals("") || request.lastName == null) {
            response.message = "Last name blank";
            response.success = false;

            return response;

        }
        if (!request.gender.equals("m") && !request.gender.equals("f")) {
            response.message = "Invalid gender";
            response.success = false;

            return response;

        }

        //DO THE LOGGING IN THING
        /*
        Insert into users table
        Insert into persons table
        Insert into auth table
        Fill 4 generations
         */

        UserDAO userDAO = new UserDAO();
        PersonDAO personDAO = new PersonDAO();
        AuthDAO authDAO = new AuthDAO();

        User newUser = new User();
        newUser.lastName = request.lastName;
        newUser.firstName = request.firstName;
        newUser.email = request.email;
        newUser.password = request.password;
        newUser.userName = request.userName;
        newUser.gender = request.gender;
        newUser.personID = UUID.randomUUID().toString();

        Person newPerson = new Person();
        newPerson.personID = newUser.personID;
        newPerson.firstName = request.firstName;
        newPerson.lastName = request.lastName;
        newPerson.descendant = request.userName;
        newPerson.gender = request.gender;

        AuthToken newToken = new AuthToken();
        newToken.userName = request.userName;
        newToken.authToken = UUID.randomUUID().toString();


        try {
            userDAO.insert(newUser);
            personDAO.insert(newPerson);
            authDAO.insert(newToken);

        }
        catch (DatabaseException e) {
            response.message = "Error inserting into tables: " + e.toString();
            response.success = false;
            return response;

        }

        Fill fillService = new Fill();
        FillResponse fillResponse = fillService.fill(newUser.userName, 4);
        if (fillResponse.success == false) {
            response.success = false;
            response.message = "error generating ancestors";
            return response;
        }

        response.authToken = newToken.authToken;
        response.personID = newPerson.personID;
        response.userName = request.userName;
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
/*

string[] args = uriPath.split("\")

 */