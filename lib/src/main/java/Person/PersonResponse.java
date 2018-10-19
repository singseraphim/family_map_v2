package Person;
import Model.Person;

/**
 * Will contain either a person object or an error message. Used by the Person.getPerson() method.
 */
public class PersonResponse {
    public String message;
    public Person person;

}
