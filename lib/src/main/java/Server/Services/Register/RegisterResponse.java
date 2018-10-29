package Server.Services.Register;

/**
 * A response object that will either contain an error message or a username, personID and authToken.
 *  The Server.Services.Register class will return a RegisterResponse object on an attempted registration.
 */
public class RegisterResponse {
    public String authToken;
    public String userName;
    public String personID;
    public String message;
    public boolean success;

}
