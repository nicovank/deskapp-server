package edu.oswego.reslife.deskapp.api.models;

public class Access {

    public enum Type {
        KEY, FOB
    }

    private String id;
    private String building;
    private Type type;

    public String getID() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public Type getType() {
        return type;
    }
}