package com.example.receptek;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        welcomeText = findViewById(R.id.welcomeText);
        logoutBtn = findViewById(R.id.logoutBtn);

        ObjectAnimator colorAnim = ObjectAnimator.ofArgb(
                welcomeText,
                "textColor",
                0xFFFF0000,  // Piros
                0xFF00FF00   // Zöld
        );
        colorAnim.setDuration(1000); // Időtartam 1 másodperc
        colorAnim.setRepeatCount(ObjectAnimator.INFINITE); // Végtelen ismétlés
        colorAnim.setRepeatMode(ObjectAnimator.REVERSE);   // Visszafelé is animáljon
        colorAnim.start();

        Animation clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click);

        if (user != null) {
            welcomeText.setText("Üdv, " + user.getEmail());
        } else {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }

        logoutBtn.setOnClickListener(v -> {
            v.startAnimation(clickAnim);
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        });

    }
}
