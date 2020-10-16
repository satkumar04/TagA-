package org.san.tgps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.san.tgps.utils.ConnectionDetector;
import org.san.tgps.utils.GlobalVariables;


public class LoadingScreen extends AppCompatActivity {

    ConnectionDetector cd;
    Boolean isConnected = false;
    Context mContext;
    Handler handler = new Handler();
    Runnable runnablelive = null;
    public String username, password, comp_id, role;

    Button btnwifi;
    boolean connected = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            connected = true;
        } else
            connected = false;
        if (connected == true) {
           // startService(new Intent(this, AlarmServices.class));

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(new Intent(this, AlarmServices.class));

            } else {*/



           // }


            try {
                /*cd = new ConnectionDetector(LoadingScreen.this);
                isConnected = cd.isConnectingToInternet();
                if (!isConnected) {

                    WifiManager wifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    GlobalVariables.showAlertDialog(LoadingScreen.this, "No Internet conncetion", "You don't have internet connection", false);
                } else {*/
                    SharedPreferences pref = getSharedPreferences("logindetails", MODE_PRIVATE);
                    username = pref.getString("username", null);
                    password = pref.getString("password", null);
                    comp_id = pref.getString("comp_id", null);
                    role = pref.getString("role", null);

                    if (username != null || password != null) {
                        GlobalVariables.Comapny_ID = comp_id;
                        GlobalVariables.role = role;
                        GlobalVariables.username = username;
                        //21.03.2016,
                        // notiList = DALServerData.getnotifications(comp_id);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(LoadingScreen.this,LoginScreen.class);
                                startActivity(i);
                            }
                        }, 200);
                        /*Intent in = new Intent(LoadingScreen.this, LoginScreen.class);
                        //  in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);*/
                    } else {
                        /*Intent i = new Intent(LoadingScreen.this, LoginScreen.class);
                        startActivity(i);*/
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(LoadingScreen.this,LoginScreen.class);
                                startActivity(i);
                            }
                        }, 200);
                    }
                    // Remove activity
                   // finish();
                    //}
               // }

            } catch (Exception e) {
                e.getStackTrace();
            }
            String a = username;
        }
        else{
            Toast toast = Toast.makeText(LoadingScreen.this, "No Internet Connection... Please check your connection", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
            v1.setTextColor(Color.GREEN);
            v1.setTextSize(15);
            toast.show();
        }
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_loading_screen, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onResume() {
        super.onResume();
       // startService(new Intent(this, AlarmServices.class));
    }
}
