package com.example.receptek.models;

import java.util.List;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private String ingredients;

    public Recipe() {
        // Firestore-hoz kell üres konstruktor
    }

    public Recipe(String name, String description, String ingredients) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    // Getterek és setterek
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
