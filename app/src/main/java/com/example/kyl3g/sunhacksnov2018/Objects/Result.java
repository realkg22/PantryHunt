package com.example.kyl3g.sunhacksnov2018.Objects;

import java.util.ArrayList;

public class Result {
    private ArrayList<Grocery> groceries;

    public Result()
    {}
    public Result(ArrayList<Grocery> groceries)
    {
        this.groceries = groceries;
    }

    public ArrayList<Grocery> getGroceries() {
        return groceries;
    }

    public void setGroceries(ArrayList<Grocery> groceries) {
        this.groceries = groceries;
    }
}
