package org.example;

import java.util.Objects;

public class AddressRecord {

    private final String city;
    private final String street;
    private final String house;
    private final int floor;

    public AddressRecord(String city, String street, String house, int floor) {
        this.city = Objects.requireNonNull(city).trim();
        this.street = Objects.requireNonNull(street).trim();
        this.house = Objects.requireNonNull(house).trim();
        this.floor = floor;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof AddressRecord other)) {
            return false;
        }

        return floor == other.floor
                && city.equals(other.city)
                && street.equals(other.street)
                && house.equals(other.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, house, floor);
    }

    @Override
    public String toString() {
        return city + ", " + street
                + ", дом " + house
                + ", этажей " + floor;
    }
}
