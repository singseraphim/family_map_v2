package Server.Services.Person;

import Server.DAO.AuthDAO;
import Server.DAO.PersonDAO;
import Server.Model.User;

/**
 * Server.Services.Person: service that handles all person requests.
 */
public class Person {
    private PersonDAO personDAO = new PersonDAO();
    /**
     * Returns all family members of the given user.
     * User is determined by the given authToken.
     * @param authToken: Authorization token for the current user.
     * @return an PeopleResponse object that either contains a list of people or an error message if there is a problem.
     */
    public PeopleResponse getPeople(String authToken) { //getEvents and getPeople both lock the database.
        System.out.println("Getting people for " + authToken);
        PeopleResponse response = new PeopleResponse();
        AuthDAO authDAO = new AuthDAO();

        //checks for valid authtoken
        User currentUser = authDAO.getUser(authToken);
        if (currentUser.personID == null) {
            response.message = "Auth token does not exist";
            response.success = false;
            return response;
        }
        response.data = personDAO.getPeople(currentUser.userName);
        response.success = true;
        return response;
    }

    /**
     * Returns a Server.Services.Person object associated with the given person ID.
     * Checks that the personID is valid, that the authToken is valid, and that the person belongs to the user corresponding
     * to the authToken.
     * @param personID: ID of the person to be returned.
     * @param authToken: authorization token of the current user.
     * @return an PersonResponse object that either contains a Server.Services.Person object or an error message if there is a problem.
     */

    public PersonResponse getPerson(String personID, String authToken) {
        System.out.println("Getting person " + personID + " related to token " + authToken);
        PersonResponse response = new PersonResponse();
        personDAO.createTables();

        //checks for valid data, if valid then sets response data
        if (validToken(personID, authToken)) {
            Server.Model.Person person = personDAO.getPerson(personID);
            response.gender = person.gender;
            response.descendant = person.descendant;
            response.lastName = person.lastName;
            response.firstName = person.firstName;
            response.father = person.father;
            response.mother = person.mother;
            response.spouse = person.spouse;
            response.personID = person.personID;
            response.success = true;
            return response;
        }
        else {
            response.message = "Person ID or authorization token are invalid.";
            response.success = false;
            return response;
        }
    }

    public boolean validToken(String personID, String authToken) {
        //get user from authtoken: make sure authtoken is valid
        //get user from personID: make sure personID is valid
        //compare the users, if identical then true.
        System.out.println("Checking if valid token");
        User currentUser = getAuthUser(authToken);
        if (currentUser.personID == null) { //if authToken doesn't exist
            return false;
        }
        User personDescendant = getPersonUser(personID);
        if (personDescendant.personID == null) { //if personID is invalid
            return false;
        }
        if (personDescendant.equals(currentUser)) { //if the current user matches the descendant of the event
            return true;
        }
        return false;
    }

    /**
     * Returns user associated with the given authToken
     * @param authToken the authToken associated with the returned user
     * @return a user associated with the given authToken, or null if the authToken doesn't exist in the table.
     */
    public User getAuthUser(String authToken) {
        System.out.println("Getting user related to auth token");
        AuthDAO authDAO = new AuthDAO();
        User currentUser = authDAO.getUser(authToken);

        return currentUser;
    }

    /**
     * Returns user associated with the given eventID
     * @param personID the eventID to find the user for
     * @return user object associated with the eventID, or null if the eventID doesn't exist in the table.
     */
    public User getPersonUser(String personID) {
        System.out.println("Getting user related to personID");
        User personUser = personDAO.getUser(personID);
        return personUser;
    }
}


