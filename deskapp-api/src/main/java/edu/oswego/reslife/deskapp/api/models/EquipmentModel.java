package edu.oswego.reslife.deskapp.api.models;

public class EquipmentModel {
    private String id;
    private String building;
    private String name;
    private String category;

    public String getID() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}