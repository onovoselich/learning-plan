package ua.novoselich.services.intentservice;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ua.novoselich.services.MainActivity;
import ua.novoselich.services.R;

public class MyIntentService extends IntentService {

    private static final int STEP = 20;

     static final String RESULT = "result";
     static final String PROGRESS = "progress";
    static final String FOREGROUND = "foreground";
     static final String NOTIFICATION = "ua.novoselich.services.intentservice";
     static final String DELAY = "delay";

    public MyIntentService(){
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent){

        if(intent.getExtras().getBoolean(FOREGROUND)){
            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.notification_message));

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.setContentIntent(pendingIntent);
            startForeground(1,notification.build());
        }

        long delay = intent.getExtras().getInt(DELAY,5000);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + delay;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(STEP);
                } catch (Exception e) {
                }
            }
            int progress = (int) (((System.currentTimeMillis()-startTime)/(float)delay)*100);
            publishProgress(progress);
        }
        publishResults();
    }

    private void publishProgress(int progress) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(PROGRESS, progress);
        sendBroadcast(intent);
    }

    private void publishResults(){
        int result = Activity.RESULT_OK;
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
