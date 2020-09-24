package com.example.kyl3g.sunhacksnov2018.Search;

import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;

import java.util.Comparator;

public class RecipeComparator implements Comparator<Recipes> {
    @Override
    public int compare(Recipes o1, Recipes o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
