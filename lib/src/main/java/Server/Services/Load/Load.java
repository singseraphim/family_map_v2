package Server.Services.Load;

import Exceptions.DatabaseException;
import Server.DAO.AuthDAO;
import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.Event;
import Server.Model.Person;
import Server.Model.User;

/**
 * Service that handles all load requests.
 */
public class Load {

    /**
     * Adds an array of users to the Users table, an array of people to the Persons table, and an array of events to the Events table.
     * Checks to make sure all data is valid and properly formatted.
     * @param request: a LoadRequest object that contains an array of users, people and events to add to their corresponding tables.
     * @return a LoadResponse object that contains a message that indicates success or failure.
     */
    private AuthDAO authDAO = new AuthDAO();
    private EventDAO eventDAO = new EventDAO();
    private PersonDAO personDAO = new PersonDAO();
    private UserDAO userDAO = new UserDAO();

    public LoadResponse load (LoadRequest request) {
        LoadResponse response = new LoadResponse();

        //clears all data
        try {
            authDAO.clear();
            eventDAO.clear();
            personDAO.clear();
            userDAO.clear();

            //adds given data to respective tables
            for (User user : request.users) {
                System.out.println("Adding " + user.firstName);
                userDAO.insert(user);
            }
            for (Event event : request.events) {
                System.out.println("Adding " + event.eventType);
                eventDAO.insert(event);
            }
            for (Person person : request.persons) {
                System.out.println("Adding " + person.firstName);

                personDAO.insert(person);
            }

        }
        catch(DatabaseException e) {
            System.out.println(e.toString());
            response.message = "Error loading: " + e.toString();
            response.success = false;
            authDAO.clear(); //clears all data if there's an error
            eventDAO.clear();
            personDAO.clear();
            userDAO.clear();
            return response;

        }
        response.message = "Successfully added " + request.users.size() + " users, " +
                request.persons.size() + " persons, and " + request.events.size() +
                " events to the database.";
        response.success = true;
        return response;
    }
}
