package org.san.tgps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.san.tgps.adapter.OnlineVehiclesAdapter;
import org.san.tgps.adapter.SosAlarmadapter;
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
 * {@link SosAlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SosAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SosAlarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener ,OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = SosAlarmFragment.class.getSimpleName();
    private List<Address> sosalarmAddress;

    String finaladdress;
    ProgressDialog progress;

    Geocoder geocoder;
    private SosAlarmFragment.OnFragmentInteractionListener mListener;
    GoogleMap map;

    List<AlarmsBean> sosalarmList = new ArrayList<>();

   // List<VehiclesBean> onlinedvcList = new ArrayList<>();
    RecyclerView rv_sosalarms;
    SwipeRefreshLayout sr_sosalarms;
  //  EditText et_searchvehicle;
    String strsearch;
    SosAlarmadapter sosadapter;
    String address;

    public SosAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SosAlarmFragment.
     */
    public static SosAlarmFragment newInstance(String param1, String param2) {
        SosAlarmFragment fragment = new SosAlarmFragment();
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

        View view = inflater.inflate(R.layout.fragment_sos_alarm, container, false);

        rv_sosalarms = (RecyclerView) view.findViewById(R.id.rv_sosalarms);
        sr_sosalarms = (SwipeRefreshLayout) view.findViewById(R.id.sr_sosalarms);
        //et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_sosalarms.setLayoutManager(llm);

        sr_sosalarms.setOnRefreshListener(this);

        sr_sosalarms.post(new Runnable() {
            @Override
            public void run() {
                //sr_sosalarms.setRefreshing(true);
                new GetSosAlarmList().execute();
               // getSosAlarms();
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
                onlineadapter = new SosAlarmadapter(getActivity(), fltrdVehicleList, strsearch);
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
        if (context instanceof SosAlarmFragment.OnFragmentInteractionListener) {
            mListener = (SosAlarmFragment.OnFragmentInteractionListener) context;
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
        new GetSosAlarmList().execute();
        //getSosAlarms();
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


    private class GetSosAlarmList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            // progress.setTitle("Loading");
            progress.setMessage("Sos Alarms "+"\n"+"Loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            sosalarmList.clear();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"FetchAlarms?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&AlarmType=sos";
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990"
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AlarmsBean sosalarmitem = new AlarmsBean();
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
                            sosalarmAddress = geocoder.getFromLocation(Lattitude, Longitude, 1);
                            address = sosalarmAddress.get(0).getAddressLine(0);
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = sosalarmAddress.get(0).getLocality();
                            String state = sosalarmAddress.get(0).getAdminArea();
                            String country = sosalarmAddress.get(0).getCountryName();
                            String postalCode = sosalarmAddress.get(0).getPostalCode();
                            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        sosalarmitem.setAlarm_date(date);
                        sosalarmitem.setAlarm_time(time);
                        sosalarmitem.setalarmaddress(address);
                        sosalarmitem.setDevice_name(devicename);
                        sosalarmitem.setAlarmtype(alarmtype);
                        sosalarmitem.setRegnumber(regnumber);


                        sosalarmList.add(sosalarmitem);

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
            if (sosalarmList.size() > 0) {
                rv_sosalarms.setAdapter(null);
                rv_sosalarms.setAdapter(new SosAlarmadapter(getActivity(), sosalarmList, ""));
            }

            else if (sosalarmList.size() == 0) {
                rv_sosalarms.setAdapter(null);
                Toast toast = Toast.makeText(getActivity(), "No sos Alarms Today", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(getResources().getColor(R.color.NewBARcolor));
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }

            sr_sosalarms.setRefreshing(false);
            progress.dismiss();
        }

    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            String a=locationAddress;
         //   txtaddress.setText(locationAddress);
        }
    }
    public void getAddressFromLocation(final double latitude, final double longitude, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                // Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\t");
                        }
                        sb.append(address.getAddressLine(0));
                        // sb.append(address.getLocality()).append("\t");
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    // Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = result;
                        finaladdress=result;

                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

}
