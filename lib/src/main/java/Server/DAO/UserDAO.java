
package Server.DAO;

import Server.Model.User;

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
        } catch (NullPointerException e) {

        }
    }

    //CREATE TABLE
    public void createTables() {
        try {
            Statement stmt = null;
            try {
                openConnection();
                System.out.println("User.createTables opened connection");

                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Users` (\n" +
                        "\t`Username`\tTEXT NOT NULL,\n" +
                        "\t`Password`\tTEXT NOT NULL,\n" +
                        "\t`Email`\tTEXT NOT NULL,\n" +
                        "\t`FirstName`\tTEXT NOT NULL,\n" +
                        "\t`LastName`\tTEXT NOT NULL,\n" +
                        "\t`Gender`\tTEXT NOT NULL,\n" +
                        "\t`PersonID`\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(`Username`)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                closeConnection(true);
                System.out.println("User.createTables closed connection");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    /**
     * Inserts the given user object into the Users table.
     *
     * @param user: the User object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(User user) throws DatabaseException {

        if (!user.gender.equals("f") && !user.gender.equals("m"))
            throw new DatabaseException("This project does not support the gender spectrum");

        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("User.insert opened connection");

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
                closeConnection(true);
                System.out.println("User.insert closed connection");

            }

        } catch (SQLException e) {
            System.out.println(e.toString());
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
    public User getUser(String userName) {
        User userObj = new User();
        String sql = "SELECT * FROM Users WHERE Username = '" + userName + "'";
        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("User.getUser opened connection");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                userObj.userName = rs.getString("Username");
                userObj.password = rs.getString("Password");
                userObj.email = rs.getString("Email");
                userObj.firstName = rs.getString("FirstName");
                userObj.lastName = rs.getString("LastName");
                userObj.gender = rs.getString("Gender");
                userObj.personID = rs.getString("PersonID");
            }
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {
            closeConnection(true);
            System.out.println("User.getUser closed connection");

        }
        return userObj;
    }

    /**
     * Executes a DROP TABLE statement on the Users table.
     *
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() {
        Statement stmt = null;
        String sql = "DROP TABLE IF EXISTS Users";
        try {
            openConnection();
            System.out.println("User.clear opened connection");

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("User.clear closed connection");

        }

        return true;
    }


    public boolean tableExists() {
        boolean tExists = false;
        String tableName = "Users";
        try {
            openConnection();
            System.out.println("User.tableExists opened connection");

            try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
                while (rs.next()) {
                    tExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {
            closeConnection(true);
            System.out.println("User.tableExists closed connection");
        }
        return tExists;
    }

    public void remove(User user) throws DatabaseException {
        String sql = "delete from Users where Username = '" + user.userName + "';";
        User testUser = getUser(user.userName);
        if (testUser.userName == null) {
            throw new DatabaseException("User cannot be removed, does not exist");
        }
        try {
            openConnection();
            System.out.println("User.remove opened connection");

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("User.remove closed connection");

        }

    }

}