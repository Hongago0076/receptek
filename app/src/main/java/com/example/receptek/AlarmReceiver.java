package com.example.receptek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "recept_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Itt jönne az új recept létrehozása a Firestore-ban
        // De most csak egy értesítést küldünk és egy toastot mutatunk

        Toast.makeText(context, "Új recept létrehozva (szimuláció)", Toast.LENGTH_LONG).show();

        // Értesítés létrehozása
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_edit)
                .setContentTitle("Recept App")
                .setContentText("Új recept készült el automatikusan!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(1001, builder.build());
    }
}
