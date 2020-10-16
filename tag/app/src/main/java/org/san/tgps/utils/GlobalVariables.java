package org.san.tgps.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;

import java.util.Date;

public class GlobalVariables {

   // public static String IPAddress = "http://103.16.140.100/sangps/";
    public static String IPAddress = "http://sangps.trackagps.com/";

    public static final String url = GlobalVariables.IPAddress + "online.php?user=";
    public static String apiserverUrl = "http://webapi.trackagps.com/api/GPSAPI/";
   // public static String apiserverUrl = "http://192.168.43.141/api/mac/";

    public static final String DATA_URL1 = "http://192.168.1.5/vehicle/showonline.php";
    public static final String KEY_DNAME = "dname";

    public static final String JSON_ARRAY1= "result1";

    public static  String KEY_DSTATUS0 = "dstatus0";
    public static  String KEY_DSTATUS1 = "dstatus1";
    public static  String KEY_DSTATUS2 = "dstatus2";

    public static int online;
    public static int offline;
    public static int running;

    public static String speedimei;
    public static String speed;
    public static String regno;
    public static int vehicleclickstatus;


    public static final String JSON_ARRAY = "result";
    public static String Comapny_ID;
    public static String userid;
    public static String role;
    public static String username;
    public static String user;
    public static String imei;
    public static String customdate;
    public static String DeviceId;
    public static String cdate;


    public static String customtime;
    public static String cstmTodateTime;
    public static String cstmFrmdateTime;
    public static double gllatitude;
    public static double gllogitude;
    public static Date curtime;

    public static int db_sosState;
    public static int db_overspeedState;
    public static int db_accState;
    public static int db_lowbatteryState;
    public static int db_powercutState;
    public static int db_geofenceState;

    public static int Systemsos;
    public static int SystemOverspeed;
    public static int SystemAcc;
    public static int SystemLowbattery;
    public static int SystemPowercut;
    public static int SystemGeofence;

    public static String TodayDate;
    public static String FromDate;
    public static MediaPlayer player;


    public static String sosalarmAddress;






    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static String getcurrentime() {
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        String curTime = hours + ":" + minutes + ":" + seconds;
        return curTime;
    }
}
