package Server.Services.Clear;
import Server.DAO.*;
import Exceptions.DatabaseException;

/**
 * Deletes all data from database.
 */
public class Clear {
    public Clear() {

    }
    /**
     * Calls the clear method on each Server.DAO. Handles any exceptions that may occur.
     * @return a ClearResponse object that contains either a successful response message or an error message.
     */
    public ClearResponse clear() {
        AuthDAO auth = new AuthDAO();
        EventDAO event = new EventDAO();
        PersonDAO person = new PersonDAO();
        UserDAO user = new UserDAO();
        ClearResponse response = new ClearResponse();
        auth.clear();
        event.clear();
        person.clear();
        user.clear();

        response.message = "Server.Services.Clear complete.";
        return response;
    }

}
