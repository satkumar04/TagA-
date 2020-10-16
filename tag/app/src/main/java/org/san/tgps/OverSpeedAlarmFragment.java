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
import org.san.tgps.adapter.OverSpeedAlarmAdapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.AlarmsBean;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverSpeedAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverSpeedAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverSpeedAlarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = OverSpeedAlarmFragment.class.getSimpleName();
    private List<Address> overspeedalarmAddress;
    Geocoder geocoder;
    String address;
    ProgressDialog progress;
    private OverSpeedAlarmFragment.OnFragmentInteractionListener mListener;

    List<AlarmsBean> overspeedalarmList = new ArrayList<>();

    // List<VehiclesBean> onlinedvcList = new ArrayList<>();
    RecyclerView rv_overspeedalarms;
    SwipeRefreshLayout sr_overspeedalarms;
    //  EditText et_searchvehicle;
    String strsearch;
    OverSpeedAlarmAdapter overspeedadapter;

    public OverSpeedAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverSpeedAlarmFragment.
     */
    public static OverSpeedAlarmFragment newInstance(String param1, String param2) {
        OverSpeedAlarmFragment fragment = new OverSpeedAlarmFragment();
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

        View view = inflater.inflate(R.layout.fragment_over_speed_alarm, container, false);

        rv_overspeedalarms = (RecyclerView) view.findViewById(R.id.rv_overspeedalarms);
        sr_overspeedalarms = (SwipeRefreshLayout) view.findViewById(R.id.sr_overspeedalarms);
        //et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_overspeedalarms.setLayoutManager(llm);

        sr_overspeedalarms.setOnRefreshListener(this);

        sr_overspeedalarms.post(new Runnable() {
            @Override
            public void run() {
              //  sr_overspeedalarms.setRefreshing(true);
                new GetOverSpeedAlarmList().execute();
                //getOverSpeedAlarms();
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
                onlineadapter = new OverSpeedAlarmadapter(getActivity(), fltrdVehicleList, strsearch);
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
        if (context instanceof OverSpeedAlarmFragment.OnFragmentInteractionListener) {
            mListener = (OverSpeedAlarmFragment.OnFragmentInteractionListener) context;
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
        new GetOverSpeedAlarmList().execute();
        //getOverSpeedAlarms();
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


    private class GetOverSpeedAlarmList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            // progress.setTitle("Loading");
            progress.setMessage("OverSpeed Alarms "+"\n"+"Loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            overspeedalarmList.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"FetchAlarms?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&AlarmType=OverSpeed";
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990"
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AlarmsBean overspeedalarmitem = new AlarmsBean();
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
                            overspeedalarmAddress = geocoder.getFromLocation(Lattitude, Longitude, 1);
                            address = overspeedalarmAddress.get(0).getAddressLine(0);
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = overspeedalarmAddress.get(0).getLocality();
                            String state = overspeedalarmAddress.get(0).getAdminArea();
                            String country = overspeedalarmAddress.get(0).getCountryName();
                            String postalCode = overspeedalarmAddress.get(0).getPostalCode();
                            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        overspeedalarmitem.setAlarm_date(date);
                        overspeedalarmitem.setAlarm_time(time);
                        overspeedalarmitem.setalarmaddress(address);
                        overspeedalarmitem.setDevice_name(devicename);
                        overspeedalarmitem.setAlarmtype(alarmtype);
                        overspeedalarmitem.setRegnumber(regnumber);


                        overspeedalarmList.add(overspeedalarmitem);

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
            if (overspeedalarmList.size() > 0) {
                rv_overspeedalarms.setAdapter(null);
                rv_overspeedalarms.setAdapter(new OverSpeedAlarmAdapter(getActivity(), overspeedalarmList, ""));
            }

            else if (overspeedalarmList.size() == 0) {
                rv_overspeedalarms.setAdapter(null);
                Toast toast = Toast.makeText(getActivity(), "No OverSpeed Alarms Today", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }

            sr_overspeedalarms.setRefreshing(false);
            progress.dismiss();
        }

    }
}
