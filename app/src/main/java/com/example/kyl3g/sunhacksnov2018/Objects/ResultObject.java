package com.example.kyl3g.sunhacksnov2018.Objects;

public class ResultObject {

    private String name;
    private double price;
    private String grocery;
    private int amount;
    public ResultObject()
    {}

    public ResultObject(String name, double price, String grocery, int amount)
    {
        this.name = name;
        this.price = price;
        this.grocery = grocery;
        this.amount = amount;

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

    public String getGrocery() {
        return grocery;
    }

    public void setGrocery(String grocery) {
        this.grocery = grocery;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
