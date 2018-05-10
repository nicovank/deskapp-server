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

    public void setID(String id) {
        this.id = id;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type.toUpperCase());
    }
}