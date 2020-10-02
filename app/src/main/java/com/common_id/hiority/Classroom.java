package com.common_id.hiority;

public class Classroom {

    private String id, name, floor, longitude, latitude;

    public Classroom(){
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFloor() {
        return floor;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
