package org.san.tgps;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.san.tgps.adapter.SpeedVehiclesAdapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTECH on 2017-12-21.
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpeedVehiclesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpeedVehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SpeedVehiclesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SpeedVehiclesFragment.OnFragmentInteractionListener mListener;

    List<VehiclesBean> speedDvcList = new ArrayList<>();

    List<VehiclesBean> speeddvcList = new ArrayList<>();
    RecyclerView rv_speedvehicles;
    SwipeRefreshLayout sr_speedvehicles;
    EditText et_searchvehicle;
    String strsearch;
    SpeedVehiclesAdapter speedadapter;

    public SpeedVehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnlineVehiclesFragment.
     */
    public static SpeedVehiclesFragment newInstance(String param1, String param2) {
        SpeedVehiclesFragment fragment = new SpeedVehiclesFragment();
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

        View view = inflater.inflate(R.layout.speed_vehicles, container, false);

        rv_speedvehicles = (RecyclerView) view.findViewById(R.id.rv_speedvehicles);
        sr_speedvehicles = (SwipeRefreshLayout) view.findViewById(R.id.sr_speedvehicles);
        et_searchvehicle = (EditText) view.findViewById(R.id.et_searchspeedvehicle);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_speedvehicles.setLayoutManager(llm);

        sr_speedvehicles.setOnRefreshListener(this);

        sr_speedvehicles.post(new Runnable() {
            @Override
            public void run() {
                sr_speedvehicles.setRefreshing(false);
                getspeedvehicles();
            }
        });

        et_searchvehicle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                et_searchvehicle.setCursorVisible(true);

                return false;
            }
        });

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
                for (int i = 0; i < speedDvcList.size(); i++) {
                    final String text = speedDvcList.get(i).getDevice_Name().toUpperCase();
                    if (text.startsWith(strsearch)) {
                        fltrdVehicleList.add(speedDvcList.get(i));
                    }
                }

                rv_speedvehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                speedadapter = new SpeedVehiclesAdapter(getActivity(), fltrdVehicleList, strsearch);
                rv_speedvehicles.setAdapter(speedadapter);
                speedadapter.notifyDataSetChanged();
            }
        });
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
        if (context instanceof SpeedVehiclesFragment.OnFragmentInteractionListener) {
            mListener = (SpeedVehiclesFragment.OnFragmentInteractionListener) context;
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
        getspeedvehicles();
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



    private void getspeedvehicles() {

        speedDvcList.clear();


        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "speedcheckdate.php?username=" + GlobalVariables.username;
        } else if (GlobalVariables.role.equalsIgnoreCase("subuser")) {
            url = GlobalVariables.IPAddress + "speedchecktime.php?username=" + GlobalVariables.username;
        }

        //JsonObjectRequest jrq = new JsonObjectRequest(url);
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
                                    VehiclesBean moditem = new VehiclesBean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    String imei = jobj.getString("imei");
                                    String name = jobj.getString("dname");
                                    String dstatus = jobj.getString("dstatus");
                                    String regno = jobj.getString("regno");
                                    String cdate=jobj.getString("cdate");


                                    moditem.setSpeedv_regno(regno);
                                    moditem.setSpeedv_Imei(imei);
                                    moditem.setSpeedv_Name(name);
                                    moditem.setDevice_Status(dstatus);
                                    moditem.setSpeedv_time(cdate);


                                    speedDvcList.add(moditem);
                                }

                                if (speedDvcList.size() > 0) {
                                    rv_speedvehicles.setAdapter(null);
                                    rv_speedvehicles.setAdapter(new SpeedVehiclesAdapter(getActivity(), speedDvcList, ""));
                                }
                                else if (speedDvcList.size() == 0) {
                                    rv_speedvehicles.setAdapter(null);
                                }
                                sr_speedvehicles.setRefreshing(false);
                            }
                            else if (success == 0) {
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No Alert found...", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.BLACK);
                                v.setTextSize(30);
                                //v.setBackgroundColor(0xFFFFFFFF);
                                toast.show();

                            }



                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //PD.dismiss();
                sr_speedvehicles.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);

    }
}
