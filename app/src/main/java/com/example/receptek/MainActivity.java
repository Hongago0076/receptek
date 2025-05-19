package com.example.receptek;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    private Button btnRecipes, btnAddRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            } else {
                sendNotification();
            }
        } else {
            sendNotification();
        }


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

        btnRecipes = findViewById(R.id.btnRecipes);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);

        btnRecipes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RecipeListActivity.class));
        });

        btnAddRecipe.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
        });

        setupDailyAlarm();


    }
    private void sendNotification() {
        String channelId = "recipe_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Recept értesítések",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Új receptet adnál hozzá?")
                .setContentText("Ne felejtsd el megosztani a kedvenc recepted!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }

    public void setupDailyAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Időpont beállítása: minden nap 10:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Ha már elmúlt 10:00 ma, akkor holnapra állítjuk
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Ismétlődő ébresztés beállítása
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
