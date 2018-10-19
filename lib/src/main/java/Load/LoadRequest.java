package Load;
import java.util.ArrayList;

import Model.*;

/**
 * Request object that contains the data needed for the Load.load() method.
 * users: array of users to be created
 * people and events: family history information for the users
 */
public class LoadRequest {
    ArrayList<User> users;
    ArrayList<Person> people;
    ArrayList<Event> events;


}
