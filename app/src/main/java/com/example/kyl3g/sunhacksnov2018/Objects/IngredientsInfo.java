package com.example.kyl3g.sunhacksnov2018.Objects;

public class IngredientsInfo {
    private String name;
    private int amount;

    public IngredientsInfo()
    {

    }

    public IngredientsInfo(String name, int amount)
    {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount){
        this.amount += amount;
    }
}
