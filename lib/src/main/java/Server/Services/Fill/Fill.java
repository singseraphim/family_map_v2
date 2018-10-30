package Server.Services.Fill;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;
import Server.DAO.UserDAO;
import Server.Model.Event;
import Server.Model.Person;
import Server.Model.User;

/**
 * Service that handles all fill requests.
 */
public class Fill {

    /**
     * Generates family history data for the given user.
     * Checks for valid username and generation number, then uses files of Server.Services.Person and Server.Services.Event data to randomly
     * generate events and people to associate with the given user.
     * @param username: the username of the current user
     * @param generations: the number of generations to generate data for
     * @return a FillResponse object with a message indicating whether or not the fill was successful.
     */

    MaleNames maleNames;
    FemaleNames femaleNames;
    Surnames surnames;
    Locations locations;
    ArrayList<Person> currentGen = new ArrayList<>();
    ArrayList<Person> nextGen = new ArrayList<>();
    User currentUser;
    int numPeopleAdded = 0;
    int numEventsAdded = 0;

    int finalGenNum = 0;
    int currentGenNum = 0;
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    public FillResponse fill(String username, int generations) {
        FillResponse response = new FillResponse();
        UserDAO userDAO = new UserDAO();
        if (username == null || username == "") {
            response.message = "Empty username field";
            response.success = false;
            return response;
        }
        User testUser = userDAO.getUser(username);
        if (testUser.userName == null) {
            response.message = "User does not exist";
            response.success = false;
            return response;
        }
        if (generations < 0) {
            response.message = "Invalid number of generations";
            response.success = false;
            return response;
        }


        Gson gson = new Gson();
        String mNamesJson = readFile("lib/src/Resources/mnames.json");
        String fNamesJson = readFile("lib/src/Resources/fnames.json");
        String locationsJson = readFile("lib/src/Resources/locations.json");
        String sNamesJson = readFile("lib/src/Resources/snames.json");

        try {
            maleNames = gson.fromJson(mNamesJson, MaleNames.class);
            femaleNames = gson.fromJson(fNamesJson, FemaleNames.class);
            surnames = gson.fromJson(sNamesJson, Surnames.class);
            locations = gson.fromJson(locationsJson, Locations.class);

        }
        catch(Exception e) {
            System.out.println(e.toString());
        }

        Person currentPerson = new Person();
        try {
            currentUser = userDAO.getUser(username);
            currentPerson = personDAO.getPerson(currentUser.personID);
        }
        catch(Exception e) {

        }

        finalGenNum = generations;
        currentGenNum = 0;
        currentGen.add(currentPerson);
        addGeneration();
        response.message = "Successfully added " + numPeopleAdded + " persons" +
                " and " + numEventsAdded + " events to the database.\n";
        response.success = true;
        return response;
    }

    boolean addGeneration() {
        if (currentGenNum == finalGenNum) {
            return true;
        }
        for (Person child : currentGen) {
            Person mom = makeMom(child);
            Person dad = makeDad(child);
            marry(mom, dad, child);
            child.setParents(mom, dad);
            mom.setSpouse(dad);
            dad.setSpouse(mom);
            nextGen.add(mom);
            nextGen.add(dad);
        }
        currentGen = new ArrayList<Person>(nextGen);
        nextGen.clear();
        ++currentGenNum;
        addGeneration();
        return true;
    }

