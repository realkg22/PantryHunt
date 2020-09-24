package com.example.kyl3g.sunhacksnov2018.Objects;

import com.example.kyl3g.sunhacksnov2018.Search.HuntedRecipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String name;
    private DietPattern dietPattern;
    private List<Recipes> huntedRecipes = new ArrayList<Recipes>();

    public User()
    {

    }

    public User(String id, String email, String name, DietPattern dietPattern)
    {
        this.id = id;
        this.email = email;
        this.name = email;
        this.dietPattern = dietPattern;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isCompatible(DietPattern dietPattern){
        boolean returned = true;
        if(!this.dietPattern.isRedMeat() && dietPattern.isRedMeat()){
            returned = false;
        }
        if(!this.dietPattern.isWhiteMeat() && dietPattern.isWhiteMeat()){
            returned = false;
        }
        if(!this.dietPattern.isSeafood() && dietPattern.isSeafood()){
            returned = false;
        }
        return returned;
    }

    public List<Recipes> getHuntedRecipes() {
        return huntedRecipes;
    }

    public void setHuntedRecipes(List<Recipes> huntedRecipes) {
        this.huntedRecipes = huntedRecipes;
    }

    public void addHuntedRecipes(Recipes recipe){
        boolean flag = true;
        for(int i = 0; i < huntedRecipes.size(); i++){
            if(huntedRecipes.get(i).getName().equals(recipe.getName())){
                flag = false;
                break;
            }
        }
        if(flag)
        huntedRecipes.add(recipe);
    }

    public void removeHuntedRecipes(Recipes recipe){
        for(int i = 0; i < huntedRecipes.size(); i++){
            if(huntedRecipes.get(i).getName().equals(recipe.getName())){
                huntedRecipes.remove(i);
                break;
            }
        }

    }

    public boolean checkDuplicateRecipes(Recipes recipe){
        boolean flag = false;

        for(int i = 0; i < this.huntedRecipes.size(); i++){
            if(this.huntedRecipes.get(i).getName().equals(recipe.getName())){
                flag  = true;
                break;
            }

        }

        return flag;
    }
}
