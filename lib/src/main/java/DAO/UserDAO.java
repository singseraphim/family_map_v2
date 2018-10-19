
package DAO;

import Model.User;

import java.sql.*;

import Exceptions.DatabaseException;

/**
 * Data Access Operation class that handles all interactions with the Users table.
 */
public class UserDAO {

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
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Users` (\n" +
                        "\t`Username`\tTEXT NOT NULL UNIQUE,\n" +
                        "\t`Password`\tTEXT NOT NULL,\n" +
                        "\t`Email`\tTEXT NOT NULL,\n" +
                        "\t`FirstName`\tTEXT NOT NULL,\n" +
                        "\t`LastName`\tTEXT NOT NULL,\n" +
                        "\t`Gender`\tTEXT NOT NULL,\n" +
                        "\t`PersonID`\tTEXT NOT NULL UNIQUE,\n" +
                        "\tFOREIGN KEY(`FirstName`) REFERENCES `Persons`(`FirstName`),\n" +
                        "\tFOREIGN KEY(`Gender`) REFERENCES `Persons`(`Gender`),\n" +
                        "\tFOREIGN KEY(`PersonID`) REFERENCES `Persons`(`PersonID`),\n" +
                        "\tFOREIGN KEY(`LastName`) REFERENCES `Persons`(`LastName`),\n" +
                        "\tPRIMARY KEY(`Username`)\n" +
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

    public static void test(String[] args) {
        UserDAO user = new UserDAO();
        try {
            user.openConnection();
        } catch (DatabaseException e) {
            System.out.println("open didn't work my guy");
        }
        try {
            user.createTables();
        } catch (DatabaseException e) {
            System.out.println("create didn't work my guy");
        }

        User newUser = new User();
        User secUser = new User();
        newUser.userName = "mattie";
        newUser.personID = "gobblegook";
        newUser.gender = "z";
        newUser.lastName = "andrebest";
        newUser.firstName = "madeleine";
        newUser.email = "thing@thing";
        newUser.password = "supersecret";

        secUser.userName = "theBeast";
        secUser.personID = "doot";
        secUser.firstName = "bob";
        secUser.lastName = "gob";
        secUser.email = "beast@beastiness";
        secUser.gender = "m";
        secUser.password = "blahb";

        try {
            user.insert(newUser);
            user.insert(secUser);
        } catch (DatabaseException e) {
            System.out.println("insert didn't work my guy");
        }

        //User retUser = user.getUser("theBeast");
        //System.out.println("return user has password of " + retUser.password);
     /*   try {
            user.clear();
        } catch (DatabaseException e) {
            System.out.println("clear didn't work my guy");
        }*/

        try {
            user.closeConnection(true);
        } catch (DatabaseException e) {
            System.out.println("close didn't work my guy");
        }




    }

    /**
     * Inserts the given user object into the Users table.
     *
     * @param user: the User object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(User user) throws DatabaseException {
        assert(user.firstName != null);
        assert(user.lastName != null);
        assert(user.email != null);
        assert(user.password != null);
        assert(user.userName != null);
        assert(user.personID != null);
        assert(user.gender != null);
        assert(user.gender == "f" || user.gender == "m");
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Users " +
                        "(Username, Password, Email, FirstName, LastName, Gender, PersonID)" +
                        " values (?, ?, ?, ?, ?, ?, ?)";

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.userName);
                stmt.setString(2, user.password);
                stmt.setString(3, user.email);
                stmt.setString(4, user.firstName);
                stmt.setString(5, user.lastName);
                stmt.setString(6, user.gender);
                stmt.setString(7, user.personID);
                //System.out.println("inserted user");
                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("Insert failed: could not insert user.");
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks that the username exists, and if it does it
     * returns a User object associated with the given username. If the username doesn't exist, the function returns null.
     *
     * @param userName: the username of the user to find and return
     * @return a User object with all the data associated with the given username.
     */
    public User getUser(String userName) throws DatabaseException {
        User userObj = new User();
        assert(userName != null);
        String sql = "SELECT * FROM Users WHERE Username = '" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            userObj.userName = rs.getString("Username");
            userObj.password = rs.getString("Password");
            userObj.email = rs.getString("Email");
            userObj.firstName = rs.getString("FirstName");
            userObj.lastName = rs.getString("LastName");
            userObj.gender = rs.getString("Gender");
            userObj.personID = rs.getString("PersonID");

        }
        catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }
        return userObj;
    }

    /**
     * Executes a DROP TABLE statement on the Users table.
     *
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() throws DatabaseException{
        Statement stmt = null;
        String sql = "DROP TABLE Users";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Error: could not drop table");
            throw new DatabaseException();
        }

        return true;
    }

}

/*
Captains log:
Unit tests may or may not work, and debugging is tricky with sql and such big stuff which is the point.
But insert and getUser work awesome! A lot of error checking will happen in the services.
UserDAO works! It's dubious but it does the thing!
 */