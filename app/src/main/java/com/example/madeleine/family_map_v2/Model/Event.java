package com.example.madeleine.family_map_v2.Model;

import java.util.Objects;

/**
 * A model class that can hold a tuple in the Events table.
 */
public class Event implements Comparable<Event> {
    public String eventID;
    public String descendant;
    public String person;
    public double latitude;
    public double longitude;
    public String country;
    public String city;
    public String eventType;
    public int year;


    @Override
    public String toString() {
        return "Server.Services.Event{" +
                "eventID='" + eventID + '\'' +
                ", descendant='" + descendant + '\'' +
                ", person='" + person + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", eventType='" + eventType + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.latitude, latitude) == 0 &&
                Double.compare(event.longitude, longitude) == 0 &&
                year == event.year &&
                Objects.equals(eventID, event.eventID) &&
                Objects.equals(descendant, event.descendant) &&
                Objects.equals(person, event.person) &&
                Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) &&
                Objects.equals(eventType, event.eventType);
    }

    @Override
    public int compareTo(Event event){ //DUBIOUS
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal

        if (event.year < year) return -1;
        else if (event.year == year) return 0;
        else return 1;

    }


}

