package com.example.madeleine.family_map_v2.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class CurrentSessionTest {
    CurrentSession session = CurrentSession.getInstance();
    ArrayList<Person> personList = new ArrayList<>();
    ArrayList<Event> eventList = new ArrayList<>();
    Person user = new Person();
    Person mother = new Person();
    Person father = new Person();
    Person mGrandma = new Person();
    Person mGrandpa = new Person();
    Person fGrandma = new Person();
    Person fGrandpa = new Person();
    Event kidBirth = new Event();
    Event momBirth = new Event();
    Event momDeath = new Event();
    Event dadBirth = new Event();
    Event dadDeath = new Event();


    @Before
    public void setUp() throws Exception {

        user.personID = "88db1671-d82b-42a0-b64f-e5107f80d4a1";
        user.descendant = "username";
        user.firstName = "Kid";
        user.lastName = "McChild";
        user.gender = "f";
        user.father = "5c7c47f7-920f-41ac-8215-0aa994ae6799";
        user.mother = "685e99e1-5531-4f07-b109-23cb9e7366f0";

        mother.personID = "685e99e1-5531-4f07-b109-23cb9e7366f0";
        mother.descendant = "username";
        mother.firstName = "Lucy";
        mother.lastName = "Critchfield";
        mother.gender = "f";
        mother.father = "740cfbb5-4ece-4e7b-b6c3-3b94931f9db9";
        mother.mother = "4c5d4382-f5a8-4048-ace1-ff55ba1419b1";
        mother.spouse = "5c7c47f7-920f-41ac-8215-0aa994ae6799";

        father.personID = "5c7c47f7-920f-41ac-8215-0aa994ae6799";
        father.descendant = "username";
        father.firstName = "Gino";
        father.lastName = "Line";
        father.gender = "m";
        father.father = "4dd9d89b-3709-4c64-ac3b-7794df855ee0";
        father.mother = "60eb92a1-388f-4b7a-b411-b0cbcd1f978b";
        father.spouse = "685e99e1-5531-4f07-b109-23cb9e7366f0";

        mGrandma.personID = "4c5d4382-f5a8-4048-ace1-ff55ba1419b1";
        mGrandma.descendant = "username";
        mGrandma.firstName = "Elnora";
        mGrandma.lastName = "Seeger";
        mGrandma.gender = "f";
        mGrandma.spouse = "740cfbb5-4ece-4e7b-b6c3-3b94931f9db9";

        mGrandpa.personID = "740cfbb5-4ece-4e7b-b6c3-3b94931f9db9";
        mGrandpa.descendant = "username";
        mGrandpa.firstName = "Hayden";
        mGrandpa.lastName = "Crain";
        mGrandpa.gender = "m";
        mGrandpa.spouse = "4c5d4382-f5a8-4048-ace1-ff55ba1419b1";

        fGrandma.personID = "60eb92a1-388f-4b7a-b411-b0cbcd1f978b";
        fGrandma.descendant = "username";
        fGrandma.firstName = "Yuonne";
        fGrandma.lastName = "Meiers";
        fGrandma.gender = "f";
        fGrandma.spouse = "4dd9d89b-3709-4c64-ac3b-7794df855ee0";

        fGrandpa.personID = "4dd9d89b-3709-4c64-ac3b-7794df855ee0";
        fGrandpa.descendant = "username";
        fGrandpa.firstName = "Larry";
        fGrandpa.lastName = "Gies";
        fGrandpa.gender = "m";
        fGrandpa.spouse = "60eb92a1-388f-4b7a-b411-b0cbcd1f978b";

        personList.add(user);
        personList.add(mother);
        personList.add(father);
        personList.add(mGrandma);
        personList.add(mGrandpa);
        personList.add(fGrandma);
        personList.add(fGrandpa);

        session.personList = personList;

        kidBirth.eventType = "Birth";
        kidBirth.eventID = "def518c2-019d-4c22-8a71-cbbdf1aa06c3";
        kidBirth.person = "88db1671-d82b-42a0-b64f-e5107f80d4a1";
        kidBirth.country = "Guinea";
        kidBirth.city = "Nzérékoré";
        kidBirth.year = 1992;

        momBirth.eventType = "Birth";
        momBirth.eventID = "737bb64c-32fa-4dd4-abc3-5ce377045c2b";
        momBirth.person = "685e99e1-5531-4f07-b109-23cb9e7366f0";
        momBirth.country = "Malaysia";
        momBirth.city = "Malacca Town";
        momBirth.year = 1967;

        momDeath.eventType = "Death";
        momDeath.eventID = "96332e37-a5cb-4f78-9f9f-46595f15ddad";
        momDeath.person = "685e99e1-5531-4f07-b109-23cb9e7366f0";
        momDeath.country = "United States";
        momDeath.city = "Baltimore";
        momDeath.year = 2032;

        dadBirth.eventType = "Birth";
        dadBirth.eventID = "ce7966b6-2dde-4adc-b162-09a9c2706c5a";
        dadBirth.person = "5c7c47f7-920f-41ac-8215-0aa994ae6799";
        dadBirth.country = "Colombia";
        dadBirth.city = "Cartagena";
        dadBirth.year = 1968;

        dadDeath.eventType = "Death";
        dadDeath.eventID = "ba61c244-7102-4389-ba17-db06189d18c3";
        dadDeath.person = "5c7c47f7-920f-41ac-8215-0aa994ae6799";
        dadDeath.country = "Brazil";
        dadDeath.city = "Sobral";
        dadDeath.year = 2000;


        eventList.add(kidBirth);
        eventList.add(momBirth);
        eventList.add(momDeath);
        eventList.add(dadBirth);
        eventList.add(dadDeath);

        session.eventList = eventList;
        session.filteredPersonList = personList;
        session.filteredEventList = eventList;
        session.currentUser = user;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void searchPersons() {
        String searchTerm = "ie";
        ArrayList<Person> searchResults = session.searchPersons(searchTerm);

        assertTrue(searchResults.contains(mother));
        assertTrue(searchResults.contains(fGrandpa));
        assertTrue(searchResults.contains(fGrandma));

    }

    @Test
    public void searchEvents() {
        String searchTerm = "20";
        ArrayList<Event> searchResults = session.searchEvents(searchTerm);
        assertTrue(searchResults.contains(dadDeath));
        assertTrue(searchResults.contains(momDeath));
    }

    @Test
    public void filterEventsByType() {
        session.includeBirthEvents = false;
        session.filter();
        assertTrue(session.filteredEventList.size() == 2);
        assertTrue(session.filteredEventList.get(0).eventType == "Death");
        assertTrue(session.filteredEventList.get(1).eventType == "Death");


    }
    @Test
    public void filterByGender() {
        session.includeMale = false;
        session.filter();
        assertFalse(session.filteredPersonList.contains(father));
        assertFalse(session.filteredPersonList.contains(mGrandpa));
        assertFalse(session.filteredPersonList.contains(fGrandpa));
        assertFalse(session.filteredEventList.contains(dadBirth));
        assertFalse(session.filteredEventList.contains(dadDeath));

    }
    @Test
    public void filterBySide() {
        session.includeMothersSide = false;
        session.filter();
        assertFalse(session.filteredPersonList.contains(mother));
        assertFalse(session.filteredPersonList.contains(mGrandma));
        assertFalse(session.filteredPersonList.contains(mGrandpa));
        assertFalse(session.filteredEventList.contains(momDeath));
        assertFalse(session.filteredEventList.contains(momBirth));

    }


    @Test
    public void getEventsForPerson() {
        ArrayList<Event> results = session.getEventsForPerson("685e99e1-5531-4f07-b109-23cb9e7366f0");
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).eventType == "Birth");
        assertTrue(results.get(1).eventType == "Death");
    }

    @Test
    public void getFamily() {
        TreeMap<String, Person> results = session.getFamily(mother);
        assertTrue(results.get("child") == user);
        assertTrue(results.get("spouse") == father);
        assertTrue(results.get("mother") == mGrandma);
        assertTrue(results.get("father") == mGrandpa);
    }
}