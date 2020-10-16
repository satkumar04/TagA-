package org.san.tgps;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveTrackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveTrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveTrackFragment extends Fragment implements OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Marker parkMarker, spmarker,smarker;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    GoogleMap map;
    Handler handler = new Handler();
    Runnable runnablelive = null;
    List<VehiclesBean> trackdataList = new ArrayList<>();
    double tmpLattitudelive = 0, tmpLongitudelive = 0;
    Marker mMarker;
    Geocoder geocoder;
    SupportMapFragment mapFragment;
    int dotcount=0;

    TextView txtvehiclename, txtspeedcnt, txtaddress;

    public LiveTrackFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveTrackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveTrackFragment newInstance(String param1, String param2) {
        LiveTrackFragment fragment = new LiveTrackFragment();
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
        View view = inflater.inflate(R.layout.live_track, container, false);

        txtvehiclename = (TextView) view.findViewById(R.id.txtvehiclename);
        txtspeedcnt = (TextView) view.findViewById(R.id.txtspeedcnt);
        txtaddress = (TextView) view.findViewById(R.id.txtaddress);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        //map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.livemap)).getMap();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.livemap);
        mapFragment.getMapAsync(this);
        livepositiontrack();
        startTimer();
        return view;
    }

    private void livepositiontrack() {

        trackdataList.clear();
        dotcount++;
        String url = GlobalVariables.IPAddress + "getliveposition.php?DeviceId=" + GlobalVariables.DeviceId;

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
                                //  for (int i = 0; i < ja.length(); i++) {
                                VehiclesBean vehiclebin = new VehiclesBean();
                                JSONObject jobj = ja.getJSONObject(0);
                                String latit = jobj.getString("Lattitude");
                                String longit = jobj.getString("Longitude");
                                String dv_speed = String.valueOf(jobj.getInt("speed"));
                                String dv_name = jobj.getString("cDeviceName");
                                String ddegree = jobj.getString("ddegree");
                                String dstatus = jobj.getString("vstatus");
                                String RegNumber = jobj.getString("cRegNumber");

                                vehiclebin.setDvc_lattitude(latit);
                                vehiclebin.setDvc_longitude(longit);
                                vehiclebin.setSpeed(dv_speed);
                                vehiclebin.setDdegree(ddegree);
                                vehiclebin.setDevice_Name(dv_name);
                                vehiclebin.setRegNumber(RegNumber);
                                vehiclebin.setDevice_Status(dstatus);
                                vehiclebin.setDeviceId(GlobalVariables.DeviceId);
                                trackdataList.add(vehiclebin);

                                if (map != null)
                                {
                                    String speed = trackdataList.get(0).getSpeed();
                                    String imeiNumber = trackdataList.get(0).getDeviceId();
                                    String vehicle = trackdataList.get(0).getRegNumber();
                                    Double lattitude = Double.valueOf(trackdataList.get(0).getDvc_lattitude());
                                    Double longitude = Double.valueOf(trackdataList.get(0).getDvc_longitude());

                                    String dev_status = trackdataList.get(0).getDevice_Status();
                                    float rotdegree = Float.valueOf(trackdataList.get(0).getDdegree());
                                    // String snipt = "Vehicle : " + vehicle + "\n" + "Speed:" + speed + " kms \n IMEI: " + imeiNumber;

                                    txtvehiclename.setText(vehicle);
                                    txtspeedcnt.setText("" + speed + "km/h");

                                    txtvehiclename.setSelected(true);

                                    if (mMarker != null) {
                                        mMarker.remove();
                                    }

                                    LatLng coordinate = new LatLng(lattitude, longitude);
                                    BitmapDescriptor icon;
                                    MarkerOptions markerOptions;
                                    if (dev_status.equals("0")) {
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.carred);
                                        markerOptions = new MarkerOptions().position(coordinate).icon(icon).anchor(0.5f, 0.0f).rotation(rotdegree);
                                        mMarker = map.addMarker(markerOptions);

                                    } else if (dev_status.equals("1")) {
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.cargreen);
                                        markerOptions = new MarkerOptions().position(coordinate).icon(icon).anchor(0.5f, 1.0f).rotation(rotdegree);
                                        mMarker = map.addMarker(markerOptions);


                                    } else if (dev_status.equals("2")) {
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.cargrey);
                                        markerOptions = new MarkerOptions().position(coordinate).icon(icon).anchor(0.5f, 1.0f).rotation(rotdegree);
                                        mMarker = map.addMarker(markerOptions);
                                        //if(dotcount>2) {
                                            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.markercircle);
                                            MarkerOptions markerOptions1 = new MarkerOptions().position(coordinate).icon(icon1);
                                            smarker = map.addMarker(markerOptions1);
                                            //dotcount++;
                                       // }
                                    }

                                    getAddressFromLocation(lattitude, longitude, new GeocoderHandler());

                                    map.getUiSettings().setMyLocationButtonEnabled(false);
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lattitude, longitude), 15.0f));


                                  /*  LatLng locationhistory = new LatLng(lattitude, longitude);
                                    map.animateCamera(CameraUpdateFactory.newLatLng(locationhistory));*/

                                    try {
                                        if (tmpLattitudelive == 0 || tmpLongitudelive == 0) {

                                        } else {
                                            map.addPolyline(new PolylineOptions()
                                                    .add(new LatLng(tmpLattitudelive, tmpLongitudelive), new LatLng(lattitude, longitude))
                                                    .width(14).color(Color.BLUE));

                                        }

                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    tmpLattitudelive = lattitude;
                                    tmpLongitudelive = longitude;

                                }

                            } else if (success == 0) {
                                Toast.makeText(getActivity(), "Not found...", Toast.LENGTH_SHORT).show();
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
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void startTimer() {

        handler = new Handler();
        runnablelive = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                livepositiontrack();
                /*int c=dotcount;
                dotcount++;*/
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnablelive, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
//        handler.removeCallbacks(runnablelive);
    }

    @Override
    public void onPause()
    {
        super.onPause();
//        handler.removeCallbacks(runnablelive);
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
            txtaddress.setText(locationAddress);
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
