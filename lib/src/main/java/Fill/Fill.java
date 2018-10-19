package Fill;

/**
 * Service that handles all fill requests.
 */
public class Fill {

    /**
     * Generates family history data for the given user.
     * Checks for valid username and generation number, then uses files of Person and Event data to randomly
     * generate events and people to associate with the given user.
     * @param username: the username of the current user
     * @param generations: the number of generations to generate data for
     * @return a FillResponse object with a message indicating whether or not the fill was successful.
     */
    public FillResponse fill(String username, int generations) {
        return null;
    }

}
