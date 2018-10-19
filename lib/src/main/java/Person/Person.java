package Person;
import java.util.List;

/**
 * Person: service that handles all person requests.
 */
public class Person {

    /**
     * Returns all family members of the given user.
     * User is determined by the given authToken.
     * @param authToken: Authorization token for the current user.
     * @return an PeopleResponse object that either contains a list of people or an error message if there is a problem.
     */
    public List<Person> getPeople(String authToken) {
        return null;
    }

    /**
     * Returns a Person object associated with the given person ID.
     * Checks that the personID is valid, that the authToken is valid, and that the person belongs to the user corresponding
     * to the authToken.
     * @param personID: ID of the person to be returned.
     * @param authToken: authorization token of the current user.
     * @return an PersonResponse object that either contains a Person object or an error message if there is a problem.
     */

    public Person getPerson(String personID, String authToken) {
        return null;
    }
}
