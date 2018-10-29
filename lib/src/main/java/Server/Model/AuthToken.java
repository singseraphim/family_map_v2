package Server.Model;

/**
 * Server.Model class that can hold a tuple in the AuthToken table.
 */
public class AuthToken {
    public String userName;
    public String authToken;

    public boolean equals(AuthToken token) {
        if (this.userName.equals(token.userName) && this.authToken.equals(token.authToken)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "Name: " + userName + ", ID: " + authToken;
    }

}
