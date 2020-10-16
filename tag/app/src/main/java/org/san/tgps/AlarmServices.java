package org.san.tgps;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.san.tgps.app.AppController;
import org.san.tgps.utils.GlobalVariables;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.os.SystemClock.elapsedRealtime;
import static android.os.SystemClock.sleep;

public class AlarmServices extends Service {

    Intent intent;
    private String TAG = AlarmServices.class.getSimpleName();
    public static final String BROADCAST_ACTION = "Hello World";
    public Context context = this;
    String alarmtype;

    private String Alarmtype, alarmimei, alarmvehicleno, alarmvehiclemodel, newalarmimei;
    int DeviceId;
    private String UserId,userrole;
    SharedPreferences sp;

    String NOTIFICATION_CHANNEL_ID = "1";
    Calendar cal = Calendar.getInstance();


    public static final int notify = 10000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public void onCreate() {
        //  Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        // geocoder = new Geocoder(this, Locale.getDefault());
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //startForegroundServiceWithNotificationChannel();
            startMyOwnForeground();
        }else
        {
            startForeground(1, new Notification());
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundServiceWithNotificationChannel() {
        String channelName = "ALarm Notification";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder nottificationbuilder = new NotificationCompat.Builder(this);
        Notification notification = nottificationbuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("SAN_TAG App Notification")
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  super.onStartCommand(intent, flags, startId);
        onTaskRemoved(intent);
         //  Toast.makeText(this, "Service is running 123", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //performe the deskred task
                // Toast t =Toast.makeText(AccAlarmService.this, "Service is running", Toast.LENGTH_SHORT);
                // t.show();
                sp = getSharedPreferences("SanTag", Context.MODE_PRIVATE);
                UserId = sp.getString("UserId", null);
                userrole= sp.getString("UserRole", null);
                new GetAlarms().execute();
            }
        }, 10000);

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmService.set(ELAPSED_REALTIME, elapsedRealtime() + 1000,
                restartServicePendingIntent);
        //  super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Override
    public void onDestroy() {
     /*   Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
       // super.onDestroy();
       // handler.removeCallbacksAndMessages(null);
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("san.com.bikeapp.AccAlarmService");
        intent.putExtra("yourvalue", "torestore");
        sendBroadcast(broadcastIntent);*/
        super.onDestroy();
        // mTimer.cancel();    //For Cancel Timer

       // Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }




    public class GetAlarms extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl + "AlarmService?UserId=" + UserId+"&UserRole="+userrole;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    System.out.println(jsonObj);

                    Alarmtype = jsonObj.getString("cAlarmType");
                    //newalarmimei = jsonObj.getString("cIMEI");
                    DeviceId=jsonObj.getInt("DeviceId");
                    alarmvehicleno = jsonObj.getString("cRegNumber");
                    GlobalVariables.userid=UserId;
                    GlobalVariables.role=userrole;
                    // String a=vStatus;
                    // String b=a;


                } catch (final JSONException e) {
                    e.printStackTrace();

                }
            } else {


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Toast.makeText(context, ""+newalarmimei, Toast.LENGTH_LONG).show();
            if (Alarmtype == null) {

            } else {
                if (Alarmtype.equals("sos")) {

                    addASosNotification();

                }
                if (Alarmtype.equals("AccOn")) {

                    addAccOnNotification();
                }
                if (Alarmtype.equals("AccOff")) {

                    addAccOffNotification();
                }

                 if (Alarmtype.equals("powerCut")) {

                    addpowerCutNotification();

                }
                if (Alarmtype.equals("lowbattery")) {

                    addLowBatteryNotification();

                }
                if (Alarmtype.equals("OverSpeed")) {

                    addOverspeedNotification();

                }
                if (Alarmtype.equals("Geofence")) {

                    addGeofenceNotification();

                }
                else {
                }

            }
        }


    }
 /*   private class UpdateAlarmCountTabstatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"UpdateAlarm?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&AlarmType="+Alarmtype;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    *//*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*//*
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    String offline = jsonObj.getString("Status");


                } catch (final JSONException e) {


                }
            } else {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (result == null) {
            }
        }

    }*/

    public void addASosNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + " "+"Sos Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
       /* Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);*/

        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();
        //alarmtype="sos";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public void addOverspeedNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + " "+"OverSpeed Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();

        //alarmtype="OverSpeed";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
    public void addAccOffNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + " "+"AccOff Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();

        alarmtype="AccOff";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
    public void addAccOnNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + " "+"AccOn Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
        /*Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setSound(uri);*/
        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();
        alarmtype="AccOn";
        //alarmtype="AccOn";
        //new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
    public void addLowBatteryNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + " "+"LowBattery Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();

       // alarmtype="lowbattery";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
    public void addpowerCutNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("" + alarmvehicleno + ""+"PowerCut Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);

        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();

       // alarmtype="powerCut";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
    public void addGeofenceNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle(""+ alarmvehicleno + " "+"Geofence Notification")
                        .setContentText("Touch for view notification")
                        .setAutoCancel(true);
        GlobalVariables.player = MediaPlayer.create(this, R.raw.alarm);
        GlobalVariables.player.setLooping(false); // Set looping
        GlobalVariables.player.setVolume(100, 100);
        GlobalVariables.player.start();
       // alarmtype="Geofence";
       // new UpdateAlarmCountTabstatus().execute();
        Intent notificationIntent = new Intent(this, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}