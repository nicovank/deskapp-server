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

    public void setID(String id) {
        this.id = id;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}