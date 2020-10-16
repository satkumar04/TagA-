package org.san.tgps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import org.san.tgps.bean.AlarmsBean;
import org.san.tgps.utils.GlobalVariables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class AlertsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    public static final String UPLOAD_URL = GlobalVariables.IPAddress+"insert_record.php";

    Switch swt_overspeed,swt_acc,swt_lowbattery,swt_powercutoff,swt_geofence;
    public int overspeed_state,acc_state,lowbattery_state,powercutoff_state,geofence_state;
    int sos_state=1;

    Button btnsave;
    Bitmap bitmap;
    private String TAG = AlertsActivity.class.getSimpleName();
    int db_overspeed,db_acc,db_lowbattery,db_powercutoff,db_geofence;
    int overspeed,acc,lowbattery,powercutoff,geofence;
    String alarmstatussavests;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new CheckAlarmSwitchState().execute();
       // checkswitchesstate();
        new GetSystemAlarmSetting().execute();
        //getSysytemAlarmSetting();

        swt_overspeed = (Switch)findViewById(R.id.switch_overspeed);
        swt_acc=(Switch)findViewById(R.id.switch_acc);
        swt_lowbattery=(Switch)findViewById(R.id.switch_lowbattery);
        swt_powercutoff=(Switch)findViewById(R.id.switch_powercutoff);
        swt_geofence=(Switch)findViewById(R.id.switch_geofence);
        btnsave=(Button)findViewById(R.id.btn_save);


        requestQueue = Volley.newRequestQueue(AlertsActivity.this);
        /*SharedPreferences sharedPrefs = getSharedPreferences("org.san.tgps", MODE_PRIVATE);
        swt_overspeed.setChecked(sharedPrefs.getBoolean("NameOfThingToSave", false));*/

        swt_overspeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    overspeed_state=1;
                    /*SharedPreferences.Editor editor = getSharedPreferences("org.san.tgps", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", true);
                    editor.commit();*/
                }
                else{
                    overspeed_state=0;
                    /*SharedPreferences.Editor editor = getSharedPreferences("org.san.tgps", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", false);
                    editor.commit();*/
                }
            }
        });
        swt_acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    acc_state=1;
                }
                else{
                    acc_state=0;
                }
            }
        });
        swt_lowbattery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    lowbattery_state=1;
                }
                else{
                    lowbattery_state=0;
                }
            }
        });
        swt_powercutoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    powercutoff_state=1;
                }
                else{
                    powercutoff_state=0;
                }
            }
        });
        swt_geofence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if(isChecked){
                    geofence_state=1;
                }
                else{
                    geofence_state=0;
                }
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new UpdateAlarmCountTabstatus().execute();
               // checkalarmuser();

            /*if(usercount==0)
                {
                    insertalarmstates();
                }else
                    {
                        updatealarmstatus();
                    }*/
            }
        });

    }




    private void insertalarmstates(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AlertsActivity.this, "Uploading ", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap = params[0];
               // String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                //data.put(UPLOAD_KEY, uploadImage);
                data.put("cUserId", GlobalVariables.Comapny_ID);
                data.put("nSos", ""+sos_state);
                data.put("nOverSpeed", ""+overspeed_state);
                data.put("nAcc", ""+acc_state);
                data.put("nLowBattery", ""+lowbattery_state);
                data.put("nPowerCutoff", ""+powercutoff_state);
                data.put("nGeofence", ""+geofence_state);

                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

   /* private void checkalarmuser() {

            String userUrl = GlobalVariables.IPAddress + "checkalarmuser.php?cUserId="+GlobalVariables.userid;
            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int success = 0;
                                try {
                                    success = response.getInt("success");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (success == 1) {
                                    JSONArray ja = response.getJSONArray("items");

                                    JSONObject jobj = ja.getJSONObject(0);
                                    String userid = jobj.getString("cUserId");
                                    updatealarmstatus();

                                } else if (success == 0) {
                                    insertalarmsts();

                                   }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // getToast("Might be Server down. So, Please try after some time");
                    Toast toast = Toast.makeText(AlertsActivity.this, "Might be Server down. So, Please try after some time", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(Color.GREEN);
                    v.setTextSize(30);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    //v.setBackgroundColor(0xFFFFFFFF);
                    toast.show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jreq);

    }*/
   /* private void updatealarmstatus() {

        String url = GlobalVariables.IPAddress + "updatealarmstatus.php?cUserId=" +GlobalVariables.userid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Toast.makeText(AlertsActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nOverSpeed", ""+overspeed_state);
                params.put("nAcc", ""+acc_state);
                params.put("nLowBattery", ""+lowbattery_state);
                params.put("nPowerCutoff", ""+powercutoff_state);
                params.put("nGeofence", ""+geofence_state);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void insertalarmsts() {

        String url = GlobalVariables.IPAddress + "insertalarmsts.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AlertsActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cUserId", GlobalVariables.Comapny_ID);
                params.put("nSos", ""+sos_state);
                params.put("nOverSpeed", ""+overspeed_state);
                params.put("nAcc", ""+acc_state);
                params.put("nLowBattery", ""+lowbattery_state);
                params.put("nPowerCutoff", ""+powercutoff_state);
                params.put("nGeofence", ""+geofence_state);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }*/
    public void onBackPressed()
    {
        Intent i=new Intent(AlertsActivity.this,Navigation.class);
        startActivity(i);
    }

    /*private void checkswitchesstate() {

        String userUrl = GlobalVariables.IPAddress + "checkswitchstates.php?cUserId="+GlobalVariables.userid;
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = 0;
                            try {
                                success = response.getInt("success");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("items");

                                JSONObject jobj = ja.getJSONObject(0);
                                db_overspeed = jobj.getInt("nOverSpeed");
                                db_acc = jobj.getInt("nAcc");
                                db_lowbattery = jobj.getInt("nLowBattery");
                                db_powercutoff = jobj.getInt("nPowerCutoff");
                                db_geofence = jobj.getInt("nGeofence");

                                if(db_overspeed==1 && GlobalVariables.SystemOverspeed>0)
                                {
                                    swt_overspeed.setChecked(true);
                                }else{
                                    swt_overspeed.setChecked(false);
                                }
                                if(db_acc==1 && GlobalVariables.SystemAcc>0)
                                {
                                    swt_acc.setChecked(true);
                                }else{
                                    swt_acc.setChecked(false);
                                }
                                if(db_lowbattery==1 && GlobalVariables.SystemLowbattery>0)
                                {
                                    swt_lowbattery.setChecked(true);
                                }else{
                                    swt_lowbattery.setChecked(false);
                                }
                                if(db_powercutoff==1 && GlobalVariables.SystemPowercut>0)
                                {
                                    swt_powercutoff.setChecked(true);
                                }else{
                                    swt_powercutoff.setChecked(false);
                                }
                                if(db_geofence==1 && GlobalVariables.SystemGeofence>0)
                                {
                                    swt_geofence.setChecked(true);
                                }else{
                                    swt_geofence.setChecked(false);
                                }

                            } else if (success == 0) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // getToast("Might be Server down. So, Please try after some time");
                Toast toast = Toast.makeText(AlertsActivity.this, "Might be Server down. So, Please try after some time", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.GREEN);
                v.setTextSize(30);
                toast.setDuration(Toast.LENGTH_SHORT);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);

    }*/
   /* public  void  getSysytemAlarmSetting()
    {
        // String usercheckUrl =GlobalVariables.IPAddress + "vehiclecountbyuser.php?cUserId=" + GlobalVariables.Comapny_ID;
        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "getSysAlarmSetttingbyUser.php?cUserId=" +GlobalVariables.Comapny_ID;
        } else if (GlobalVariables.role.equalsIgnoreCase("Dealer")) {
            url = GlobalVariables.IPAddress + "getSysAlarmSettingbyDealer.php?cUserId=" +GlobalVariables.Comapny_ID;
        }
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = 0;
                            try {
                                success = response.getInt("success");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("items");
                                JSONObject jobj = ja.getJSONObject(0);
                                int overspeed=jobj.getInt("overspeed");
                                int acc=jobj.getInt("acc");
                                int lowbattery=jobj.getInt("lowbattery");
                                int powercutoff=jobj.getInt("powercutoff");
                                int geofence=jobj.getInt("geofence");


                                if(overspeed>0)
                                {
                                    swt_overspeed.setClickable(true);
                                }else{

                                    swt_overspeed.setClickable(false);
                                }
                                if(acc>0)
                                {
                                    swt_acc.setClickable(true);
                                }else{

                                    swt_acc.setClickable(false);

                                }
                                if(lowbattery>0)
                                {
                                    swt_lowbattery.setClickable(true);
                                }else{

                                    swt_lowbattery.setClickable(false);
                                }
                                if(powercutoff>0)
                                {
                                    swt_powercutoff.setClickable(true);
                                }else{

                                    swt_powercutoff.setClickable(false);
                                }
                                if(geofence>0)
                                {
                                    swt_geofence.setClickable(true);
                                }else{

                                    swt_geofence.setClickable(false);
                                }
                                    *//*Intent in = new Intent(LoginScreen.this, Navigation.class);
                                   // in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);*//*

                                // pDialoge.dismiss();
                                //  Toast.makeText(LoginScreen.this, "Company" +GlobalVariables.Comapny_ID, Toast.LENGTH_SHORT ).show();



                            }
                            else if (success == 0)
                            {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);


    }*/
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private class CheckAlarmSwitchState extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"Alarmsts?UserId=" + GlobalVariables.userid;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990"
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                        JSONObject jobj = jsonArr.getJSONObject(0);
                        db_overspeed = jobj.getInt("nOverSpeed");
                        db_acc = jobj.getInt("nAcc");
                        db_lowbattery = jobj.getInt("nLowBattery");
                        db_powercutoff = jobj.getInt("nPowerCutoff");
                        db_geofence = jobj.getInt("nGeofence");



                } catch (final JSONException e) {


                }


            } else {


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(db_overspeed==1 && GlobalVariables.SystemOverspeed>0)
            {
                swt_overspeed.setChecked(true);
            }else{
                swt_overspeed.setChecked(false);
            }
            if(db_acc==1 && GlobalVariables.SystemAcc>0)
            {
                swt_acc.setChecked(true);
            }else{
                swt_acc.setChecked(false);
            }
            if(db_lowbattery==1 && GlobalVariables.SystemLowbattery>0)
            {
                swt_lowbattery.setChecked(true);
            }else{
                swt_lowbattery.setChecked(false);
            }
            if(db_powercutoff==1 && GlobalVariables.SystemPowercut>0)
            {
                swt_powercutoff.setChecked(true);
            }else{
                swt_powercutoff.setChecked(false);
            }
            if(db_geofence==1 && GlobalVariables.SystemGeofence>0)
            {
                swt_geofence.setChecked(true);
            }else{
                swt_geofence.setChecked(false);
            }
        }

    }
    private class GetSystemAlarmSetting extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"AlarmsSetting?UserId=" + GlobalVariables.userid+"&Role="+GlobalVariables.role;
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990"
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj

                    JSONObject jobj = jsonArr.getJSONObject(0);
                    overspeed=jobj.getInt("nOverSpeed");
                    acc=jobj.getInt("nAcc");
                    lowbattery=jobj.getInt("nLowBattery");
                    powercutoff=jobj.getInt("nPowerCutoff");
                    geofence=jobj.getInt("nGeofence");



                } catch (final JSONException e) {


                }


            } else {


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(overspeed>0)
            {
                swt_overspeed.setClickable(true);
            }else{

                swt_overspeed.setClickable(false);
            }
            if(acc>0)
            {
                swt_acc.setClickable(true);
            }else{

                swt_acc.setClickable(false);

            }
            if(lowbattery>0)
            {
                swt_lowbattery.setClickable(true);
            }else{

                swt_lowbattery.setClickable(false);
            }
            if(powercutoff>0)
            {
                swt_powercutoff.setClickable(true);
            }else{

                swt_powercutoff.setClickable(false);
            }
            if(geofence>0)
            {
                swt_geofence.setClickable(true);
            }else{

                swt_geofence.setClickable(false);
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
            String url = GlobalVariables.apiserverUrl+"UpdatetabAlarms?UserId="+GlobalVariables.userid+"&nOverSpeed="+overspeed_state+"&nAcc="+acc_state+"&nLowBattery="+lowbattery_state+"&nPowerCutoff="+powercutoff_state+"&nGeofence="+geofence_state;
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
                    alarmstatussavests = jsonObj.getString("Status");


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
            if(alarmstatussavests.equals("True")){
                Toast toast = Toast.makeText(AlertsActivity.this, "Alarms Set Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }
        }

    }
}