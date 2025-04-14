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

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginBtn, backBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        backBtn = findViewById(R.id.backBtn);

        Animation clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click);

        loginBtn.setOnClickListener(v -> {
            v.startAnimation(clickAnim);
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kérlek, tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sikeres bejelentkezés", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            String errorMessage = "Hiba: " + task.getException().getMessage();

                            if (task.getException() != null) {
                                String msg = task.getException().getMessage();
                                if (msg.contains("There is no user record")) {
                                    errorMessage = "Nincs ilyen e-mail cím regisztrálva.";
                                } else if (msg.contains("The password is invalid") || msg.contains("The supplied auth credential is incorrect")) {
                                    errorMessage = "Hibás jelszó. Próbáld újra!";
                                }
                                else if (msg.contains("The email address is badly formatted")) {
                                    errorMessage = "Érvénytelen e-mail formátum.";
                                }
                            }

                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        backBtn.setOnClickListener(v -> {
            v.startAnimation(clickAnim);
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}