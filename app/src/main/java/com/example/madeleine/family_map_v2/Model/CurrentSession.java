package com.example.madeleine.family_map_v2.Model;

import com.example.madeleine.family_map_v2.ServerProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class CurrentSession {

    public ArrayList<Person> personList = new ArrayList<>();
    public ArrayList<Event> eventList = new ArrayList<>();
    ServerProxy proxy = new ServerProxy();
    private static CurrentSession instance = null;
    Person currentUser = new Person();

    //login data
    public AuthToken authToken = new AuthToken();
    public boolean loggedIn = false;

    //filter data
    public LinkedHashMap<String, Boolean> filters = new LinkedHashMap<>();
    public ArrayList<Event> filteredEventList = new ArrayList<>();
    public ArrayList<Person> filteredPersonList = new ArrayList<>();
    private ArrayList<Person> familySide = new ArrayList<>();

    //settings data
    public boolean lifeStoryLines = true;
    public boolean familyTreeLines = true;
    public boolean spouseLines = true;
    public int lifeColorIndex = 0;
    public int familyColorIndex = 1;
    public int spouseColorIndex = 2;
    public int mapTypeIndex = 0;

    public static CurrentSession getInstance() {
        if (instance == null) instance = new CurrentSession();
        return instance;
    }


    public void setFilterOptions() {

        filters.put("Birth", true);
        filters.put("Marriage", true);
        filters.put("Death", true);
        filters.put("Mother's Side", true);
        filters.put("Father's Side", true);
        filters.put("Male Data", true);
        filters.put("Female Data", true);

    }

    private void jitterCoordinates() {
        for (Event a : eventList) {
            for (Event b : eventList) {
                if (a.latitude == b.latitude && a.longitude == b.longitude) {
                    b.latitude += .0001;
                }
            }
        }
    }

    public Event getEventByCoordinates(double latitude, double longitude) {
        for (Event event : eventList) {
            if (event.latitude == latitude && event.longitude == longitude) {
                return event;
            }
        }
        return null;
    }

    public ArrayList<Person> searchPersons(String input) {
        ArrayList<Person> returnList = new ArrayList<>();
        for (Person person : filteredPersonList) {
            String firstLastConcat = person.firstName + " " + person.lastName;
            if (firstLastConcat.toLowerCase().contains(input.toLowerCase())) {
                returnList.add(person);
            }
        }
        return returnList;
    }
    public ArrayList<Event> searchEvents(String input) {
        ArrayList<Event> returnList = new ArrayList<>();

        for (Event event : filteredEventList) {
            String eventDataConcat = event.eventType + " " + event.country + " " + event.city
                    + " " + event.year;
            if (eventDataConcat.toLowerCase().contains(input.toLowerCase())) {
                returnList.add(event);
            }
        }
        return returnList;
    }

    public void filter() {
        filteredEventList = eventList;
        filteredPersonList = personList;

        if (!filters.get("Father's Side")) {
            removeSide("father");
        }
        if (!filters.get("Mother's Side")) {
            removeSide("mother");
        }

        if (!filters.get("Female Data")) {
            for (int i = 0; i < filteredPersonList.size(); ++i) {
                Person person = filteredPersonList.get(i);
                if (person.gender.equals("f")) {
                    filteredPersonList.remove(person);
                    removePersonsEvents(person.personID);
                    i = -1;
                }
            }
        }
        if (!filters.get("Male Data"))  {
            for (int i = 0; i < filteredPersonList.size(); ++i) {
                Person person = filteredPersonList.get(i);
                if (person.gender.equals("m")) {
                    filteredPersonList.remove(person);
                    removePersonsEvents(person.personID);
                    i = -1;
                }
            }
        }
        if (!filters.get("Birth") || !filters.get("Marriage") || !filters.get("Death")) {
            for (int i = 0; i < filteredEventList.size(); i++) {
                Event event = filteredEventList.get(i);
                if (!filters.get("Birth")) {
                    if (event.eventType.equals("Birth")) {
                        filteredEventList.remove(event);
                        i = -1;
                    }
                }
                if (!filters.get("Marriage")) {
                    if (event.eventType.equals("Marriage")) {
                        filteredEventList.remove(event);
                        i = -1;
                    }
                }
                if (!filters.get("Death")) {
                    if (event.eventType.equals("Death")) {
                        filteredEventList.remove(event);
                        i = -1;
                    }
                }
            }
        }
    }

    private void removePersonsEvents(String personID) {
        for (int i = 0; i < filteredEventList.size(); ++i) {
            if (filteredEventList.get(i).person.equals(personID)) {
                filteredEventList.remove(filteredEventList.get(i));
                i = -1;
            }
        }
    }

    public Person getPersonByID(String id) {
        for (Person person : personList) {
            if (person.personID.equals(id)) {
                return person;
            }
        }
        return new Person();
    }

    public Event getEventByID(String id) {
        for (Event event : eventList) {
            if (event.eventID.equals(id)) {
                return event;
            }
        }
        return new Event();
    }

    private void removeSide(String side) {
        familySide.clear();
        if (side.equals("mother")) {
            Person mother = getPersonByID(currentUser.mother);
            getAncestors(mother);
        }
        else if (side.equals("father")) {
            Person father = getPersonByID(currentUser.father);
            getAncestors(father);
        }

        ArrayList<Event> eventsToRemove = new ArrayList<>();
        for (Person person : familySide) {
            eventsToRemove.addAll(getEventsForPerson(person.personID));
            filteredPersonList.remove(person);
        }
        filteredEventList.removeAll(eventsToRemove);
    }


    private void getAncestors(Person person) {
        familySide.add(person);
        if (person.mother != null) {
            getAncestors(getPersonByID(person.mother));
        }
        if (person.father != null) {
            getAncestors(getPersonByID(person.father));
        }
    }

    public ArrayList<Event> getEventsForPerson(String personID) {
        ArrayList<Event> eventList = new ArrayList<>();
        for (Event event : filteredEventList) {
            if (event.person.equals(personID)) {
                eventList.add(event);
            }
        }
        Collections.sort(eventList, Collections.<Event>reverseOrder());
        return eventList;
    }
    public TreeMap<String, Person> getFamily(Person currentPerson) {

        TreeMap<String, Person> family = new TreeMap<>();
        family.put("child", getChild(currentPerson.personID));
        family.put("spouse", getPersonByID(currentPerson.spouse));
        family.put("mother", getPersonByID(currentPerson.mother));
        family.put("father", getPersonByID(currentPerson.father));

        return family;
    }
    private Person getChild(String personID) {
        for (Person person : personList) {
            if (person.father != null && person.mother != null) {
                if (person.father.equals(personID) || person.mother.equals(personID)) {
                    return person;
                }
            }
        }
        return new Person();
    }
    public void setPortHost(String port, String host) {
        proxy.serverPort = port;
        proxy.serverHost = host;
    }
    public LoginResponse login(LoginRequest request) {

        LoginResponse response = proxy.login(request);

        if (response.success) {
            authToken.authToken = response.authToken;
            authToken.username = response.userName;
            if (!syncServerData()) {
                response.success = false;
                response.message = "Error syncing data from server. Please try again.";
            }
            else {
                filteredEventList = eventList;
                filteredPersonList = personList;
                currentUser = getPersonByID(response.personID);
            }
        }
        setFilterOptions();
        jitterCoordinates();
        return response;
    }
    public RegisterResponse register(RegisterRequest request) {

        RegisterResponse response = proxy.register(request);
        if (response.success) {
            authToken.authToken = response.authToken;
            authToken.username = response.userName;
            if (!syncServerData()) {
                response.success = false;
                response.message = "Error syncing data from server. Please try again.";
            }
            else {
                filteredEventList = eventList;
                filteredPersonList = personList;
                currentUser = getPersonByID(response.personID);
            }
        }
        setFilterOptions();
        jitterCoordinates();
        return response;
    }
    public void logout() {
        authToken = new AuthToken();
        currentUser = new Person();

        personList.clear();
        eventList.clear();
        filteredEventList.clear();
        filteredPersonList.clear();

        loggedIn = false;

        //will need to add more things here probably
        //how would I go about deleting a singleton?
    }
    public boolean syncServerData() {
        PeopleResponse peopleResponse = proxy.getPeople(authToken.authToken);
        EventsResponse eventsResponse = proxy.getEvents(authToken.authToken);

        if (peopleResponse.success) personList = peopleResponse.data;
        if (eventsResponse.success) eventList = eventsResponse.data;

        if (!peopleResponse.success || !eventsResponse.success) {
            return false;
        }
        return true;
    }

}





