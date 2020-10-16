package org.san.tgps;

import android.content.Context;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentFragment extends Fragment implements OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    GoogleMap map;
    Marker mMarker;
    // SupportMapFragment mapFragment;

    List<VehiclesBean> currentList = new ArrayList<>();


    TextView txtaddress,txtvehiclename,txtspeedcnt;
    private MapView mapView;

    public CurrentFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentFragment.
     */
    public static CurrentFragment newInstance(String param1, String param2) {
        CurrentFragment fragment = new CurrentFragment();
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
        View view = inflater.inflate(R.layout.current, container, false);

        txtvehiclename = (TextView) view.findViewById(R.id.txtvehiclename);
        // txtspeedcnt = (TextView) view.findViewById(R.id.txtspeedcnt);
        txtaddress = (TextView) view.findViewById(R.id.txtaddress);
        //map = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.currentmap)).getMap();

        // mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.currentmap);
        mapView = (MapView) view.findViewById(R.id.currentmap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        /*getCurrentLocation();*/

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
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getCurrentLocation();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    private void getCurrentLocation() {

        String url = GlobalVariables.IPAddress + "getcurrentpostion.php?DeviceId=" + GlobalVariables.DeviceId;
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

                                String lattitude = jobj.getString("Lattitude");
                                String longitude = jobj.getString("Longitude");
                                String regno = jobj.getString("cRegNumber");
                                String dstatus = jobj.getString("vstatus");
                                String lastcdate = jobj.getString("cdate");
                                // String dv_speed = String.valueOf(jobj.getInt("speed"));
                                GlobalVariables.cdate=lastcdate;
                                Double latti = Double.valueOf(lattitude);
                                Double longi = Double.valueOf(longitude);
                                getAddressFromLocation(latti, longi, new GeocoderHandler());

                                GlobalVariables.gllatitude = latti;
                                GlobalVariables.gllogitude = longi;
                                txtvehiclename.setText(regno);
                                // txtspeedcnt.setText("" + dv_speed + "km/h");

                                dispalyonmap(latti, longi, regno, dstatus);

                            } else if (success == 0) {
                                Toast.makeText(getActivity(), "No Items found...", Toast.LENGTH_SHORT).show();
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

    private void dispalyonmap(double latitude, double longitude, String regnumber, String dstatus) {
        try {
            if (map != null) {
                Date currentTime = Calendar.getInstance().getTime();
                String regNum = regnumber;
                LatLng coordinate = new LatLng(latitude, longitude);
                BitmapDescriptor icon = null;
                if (dstatus.equals("0")) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.carred);
                } else if (dstatus.equals("1")) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.cargreen);
                } else if (dstatus.equals("2")) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.cargrey);
                }
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(coordinate).title(regNum).snippet(String.valueOf(GlobalVariables.cdate)).icon(icon);
                mMarker = map.addMarker(markerOptions);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latitude, longitude), 16.5f));
            }
        } catch (Exception ex) {

            ex.getStackTrace();
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
            txtaddress.setText(locationAddress);

        }
    }

    public void getAddressFromLocation(final double latitude, final double longitude, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
                        // sb.append(address.getLocality()).append("\t");
                        // sb.append(address.getPostalCode()).append("\t");
                        //sb.append(address.getCountryName());
                        sb.append(address.getAddressLine(0));
                        // sb.append(address.getFeatureName());
                        // sb.append(address.getSubLocality());
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
