package uni.bamberg.hellobeacon;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServiceImpl extends IntentService {
    private BufferedReader reader;
    private ArrayList<Beacon> scanned_beacons = new ArrayList<>();
    public static int NOTIFICATION_ID = 1;

    public ServiceImpl() {
        super("ServiceImpl");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("ServiceImpl", "onHandleIntent running");

        //TODO: uncomment this, when implemented setupInputReader();
        setupInputReader();
        //TODO: get the seconds from intent
        long seconds = intent.getExtras().getLong("seconds");
        //how long the service should sleep, in milliseconds
        long millis = seconds * 1000;

        while (true) {
            try {
                Beacon beacon = scanBeacon();
                if(beacon != null){
                    //TODO: add beacons to the List of scanned beacons
                    scanned_beacons.add(beacon);
                    //TODO: notification
                    //TODO: intent to AddBeaconsActivity
                    NotificationCompat.Builder notification = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("New Beacon found")
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setContentText(beacon.toString());
                    Intent intent1 = new Intent(this, AddBeaconsActivity.class);
                    intent1.putParcelableArrayListExtra("Beacon Array", scanned_beacons);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(AddBeaconsActivity.class).addNextIntent(intent1);
                    PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pIntent);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, notification.build());
                }
                else {
                    break;
                }

                //TODO: put the service to sleep
                Thread.sleep(millis);
            }
            catch (InterruptedException iEx){
                Log.d("Message", iEx.getMessage());
            }
        }
    }

    private void setupInputReader() {

        //TODO: read the file "Beacon.txt"
        //read the header in advance to exclude it from the output
        try {
            File file = new File(getFilesDir(), "Beacons.txt");
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("ServiceImpl", "setupInputReader executed");
    }

    private Beacon scanBeacon() {
        //TODO: Read a line and split one row into the beacon components uuid, major and minor
        Log.v("ServiceImpl", "scanBeacon() called");
        String line;
        Beacon scanned_beacon = null;
        try {
            line = reader.readLine();
            if(!line.isEmpty()) {
                String[] beacon_params = line.split(MainActivity.COMMA_DELIMITER);
                String uuid = beacon_params[0];
                int major = Integer.parseInt(beacon_params[1]);
                int minor = Integer.parseInt(beacon_params[2]);
                scanned_beacon = new Beacon(uuid, major, minor);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return scanned_beacon;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("ServiceImpl", "onDestroy");
    }
}