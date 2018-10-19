package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Model.Person;

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
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Testing` (\n" +
                        "\t`PersonID`\tTEXT NOT NULL UNIQUE,\n" +
                        "\t`Descendant`\tTEXT NOT NULL,\n" +
                        "\t`FirstName`\tTEXT NOT NULL,\n" +
                        "\t`LastName`\tTEXT NOT NULL,\n" +
                        "\t`Gender`\tTEXT NOT NULL CHECK(Gender = \"f\" OR Gender = \"m\"),\n" +
                        "\t`Father`\tTEXT,\n" +
                        "\t`Mother`\tTEXT,\n" +
                        "\t`Spouse`\tTEXT,\n" +
                        "\tPRIMARY KEY(`PersonID`),\n" +
                        "\tFOREIGN KEY(`Descendant`) REFERENCES `Users`(`Username`)\n" +
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
        PersonDAO person = new PersonDAO();
        try {
            person.openConnection();
            // System.out.println("connection open");
        } catch (DatabaseException e) {
            System.out.println("open didn't work my guy");
        }
        try {
            person.createTables();
            // System.out.println("tables created");
        } catch (DatabaseException e) {
            System.out.println("create didn't work my guy");
        }
/*

        try {
            person.eraseData();
        }
        catch (DatabaseException e) {
            System.out.println("Issue erasing data");
        }
*/

        //MAKE SOME FAKE DATA

        Person p1 = new Person();
        p1.personID = "p1id";
        p1.gender = "f";
        p1.lastName = "p1last";
        p1.firstName = "p1first";
        p1.descendant = "p1desc";
        p1.father = "p1father";
        p1.mother = "p1mother";
        p1.spouse = "p1spouse";

        Person p2 = new Person();
        p2.personID = "p2id";
        p2.gender = "f";
        p2.lastName = "p2last";
        p2.firstName = "p2first";
        p2.descendant = "p1desc";
        p2.father = "p2father";
        p2.mother = "p2mother";
        p2.spouse = "p2spouse";

        Person p3 = new Person();
        p3.personID = "p3id";
        p3.gender = "f";
        p3.lastName = "p3last";
        p3.firstName = "p3changedfirst";
        p3.descendant = "p4desc";
        p3.father = "p3father";
        p3.mother = "p3mother";
        p3.spouse = "p3spouse";

/*        try {
            person.clear();

        } catch(DatabaseException e) {
            System.out.println("clear didn't work");
        }*/


        try {
            person.insert(p1); //if it finds something already in there then it stops.
            person.insert(p2);
            person.insert(p3);
            // System.out.println("tables created");
        } catch (DatabaseException e) {
            //System.out.println("insert didn't work my guy");
        }

   /*     try {
            person.clear();
        } catch (DatabaseException e) {
            System.out.println("clear didn't work");
        }*/

/*        try {
            ArrayList<Person> related = person.getPeople("p1desc");
            for (Person ancestor : related) {
                System.out.println(ancestor.firstName);
            }

        } catch (DatabaseException e) {
            System.out.println("get people didn't work my guy");
        }*/

  /*      try {
            Person resPerson = person.getPerson("p2id");
            System.out.println("return father: " + resPerson.father);

        }
        catch (DatabaseException e) {
            System.out.println("issues with getperson");
        }*/
        /*try {
            person.remove(p2);

        } catch (DatabaseException e) {
            System.out.println("remove didn't work");
        }
*/
        try {
            person.removePeople("p1desc");

        } catch (DatabaseException e) {
            System.out.println("remove didn't work");
        }




        /*
        TO TEST:
        -Insert
        -Get people
        -Get person
        -Remove
        -Remove people
        -Clear
         */


        try {
            person.closeConnection(true);
        } catch (DatabaseException e) {
            System.out.println("close didn't work my guy");
        }


    }


    /**
     * Returns a list of all persons associated with a given user.
     *
     * @param userName: the username that the persons will be associated with.
     * @return a list of Person objects associated with the given username.
     */
    public ArrayList<Person> getPeople(String userName) throws DatabaseException {
        ArrayList<Person> personList = new ArrayList<Person>();
        assert (userName != null);
        String sql = "SELECT * FROM Testing WHERE Descendant = '" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Person newPerson = new Person();
                newPerson.descendant = rs.getString("Descendant");
                newPerson.firstName = rs.getString("FirstName");
                newPerson.lastName = rs.getString("LastName");
                newPerson.personID = rs.getString("PersonID");
                newPerson.father = rs.getString("Father");
                newPerson.mother = rs.getString("Mother");
                newPerson.gender = rs.getString("Gender");
                newPerson.spouse = rs.getString("Spouse");

                personList.add(newPerson);
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }


        return personList;
    }

    /**
     * Checks that the personID exists, and if it does it returns a person object associated with the given person ID. If the personID doesn't exist, the function returns null.
     *
     * @param personID the ID of the person to find and return
     * @return a Person object with all the data associated with the given person ID.
     */
    public Person getPerson(String personID) throws DatabaseException {
        Person person = new Person();
        assert (personID != null);
        String sql = "SELECT * FROM Testing where PersonID = '" + personID + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            person.firstName = rs.getString("FirstName");
            person.lastName = rs.getString("LastName");
            person.gender = rs.getString("Gender");
            person.personID = rs.getString("PersonID");
            person.descendant = rs.getString("Descendant");
            person.father = rs.getString("Father");
            person.mother = rs.getString("Mother");
            person.spouse = rs.getString("Spouse");

        } catch (SQLException e) {
            System.out.println("getUser is having issues.");
            throw new DatabaseException();
        }
        return person;
    }

    /**
     * Inserts the given person object into the Persons table.
     *
     * @param person: the Person object containing the data to be inserted
     * @return a bool that indicates whether the insert operation was successful
     */
    public boolean insert(Person person) throws DatabaseException {

        assert (person.descendant != null);
        assert (person.firstName != null);
        assert (person.lastName != null);
        assert (person.gender != null);
        assert (person.gender == "f" || person.gender == "m");
        assert (person.personID != null);
        try {
            PreparedStatement stmt = null;
            try {
                String sql = "INSERT INTO Testing " +
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
            }

        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return true;
    }

    /**
     * Checks whether the given person data is in the Persons table. If the data exists, it removes it.
     *
     * @param person: the Person object containing the data to be deleted
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean remove(Person person) throws DatabaseException {
        assert (person != null);
        String sql = "delete from Testing where PersonID = '" + person.personID + "';";

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
     * removePeople(String user): removes all persons associated with a specific user.
     *
     * @param userName: the username for which all associated persons will be removed
     * @return a bool that indicates whether the remove operation was successful
     */
    public boolean removePeople(String userName) throws DatabaseException {
        assert (userName != null);
        String sql = "DELETE FROM Testing WHERE Descendant = '" + userName + "'";
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
     * Executes a DROP TABLE statement on the Persons table.
     *
     * @return a bool that indicates whether the clear operation was successful
     */
    public boolean clear() throws DatabaseException {
        Statement stmt = null;
        String sql = "DROP TABLE Testing";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: could not drop table");
            throw new DatabaseException();
        }

        return true;
    }

    public boolean eraseData() throws DatabaseException {
        try {
            Statement stmt = null;
            String sql = "DELETE FROM Testing;";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseException();
        }
        return true;
    }


}

/*
CAPTAIN'S LOG:
Insert throws an exception even though it's doing everything right. I think it's happening at close();
I think I need to talk to the TA's about how to deal with foreign keys.
I also think if you try to insert into a table and the thing is already there it yells at you.
 */