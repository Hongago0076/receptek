package com.example.receptek;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView nameText, ingredientsText, instructionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        nameText = findViewById(R.id.detailName);
        ingredientsText = findViewById(R.id.detailIngredients);
        instructionsText = findViewById(R.id.detailInstructions);

        // Adatok lekérése az intentből
        String name = getIntent().getStringExtra("name");
        String ingredients = getIntent().getStringExtra("ingredients");
        String instructions = getIntent().getStringExtra("instructions");

        // Nézetek feltöltése
        nameText.setText(name);
        ingredientsText.setText("Hozzávalók:\n" + ingredients);
        instructionsText.setText("Elkészítés:\n" + instructions);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
    }
}
