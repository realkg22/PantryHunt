package com.example.kyl3g.sunhacksnov2018.Objects;

import java.util.ArrayList;

public class Recipes {
    private String name;
    private String description;
    private ArrayList<IngredientsInfo> ingredientsInfos;
    private DietPattern dietPattern;

    public Recipes()
    {}

    public Recipes(String name, String description, ArrayList<IngredientsInfo> ingredientsInfos, DietPattern dietPattern)
    {
        this.name = name;
        this.description = description;
        this.ingredientsInfos = ingredientsInfos;
        this.dietPattern = dietPattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DietPattern getDietPattern() {
        return dietPattern;
    }

    public void setDietPattern(DietPattern dietPattern) {
        this.dietPattern = dietPattern;
    }

    public ArrayList<IngredientsInfo> getIngredientsInfos() {
        return ingredientsInfos;
    }

    public void setIngredientsInfos(ArrayList<IngredientsInfo> ingredientsInfos) {
        this.ingredientsInfos = ingredientsInfos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
