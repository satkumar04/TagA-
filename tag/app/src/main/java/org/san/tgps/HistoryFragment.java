package org.san.tgps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.Historybean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    GoogleMap map;
    Location mLastLocation;
    Button btnhistory;
    TextView txtdatetime, txtvehiclename;
    RadioGroup rb_date;
    long tmisec = 0, timeinminute = 0;

    Marker parkMarker, mMarker, spmarker,smarker;
    String TAG = "HistoryFragment";

    ProgressDialog pDialog;
    SupportMapFragment mapFragment;

    List<Historybean> historylist = new ArrayList<>();
    List<Historybean> markerList = new ArrayList<>();
    double tmpLattitude = 0, tmpLongitude = 0, latitude = 0, longitude = 0, strtlatitude = 0, strtlogitude = 0, endlatitude = 0, endlongitude = 0;

    Handler handlerhistory = new Handler();
    Runnable runnablehistory = null;
    String hstrtTime, hendTime, vehicle;
    String tempTime, presentTime;
    int timeinterval = 0;

    int strthrs = 0;
    int strtmin = 0;
    int strtsec = 59;
    public DateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        vehicle = getArguments().getString("vehicle");
        // map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.historymap)).getMap();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.historymap);
        mapFragment.getMapAsync(this);

        btnhistory = (Button) view.findViewById(R.id.btnhistory);
        txtdatetime = (TextView) view.findViewById(R.id.txtdatetime);
        txtvehiclename = (TextView) view.findViewById(R.id.txtvehiclename);

        txtvehiclename.setText(vehicle);
        btnhistory.setOnClickListener(this);
        rb_date = (RadioGroup) view.findViewById(R.id.rb_date);

        rb_date.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId == R.id.rb_custom) {

                    DialogFragment newFragment = new DateTimeDailog();
                    newFragment.show(getFragmentManager(), "DatePicker");
                }
            }
        });

        try {
            if (map != null) {

                // LatLng coordinate = new LatLng(latitude, longitude);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(GlobalVariables.gllatitude, GlobalVariables.gllogitude), 13.0f));
                //  map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalVariables.gllatitude,GlobalVariables.gllogitude) , 14.0f) );
                // map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            }
        } catch (Exception ex) {

            ex.getStackTrace();
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnhistory:
                String btnString = btnhistory.getText().toString().trim();

                if (btnString.equals("PLAY")) {

                    btnhistory.setText(getResources().getString(R.string.stop));
                    tmpLattitude = 0;
                    tmpLongitude = 0;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.stop);
                    btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                    btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.strogred));
                    // stopTimer();
                    map.clear();
                    if (parkMarker != null) {
                        parkMarker.remove();
                    }

                    int selectedId = rb_date.getCheckedRadioButtonId();
                    if (selectedId == R.id.rb_today) {
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        /*Calendar c = Calendar.getInstance();
                        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                        String formattedDate1 = df1.format(c.getTime());
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        String url = GlobalVariables.IPAddress + "gethistorydata.php?imei=" + GlobalVariables.imei + "&fromdate=" + df2.format(c.getTime()) + "_00:00:59"
                                + "&todate=" + formattedDate1;
                        gethistoryjsondata(url);*/

                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                        Calendar caltd1 = Calendar.getInstance();
                        caltd1.set(caltd1.get(Calendar.YEAR), caltd1.get(Calendar.MONTH), caltd1.get(Calendar.DAY_OF_MONTH), strthrs, strtmin, strtsec);

                        Calendar caltd2 = Calendar.getInstance();
                        caltd2.setTime(caltd1.getTime());
                        caltd2.add(Calendar.HOUR,23);
                        caltd2.add(Calendar.MINUTE,59);
                        String imeiString="imei_";
                        String url1 = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei + "&fromdate=" + dateFormat1.format(caltd1.getTime()) + "&todate=" + dateFormat1.format(caltd2.getTime());
                        // appendLog(url1);
                        geTodayhistoryjsondata(url1, caltd1, caltd2);
                        /*Toast toast = Toast.makeText(getActivity(), "Loading Vehicle history.....", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                        v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                        v1.setTextSize(15);
                        //v.setBackgroundColor(0xFFFFFFFF);
                        toast.show();*/

                    } else if (selectedId == R.id.rb_yestrday) {

                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);*/
                      /*  LatLng coordinatemark = new LatLng(colatitude, colongitude);

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parking);
                        MarkerOptions markerOptions = new MarkerOptions().position(coordinatemark).icon(icon).anchor(0.5f, 0.5f);

                        spmarker = map.addMarker(markerOptions);
                      */  DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                        Calendar cal1 = Calendar.getInstance();
                        cal1.add(Calendar.DATE, -1);
                        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), strthrs, strtmin, strtsec);

                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(cal1.getTime());
                        cal2.add(Calendar.HOUR, 23);
                        cal2.add(Calendar.MINUTE, 59);
                        String imeiString="imei_";
                        String url = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei+ "&fromdate=" + dateFormat1.format(cal1.getTime()) + "&todate=" + dateFormat1.format(cal2.getTime());
                        // appendLog(url);
                        //gethistoryjsondata(url);
                        getyestrdayhistoryjsondata(url, cal1, cal2);
                        /*Toast toast = Toast.makeText(getActivity(), "Loading Vehicle history.....", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                        v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                        v1.setTextSize(15);
                        //v.setBackgroundColor(0xFFFFFFFF);
                        toast.show();*/
                        // Toast.makeText(getActivity(), "Over", Toast.LENGTH_SHORT).show();

                    } else if (selectedId == R.id.rb_custom) {

                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        String imeiString="imei_";
                        String url = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei +"&fromdate=" + GlobalVariables.cstmFrmdateTime + "&todate=" + GlobalVariables.cstmTodateTime;
                        gethistoryjsondata(url);

                    } else if (btnString.equals(getResources().getString(R.string.stop))) {
                        historylist.clear();
                        tmpLattitude = 0;
                        tmpLongitude = 0;
                        btnhistory.setText(getResources().getString(R.string.play));
                        handlerhistory.removeCallbacks(runnablehistory);
                    }

                } else if (btnString.equals(getResources().getString(R.string.stop))) {

                    historylist.clear();
                    tmpLattitude = 0;
                    tmpLongitude = 0;

                    Drawable img = getContext().getResources().getDrawable(R.drawable.play);
                    btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                    btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.verydark));
                    btnhistory.setText(getResources().getString(R.string.play));
                    handlerhistory.removeCallbacks(runnablehistory);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void gethistoryjsondata(String url) {

        historylist.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Vehicle history.....");
        pDialog.setCancelable(false);
        pDialog.show();
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
                                for (int i = 0; i < ja.length(); i++) {
                                    Historybean historybean = new Historybean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    historybean.setH_lattitude(jobj.getString("Lattitude"));
                                    historybean.setH_longitude(jobj.getString("Longitude"));
                                    historybean.setH_ddegree(jobj.getString("ddegree"));
                                    historybean.setH_cdate(jobj.getString("cdate"));
                                    historylist.add(historybean);
                                }

                                if (historylist.size() == 0) {
                                    showAlertDialog(getActivity(), "No History found", "History Not Available", false);
                                    Drawable img = getContext().getResources().getDrawable(R.drawable.play);
                                    btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                                    btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.verydark));
                                    btnhistory.setText(getResources().getString(R.string.play));
                                } else {
                                    try {
                                        hstrtTime = historylist.get(0).getH_cdate();
                                        hendTime = historylist.get(historylist.size() - 1).getH_cdate();

                                        latitude = Double.parseDouble(historylist.get(0).getH_lattitude());
                                        longitude = Double.parseDouble(historylist.get(0).getH_longitude());
                                        float ddegree = Float.valueOf(historylist.get(0).getH_ddegree());

                                        LatLng coordinate = new LatLng(latitude, longitude);
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(latitude, longitude), 15.0f));

                                        historydata();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pDialog.dismiss();
                            } else if (success == 0) {
                                //Toast.makeText(getActivity(), "No History found...", Toast.LENGTH_SHORT).show();

                                Toast toast = Toast.makeText(getActivity(), "No History found...", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                                v1.setTextSize(15);
                                //v.setBackgroundColor(0xFFFFFFFF);
                                toast.show();
                                pDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                showAlertDialog2(getActivity(), "Something went wrong", "Please try after some time", false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                tmpLattitude = 0;
                tmpLongitude = 0;
                historylist.clear();

                Drawable img = getContext().getResources().getDrawable(R.drawable.play);

                btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.verydark));
                btnhistory.setText(getResources().getString(R.string.play));

                handlerhistory.removeCallbacks(runnablehistory);


                if (markerList.size() != 0) {
                    for (int i = 0; i < markerList.size(); i++) {
                        long hours = Integer.parseInt(markerList.get(i).getpTime()) / 60; //since both are ints, you get an int
                        long minutes = Integer.parseInt(markerList.get(i).getpTime()) % 60;
                        Log.d(TAG, "Time " + hours + " : " + minutes);

                        double colatitude = Double.parseDouble(markerList.get(i).getH_lattitude());
                        double colongitude = Double.parseDouble(markerList.get(i).getH_longitude());

                        LatLng coordinatemark = new LatLng(colatitude, colongitude);

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parking);
                        MarkerOptions markerOptions = new MarkerOptions().position(coordinatemark).icon(icon).anchor(0.5f, 0.5f);

                        spmarker = map.addMarker(markerOptions);
                    }
                }
            }
        });

        alertDialog.show();
    }

    public void showAlertDialog2(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        alertDialog.show();
    }

    private void historydata() throws InterruptedException {

        tmpLattitude = 0;
        tmpLongitude = 0;
        if (map != null) {
            map.clear();
        }

        handlerhistory = new Handler();
        runnablehistory = new Runnable() {
            int i = 0;

            @Override
            public void run() {

                if (historylist.size() == 0) {

                    handlerhistory.removeCallbacks(this);

                } else if (i == historylist.size()) {
                    handlerhistory.removeCallbacks(this);
                    showAlertDialog(getActivity(), "History over", "History From " + hstrtTime + "\n" + " To " + hendTime, false);

                    historylist.clear();
                } else if (i <= historylist.size()) {

                    strtlatitude = Double.parseDouble(historylist.get(0).getH_lattitude());
                    strtlogitude = Double.parseDouble(historylist.get(0).getH_longitude());
                    LatLng coordinate1 = new LatLng(strtlatitude, strtlogitude);
                    BitmapDescriptor icon1= BitmapDescriptorFactory.fromResource(R.drawable.maps);
                    MarkerOptions markerOptions1 = new MarkerOptions().position(coordinate1).icon(icon1);
                    smarker = map.addMarker(markerOptions1);
                    endlatitude = Double.parseDouble(historylist.get(historylist.size() - 1).getH_lattitude());
                    endlongitude = Double.parseDouble(historylist.get(historylist.size() - 1).getH_longitude());
                    /*LatLng coordinate2 = new LatLng(endlatitude, endlongitude);
                    BitmapDescriptor icon2= BitmapDescriptorFactory.fromResource(R.drawable.end);
                    MarkerOptions markerOptions2 = new MarkerOptions().position(coordinate2).icon(icon2);
                    smarker = map.addMarker(markerOptions2);*/
                    latitude = Double.parseDouble(historylist.get(i).getH_lattitude());
                    longitude = Double.parseDouble(historylist.get(i).getH_longitude());
                    float ddegree = Float.valueOf(historylist.get(i).getH_ddegree());
                    LatLng coordinate = new LatLng(latitude, longitude);

                    txtdatetime.setText("" + historylist.get(i).getH_cdate());
                    if (mMarker != null) {
                        mMarker.remove();
                    }

                    Location tmpploca = new Location("TEMP");
                    tmpploca.setLatitude(tmpLattitude);
                    tmpploca.setLongitude(tmpLongitude);

                    Location loccurrent = new Location("Current");
                    loccurrent.setLatitude(latitude);
                    loccurrent.setLongitude(longitude);

                    double distancefirst = tmpploca.distanceTo(loccurrent);
                    double distance = distancefirst / 1000;
                    timeinterval = 250;
                    if (distance < 0.001) {

                        timeinterval = 20;

                        Date currentdt, tmpdt;
                        presentTime = historylist.get(i).getH_cdate();
                        if (i == 0) {

                        } else {
                            tempTime = historylist.get(i - 1).getH_cdate();

                            SimpleDateFormat smplformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            try {
                                currentdt = smplformat.parse(presentTime);
                                //  if(!tempTime.equals("") || tempTime != null){
                                tmpdt = smplformat.parse(tempTime);
                                long diff = currentdt.getTime() - tmpdt.getTime();
                                long diffSeconds = diff / 1000 % 60;
                                tmisec = tmisec + diffSeconds;

                                timeinminute = timeinminute + (diff / (60 * 1000) % 60);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        //take time difference, if time more than 2 minute then show parking
                        if (parkMarker != null) {
                            // map.clear();
                            parkMarker.remove();
                        }
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.parking);
                        MarkerOptions markerOptions = new MarkerOptions().position(coordinate).icon(icon);
                        parkMarker = map.addMarker(markerOptions);
                    } else {
                        if (timeinminute >= 5) {
                            Historybean markerbean = new Historybean();
                            markerbean.setH_lattitude(String.valueOf(tmpLattitude));
                            markerbean.setH_longitude(String.valueOf(tmpLongitude));
                            markerbean.setH_cdate(historylist.get(i).getH_cdate());
                            markerbean.setpTime(String.valueOf(timeinminute));
                            markerList.add(markerbean);
                        }
                        tmisec = 0;
                        timeinminute = 0;

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.positionindicator);
                        MarkerOptions markerOptions = new MarkerOptions().position(coordinate).icon(icon).anchor(0.5f, 0.5f).rotation(ddegree);

                        mMarker = map.addMarker(markerOptions);

                        try {
                            if (tmpLattitude != 0 || tmpLongitude != 0) {
                                map.addPolyline(new PolylineOptions()
                                        .add(new LatLng(tmpLattitude, tmpLongitude), new LatLng(latitude, longitude))
                                        .geodesic(true)
                                        .width(10).color(Color.RED));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    map.getUiSettings().setMyLocationButtonEnabled(false);

                    LatLng locationhistory = new LatLng(latitude, longitude);
                    map.animateCamera(CameraUpdateFactory.newLatLng(locationhistory));
                    // map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 14.0f) );
                    tmpLattitude = latitude;
                    tmpLongitude = longitude;

                } else {
                    handlerhistory.removeCallbacks(this);
                }
                i++;
                handlerhistory.postDelayed(this, timeinterval);
            }
        };
        handlerhistory.postDelayed(runnablehistory, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        handlerhistory.removeCallbacks(runnablehistory);
    }

    @Override
    public void onPause() {
        super.onPause();
        handlerhistory.removeCallbacks(runnablehistory);
    }

    private void getyestrdayhistoryjsondata(String url, final Calendar cal1, final Calendar cal2) {

        // historylist.clear();
         pDialog = new ProgressDialog(getActivity());
         pDialog.setMessage("Loading Vehicle history.....");
         pDialog.setCancelable(false);
         pDialog.show();
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
                                for (int i = 0; i < ja.length(); i++) {
                                    Historybean historybean = new Historybean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    historybean.setH_lattitude(jobj.getString("Lattitude"));
                                    historybean.setH_longitude(jobj.getString("Longitude"));
                                    historybean.setH_ddegree(jobj.getString("ddegree"));
                                    historybean.setH_cdate(jobj.getString("cdate"));
                                    historylist.add(historybean);

                                    // appendLog(jobj.getString("Lattitude") + " " + jobj.getString("Longitude") + " " + jobj.getString("cdate"));
                                }
                                showmap();
                                  pDialog.dismiss();


                               /* DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);

                                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());

                                if (dateFormat1.format(cal1.getTime()).equals(dateFormat.format(cal.getTime()) + "_23:00:59")) {
                                    // Toast.makeText(getActivity(), "Time Over...", Toast.LENGTH_SHORT).show();

                                    showmap();
                                } else {

                                    cal1.setTime(cal1.getTime());
                                    cal1.add(Calendar.HOUR, 1);

                                    cal2.setTime(cal1.getTime());
                                    cal2.add(Calendar.HOUR, 1);
                                    String imeiString="imei_";
                                    String rpturl = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei+  "&fromdate=" + dateFormat1.format(cal1.getTime()) + "&todate=" + dateFormat1.format(cal2.getTime());
                                    getyestrdayhistoryjsondata(rpturl, cal1, cal2);
                                }*/

                            } else if (success == 0) {
                                // Toast.makeText(getActivity(), "No History found...", Toast.LENGTH_SHORT).show();
                                 pDialog.dismiss();
                                showmap();

                                /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, -1);

                                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());

                                if (dateFormat1.format(cal1.getTime()).equals(dateFormat.format(cal.getTime()) + "_23:00:59")) {
                                    showmap();
                                } else {

                                    cal1.setTime(cal1.getTime());
                                    cal1.add(Calendar.HOUR, 1);

                                    cal2.setTime(cal1.getTime());
                                    cal2.add(Calendar.HOUR, 1);
                                    String imeiString="imei_";
                                    String rpturl = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei+ "&fromdate=" + dateFormat1.format(cal1.getTime()) + "&todate=" + dateFormat1.format(cal2.getTime());
                                    getyestrdayhistoryjsondata(rpturl, cal1, cal2);
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                showAlertDialog2(getActivity(), "Something went wrong", "Please try after some time", false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }


    private void showmap() {
        if (historylist.size() == 0) {
           // showAlertDialog(getActivity(), "No History found", "History Not Available", false);
             Toast toast = Toast.makeText(getActivity(), "No History found", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                                v1.setTextSize(15);
                                //v.setBackgroundColor(0xFFFFFFFF);
                                toast.show();

            Drawable img = getContext().getResources().getDrawable(R.drawable.play);
            btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

            btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.verydark));
            btnhistory.setText(getResources().getString(R.string.play));
        } else {
            try {
                hstrtTime = historylist.get(0).getH_cdate();
                hendTime = historylist.get(historylist.size() - 1).getH_cdate();

                latitude = Double.parseDouble(historylist.get(0).getH_lattitude());
                longitude = Double.parseDouble(historylist.get(0).getH_longitude());
                float ddegree = Float.valueOf(historylist.get(0).getH_ddegree());

                LatLng coordinate = new LatLng(latitude, longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latitude, longitude), 15.0f));
                //  pDialog.dismiss();

                historydata();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLog(String text) {
        File logFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/trackagps.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append("\n" + text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void geTodayhistoryjsondata(String url, final Calendar caltd1, final Calendar caltd2) {

        historylist.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Vehicle history.....");
        pDialog.setCancelable(false);
        pDialog.show();
        /*Toast toast = Toast.makeText(getActivity(), "Loading Vehicle history.....", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
        v1.setTextColor(Color.GREEN);
        v1.setTextSize(30);
        //v.setBackgroundColor(0xFFFFFFFF);
        toast.show();*/

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

                                //  Toast.makeText(getActivity().getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();

                                JSONArray ja = response.getJSONArray("items");
                                for (int i = 0; i < ja.length(); i++) {
                                    Historybean historybean = new Historybean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    historybean.setH_lattitude(jobj.getString("Lattitude"));
                                    historybean.setH_longitude(jobj.getString("Longitude"));
                                    historybean.setH_ddegree(jobj.getString("ddegree"));
                                    historybean.setH_cdate(jobj.getString("cdate"));
                                    historylist.add(historybean);

                                }
                                showmap();
                                  pDialog.dismiss();

                               /* Calendar cal = Calendar.getInstance();
                                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());

                                if (dateFormat1.format(caltd2.getTime()).equals(dateFormat1.format(cal.getTime()))) {
                                    showmap();
                                } else if (caltd2.getTime().compareTo(cal.getTime()) > 0) {
                                    showmap();
                                } else if (caltd2.getTime().compareTo(cal.getTime()) == 0) {
                                    showmap();
                                } else {

                                    caltd1.setTime(caltd1.getTime());
                                    caltd1.add(Calendar.HOUR, 1);

                                    caltd2.setTime(caltd1.getTime());
                                    caltd2.add(Calendar.HOUR, 10);
                                    String imeiString="imei_";
                                    String rpturl = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei+ "&fromdate=" + dateFormat1.format(caltd1.getTime()) + "&todate=" + dateFormat1.format(caltd2.getTime());
                                    geTodayhistoryjsondata(rpturl, caltd1, caltd2);
                                }*/

                            } else if (success == 0) {
                                pDialog.dismiss();
                                showmap();
                                /*Toast toast = Toast.makeText(getActivity(), "Today History Not Found", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                                v1.setTextSize(15);
                                //v.setBackgroundColor(0xFFFFFFFF);
                                toast.show();
                                Drawable img = getContext().getResources().getDrawable(R.drawable.play);
                                btnhistory.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                                btnhistory.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.verydark));
                                btnhistory.setText(getResources().getString(R.string.play));
*/

                             /*   DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                                Calendar cal = Calendar.getInstance();

                                if (dateFormat1.format(caltd2.getTime()).equals(dateFormat1.format(cal.getTime()))) {
                                    showmap();
                                } else if (caltd2.getTime().compareTo(cal.getTime()) > 0) {
                                    showmap();
                                } else if (caltd2.getTime().compareTo(cal.getTime()) == 0) {
                                    showmap();
                                } else {

                                    caltd1.setTime(caltd1.getTime());
                                    caltd1.add(Calendar.HOUR, 1);

                                    caltd2.setTime(caltd1.getTime());
                                    caltd2.add(Calendar.HOUR, 23);
                                    String imeiString="imei_";
                                    String rpturl = GlobalVariables.IPAddress + "gethistorydata.php?DeviceId=" + GlobalVariables.DeviceId +"&tableimie="+imeiString+GlobalVariables.imei+ "&fromdate=" + dateFormat1.format(caltd1.getTime()) + "&todate=" + dateFormat1.format(caltd2.getTime());
                                    geTodayhistoryjsondata(rpturl, caltd1, caltd2);
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                showAlertDialog2(getActivity(), "Something went wrong", "Please try after some time", false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }

}
