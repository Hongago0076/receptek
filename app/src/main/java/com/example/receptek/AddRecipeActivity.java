package com.example.receptek;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.receptek.models.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText nameInput, descriptionInput, ingredientsInput;
    private Button saveBtn, backBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.nameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        saveBtn = findViewById(R.id.saveRecipeBtn);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String desc = descriptionInput.getText().toString().trim();
            String ingredientsText = ingredientsInput.getText().toString().trim();

            if (name.isEmpty() || desc.isEmpty() || ingredientsText.isEmpty()) {
                Toast.makeText(this, "Tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> recipe = new HashMap<>();
            recipe.put("name", name);
            recipe.put("description", desc);
            recipe.put("ingredients", ingredientsText);

            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(documentReference -> {
                        String generatedId = documentReference.getId();

                        // Most frissítsük a saját "id" mezőt ezzel az auto-ID-vel
                        documentReference.update("id", generatedId)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "ID mező frissítve: " + generatedId);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "ID mező frissítése sikertelen", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Dokumentum létrehozása sikertelen", e);
                    });
        });
    }

}
