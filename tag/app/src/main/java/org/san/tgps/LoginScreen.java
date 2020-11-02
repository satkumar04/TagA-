package org.san.tgps;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
//import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.san.tgps.app.AppController;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;




public class LoginScreen extends AppCompatActivity {

    int running=0;
    int online=0;
    int offline = 0;

    String imei;
    String speed;
    String regno;
    private String TAG = LoginScreen.class.getSimpleName();

    EditText et_username, et_password;
    Button btnlogin;
    CheckBox chkShowPswd;
    ProgressDialog pDialoge;
    Handler handler;
    private String passwordhash,username,password;

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private boolean bound = false;

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    int dbsos,dboverspeed,dbacc,dblowbattery,dbpowercutoff,dbgeofence;
    Runnable runnablelive = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);
        GlobalVariables.TodayDate=formattedDate;

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String formattedDate1 = df1.format(c);
        GlobalVariables.FromDate=formattedDate1;



       // boolean result = verifyPassword("AL09HpS8X96sH4rQjeBqrZJ4Daw+Fr4yFdLjNRLNlFZsrjxsvoRGTlICO0wnBg5N7Q==", "mypass");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gethandles();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            et_username.setText(loginPreferences.getString("username", ""));
            et_password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        chkShowPswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    et_password.setInputType(129);
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_username.getWindowToken(), 0);

                username = et_username.getText().toString();
                password = et_password.getText().toString();

                if (saveLoginCheckBox.isChecked())
                {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

               // new getpasword()hash().execute();
                executeAsyncTask();
            }
        });
    }
    /*public void getpaswordhash() {
        if (et_username.getText().length() == 0 || et_password.getText().length() == 0) {
            getToast("Field cannot be empty");
        } else {

            pDialoge = new ProgressDialog(LoginScreen.this);
            pDialoge.setMessage("Getting.....");
            pDialoge.setCancelable(false);
            pDialoge.show();

            username = et_username.getText().toString().trim();
            username = username.replaceAll(" ", "%20");
            password = et_password.getText().toString().trim();
            password = password.replaceAll(" ", "%20");

            String usercheckUrl = GlobalVariables.IPAddress + "getpasswordhash.php?UserName=" + username;
            GlobalVariables.user = username;
            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, usercheckUrl, null,
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
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jobj = ja.getJSONObject(i);
                                        String passwordhash = jobj.getString("PasswordHash");

                                        boolean result = verifyPassword(passwordhash, password);
                                        if(result==true){
                                            Login();
                                            break;
                                        }
                                        else{
                                            pDialoge.dismiss();
                                            getToast("User does not exist." + "\n" + " Please try with valid user name and password");
                                        }
                                        boolean test = result;

                                    }

                                } else if (success == 0) {
                                    //alertbox("User not exist", "Please check your Credentials");
                                    getToast("User does not exist." + "\n" + " Please try with valid user name and password");

                                    pDialoge.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    getToast("Might be Server down. So, Please try after some time");
                    pDialoge.dismiss();

                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jreq);
        }

    }*/

   /* public void Login() {

            String usercheckUrl = GlobalVariables.IPAddress + "userlogin.php?UserName=" + username;
            GlobalVariables.user=username;
            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, usercheckUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int success = 0;
                                try {
                                    success = response.getInt("success");
                                   // et_username.setText("");
                                   // et_password.setText("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (success == 1) {
                                    JSONArray ja = response.getJSONArray("items");

                                    JSONObject jobj = ja.getJSONObject(0);
                                    String comid = jobj.getString("Id");
                                    String user = jobj.getString("UserName");
                                    String role = jobj.getString("role");
                                    GlobalVariables.Comapny_ID = comid;
                                    GlobalVariables.role = role;
                                    GlobalVariables.username = user;
                                    GlobalVariables.userid=comid;

                                    getdbalarmstatus();
                                    startTimer();
                                    getData();
                                    getSysytemAlarmSetting();


                                    // speedcheck();
                                 //   handler = new Handler();

                                    ///final Runnable r = new Runnable() {
                                      //  public void run() {

                                         //  speedcheck();
                                         //   handler.postDelayed(this, 870000);
                                       // }
                                    //};

                                  //  handler.postDelayed(r, 17400);
                                    //  Toast.makeText(LoginScreen.this, "Company" +GlobalVariables.Comapny_ID, Toast.LENGTH_SHORT ).show();

                                    remmeberMe(user, comid, role);
                                    //pDialoge.dismiss();



                                } else if (success == 0) {
                                    //alertbox("User not exist", "Please check your Credentials");
                                    getToast("User does not exist." + "\n" + " Please try with valid user name and password");
                                    pDialoge.dismiss();



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    getToast("Might be Server down. So, Please try after some time");
                    pDialoge.dismiss();

                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jreq);
    }*/
 /*   public void getdbalarmstatus() {

        String usercheckUrl = GlobalVariables.IPAddress + "getdbalarmstatus.php?cUserId=" + GlobalVariables.userid;
        GlobalVariables.user=username;
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, usercheckUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = 0;
                            try {
                                success = response.getInt("success");
                                // et_username.setText("");
                                // et_password.setText("");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("items");

                                JSONObject jobj = ja.getJSONObject(0);
                                dbsos = jobj.getInt("nSos");
                                dboverspeed = jobj.getInt("nOverSpeed");
                                dbacc = jobj.getInt("nAcc");
                                dblowbattery = jobj.getInt("nLowBattery");
                                dbpowercutoff= jobj.getInt("nPowerCutoff");
                                dbgeofence = jobj.getInt("nGeofence");

                                GlobalVariables.db_sosState=dbsos;
                                GlobalVariables.db_overspeedState=dboverspeed;
                                GlobalVariables.db_accState=dbacc;
                                GlobalVariables.db_lowbatteryState=dblowbattery;
                                GlobalVariables.db_powercutState=dbpowercutoff;
                                GlobalVariables.db_geofenceState=dbgeofence;


                            } else if (success == 0) {


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  getToast("Might be Server down. So, Please try after some time");
                pDialoge.dismiss();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }*/

  /*  public  void  getData()
    {
       // String usercheckUrl =GlobalVariables.IPAddress + "vehiclecountbyuser.php?cUserId=" + GlobalVariables.Comapny_ID;
        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "vehiclecountbyuser.php?cUserId=" + GlobalVariables.Comapny_ID;
        } else if (GlobalVariables.role.equalsIgnoreCase("Dealer")) {
            url = GlobalVariables.IPAddress + "vehiclecountbydealer.php?cUserId=" + GlobalVariables.Comapny_ID;
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
                                  offline = jobj.getInt("offline");
                                    online = jobj.getInt("online");
                                    running = jobj.getInt("running");
                                    GlobalVariables.online=online;
                                    GlobalVariables.offline=offline;
                                    GlobalVariables.running=running;




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
                  //  getToast("Might be Server down. So, Please try after some time");
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jreq);


    }*/
    /*public  void  getSysytemAlarmSetting()
    {
        // String usercheckUrl =GlobalVariables.IPAddress + "vehiclecountbyuser.php?cUserId=" + GlobalVariables.Comapny_ID;
        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "getSysAlarmSetttingbyUser.php?cUserId=" +GlobalVariables.Comapny_ID;
        } else if (GlobalVariables.role.equalsIgnoreCase("Dealer")) {
            url = GlobalVariables.IPAddress + "getSysAlarmSettingbyDealer.php?cUserId="  +GlobalVariables.Comapny_ID;
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

                                GlobalVariables.Systemsos=1;
                                GlobalVariables.SystemOverspeed=overspeed;
                                GlobalVariables.SystemAcc=acc;
                                GlobalVariables.SystemLowbattery=lowbattery;
                                GlobalVariables.SystemPowercut=powercutoff;

                                GlobalVariables.SystemGeofence=geofence;
                                Intent in = new Intent(LoginScreen.this, Navigation.class);
                                // in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);


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
                getToast("Might be Server down. So, Please try after some time");
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);


    }*/


    private void gethandles() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        chkShowPswd = (CheckBox) findViewById(R.id.chkShowPswd);
    }

    public void remmeberMe(String username, String comp_id, String role) {
        getSharedPreferences("logindetails", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                //.putString("password", password)
                .putString("comp_id", comp_id)
                .putString("role", role)
                .apply();

    }

    private void getToast(String message) {
        Context context = getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.customtoast, null);
        TextView myMessage = (TextView) toastRoot.findViewById(R.id.txtvdisplay);
        myMessage.setText(message);
        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }


    private boolean verifyPassword(String hashedPass, String password){
        if (hashedPass == null){
            return false;
        }

        byte[] numArray = Base64.decode(hashedPass.getBytes(), Base64.DEFAULT);
        if (numArray.length != 49 || (int) numArray[0] != 0){
            return false;
        }

        byte[] salt = new byte[16];
        System.arraycopy(numArray, 1, salt, 0, 16);

        byte[] a = new byte[32];
        System.arraycopy(numArray, 17, a, 0, 32);

        byte[] b = generateHash(password, salt);

        byte[] f=b;

        return Arrays.equals(a, b);

    }

    /**
     * Generates a hash using PBKDF2WithHmacSHA1 algorithm
     * @param password
     * @param salt
     * @return hash as byte array
     */
    private byte[] generateHash(String password, byte[] salt) {


        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 1000, 256);
        SecretKey secretKey = null;
        try {
            secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey.getEncoded();
    }
    private void startTimer() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                executeDBAlarmstsTask();
                //new GetDBAlarmsts().execute();
               // getdbalarmstatus();
               // getData();

               // getData();
                //  livepositiontrack();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnablelive, 10000);
    }

    public void executeAsyncTask() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
                if (et_username.getText().length() == 0 || et_password.getText().length() == 0) {
                    // getToast("Field cannot be empty");
                } else {

                    pDialoge = new ProgressDialog(LoginScreen.this);
                    pDialoge.setMessage("Getting.....");
                    pDialoge.setCancelable(false);
                    pDialoge.show();

                    username = et_username.getText().toString().trim();
                    username = username.replaceAll(" ", "%20");
                    password = et_password.getText().toString().trim();
                    password = password.replaceAll(" ", "%20");
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                startService(new Intent(LoginScreen.this, AlarmServices.class));

                Log.v("AsyncTask", "doInBackground");
                HttpHandler sh = new HttpHandler();
                String url = GlobalVariables.apiserverUrl+"PassWord?UserName="+username;
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
                        JSONObject jsonObj1 = jsonArr.getJSONObject(0);
                        System.out.println(jsonObj1);
                        passwordhash=jsonObj1.getString("PasswordHash");
                        // mobileno=jsonObj1.getString("MobileNo");



                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Might be Server down. So, Please try after some time",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                return null;
            }
            @Override
            protected void onPostExecute(String msg) {
                super.onPostExecute(msg);
                Log.v("AsyncTask", "onPostExecute");
                GlobalVariables.user = username;
           /* if (result == null) {
            }
            else{
           */
           /* if (password == "") {
                getToast("Field cannot be empty");
            }else{*/
                if (et_username.getText().length() == 0 || et_password.getText().length() == 0){
                    getToast("Field cannot be empty");
                }
                else {
                    boolean verified = verifyPassword(passwordhash, password);
                    if (verified == true) {
                        //Login();
                        executeLogin();
                        // break;
                    } else if (et_username.getText().length() == 0 || et_password.getText().length() == 0) {
                        getToast("Field cannot be empty");
                    } else {
                        // pDialoge.dismiss();
                        getToast("User does not exist." + "\n" + " Please try with valid user name and password");
                        pDialoge.dismiss();
                    }
                }
                // }
                // }

            }
        };

        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

    }


    public void executeLogin() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
            }

            @Override
            protected String doInBackground(Void... params) {
                Log.v("AsyncTask", "doInBackground");
                HttpHandler sh = new HttpHandler();
                String url = GlobalVariables.apiserverUrl+"UserLogin?UserName="+username;
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
                        JSONObject jsonObj1 = jsonArr.getJSONObject(0);
                        String comid=jsonObj1.getString("Id");
                        String user=jsonObj1.getString("UserName");
                        String role=jsonObj1.getString("Name");

                        GlobalVariables.Comapny_ID = comid;
                        GlobalVariables.role = role;
                        GlobalVariables.username = user;
                        GlobalVariables.userid=comid;
                        SharedPreferences sp=getSharedPreferences("SanTag",Context.MODE_PRIVATE);
                        //String sharedimei=sp.getString(imei,"0");
                        //sp.edit().putString("imei",imei).commit();
                        sp.edit().putString("UserId",comid).commit();
                        sp.edit().putString("UserRole",role).commit();


                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Might be Server down. So, Please try after some time",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                return null;
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.v("AsyncTask", "onPostExecute");
                super.onPostExecute(msg);
               // new GetDBAlarmsts().execute();
                executeDBAlarmstsTask();
                //new GetVehicleCount().execute();
                executeVehicleCountTask();
                startTimer();
                executeSystemAlarmSetting();
                //new GetSystemAlarmSetting().execute();
            }
        };

        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }


    public void executeDBAlarmstsTask()
    {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
            }

            @Override
            protected String doInBackground(Void... params) {
                Log.v("AsyncTask", "doInBackground");
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
                        dbsos = jsonObj.getInt("nSos");
                        dboverspeed = jsonObj.getInt("nOverSpeed");
                        dbacc = jsonObj.getInt("nAcc");
                        dblowbattery = jsonObj.getInt("nLowBattery");
                        dbpowercutoff= jsonObj.getInt("nPowerCutoff");
                        dbgeofence = jsonObj.getInt("nGeofence");

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
            protected void onPostExecute(String msg) {
                Log.v("AsyncTask", "onPostExecute");
                super.onPostExecute(msg);

                if (msg == null) {
                }
            }
        };

        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }


    public void executeVehicleCountTask()
    {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
            }

            @Override
            protected String doInBackground(Void... params) {
                Log.v("AsyncTask", "doInBackground");
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
                        offline = jsonObj.getInt("offline");
                        online = jsonObj.getInt("online");
                        running = jsonObj.getInt("running");
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
            protected void onPostExecute(String msg) {
                Log.v("AsyncTask", "onPostExecute");

                if (msg == null) {
                }
            }
        };

        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }


    public void executeSystemAlarmSetting()
    {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
            }

            @Override
            protected String doInBackground(Void... params) {
                Log.v("AsyncTask", "doInBackground");
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

                        GlobalVariables.SystemGeofence=geofence;
                        Intent in = new Intent(LoginScreen.this, Navigation.class);
                        // in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);


                    } catch (final JSONException e) {


                    }
                } else {


                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                Log.v("AsyncTask", "onPostExecute");
                super.onPostExecute(result);

                if (result == null) {
                }
            }
        };

        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }


    public void onBackPressed() {

      /*  try {

            AlertDialog alertDialog = new AlertDialog.Builder(LoginScreen.this).create();
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are You sure Want to Close the App");
            alertDialog.setIcon(R.drawable.warning);
            alertDialog.setButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    LoginScreen.this.finish();
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

        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

