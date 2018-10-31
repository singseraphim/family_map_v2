package Server.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.Person;
import Server.Model.User;

/**
 * Data Access Operation class that handles all interactions with the Persons table.
 */
public class PersonDAO {

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
        catch(NullPointerException e) {

        }
    }

    //CREATE TABLE
    public void createTables() {
        try {
            Statement stmt = null;
            try {
                openConnection();
                System.out.println("Person.createTables opened connection");
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Persons` (\n" +
                        "\t`PersonID`\tTEXT NOT NULL,\n" +
                        "\t`Descendant`\tTEXT NOT NULL,\n" +
                        "\t`FirstName`\tTEXT NOT NULL,\n" +
                        "\t`LastName`\tTEXT NOT NULL,\n" +
                        "\t`Gender`\tTEXT NOT NULL,\n" +
                        "\t`Father`\tTEXT,\n" +
                         "\t`Mother`\tTEXT,\n" +
                        "\t`Spouse`\tTEXT,\n" +
                        "\tPRIMARY KEY(`PersonID`)\n" +
                        ");");

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;

                }
                closeConnection(true);
                System.out.println("Person.createTables closed connection");

            }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
    }

    /**
     * Returns a list of all persons associated with a given user.
     *
     * @param userName: the username that the persons will be associated with.
     * @return a list of Server.Services.Person objects associated with the given username.
     */
    public ArrayList<Person> getPeople(String userName) {
        ArrayList<Person> personList = new ArrayList<Person>();
        String sql = "SELECT * FROM Persons WHERE Descendant = '" + userName + "'";
        try {
            createTables();
            openConnection();
            System.out.println("Person.getPeople opened connection");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Person newPerson = getPerson(rs.getString("PersonID"));
                personList.add(newPerson);
            }
            st.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Person.getPeople closed connection");

        }


        return personList;
    }

    /**
     * Checks that the personID exists, and if it does it returns a person object associated with the given person ID. If the personID doesn't exist, the function returns null.
     *
     * @param personID the ID of the person to find and return
     * @return a Server.Services.Person object with all the data associated with the given person ID.
     */
    public Person getPerson(String personID) {
        Person person = new Person();
        String sql = "SELECT * FROM Persons where PersonID = '" + personID + "'";

        try {
            createTables();
            openConnection();
            System.out.println("Person.getPerson opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                person.firstName = rs.getString("FirstName");
                person.lastName = rs.getString("LastName");
                person.gender = rs.getString("Gender");
                person.personID = rs.getString("PersonID");
                person.descendant = rs.getString("Descendant");
                person.father = rs.getString("Father");
                person.mother = rs.getString("Mother");
                person.spouse = rs.getString("Spouse");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Person.getUser closed connection");
        }
        return person;
    }

    /**
     * Inserts the given person object into the Persons table.
     *
     * @param person: the Server.Services.Person object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(Person person) throws DatabaseException {
        if (person.descendant == null || person.descendant.equals("") || person.gender == null || person.gender.equals("")
                || person.firstName == null || person.firstName.equals("") || person.lastName == null || person.lastName.equals("")
                || person.personID == null || person.personID.equals(""))
            throw new DatabaseException("Required field is empty");
        if (!person.gender.equals("f") && !person.gender.equals("m"))
            throw new DatabaseException("This project does not support the gender spectrum");

        if(existsInPersons(person.personID)) throw new DatabaseException("Person already exists in table");

        try {
            PreparedStatement stmt = null;
            try {
                if (!tableExists()) createTables();
                openConnection();
                System.out.println("Person.insert opened connection");
                String sql = "INSERT INTO Persons " +
                        "(PersonID, Descendant, FirstName, LastName, Gender, Father, Mother, Spouse)" +
                        " values (?, ?, ?, ?, ?, ?, ?, ?)";

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, person.personID);
                stmt.setString(2, person.descendant);
                stmt.setString(3, person.firstName);
                stmt.setString(4, person.lastName);
                stmt.setString(5, person.gender);
                stmt.setString(6, person.father);
                stmt.setString(7, person.mother);
                stmt.setString(8, person.spouse);
                //System.out.println("inserted user");
                if (stmt.executeUpdate() != 1) {
                    throw new DatabaseException("Insert failed: could not insert person.");
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                closeConnection(true);
                System.out.println("Person.insert closed connection");

            }

        } catch (SQLException e) {
            throw new DatabaseException();

        }
        return true;
    }

    /**
     * Checks whether the given person data is in the Persons table. If the data exists, it removes it.
     *
     * @param person: the Server.Services.Person object containing the data to be deleted
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean remove(Person person) throws DatabaseException {
        assert (person != null);
        String sql = "delete from Persons where PersonID = '" + person.personID + "';";
        Person testPerson = getPerson(person.personID);
        if (testPerson.personID == null) throw new DatabaseException("Person doesn't exist");
        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.remove opened connection");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Issue removing person");
            throw new DatabaseException();
        }
        finally {
            closeConnection(true);
            System.out.println("Person.remove closed connection");
        }

        return true;
    }

    /**
     * removePeople(String user): removes all persons associated with a specific user.
     *
     * @param userName: the username for which all associated persons will be removed
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean removePeople(String userName) throws DatabaseException {
        String sql = "DELETE FROM Persons WHERE Descendant = '" + userName + "'";
        UserDAO userDAO = new UserDAO();
        User newUser = userDAO.getUser(userName);
        if (newUser.personID == null) throw new DatabaseException("User does not exist");
        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.removePeople opened connection");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Person.removePeople closed connection");
        }
        return true;
    }

    /**
     * Executes a DROP TABLE statement on the Persons table.
     *
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() {
        Statement stmt = null;
        String sql = "DROP TABLE IF EXISTS Persons";
        try {
            openConnection();
            System.out.println("Person.clear opened connection");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        catch (NullPointerException e) {

        }
        finally {
            closeConnection(true);
            System.out.println("Person.clear closed connection");

        }
        return true;
    }

    public User getUser(String personID) {
        User returnUser = new User();
        //get username
        //get corresponding user

        String sql = "SELECT * FROM Persons WHERE PersonID = '" + personID + "'";
        ArrayList<String> userList = new ArrayList<>();

        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.getUser opened connection");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                userList.add(rs.getString("Descendant"));
            }
            closeConnection(true);
            System.out.println("Person.getUser closed connection");

            if (userList.size() == 0) {
                return returnUser;
            }
            UserDAO userDAO = new UserDAO();
            returnUser = userDAO.getUser(userList.get(0));
        }
        catch (SQLException e) {

        }

        return returnUser;
    }
    public boolean tableExists() {
        boolean tExists = false;
        String tableName = "Persons";
        try {
            openConnection();
            System.out.println("Person.tableExists opened connection");

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
            System.out.println("Person.tableExists closed connection");
        }
        return tExists;
    }

    public boolean addParents(String momID, String dadID, String personID) throws DatabaseException {
        String sql = "update Persons set Mother = '" + momID + "', Father = '" + dadID +
                "' where PersonID = '" + personID + "';";
        if (momID == null || momID.equals("") || dadID == null || dadID.equals("")) {
            throw new DatabaseException("No parent ID's");
        }
        Person testMom = getPerson(momID);
        Person testDad = getPerson(dadID);
        Person testKid = getPerson(personID);

        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.addParents opened connection");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new DatabaseException();
        }
        finally {
            closeConnection(true);
            System.out.println("Person.addParents closed connection");
        }
        return true;
    }

    public boolean addSpouse(String spouseID, String personID) throws DatabaseException {
        String sql = "update Persons set Spouse = '" + spouseID + "' where PersonID = '" + personID + "';";
        if (spouseID == null || spouseID.equals("") || personID == null || personID.equals(""))
            throw new DatabaseException("Empty field");
        getPerson(spouseID);
        getPerson(personID);
        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.addSpouse opened connection");
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new DatabaseException();
        }
        finally {
            closeConnection(true);
            System.out.println("Person.addSpouse closed connection");
        }
        return true;
    }

    public boolean existsInPersons(String personID) {
        Person person = new Person();
        String sql = "SELECT * FROM Persons where PersonID = '" + personID + "'";

        try {
            if (!tableExists()) createTables();
            openConnection();
            System.out.println("Person.existsInPersons opened connection");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                person.firstName = rs.getString("FirstName");
                person.lastName = rs.getString("LastName");
                person.gender = rs.getString("Gender");
                person.personID = rs.getString("PersonID");
                person.descendant = rs.getString("Descendant");
                person.father = rs.getString("Father");
                person.mother = rs.getString("Mother");
                person.spouse = rs.getString("Spouse");
            }
            if(person.personID == null) return false;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            closeConnection(true);
            System.out.println("Person.existsInPersons closed connection");
        }
        return true;
    }

}