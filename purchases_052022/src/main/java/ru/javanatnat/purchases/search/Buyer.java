package ru.javanatnat.purchases.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Buyer {
    @JsonIgnore
    private final Long id;
    private final String firstName;
    private final String lastName;

    public Buyer(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public String getUnionName() {
        return getLastName() + " " + getFirstName();
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
