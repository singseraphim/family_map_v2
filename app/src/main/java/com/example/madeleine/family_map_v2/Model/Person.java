package com.example.madeleine.family_map_v2.Model;

import java.util.Objects;
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
}
