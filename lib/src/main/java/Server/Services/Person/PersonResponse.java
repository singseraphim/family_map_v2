package Server.Services.Person;
import Server.Model.Person;

/**
 * Will contain either a person object or an error message. Used by the Server.Services.Person.getPerson() method.
 */
public class PersonResponse {
    public String message;
    public String descendant;
    public String personID;
    public String firstName;
    public String lastName;
    public String gender;
    public String father;
    public String mother;
    public String spouse;
    public boolean success = true;

}
