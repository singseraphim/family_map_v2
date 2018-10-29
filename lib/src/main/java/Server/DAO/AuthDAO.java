package Server.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Exceptions.DatabaseException;
import Server.Model.AuthToken;
import Server.Model.Event;
import Server.Model.User;

/**
 * Data Access Operation class that handles all interactions with the AuthToken table.
 */
public class AuthDAO {

    //SETUP DRIVER
    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //OPEN CONNECTION
    private Connection conn;

    public void openConnection() {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familyMapDB.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    //CLOSE CONNECTION
    public void closeConnection(boolean commit) {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        catch (NullPointerException e) {

        }
    }

    //CREATE TABLE
    public void createTables() {
        try {
            Statement stmt = null;
            try {
                openConnection();
                System.out.println("Auth.createTables opened connection");

                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `AuthTokens` (\n" +
                        "\t`UserName`\tTEXT NOT NULL,\n" +
                        "\t`AuthToken`\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(`AuthToken`)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                closeConnection(true);
                System.out.println("Auth.createTables closed connection");

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }




        /**
         * Inserts the data in the given token into the AuthToken table.
         * @param token: token object that represents the data to be inserted
         * @return a bool that indicates whether or not the insertion was successful.
         */
    public boolean insert(AuthToken token) throws DatabaseException{
        if (!tableExists()) {
            createTables();
        }
        if (token.userName == null || token.userName == "") {
            throw new DatabaseException("Token missing username");
        }
        if (token.authToken == null || token.authToken == "") {
            throw new DatabaseException("Token missing token string");
        }
        AuthToken testToken = get(token.userName);

        if (testToken.userName != null) {
            throw new DatabaseException("Username exists in table");
        }
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO AuthTokens " +
                        "(UserName, AuthToken)" +
                        " values ('" + token.userName + "','" + token.authToken + "');";

                openConnection();
                System.out.println("Auth.insert opened connection");

                stmt = conn.prepareStatement(sql);
                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("Insert failed: could not insert person.");
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                closeConnection(true);
                System.out.println("Auth.insert closed connection");

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DatabaseException(e.toString());
        }
        return true;
    }

    /**
     * Removes the given token from the AuthToken table.
     * @param token: token object that represents the data to be deleted
     * @return a bool that indicates whether or not the remove was successful.
     */
    public boolean remove(AuthToken token) throws DatabaseException {
        String sql = "delete from AuthTokens where AuthToken = '" + token.authToken + "';";

        if (!tableExists()) {
            createTables();
        }
        AuthToken testToken = get(token.userName);
        if (testToken.userName == null) {
            throw new DatabaseException("User does not exist in auth table");
        }
        try {
            openConnection();
            System.out.println("Auth.remove opened connection");

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Auth.remove closed connection");

        }
        return true;
    }

    /**
     * Executes a DROP TABLE statement on the AuthToken table.
     * @return a bool that indicates whether or not the clear was successful.
     */
    public boolean clear() {

        Statement stmt = null;
        String sql = "DROP TABLE IF EXISTS AuthTokens";
        try {
            openConnection();
            System.out.println("Auth.clear opened connection");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Auth.clear closed connection");

        }

        return true;
    }

    /**
     * Returns an authtoken corresponding to the given username
     * @param userName username tied to the token that will be returned
     * @return an authtoken object
     */
    public AuthToken get(String userName) {
        AuthToken token = new AuthToken();
        String sql = "SELECT * FROM AuthTokens where UserName = '" + userName + "'";
        try {
            if (!tableExists()) {
                createTables();
            }
            openConnection();
            System.out.println("Auth.get opened connection"); //    WHY DOES THE DATA EXIST IN THE TABLE ALREADY

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                token.userName = rs.getString("UserName");
                token.authToken = rs.getString("AuthToken");
            }
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Auth.get closed connection");
        }
        return token;
    }

    public static void main(String[] args) {
        AuthDAO authDAO = new AuthDAO();
        User user = authDAO.getUser("TestToken");
        System.out.println(user.firstName);
    }

    public User getUser(String authToken) {
        String sql = "SELECT * FROM AuthTokens WHERE AuthToken = '" + authToken + "'";
        User returnUser = new User();
        if (!tableExists()) {
            createTables();
        }
        ArrayList<String> userList = new ArrayList<>();
        try {
            openConnection();
            System.out.println("Auth.getUser opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
               String username = rs.getString("UserName");
                userList.add(username);
            }
            st.close();
            closeConnection(true);
            System.out.println("Auth.getUser closed connection");

            if (userList.size() == 0) {
                return returnUser;
            }
            UserDAO userDAO = new UserDAO();
            returnUser = userDAO.getUser(userList.get(0));
        }
        catch(SQLException e) {
            System.out.println(e.toString());
        }
        return returnUser;
    }

    public boolean tableExists() {
        boolean tExists = false;
        String tableName = "AuthTokens";
        try {
            openConnection();
            System.out.println("Auth.tableExists opened connection");

            try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
                while (rs.next()) {
                    tExists = true;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Auth.tableExists closed connection");
        }
        return tExists;
    }



}
