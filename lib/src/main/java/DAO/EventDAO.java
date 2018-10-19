package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.DatabaseException;
import Model.Event;
import Model.Person;

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
                stmt.executeUpdate("CREATE TABLE `ETesting` (\n" +
                        "\t`EventID`\tTEXT NOT NULL UNIQUE,\n" +
                        "\t`Descendant`\tTEXT NOT NULL,\n" +
                        "\t`Person`\tTEXT NOT NULL,\n" +
                        "\t`Latitude`\tREAL,\n" +
                        "\t`Longitude`\tREAL,\n" +
                        "\t`Country`\tTEXT,\n" +
                        "\t`City`\tTEXT,\n" +
                        "\t`EventType`\tTEXT,\n" +
                        "\t`Year`\tINTEGER,\n" +
                        "\tFOREIGN KEY(`Person`) REFERENCES `Persons`(`PersonID`),\n" +
                        "\tFOREIGN KEY(`Descendant`) REFERENCES `Users`(`Username`),\n" +
                        "\tPRIMARY KEY(`EventID`)\n" +
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
        EventDAO eventd = new EventDAO();
        try {
            eventd.openConnection();
            // System.out.println("connection open");
        } catch (DatabaseException e) {
            System.out.println("open didn't work my guy");
        }
  /*      try {
            eventd.createTables();
            // System.out.println("tables created");
        } catch (DatabaseException e) {
            System.out.println("create didn't work my guy");
        }*/

        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();

        event1.person = "p1";
        event1.eventID = "id1";
        event1.descendant = "d2";
        event1.country = "co1";
        event1.city = "ci1";
        event1.eventType = "et1";
        event1.latitude = 5;
        event1.longitude = 10;
        event1.year = 20;

        event2.person = "p2";
        event2.eventID = "id2";
        event2.descendant = "d2";
        event2.country = "co2";
        event2.city = "ci2";
        event2.eventType = "et2";
        event2.latitude = 6;
        event2.longitude = 11;
        event2.year = 21;

        event3.person = "p3";
        event3.eventID = "id3";
        event3.descendant = "d3";
        event3.country = "co3";
        event3.city = "ci3";
        event3.eventType = "et3";
        event3.latitude = 7;
        event3.longitude = 12;
        event3.year = 22;

        try {
            eventd.insert(event1);
            eventd.insert(event2);
            eventd.insert(event3);
            // System.out.println("tables created");
        } catch (DatabaseException e) {
            System.out.println("insert didn't work my guy");
        }

/*        try {
            Event resEvent = eventd.getEvent("id2");
            System.out.println("Result country is " + resEvent.country);
        } catch (DatabaseException e) {
            System.out.println("insert didn't work my guy");
        }*/
/*        try {
            ArrayList<Event> result = eventd.getEvents("d2");
            for (Event e : result) {
                System.out.println(e.eventID);
            }
        } catch (DatabaseException e) {
            System.out.println("insert didn't work my guy");
        }*/

/*        try {
            eventd.removeEvents("d2");
        } catch (DatabaseException e) {
            System.out.println("remove events didn't work my guy");
        }*/

/*        try {
            eventd.remove(event1);
        } catch (DatabaseException e) {
            System.out.println("remove didn't work my guy");
        }*/

  /*      try {
            eventd.clear();
            // System.out.println("connection open");
        } catch (DatabaseException e) {
            System.out.println("clear failed");
        }*/



        try {
            eventd.closeConnection(true);
        } catch (DatabaseException e) {
            System.out.println("close didn't work my guy");
        }
    }

    /**
     * Returns a list of all events associated with a given user.
     * @param userName: the username that the Events will be associated with.
     * @return a list of Event objects associated with the given username.
     */
    public ArrayList<Event> getEvents(String userName) throws DatabaseException {
        ArrayList<Event> eventList = new ArrayList<>();
        assert (userName != null);
        String sql = "SELECT * FROM ETesting WHERE Descendant = '" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Event newEvent = new Event();


                newEvent.eventID = rs.getString("EventID");
                newEvent.eventType = rs.getString("EventType");
                newEvent.city = rs.getString("City");
                newEvent.country = rs.getString("Country");
                newEvent.longitude = rs.getDouble("Longitude");
                newEvent.latitude = rs.getDouble("Latitude");
                newEvent.descendant = rs.getString("Descendant");
                newEvent.person = rs.getString("Person");
                newEvent.year = rs.getInt("Year");

                eventList.add(newEvent);
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }
        return eventList;
    }

    /**
     * Checks that the eventID exists, and if it does it
     * returns an event object associated with the given event ID. If the eventID doesn't exist, the function returns null.
     * @param eventID the ID of the event to find
     * @return an Event object with all the data associated with the given event ID.
     */
    public Event getEvent(String eventID) throws DatabaseException {
        Event event = new Event();
        assert (eventID != null);
        String sql = "SELECT * FROM ETesting where EventID = '" + eventID + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            event.eventID = rs.getString("EventID");
            event.eventType = rs.getString("EventType");
            event.city = rs.getString("City");
            event.country = rs.getString("Country");
            event.longitude = rs.getDouble("Longitude");
            event.latitude = rs.getDouble("Latitude");
            event.descendant = rs.getString("Descendant");
            event.person = rs.getString("Person");
            event.year = rs.getInt("Year");

        } catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }
        return event;
    }

    /**
     * Inserts the given Event object into the Events table.
     * @param event: the event object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(Event event) throws DatabaseException {
        assert (event.descendant != null);
        assert (event.eventID != null);
        assert (event.person != null);

        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO ETesting " +
                        "(EventID, Descendant, Person, Latitude, Longitude, Country, City, EventType, Year)" +
                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            }

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return true;
    }

    /**
     * Checks whether the given Event data is in the Events table. If the data exists, it removes it.
     * @param event: the event object containing the data to be deleted
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean remove(Event event) throws DatabaseException {
        assert (event != null);
        String sql = "delete from ETesting where EventID = '" + event.eventID + "';";

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
     * Removes all events associated with a specific user.
     * @param userName: the username for which all associated events will be removed
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean removeEvents(String userName) throws DatabaseException {
        assert (userName != null);
        String sql = "DELETE FROM ETesting WHERE Descendant = '" + userName + "'";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Issue removing person");
            throw new DatabaseException();
        }
        return true;
    }

    /**
     * Executes a DROP TABLE statement on the Events table.
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() throws DatabaseException {
        Statement stmt = null;
        String sql = "DROP TABLE ETesting";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: could not drop table");
            throw new DatabaseException();
        }

        return true;
    }


}
