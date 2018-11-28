package Server.DAO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.Event;
import Server.Model.User;

/**
 * Data Access Operation class that handles all interactions with the Events table.
 */
public class EventDAO {
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
                System.out.println("EventDAO.createTables opened connection");
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Events` (\n" +
                        "\t`EventID`\tTEXT NOT NULL,\n" +
                        "\t`Descendant`\tTEXT,\n" +
                        "\t`Person`\tTEXT,\n" +
                        "\t`Latitude`\tREAL,\n" +
                        "\t`Longitude`\tREAL,\n" +
                        "\t`Country`\tTEXT,\n" +
                        "\t`City`\tTEXT,\n" +
                        "\t`EventType`\tTEXT,\n" +
                        "\t`Year`\tINTEGER,\n" +
                        "\tPRIMARY KEY(`EventID`)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                    closeConnection(true);
                    System.out.println("EventDAO.createTables closed connection");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Returns a list of all events associated with a given user.
     *
     * @param userName: the username that the Events will be associated with.
     * @return a list of Server.Services.Event objects associated with the given username.
     */
    public ArrayList<Server.Model.Event> getEvents(String userName) throws DatabaseException {
        ArrayList<Event> eventList = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        String sql = "SELECT * FROM Events WHERE Descendant = '" + userName + "'";
        try {
            if (!tableExists()) createTables();
            User testUser = userDAO.getUser(userName); //checking that the username is valid
            if (testUser.userName == null) throw new DatabaseException("User does not exist");
            openConnection();
            System.out.println("EventDAO.getEvents opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ArrayList<String> eventIDs = new ArrayList<>();

            while (rs.next()) {
                eventIDs.add(rs.getString("EventID")); //makes an array of event ids
            }

            st.close();
            closeConnection(true);
            System.out.println("EventDAO.getEvents closed connection");

            for (String eventID : eventIDs) { //adds each event id to the event table
                Event newEvent = getEvent(eventID);
                eventList.add(newEvent);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return eventList;
    }

    /**
     * Checks that the eventID exists, and if it does it
     * returns an event object associated with the given event ID. If the eventID doesn't exist, the function returns null.
     *
     * @param eventID the ID of the event to find
     * @return an Server.Services.Event object with all the data associated with the given event ID.
     */
    public Event getEvent(String eventID) {
        Event event = new Event();
        assert (eventID != null);
        String sql = "SELECT * FROM Events where EventID = '" + eventID + "'";
        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("EventDAO.getEvent opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                event.eventID = rs.getString("EventID");
                event.eventType = rs.getString("EventType");
                event.city = rs.getString("City");
                event.country = rs.getString("Country");
                event.longitude = rs.getDouble("Longitude");
                event.latitude = rs.getDouble("Latitude");
                event.descendant = rs.getString("Descendant");
                event.person = rs.getString("Person");
                event.year = rs.getInt("Year");
            }
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("EventDAO.getEvent closed connection");
        }
        return event;
    }

    /**
     * Inserts the given Server.Services.Event object into the Events table.
     *
     * @param event: the event object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(Event event) throws DatabaseException {
        Event testEvent = getEvent(event.eventID); //checks that the event isn't already in the table
        if (testEvent.eventID != null) throw new DatabaseException("Event ID already exists");

        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Events " +
                        "(EventID, Descendant, Person, Latitude, Longitude, Country, City, EventType, Year)" +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                if (!tableExists()) createTables();
                openConnection();
                System.out.println("EventDAO.insert opened connection");

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, event.eventID);
                stmt.setString(2, event.descendant);
                stmt.setString(3, event.person);
                stmt.setDouble(4, event.latitude);
                stmt.setDouble(5, event.longitude);
                stmt.setString(6, event.country);
                stmt.setString(7, event.city);
                stmt.setString(8, event.eventType);
                stmt.setInt(9, event.year);
                //System.out.println("inserted user");
                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("Insert failed: could not insert person.");
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                closeConnection(true);
                System.out.println("EventDAO.insert closed connection");
            }

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return true;
    }

    /**
     * Checks whether the given Server.Services.Event data is in the Events table. If the data exists, it removes it.
     *
     * @param event: the event object containing the data to be deleted
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean remove(Event event) throws DatabaseException {
        assert (event != null);
        String sql = "delete from Events where EventID = '" + event.eventID + "';";
        Event testEvent = getEvent(event.eventID); //checks that the event exists in the table
        if (testEvent.eventID == null) throw new DatabaseException("Event does not exist");

        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("EventDAO.remove opened connection");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("EventDAO.remove closed connection");
        }

        return true;
    }

    /**
     * Removes all events associated with a specific user.
     *
     * @param userName: the username for which all associated events will be removed
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean removeEvents(String userName) throws DatabaseException {
        String sql = "DELETE FROM Events WHERE Descendant = '" + userName + "'";
        try {
            UserDAO userDAO = new UserDAO();
            User testUser = userDAO.getUser(userName); //checks that the user exists
            if (testUser.userName == null) throw new DatabaseException();
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("EventDAO.removeEvents opened connection");

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Issue removing person");
        } finally {
            closeConnection(true);
            System.out.println("EventDAO.removeEvents closed connection");
        }
        return true;
    }

    /**
     * Executes a DROP TABLE statement on the Events table.
     *
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() {
        Statement stmt = null;
        String sql = "DROP TABLE IF EXISTS Events";
        try {
            openConnection();
            System.out.println("Event.clear opened connection");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("Event.clear closed connection");

        }

        return true;
    }

    /**
     * returns the user object that is the descendant of the given eventID.
     *
     * @param eventID the id to find the user associated with.
     * @return a user object associated with the eventID.
     */
    public User getUser(String eventID) throws DatabaseException {
        String sql = "SELECT * FROM Events WHERE EventID = '" + eventID + "'";
        User returnUser = new User();
        if (!tableExists()) {
            createTables();
        }
        ArrayList<String> userList = new ArrayList<>();
        Event testEvent = getEvent(eventID);
        if (testEvent.eventID == null) throw new DatabaseException("Event ID does not exist");
        try {
            openConnection();
            System.out.println("EventDAO.getUser opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userList.add(rs.getString("Descendant"));
            }
            st.close();
            closeConnection(true);
            System.out.println("EventDAO.getUser closed connection");

            if (userList.size() == 0) {
                return returnUser;
            }
            UserDAO userDAO = new UserDAO();
            returnUser = userDAO.getUser(userList.get(0));
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return returnUser;
    }

    public int getBirth(String personID) { //returns the birth year of a given person
        int returnYear = 0;
        String sql = "SELECT * FROM Events WHERE Person = '" + personID + "' AND EventType = 'Birth'";
        try {
            openConnection();
            System.out.println("EventDAO.getBirth opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                returnYear = rs.getInt("Year");
            }
            st.close();
            closeConnection(true);
            System.out.println("EventDAO.getBirth closed connection");

        } catch (Exception e) {

        }
        return returnYear;
    }

    public int getDeath(String personID) { //returns death year of a given person
        int returnYear = 0;
        String sql = "SELECT * FROM Events WHERE Person = '" + personID + "' AND EventType = 'Death'";
        try {
            openConnection();
            System.out.println("EventDAO.getDeath opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                returnYear = rs.getInt("Year");
            }
            st.close();
            closeConnection(true);
            System.out.println("EventDAO.getDeath closed connection");

        } catch (Exception e) {

        }
        return returnYear;
    }

    public int getMarriage(String personID) { //returns marriage year of a given person
        int returnYear = 0;
        String sql = "SELECT * FROM Events WHERE Person = '" + personID + "' AND EventType = 'Marriage'";
        try {
            openConnection();
            System.out.println("EventDAO.getMarriage opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                returnYear = rs.getInt("Year");
            }
            st.close();
            closeConnection(true);
            System.out.println("EventDAO.getMarriage closed connection");

        } catch (Exception e) {

        }
        return returnYear;
    }

    public boolean tableExists() {
        boolean tExists = false;
        String tableName = "Events";
        try {
            openConnection();
            System.out.println("Person.tableExists opened connection");

            try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
                while (rs.next()) {
                    tExists = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            closeConnection(true);
            System.out.println("Person.tableExists closed connection");
        }
        return tExists;
    }


}
