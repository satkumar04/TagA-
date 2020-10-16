package org.san.tgps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.san.tgps.adapter.SosAlarmadapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.AlarmsBean;
import org.san.tgps.utils.GlobalVariables;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Navigation extends AppCompatActivity {

    Button buttonGet;
    public static int a,b,c,d;
    int sos,acc,lowbattery,powercut,overspeed,geofence;
    PieChart pieChart;
    private ProgressDialog loading;
    private String TAG = LoginScreen.class.getSimpleName();
    Button btnonline,btnoffline,btnmoving,btnlogout,Btn_sos,Btn_overspeed,Btn_acc,Btn_lowbattery,Btn_powercutoff,Btn_geofence;
    String time;
    final int[] MY_COLORS = {Color.rgb(197,50,56),Color.rgb(23,169,92),Color.rgb(161,182,171)};

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;
    private boolean mIsRotationEnabled = true;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    TextView curdate,offline,online,moving,num_online,num_offline,num_moving;

    Handler handler = new Handler();
    Runnable runnablelive = null;

    TextView sos_count,overspeed_count,acc_count,lowbattery_count,powercut_count,geofence_count;
    TextView slideshow,gallery;
    String clickedAlarmValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        int a=GlobalVariables.offline;
        int b=GlobalVariables.online;
        int d=GlobalVariables.running;
        //getSysytemAlarmSetting();
        new GetSystemAlarmSetting().execute();
        new GetVehicleCount().execute();
        new GetDBAlarmsts().execute();
        //getData();
        startTimer1();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.NewBARcolor)));

        curdate=(TextView)findViewById(R.id.txt_curdate);
        offline=(TextView)findViewById(R.id.txt_off);
        online=(TextView)findViewById(R.id.txt_on);
        moving=(TextView)findViewById(R.id.txt_move);

        num_online=(TextView)findViewById(R.id.num_online);
        num_offline=(TextView)findViewById(R.id.num_offline);
        num_moving=(TextView)findViewById(R.id.num_moving);

        btnonline=(Button) findViewById(R.id.btn_online);
        btnoffline=(Button) findViewById(R.id.btn_offline);
        btnmoving=(Button) findViewById(R.id.btn_moving);
        btnlogout=(Button) findViewById(R.id.btn_logout);

        sos_count=(TextView)findViewById(R.id.txt_soscount);
        overspeed_count=(TextView)findViewById(R.id.txt_overspeed);
        acc_count=(TextView)findViewById(R.id.txt_acc);
        lowbattery_count=(TextView)findViewById(R.id.txt_lowbattery);
        powercut_count=(TextView)findViewById(R.id.txt_powercut);
        geofence_count=(TextView)findViewById(R.id.txt_geofence);



        Btn_sos=(Button)findViewById(R.id.btn_sos);
        Btn_overspeed=(Button)findViewById(R.id.btn_overspeed);
        Btn_acc=(Button)findViewById(R.id.btn_acc);
        Btn_lowbattery=(Button)findViewById(R.id.btn_lowbattery);
        Btn_powercutoff=(Button)findViewById(R.id.btn_powercut);
        Btn_geofence=(Button)findViewById(R.id.btn_geofence);
        startTimer();
        autorefreshdate();
        barchartdesign();
        num_online.setText(""+GlobalVariables.online);
        num_offline.setText(""+GlobalVariables.offline);
        num_moving.setText(""+GlobalVariables.running);
        setAlarmButtons();
        alarmbuttonTimer();
        initInstances();
        //barchardesign();
        new GetAlarmCount().execute();
       // getalarmcount();
        //barchardesign();
        chart = (BarChart) findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        updatebarcharTimer();
        num_online.setText(""+GlobalVariables.online);
        num_offline.setText(""+GlobalVariables.offline);
        num_moving.setText(""+GlobalVariables.running);
        Bardataset = new BarDataSet(BARENTRY, "Projects");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        ArrayList<Integer> color = new ArrayList<Integer>();

        for(int c: MY_COLORS) color.add(c);
        Bardataset.setColors(color);
        Bardataset.setBarSpacePercent(50f);

       // Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);
        chart.animateY(1000);
        //remove xaxis values
        chart.setDescription("");    // Hide the description
       // chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);

        // convert float to integer
        BARDATA.setValueFormatter(new MyValueFormatter());
        Bardataset.setValueFormatter(new MyValueFormatter());
        Bardataset.setDrawValues(false);

       // num_online.setText(GlobalVariables.online);

        btnonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(Navigation.this,VehiclesMain.class);
                startActivity(i);
                GlobalVariables.vehicleclickstatus=1;
            }
        });

        btnoffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i=new Intent(Navigation.this,VehiclesMain.class);
                startActivity(i);
                GlobalVariables.vehicleclickstatus=2;
            }
        });
        btnmoving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Navigation.this,VehiclesMain.class);
                startActivity(i);
                GlobalVariables.vehicleclickstatus=3;
            }
        });


        Btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedAlarmValue="sos";
                //updateTabStatus();
                new UpdateAlarmCountTabstatus().execute();
                Intent i=new Intent(Navigation.this,SosAlarmMain.class);
                startActivity(i);

            }
        });
        Btn_overspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAlarmValue="OverSpeed";
                new UpdateAlarmCountTabstatus().execute();
                //updateSpeedTabStat();
                Intent i=new Intent(Navigation.this,OverSpeedAlarmMain.class);
                startActivity(i);
        }
        });
        Btn_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAlarmValue="AccOn";
                new UpdateAlarmCountTabstatus().execute();
                //updateTabStatus();
                Intent i=new Intent(Navigation.this,AccAlarmMain.class);
                startActivity(i);
            }
        });

        Btn_lowbattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAlarmValue="lowBattery";
                new UpdateAlarmCountTabstatus().execute();
                //updateTabStatus();
                Intent i=new Intent(Navigation.this,LowBatteryAlarmMain.class);
                startActivity(i);
            }
        });
        Btn_powercutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAlarmValue="powerCut";
                new UpdateAlarmCountTabstatus().execute();
                //updateTabStatus();
                Intent i=new Intent(Navigation.this,PowerCutAlarmMain.class);
                startActivity(i);
            }
        });

        Btn_geofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAlarmValue="Geofence";
                new UpdateAlarmCountTabstatus().execute();
                //updateGeofenceTabStat();
                Intent i=new Intent(Navigation.this,GeofenceAlarmMain.class);
                startActivity(i);
            }
        });


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    AlertDialog alertDialog = new AlertDialog.Builder(Navigation.this).create();
                    alertDialog.setTitle("Logout");
                    alertDialog.setMessage("Are You sure Want to Logout?");
                    alertDialog.setIcon(R.drawable.warning);
                    alertDialog.setButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            SharedPreferences pref = getSharedPreferences("logindetails", MODE_PRIVATE);
                            pref.edit().clear().commit();

                            Intent i1 = new Intent(getApplicationContext(), LoginScreen.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i1);
                            Navigation.this.finish();
                        }
                    });
                    alertDialog.setButton2("CANCEL", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();

                        }
                    });

                    alertDialog.show();
                }
                catch (Exception e) {

                }
            }
        });
    }
    public void AddValuesToBARENTRY()
    {
        BARENTRY.add(new BarEntry(GlobalVariables.offline, 0));
        BARENTRY.add(new BarEntry(GlobalVariables.online, 1));
        BARENTRY.add(new BarEntry(GlobalVariables.running, 2));

    }

    public void AddValuesToBarEntryLabels()
    {
        BarEntryLabels.add("OFFLINE");
        BarEntryLabels.add("ONLINE");
        BarEntryLabels.add("MOVING");
    }

    private class MyValueFormatter implements ValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + "";
        }
    }


    private CharSequence value()
    {
        return null;
    }

    private void initInstances() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(Navigation.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);


        navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_1:
                        Intent in = new Intent(Navigation.this, VehiclesMain.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        break;

                    case R.id.navigation_item_2:
                        Intent in1 = new Intent(Navigation.this, Navigation.class);
                        in1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in1);
                        break;

                    /*case R.id.navigation_item_4:
                        Intent in2 = new Intent(Navigation.this, LiveTracking.class);
                        in2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in2);
                        break;*/

                    case R.id.navigation_item_5:
                        Intent in3 = new Intent(Navigation.this,HistoryVehiclesMain.class);
                        in3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in3);
                        break;

                    case R.id.navigation_item_6:
                        Intent in4 = new Intent(Navigation.this, AlertsActivity.class);
                        in4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in4);
                        break;
                    case R.id.navigation_item_7:
                       // Intent i1 = new Intent(mContext, VehicleTrackMain.class);
                        Intent in5 = new Intent(Navigation.this, CompanyInformation.class);
                        in5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in5);
                        break;
                    }
                return false;
            }
        });

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

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


    public void onBackPressed() {
        try {

            AlertDialog alertDialog = new AlertDialog.Builder(Navigation.this).create();
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are You sure Want to Logout?");
            alertDialog.setIcon(R.drawable.warning);
            alertDialog.setButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {

                    SharedPreferences pref = getSharedPreferences("logindetails", MODE_PRIVATE);
                    pref.edit().clear().commit();

                    Intent i1 = new Intent(getApplicationContext(), LoginScreen.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i1);
                    Navigation.this.finish();
                }
            });
            alertDialog.setButton2("CANCEL", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();

                }
            });

            alertDialog.show();
        }
        catch (Exception e) {

        }
    }
    private void startTimer() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
              //  livepositiontrack();
               // setAlarmButtons();
                autorefreshdate();
                //getalarmcount();
                new GetAlarmCount().execute();
                //getGeofenceCount();
                //getSpeedAlarmCount();
                //getSysytemAlarmSetting();
                new GetSystemAlarmSetting().execute();

                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnablelive, 30000);
    }
    private void alarmbuttonTimer() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                //  livepositiontrack();
                setAlarmButtons();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnablelive, 1000);
    }
    private void updatebarcharTimer() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                //  livepositiontrack();
                barchartdesign();
                num_online.setText(""+GlobalVariables.online);
                num_offline.setText(""+GlobalVariables.offline);
                num_moving.setText(""+GlobalVariables.running);
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnablelive, 10000);
    }

    public void setAlarmButtons(){
        if(GlobalVariables.db_sosState==1 && GlobalVariables.Systemsos==1)
        {
            Btn_sos.setEnabled(true);


        }else
            {
                Btn_sos.setEnabled(false);

            }
        if(GlobalVariables.db_overspeedState==1 && GlobalVariables.SystemOverspeed>0)
        {
            Btn_overspeed.setEnabled(true);
            Btn_overspeed.getBackground().setAlpha(255);
            overspeed_count.setVisibility(View.VISIBLE);

        }
        else
        {
            Btn_overspeed.setEnabled(false);
            Btn_overspeed.getBackground().setAlpha(50);
            overspeed_count.setVisibility(View.INVISIBLE);
           // Btn_overspeed.setBackgroundResource(R.drawable.acc);

        }
        if(GlobalVariables.db_accState==1 && GlobalVariables.SystemAcc>0)
        {
            Btn_acc.setEnabled(true);
            Btn_acc.getBackground().setAlpha(255);
            acc_count.setVisibility(View.VISIBLE);
        }else
        {
           Btn_acc.setEnabled(false);
            Btn_acc.getBackground().setAlpha(50);
            acc_count.setVisibility(View.INVISIBLE);
        }
        if(GlobalVariables.db_lowbatteryState==1 && GlobalVariables.SystemLowbattery>0)
        {
            Btn_lowbattery.setEnabled(true);
            Btn_lowbattery.getBackground().setAlpha(255);
            lowbattery_count.setVisibility(View.VISIBLE);
        }else
        {
            Btn_lowbattery.setEnabled(false);
            Btn_lowbattery.getBackground().setAlpha(50);
            lowbattery_count.setVisibility(View.INVISIBLE);

        }
        if(GlobalVariables.db_powercutState==1 && GlobalVariables.SystemPowercut>0)
        {
            Btn_powercutoff.setEnabled(true);
            Btn_powercutoff.getBackground().setAlpha(255);
            powercut_count.setVisibility(View.VISIBLE);
        }else
        {
            Btn_powercutoff.setEnabled(false);
            Btn_powercutoff.getBackground().setAlpha(50);
            powercut_count.setVisibility(View.INVISIBLE);

        }
        if(GlobalVariables.db_geofenceState==1 && GlobalVariables.SystemGeofence>0)
        {
            Btn_geofence.setEnabled(true);
            Btn_geofence.getBackground().setAlpha(255);
            geofence_count.setVisibility(View.VISIBLE);
        }else
        {
            Btn_geofence.setEnabled(false);
            Btn_geofence.getBackground().setAlpha(50);
            geofence_count.setVisibility(View.INVISIBLE);
        }
    }
    public void autorefreshdate(){
        Calendar date = Calendar.getInstance();
        //System.out.println("Current time => " + date.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date.getTime());
        Date time = new Date();
        DateFormat formatter = new SimpleDateFormat("hh:mm a"); //Hourse:Minutes
        //string representation
        String formattedtime = formatter.format(time);
        curdate.setText("Vehicle Status on"+" " +formattedDate+"  "+formattedtime);
        int s= GlobalVariables.online;

    }

    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 10000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txt = (TextView) findViewById(R.id.txt_soscount);
                        if(txt.getVisibility() == View.VISIBLE){
                            txt.setVisibility(View.INVISIBLE);
                        }else{
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }

    public void barchartdesign()
    {
        chart = (BarChart) findViewById(R.id.chart1);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "Projects");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        ArrayList<Integer> color = new ArrayList<Integer>();

        for(int c: MY_COLORS) color.add(c);
        Bardataset.setColors(color);
        Bardataset.setBarSpacePercent(50f);

        // Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(BARDATA);
        chart.animateY(1000);
        //remove xaxis values
        chart.setDescription("");    // Hide the description
        // chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);

        // convert float to integer
        BARDATA.setValueFormatter(new MyValueFormatter());
        Bardataset.setValueFormatter(new MyValueFormatter());
        Bardataset.setDrawValues(false);

    }




    private void startTimer1() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                new GetVehicleCount().execute();
                //getData();

                // getData();
                //  livepositiontrack();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnablelive, 5000);
    }
    private class GetSystemAlarmSetting extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"AlarmsSetting?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    int overspeed=jsonObj.getInt("nOverSpeed");
                    int acc=jsonObj.getInt("nAcc");
                    int lowbattery=jsonObj.getInt("nLowBattery");
                    int powercutoff=jsonObj.getInt("nPowerCutoff");
                    int geofence=jsonObj.getInt("nGeofence");

                    GlobalVariables.Systemsos=1;
                    GlobalVariables.SystemOverspeed=overspeed;
                    GlobalVariables.SystemAcc=acc;
                    GlobalVariables.SystemLowbattery=lowbattery;
                    GlobalVariables.SystemPowercut=powercutoff;




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

    }
    private class GetVehicleCount extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"VehicleCount?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                   int offline = jsonObj.getInt("offline");
                    int online = jsonObj.getInt("online");
                    int running = jsonObj.getInt("running");
                    GlobalVariables.online=online;
                    GlobalVariables.offline=offline;
                    GlobalVariables.running=running;



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

    }


    private class GetAlarmCount extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"AlarmCount?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    sos = jsonObj.getInt("nSos");
                    acc = jsonObj.getInt("nAcc");
                    lowbattery = jsonObj.getInt("nLowBattery");
                    powercut = jsonObj.getInt("nPowerCutoff");
                    overspeed= jsonObj.getInt("nOverSpeed");
                    geofence= jsonObj.getInt("nGeofence");



                } catch (final JSONException e) {


                }
            } else {


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if(sos>0){
                sos_count.setBackgroundResource(R.drawable.textviewrounded);
                sos_count.setText(""+sos);
                                        /*Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                        anim.setDuration(600); //You can manage the time of the blink with this parameter
                                        anim.setStartOffset(20);
                                        anim.setRepeatMode(Animation.REVERSE);
                                        anim.setRepeatCount(Animation.INFINITE);
                                        sos_count.startAnimation(anim);*/
            }
            else{
                sos_count.setText("");
            }

            if(acc>0){
                acc_count.setBackgroundResource(R.drawable.textviewrounded);
                acc_count.setText(""+acc);
            }
            else {
                acc_count.setText("");
            }
            if(lowbattery>0){
                lowbattery_count.setBackgroundResource(R.drawable.textviewrounded);
                lowbattery_count.setText(""+lowbattery);
            }
            else {
                lowbattery_count.setText("");
            }

            if(powercut>0){
                powercut_count.setBackgroundResource(R.drawable.textviewrounded);
                powercut_count.setText(""+powercut);
            }
            else {
                powercut_count.setText("");
            }
            if(overspeed>0){
                overspeed_count.setBackgroundResource(R.drawable.textviewrounded);
                overspeed_count.setText(""+overspeed);
            }
            else {
                overspeed_count.setText("");
            }
            if(geofence>0){
                geofence_count.setBackgroundResource(R.drawable.textviewrounded);
                geofence_count.setText(""+geofence);
            }
            else {
                geofence_count.setText("");
            }
        }

    }

    private class UpdateAlarmCountTabstatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"UpdateAlarm?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&AlarmType="+clickedAlarmValue;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                    String offline = jsonObj.getString("Status");


                } catch (final JSONException e) {


                }
            }
            else
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (result == null)
            {
            }
        }

    }

    private class GetDBAlarmsts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"Alarmsts?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;


            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                    JSONObject jsonObj = jsonArr.getJSONObject(0);
                   int dbsos = jsonObj.getInt("nSos");
                    int dboverspeed = jsonObj.getInt("nOverSpeed");
                    int dbacc = jsonObj.getInt("nAcc");
                    int dblowbattery = jsonObj.getInt("nLowBattery");
                    int dbpowercutoff= jsonObj.getInt("nPowerCutoff");
                    int dbgeofence = jsonObj.getInt("nGeofence");

                    GlobalVariables.db_sosState=dbsos;
                    GlobalVariables.db_overspeedState=dboverspeed;
                    GlobalVariables.db_accState=dbacc;
                    GlobalVariables.db_lowbatteryState=dblowbattery;
                    GlobalVariables.db_powercutState=dbpowercutoff;
                    GlobalVariables.db_geofenceState=dbgeofence;
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

    }

}