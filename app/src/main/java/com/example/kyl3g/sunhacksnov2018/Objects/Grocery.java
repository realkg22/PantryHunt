package com.example.kyl3g.sunhacksnov2018.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Grocery implements Serializable {
    private String name;
    private double latCoordinate;
    private double longCoordinate;
    private ArrayList<InventoryInfo> inventory;
    private double distance;

    public Grocery()
    {

    }

    public Grocery(String name, double latCoordinate, double longCoordinate, ArrayList<InventoryInfo> inventory)
    {
        this.name = name;
        this.latCoordinate = latCoordinate;
        this.longCoordinate = longCoordinate;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatCoordinate() {
        return latCoordinate;
    }

    public ArrayList<InventoryInfo> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<InventoryInfo> inventory) {
        this.inventory = inventory;
    }

    public double getLongCoordinate() {
        return longCoordinate;
    }

    public void setLongCoordinate(double longCoordinate) {
        this.longCoordinate = longCoordinate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
