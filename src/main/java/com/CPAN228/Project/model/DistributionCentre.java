package com.CPAN228.Project.model;

import lombok.Data;



@Data
public class DistributionCentre {
    private Long id;
    private String name;
    private String items = "3";
    private double latitude;
    private double longitude;

    public DistributionCentre(Long id, String name, String items, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }   

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
