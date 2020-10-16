package org.san.tgps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.san.tgps.adapter.LowBatteryAlarmAdapter;
import org.san.tgps.adapter.PowerCutAlarmAdapter;

import org.san.tgps.app.AppController;
import org.san.tgps.bean.AlarmsBean;

import org.san.tgps.utils.GlobalVariables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PowerCutAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PowerCutAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PowerCutAlarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = PowerCutAlarmFragment.class.getSimpleName();
    private List<Address> powercutalarmAddress;
    String address;

    Geocoder geocoder;
    ProgressDialog progress;

    private PowerCutAlarmFragment.OnFragmentInteractionListener mListener;

    List<AlarmsBean> powercutalarmList = new ArrayList<>();

    // List<VehiclesBean> onlinedvcList = new ArrayList<>();
    RecyclerView rv_powercutalarms;
    SwipeRefreshLayout sr_powercutalarms;
    //  EditText et_searchvehicle;
    String strsearch;
    PowerCutAlarmAdapter powercutadapter;

    public PowerCutAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PowerCutAlarmFragment.
     */
    public static PowerCutAlarmFragment newInstance(String param1, String param2) {
        PowerCutAlarmFragment fragment = new PowerCutAlarmFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_power_cut_alarm, container, false);

        rv_powercutalarms = (RecyclerView) view.findViewById(R.id.rv_powercutalarms);
        sr_powercutalarms = (SwipeRefreshLayout) view.findViewById(R.id.sr_powercutalarms);
        //et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_powercutalarms.setLayoutManager(llm);

        sr_powercutalarms.setOnRefreshListener(this);

        sr_powercutalarms.post(new Runnable() {
            @Override
            public void run() {
               // sr_powercutalarms.setRefreshing(true);
                new GetPowerCutAlarmList().execute();
                //getPowerCutAlarms();
            }
        });

       /* et_searchvehicle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                et_searchvehicle.setCursorVisible(true);

                return false;
            }
        });*/
/*
        et_searchvehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                strsearch = editable.toString().toUpperCase();

                final List<VehiclesBean> fltrdVehicleList = new ArrayList<>();
                for (int i = 0; i < onlineDvcList.size(); i++) {
                    final String text = onlineDvcList.get(i).getDevice_Name().toUpperCase();
                    if (text.startsWith(strsearch)) {
                        fltrdVehicleList.add(onlineDvcList.get(i));
                    }
                }

                rv_onlinevehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                onlineadapter = new PowerCutAlarmadapter(getActivity(), fltrdVehicleList, strsearch);
                rv_onlinevehicles.setAdapter(onlineadapter);
                onlineadapter.notifyDataSetChanged();
            }
        });*/
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
        if (context instanceof PowerCutAlarmFragment.OnFragmentInteractionListener) {
            mListener = (PowerCutAlarmFragment.OnFragmentInteractionListener) context;
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
    public void onRefresh() {
        new GetPowerCutAlarmList().execute();
        //getPowerCutAlarms();
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



    private class GetPowerCutAlarmList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            // progress.setTitle("Loading");
            progress.setMessage("PowerCut Alarms "+"\n"+"Loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            powercutalarmList.clear();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"FetchAlarms?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&AlarmType=powerCut";
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990"
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AlarmsBean powercutalarmitem = new AlarmsBean();
                        JSONObject jobj = jsonArr.getJSONObject(i);
                        Double Lattitude = jobj.getDouble("Lattitude");
                        Double Longitude = jobj.getDouble("Longitude");
                        //getAddressFromLocation(Lattitude, Longitude, new GeocoderHandler());
                        String date = jobj.getString("dtDate");
                        String time = jobj.getString("dtTime");
                        String devicename = jobj.getString("cDeviceName");
                        String alarmtype = jobj.getString("cAlarmType");
                        String regnumber = jobj.getString("cRegNumber");
                        //getAddressFromLocation(Lattitude, Longitude, new SosAlarmFragment.GeocoderHandler());

                        try {
                            powercutalarmAddress = geocoder.getFromLocation(Lattitude, Longitude, 1);
                            address = powercutalarmAddress.get(0).getAddressLine(0);
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = powercutalarmAddress.get(0).getLocality();
                            String state = powercutalarmAddress.get(0).getAdminArea();
                            String country = powercutalarmAddress.get(0).getCountryName();
                            String postalCode = powercutalarmAddress.get(0).getPostalCode();
                            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        powercutalarmitem.setAlarm_date(date);
                        powercutalarmitem.setAlarm_time(time);
                        powercutalarmitem.setalarmaddress(address);
                        powercutalarmitem.setDevice_name(devicename);
                        powercutalarmitem.setAlarmtype(alarmtype);
                        powercutalarmitem.setRegnumber(regnumber);


                        powercutalarmList.add(powercutalarmitem);

                    }


                } catch (final JSONException e) {


                }


            } else {


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (powercutalarmList.size() > 0) {
                rv_powercutalarms.setAdapter(null);
                rv_powercutalarms.setAdapter(new PowerCutAlarmAdapter(getActivity(), powercutalarmList, ""));
            }

            else if (powercutalarmList.size() == 0) {
                rv_powercutalarms.setAdapter(null);
                Toast toast = Toast.makeText(getActivity(), "No PowerCut Alarms Today", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }

            sr_powercutalarms.setRefreshing(false);
            progress.dismiss();
        }

    }
}
