package Login;

/**
 * A response object that will either contain an error message or an authToken, userName and personID.
 * The Login class will return a LoginResponse object on an attempted login.
 */
public class LoginResponse {

    public String authToken;
    public String userName;
    public String personID;
    public String errorMessage;

}
