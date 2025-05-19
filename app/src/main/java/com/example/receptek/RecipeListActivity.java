package com.example.receptek;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receptek.models.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipeList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerView = findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadRecipes();
    }
    // ⬇️ EZT ADD HOZZÁ:
    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes(); // újratöltés minden visszatéréskor
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
    }
    private void loadRecipes() {
        db.collection("recipes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    recipeList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Recipe recipe = doc.toObject(Recipe.class);
                        recipeList.add(recipe);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba a receptek lekérdezésekor", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error getting recipes", e);
                });
    }
}
