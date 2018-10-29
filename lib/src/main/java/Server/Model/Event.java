package Server.Model;

import java.util.Objects;

/**
 * A model class that can hold a tuple in the Events table.
 */
public class Event {
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


}
