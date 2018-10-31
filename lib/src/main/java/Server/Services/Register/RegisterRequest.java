package Server.Services.Register;

import Server.Model.User;

/**
 * An object that contains the required data to register a user. Used by Server.Services.Register.register().
 */
public class RegisterRequest {
    public String userName;
    public  String password;
    public String email;
    public  String firstName;
    public String lastName;
    public String gender;

}