    Person makeMom(Person child) {
        Person mom = new Person();
        mom.firstName = getRandomFemaleName();
        mom.lastName = getRandomSurname();
        mom.descendant = currentUser.userName;
        mom.personID = UUID.randomUUID().toString();
        mom.gender = "f";
        addBirth(mom, child.getBirth());
        addDeath(mom, child.getBirth());
        try {
            personDAO.insert(mom);
            ++numPeopleAdded;
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        return mom;
    }

    Person makeDad(Person child) {
        Person dad = new Person();
        dad.firstName = getRandomMaleName();
        dad.descendant = currentUser.userName;
        dad.personID = UUID.randomUUID().toString();
        dad.gender = "m";
        addBirth(dad, child.getBirth());
        addDeath(dad, child.getBirth());
        dad.lastName = getRandomSurname();
        try {
            personDAO.insert(dad);
            ++numPeopleAdded;
        }
        catch (Exception e) {

        }
        return dad;
    }
    void addBirth(Person person, int childBirth) {
        Event birth = new Event();
        int birthYear = childBirth - ThreadLocalRandom.current().nextInt(18,  35);
        Locations.Location randomLocation = getRandomLocation();

        birth.person = person.personID;
        birth.year = birthYear;
        birth.eventType = "Birth";
        birth.eventID = UUID.randomUUID().toString();
        birth.descendant = currentUser.userName;
        birth.longitude = Double.parseDouble(randomLocation.longitude);
        birth.latitude = Double.parseDouble(randomLocation.latitude);
        birth.city = randomLocation.city;
        birth.country = randomLocation.country;

        try {
            eventDAO.insert(birth);
            ++numEventsAdded;
        }
        catch (Exception e) {
        }
        return;
    }

    void addDeath(Person person, int childBirth) {
        Event death = new Event();
        int deathYear = childBirth + ThreadLocalRandom.current().nextInt(1,  70);
        Locations.Location randomLocation = getRandomLocation();

        death.person = person.personID;
        death.year = deathYear;
        death.eventType = "Death";
        death.eventID = UUID.randomUUID().toString();
        death.descendant = currentUser.userName;
        death.longitude = Double.parseDouble(randomLocation.longitude);
        death.latitude = Double.parseDouble(randomLocation.latitude);
        death.city = randomLocation.city;
        death.country = randomLocation.country;

        try {
            eventDAO.insert(death);
            ++numEventsAdded;
        }
        catch (Exception e) {
        }
        return;
    }
    void marry(Person lovelyBride, Person dashingGroom, Person child) {
        Event brideMarriage = new Event();
        Event groomMarriage = new Event();
        int marriageYear = child.getBirth() - ThreadLocalRandom.current().nextInt(1,  10);
        Locations.Location randomLocation = getRandomLocation();

        brideMarriage.year = marriageYear;
        brideMarriage.person = lovelyBride.personID;
        brideMarriage.eventType = "Marriage";
        brideMarriage.eventID = UUID.randomUUID().toString();
        brideMarriage.descendant = currentUser.userName;
        brideMarriage.longitude = Double.parseDouble(randomLocation.longitude);
        brideMarriage.latitude = Double.parseDouble(randomLocation.latitude);
        brideMarriage.city = randomLocation.city;
        brideMarriage.country = randomLocation.country;

        groomMarriage.year = marriageYear;
        groomMarriage.person = dashingGroom.personID;
        groomMarriage.eventType = "Marriage";
        groomMarriage.eventID = UUID.randomUUID().toString();
        groomMarriage.descendant = currentUser.userName;
        groomMarriage.longitude = brideMarriage.longitude;
        groomMarriage.latitude = brideMarriage.latitude;
        groomMarriage.city = brideMarriage.city;
        groomMarriage.country = brideMarriage.country;

        try {
            eventDAO.insert(brideMarriage);
            eventDAO.insert(groomMarriage);
            numEventsAdded += 2;

        }
        catch (Exception e) {

        }

        return;
    }

    String getRandomFemaleName() {
        int rnd = new Random().nextInt(femaleNames.data.length);
        return femaleNames.data[rnd];
    }

    String getRandomMaleName() {
        int rnd = new Random().nextInt(maleNames.data.length);
        return maleNames.data[rnd];

    }

    String getRandomSurname() {
        int rnd = new Random().nextInt(surnames.data.length);
        return surnames.data[rnd];
    }

    Locations.Location getRandomLocation() {
        int rnd = new Random().nextInt(locations.data.length);
        return locations.data[rnd];
    }






    public static String readFile(String fileName) {
        String result = "";
          try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
/*
handle(HttpExchange exchange)
 */