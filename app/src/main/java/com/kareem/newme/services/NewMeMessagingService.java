package com.kareem.newme.services;


        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Build;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import com.google.firebase.messaging.RemoteMessage;
        import com.kareem.newme.MainActivity;
        import com.kareem.newme.R;


public class NewMeMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "Android News App";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //It is optional
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification
    private void sendNotification(String title, String messageBody) {


        long[] vibrate  = {500,200,200,500};
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setLights(Color.CYAN, 1, 1)
                .setPriority(1000)
                .setVibrate(vibrate)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }




}