package edu.oswego.reslife.deskapp.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resident {
    private String id;
    private String firstName;
    private String lastName;
    private String building;
    private String roomNb;
    private String email;

    @JsonProperty("id")
    public String getID() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoomNb() {
        return roomNb;
    }

    public String getEmail() {
        return email;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setRoomNb(String roomNb) {
        this.roomNb = roomNb;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
