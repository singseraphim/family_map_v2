package Server.Services.Load;
import java.util.ArrayList;

import Server.Model.*;

/**
 * Request object that contains the data needed for the Server.Services.Load.load() method.
 * users: array of users to be created
 * people and events: family history information for the users
 */
public class LoadRequest {
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Person> people = new ArrayList<>();
    public ArrayList<Event> events = new ArrayList<>();


}
