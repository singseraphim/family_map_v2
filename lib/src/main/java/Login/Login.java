package Login;

/**
 * Service that handles all login requests.
 */
public class Login {

    /**
     * Checks if the given username is in the users table and that the password is correct.
     * It also checks if the user is already logged in.
     * if the credentials are valid and the user is not logged in already, it will add a new authToken to the AuthToken table.
     *
     * @param request: a LoginRequest object that contains a username and password.
     *
     * @return a LoginResponse object that will contain an error message on an unsuccessful login,
     * or an authToken, username and personID on a successful login.
     */
    public LoginResponse login (LoginRequest request) { //check if already logged in
        return null;
    }
}
