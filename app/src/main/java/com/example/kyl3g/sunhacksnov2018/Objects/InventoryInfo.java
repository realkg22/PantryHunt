package com.example.kyl3g.sunhacksnov2018.Objects;

public class InventoryInfo {
    private String name;
    private double price;

    public InventoryInfo()
    {}

    public InventoryInfo(String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
