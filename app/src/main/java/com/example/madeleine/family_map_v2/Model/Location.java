package com.example.madeleine.family_map_v2.Model;

public class Location implements Comparable<Location> {
    public double latitude = 0;
    public double longitude = 0;

    public Location(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }
    @Override
    public int compareTo(Location o) {
        if (latitude == o.latitude && longitude == o.longitude) return 0;
        else return 1;
    }
}
