package com.example.kyl3g.sunhacksnov2018.Objects;

public class ItemTag {
    private String store;
    private double price;
    private int amount;

    public ItemTag(){

    }

    public ItemTag(String store, double price, int amount){
        this.store = store;
        this.price = price;
        this.amount = amount;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
