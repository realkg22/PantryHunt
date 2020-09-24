package com.example.kyl3g.sunhacksnov2018.Search;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonAccessor {

    private static String huntedRecipePath = Environment.getDataDirectory() + "/PantryHunt/HuntedRecipe.json";
    private static ObjectMapper mapper = new ObjectMapper();

    public static void createJsonIfNotExists(){

        File huntedRecipejson = new File(huntedRecipePath);

        if (!huntedRecipejson.exists()){
            huntedRecipejson.getParentFile().mkdirs();

            JsonReset();
        }
    }

    public static void JsonReset(){
        List<Recipes> list = new ArrayList<Recipes>();
        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(huntedRecipePath), list);


        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static List<Recipes> getHuntedRecipe(){
        List<Recipes> huntedRecipes = new ArrayList<Recipes>();
        try {
            huntedRecipes = mapper.readValue(new FileReader(huntedRecipePath), mapper.getTypeFactory().constructCollectionType(List.class, Recipes.class));


        } catch (JsonGenerationException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return huntedRecipes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addHuntedRecipe(Recipes recipe){

        try {
            List<Recipes> huntedRecipes = mapper.readValue(new FileReader(huntedRecipePath), mapper.getTypeFactory().constructCollectionType(List.class, Recipes.class));
            for(int i = 0; i < huntedRecipes.size(); i++){
                if(!huntedRecipes.contains(recipe)) {
                    huntedRecipes.add(recipe);
                    huntedRecipes.sort(new RecipeComparator());
                    mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(huntedRecipePath), huntedRecipes);
                }
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void deleteHuntedRecipe(Recipes recipe){
        try {
            List<Recipes> huntedRecipes = mapper.readValue(new FileReader(huntedRecipePath), mapper.getTypeFactory().constructCollectionType(List.class, Recipes.class));
            for(int i = 0; i < huntedRecipes.size(); i++){
                if(huntedRecipes.contains(recipe)){
                    huntedRecipes.remove(i);
                }
            }
            huntedRecipes.sort(new RecipeComparator());

            mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(huntedRecipePath),huntedRecipes);
        } catch (JsonGenerationException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
