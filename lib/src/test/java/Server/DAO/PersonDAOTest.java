package Server.DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import javax.security.auth.DestroyFailedException;
import javax.xml.crypto.Data;

import Exceptions.DatabaseException;
import Server.Model.Person;
import Server.Model.User;

import static org.junit.Assert.*;

public class PersonDAOTest {
    private PersonDAO personDAO = new PersonDAO();
    @Before
    public void setUp() throws Exception {
/*        UserDAO userDAO = new UserDAO();
        User testUser = new User();
        testUser.userName = "userName";
        testUser.personID = "userID";
        testUser.gender = "f";
        testUser.email = "g";
        testUser.firstName = "h";
        testUser.lastName = "i";
        testUser.password = "j";

        userDAO.insert(testUser);*/
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTables() {
        personDAO.createTables();
        assertTrue(personDAO.tableExists());
    }

    @Test
    public void getPeople() throws DatabaseException {
        //create two people with same descendant
        //put them in table
        //call getPeople with the descendant name
        //compare results
        //remove people

        Person person = new Person();
        Person person2 = new Person();

        person.gender = "f";
        person.descendant = "TestDescendant";
        person.lastName = "b";
        person.firstName = "c";
        person.father = "d";
        person.mother = "e";
        person.spouse = "f";
        person.personID = "g";

        person2.gender = "f";
        person2.descendant = "TestDescendant";
        person2.lastName = "v";
        person2.firstName = "w";
        person2.father = "x";
        person2.mother = "y";
        person2.spouse = "z";
        person2.personID = "s";

        try {
        personDAO.insert(person);
        personDAO.insert(person2);

        }
        catch(DatabaseException e) {
        }
        ArrayList<Person> personList = personDAO.getPeople("TestDescendant");
        assertTrue(personList.size() == 2);
        assertTrue(personList.contains(person));
        assertTrue(personList.contains(person2));

    }

    @Test
    public void getPeopleInvalid() throws DatabaseException {
        ArrayList<Person> personList = personDAO.getPeople("BadID");
        assertTrue(personList.size() == 0);
    }

    @Test
    public void getPerson() {
        //create person object
        //put it in the table
        //call getpeople with the given id
        //compare the objects
        //remove object from table
        Person person = new Person();
        person.gender = "f";
        person.descendant = "d";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID";
        try {
            personDAO.insert(person);
            Person result = personDAO.getPerson("TestID");
            assertEquals(result, person);
            personDAO.remove(person);
        }
        catch(DatabaseException e) {

        }

    }

    @Test
    public void getPersonInvalid() throws DatabaseException {
        Person returnPerson = personDAO.getPerson("BadID");
        assertTrue(returnPerson.personID == null);
    }
    @Test
    public void insert() throws DatabaseException {
        //create person object
        //put it in table
        //call getperson with given id
        //compare objects
        //remove objects from table
        Person person = new Person();
        person.gender = "f";
        person.descendant = "userName";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID";
        personDAO.insert(person);
        Person result = personDAO.getPerson("TestID");
        assertEquals(result, person);

        personDAO.remove(person);


    }

    @Test (expected = DatabaseException.class)
    public void insertNoID() throws DatabaseException {
        Person person = new Person();
        person.descendant = "userName";
        person.firstName = "testFirst";
        person.lastName = "testLast";
        person.gender = "f";

        personDAO.insert(person);

    }
    @Test (expected = DatabaseException.class)
    public void insertNoDesc() throws DatabaseException {
        Person person = new Person();
        person.personID = "testID";
        person.firstName = "testFirst";
        person.lastName = "testLast";
        person.gender = "f";

        personDAO.insert(person);
    }
    @Test (expected = DatabaseException.class)
    public void insertNoName() throws DatabaseException {
        Person person = new Person();
        person.personID = "testID";
        person.descendant = "userName";
        person.gender = "f";

        personDAO.insert(person);
    }
    @Test (expected = DatabaseException.class)
    public void insertInvalidGender() throws DatabaseException {
        Person person = new Person();
        person.personID = "testID";
        person.descendant = "userName";
        person.firstName = "testFirst";
        person.lastName = "testLast";
        person.gender = "z";

        personDAO.insert(person);
    }

    @Test (expected = DatabaseException.class)
    public void insertExtantPerson() throws DatabaseException {
        Person person = new Person();
        person.personID = "testID";
        person.descendant = "userName";
        person.firstName = "testFirst";
        person.lastName = "testLast";
        person.gender = "f";

        personDAO.insert(person);
        personDAO.insert(person);
    }
    @Test (expected = DatabaseException.class)
    public void insertNonexistentDesc() throws DatabaseException {
        Person person = new Person();
        person.personID = "testID";
        person.descendant = "BadDescendant";
        person.firstName = "testFirst";
        person.lastName = "testLast";
        person.gender = "f";

        personDAO.insert(person);
    }
    @Test
    public void remove() {
        //create person object
        //put it in the table
        //remove it from the table
        //check to make sure it's not in the table
        Person person = new Person();
        person.gender = "f";
        person.descendant = "d";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID";
        try {
            personDAO.insert(person);
            personDAO.remove(person);
            Person result = personDAO.getPerson("TestID");
            assertNull(result.personID);
            assertNull(result.descendant);
        }
        catch(DatabaseException e) {

        }
    }

    @Test (expected = DatabaseException.class)
    public void removeNonexistent() throws DatabaseException {
        Person person = new Person();
        person.gender = "f";
        person.descendant = "d";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID";

        personDAO.remove(person);
    }
    @Test
    public void removePeople() {
        //create two person objects with same descendant
        //put them in the table
        //call removePeople with the descendant
        //make sure they're both gone
        Person person = new Person();
        Person person2 = new Person();

        person.gender = "f";
        person.descendant = "RemoveDescendant";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "i";

        person2.gender = "t";
        person2.descendant = "RemoveDescendant";
        person2.lastName = "v";
        person2.firstName = "w";
        person2.father = "x";
        person2.mother = "y";
        person2.spouse = "z";
        person2.personID = "s";
        try {
            personDAO.insert(person);
            personDAO.insert(person2);
            personDAO.removePeople("RemoveDescendant");
            ArrayList<Person> eventList = personDAO.getPeople("RemoveDescendant");
            assertTrue(eventList.size() == 0);
        }
        catch (DatabaseException e) {

        }


    }

    @Test (expected = DatabaseException.class)
    public void removePeopleNonexistent() throws DatabaseException {
        personDAO.removePeople("BadUsername");
        Person person = new Person();
        person.gender = "f";
        person.descendant = "d";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "TestID";

    }

    @Test
    public void clear() {
        personDAO.clear();
        assertFalse(personDAO.tableExists());
    }

    @Test
    public void getUser() {
        //make new person object
        //make user object that is descendant
        //insert them both into respective tables
        //call getUser
        //compare two objects
        Person person = new Person();

        person.gender = "f";
        person.descendant = "GetUserUsername";
        person.lastName = "a";
        person.firstName = "b";
        person.father = "e";
        person.mother = "g";
        person.spouse = "h";
        person.personID = "GetUserID";

        User user = new User();
        user.userName = "GetUserUsername";
        user.password = "a";
        user.lastName = "b";
        user.firstName = "c";
        user.email = "e";
        user.gender = "f";
        user.personID = "g";

        UserDAO userDAO = new UserDAO();

        try {
            personDAO.insert(person);
            userDAO.insert(user);
            User result = personDAO.getUser("GetUserID");
            assertEquals(user, result);
            personDAO.remove(person);
        }
        catch(DatabaseException e) {

        }
    }

    @Test
    public void addParents() throws DatabaseException {
        Person kid = new Person();
        kid.gender = "f";
        kid.descendant = "userName";
        kid.lastName = "a";
        kid.firstName = "b";
        kid.father = "e";
        kid.mother = "g";
        kid.spouse = "h";
        kid.personID = "kid";

        Person mom = new Person();
        mom.gender = "f";
        mom.descendant = "userName";
        mom.lastName = "a";
        mom.firstName = "b";
        mom.father = "e";
        mom.mother = "g";
        mom.spouse = "h";
        mom.personID = "mom";

        Person dad = new Person();
        dad.gender = "m";
        dad.descendant = "userName";
        dad.lastName = "a";
        dad.firstName = "b";
        dad.father = "e";
        dad.mother = "g";
        dad.spouse = "h";
        dad.personID = "dad";

        try {
            personDAO.insert(kid);
            personDAO.insert(mom);
            personDAO.insert(dad);

        }
        catch(DatabaseException e) {

        }

        personDAO.addParents(mom.personID, dad.personID, kid.personID);
        Person testPerson = personDAO.getPerson(kid.personID);
        assertTrue(testPerson.father.equals(dad.personID));
        assertTrue(testPerson.mother.equals(mom.personID));

    }
    @Test (expected = DatabaseException.class)
    public void addParentsNoParents() throws DatabaseException {
        Person mom = new Person();
        Person dad = new Person();
        Person kid = new Person();
        kid.gender = "f";
        kid.descendant = "userName";
        kid.lastName = "a";
        kid.firstName = "b";
        kid.father = "e";
        kid.mother = "g";
        kid.spouse = "h";
        kid.personID = "kid";
        personDAO.addParents(mom.personID, dad.personID, kid.personID);

    }

    @Test
    public void addSpouse() throws DatabaseException {
        Person bride = new Person();
        bride.gender = "f";
        bride.descendant = "userName";
        bride.lastName = "a";
        bride.firstName = "b";
        bride.father = "e";
        bride.mother = "g";
        bride.spouse = "h";
        bride.personID = "bride";

        Person groom = new Person();
        groom.gender = "f";
        groom.descendant = "userName";
        groom.lastName = "a";
        groom.firstName = "b";
        groom.father = "e";
        groom.mother = "g";
        groom.spouse = "h";
        groom.personID = "groom";

        try {
            personDAO.insert(bride);
            personDAO.insert(groom);
        }
        catch(DatabaseException e) {

        }
        personDAO.addSpouse(bride.personID, groom.personID);
        personDAO.addSpouse(groom.personID, bride.personID);

        Person testPerson = personDAO.getPerson(bride.personID);
        assertTrue(testPerson.spouse.equals(groom.personID));

    }

    @Test (expected = DatabaseException.class)
    public void addSpouseNoSpouse() throws DatabaseException {
        Person bride = new Person();
        bride.gender = "f";
        bride.descendant = "userName";
        bride.lastName = "a";
        bride.firstName = "b";
        bride.father = "e";
        bride.mother = "g";
        bride.spouse = "h";
        bride.personID = "bride";

        Person groom = new Person();
        personDAO.addSpouse(groom.personID, bride.personID);

    }

}