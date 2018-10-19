package Person;
import java.util.ArrayList;
import Model.Person;

/**
 * Will contain either an arraylist of Person objects or an error message.
 * Used by the Person.getPerson() method.
 */
public class PeopleResponse {
    public ArrayList<Person> people;
    public String message;

}
