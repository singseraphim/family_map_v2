package Register;

/**
 * Service that handles all register requests.
 */
public class Register {


    /**
     * Checks if the username is already in the User table. If it isn't, the function will
     * create a new User object and add it to the User table, and then create a new authToken object and add it to the
     * AuthToken table.
     * @param request: a register request object that contains a username, password,
     *                email, first name, last name and gender.
     * @return RegisterResponse: a register response object that will either contain
     *                an auth token, username and personID on a successful response
     *                  or an error message on a failed response.
     */
    public RegisterResponse register(RegisterRequest request) {

        return null;
    }
}

/*
Captain's log:
All classes are constructed.
DAO is done.
Clear is done.
Login is done.
Model is done.
Event is done.
Fill is done.
Load is done.
Person is done.
Register is done.

Check with TA's to make sure it's good, get your SQL stuff looking good and generate your javadoc.

 */
