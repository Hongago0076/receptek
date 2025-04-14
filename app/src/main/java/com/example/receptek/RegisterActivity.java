package com.example.receptek;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button registerBtn, backBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        backBtn = findViewById(R.id.backBtn);

        Animation clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click);

        registerBtn.setOnClickListener(v -> {
            v.startAnimation(clickAnim);
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kérlek, tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            String errorMessage = "Hiba: " + task.getException().getMessage();

                            if (task.getException() != null) {
                                String msg = task.getException().getMessage();
                                if (msg.contains("The email address is already in use")) {
                                    errorMessage = "Ez az e-mail cím már regisztrálva van.";
                                } else if (msg.contains("The email address is badly formatted")) {
                                    errorMessage = "Érvénytelen e-mail formátum.";
                                } else if (msg.contains("The given password is invalid") || msg.contains("Password should be at least")) {
                                    errorMessage = "A jelszónak legalább 6 karakter hosszúnak kell lennie.";
                                }
                            }

                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        backBtn.setOnClickListener(v -> {
            v.startAnimation(clickAnim);
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}
