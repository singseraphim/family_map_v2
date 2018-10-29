package Server.Services.Person;
import java.util.ArrayList;
import Server.Model.Person;

/**
 * Will contain either an arraylist of Server.Services.Person objects or an error message.
 * Used by the Server.Services.Person.getPerson() method.
 */
public class PeopleResponse {
    public ArrayList<Person> data;
    public String message;
    public boolean success = true;

}
