package Server.Model;

import java.util.Objects;

import Server.DAO.EventDAO;
import Server.DAO.PersonDAO;

/**
 * A model class that can hold a tuple in the Persons table.
 */
public class Person {
    public String personID;
    public String descendant;
    public String firstName;
    public String lastName;
    public String gender;
    public String father;
    public String mother;
    public String spouse;

    @Override
    public String toString() {
        return "Server.Services.Person{" +
                "personID='" + personID + '\'' +
                ", descendant='" + descendant + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", spouse='" + spouse + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) &&
                Objects.equals(descendant, person.descendant) &&
                Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(father, person.father) &&
                Objects.equals(mother, person.mother) &&
                Objects.equals(spouse, person.spouse);
    }

    public int getBirth() {

        EventDAO eventDAO = new EventDAO();

        return eventDAO.getBirth(personID);
    }

    public int getDeath() {

        EventDAO eventDAO = new EventDAO();

        return eventDAO.getDeath(personID);
    }

    public int getMarriage() {

        EventDAO eventDAO = new EventDAO();

        return eventDAO.getMarriage(personID);
    }

    public void setParents(Person mom, Person dad) {

        PersonDAO personDAO = new PersonDAO();

        try {
            personDAO.addParents(mom.personID, dad.personID, personID);
        }
        catch (Exception e) {

        }
    }

    public void setSpouse(Person spouse) {

        PersonDAO personDAO = new PersonDAO();

        try {
            personDAO.addSpouse(spouse.personID, personID);
        }
        catch (Exception e) {

        }
    }





}
