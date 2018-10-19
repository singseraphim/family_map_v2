package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exceptions.DatabaseException;
import Model.AuthToken;
import Model.Event;

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

    public static void main (String[] args) {
        AuthDAO auth = new AuthDAO();

        try {
            auth.openConnection();
            // System.out.println("connection open");
        } catch (DatabaseException e) {
            System.out.println("open didn't work my guy");
        }
        /*try {
            auth.createTables();
            // System.out.println("tables created");
        } catch (DatabaseException e) {
            System.out.println("create didn't work my guy");
        }*/

        AuthToken a1 = new AuthToken();
        AuthToken a2 = new AuthToken();
        AuthToken a3 = new AuthToken();

        a1.authToken = "t1";
        a1.userName = "u1";
        a2.authToken = "t2";
        a2.userName = "u2";
        a3.authToken = "t3";
        a3.userName = "u3";

 /*       try {
            auth.insert(a1);
            auth.insert(a2);
            auth.insert(a3);
        }
        catch (DatabaseException e) {
            System.out.println("insert bad");
        }*/

/*        try {
            AuthToken resToken = auth.get("u3");
            System.out.println("returned " + resToken.authToken);
        } catch (DatabaseException e) {
            System.out.println("get bad");
        }*/
        try {
            auth.clear();
        } catch (DatabaseException e) {
            System.out.println("clear bad");
        }

        try {
            auth.closeConnection(true);
        } catch (DatabaseException e) {
            System.out.println("close didn't work my guy");
        }



    }

    public void openConnection() throws DatabaseException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familyMapDB.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("openConnection failed");
        }
    }

    //CLOSE CONNECTION
    public void closeConnection(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new DatabaseException("closeConnection failed");
        }
    }

    //CREATE TABLE
    public void createTables() throws DatabaseException {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE `AuthTest` (\n" +
                        "\t`UserName`\tTEXT NOT NULL,\n" +
                        "\t`AuthToken`\tTEXT NOT NULL,\n" +
                        "\tFOREIGN KEY(`UserName`) REFERENCES `Users`(`Username`),\n" +
                        "\tPRIMARY KEY(`AuthToken`)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("createTables failed");
        }
    }


    /**
     * Inserts the data in the given token into the AuthToken table.
     * @param token: token object that represents the data to be inserted
     * @return a bool that indicates whether or not the insertion was successful.
     */
    public boolean insert(AuthToken token) throws DatabaseException{
        assert (token.authToken != null);
        assert (token.userName != null);

        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO AuthTest " +
                        "(UserName, AuthToken)" +
                        " values (?, ?)";

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, token.userName);
                stmt.setString(2, token.authToken);

                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("Insert failed: could not insert person.");
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return true;
    }

    /**
     * Removes the given token from the AuthToken table.
     * @param token: token object that represents the data to be deleted
     * @return a bool that indicates whether or not the remove was successful.
     */
    public boolean remove(AuthToken token) throws DatabaseException{
        assert (token != null);
        String sql = "delete from AuthTest where AuthToken = '" + token.authToken + "';";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Issue removing person");
            throw new DatabaseException();
        }

        return true;
    }

    /**
     * Executes a DROP TABLE statement on the AuthToken table.
     * @return a bool that indicates whether or not the clear was successful.
     */
    public boolean clear() throws DatabaseException {
        Statement stmt = null;
        String sql = "DROP TABLE AuthTest";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: could not drop table");
            throw new DatabaseException();
        }

        return true;
    }

    /**
     * Returns an authtoken corresponding to the given username
     * @param userName username tied to the token that will be returned
     * @return an authtoken object
     */
    public AuthToken get(String userName) throws DatabaseException{
        AuthToken token = new AuthToken();
        assert (userName != null);
        String sql = "SELECT * FROM AuthTest where UserName = '" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            token.userName = rs.getString("UserName");
            token.authToken = rs.getString("AuthToken");


        } catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }
        return token;
    }

}
