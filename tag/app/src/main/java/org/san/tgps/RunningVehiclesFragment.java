package org.san.tgps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
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

import org.san.tgps.adapter.OnlineVehiclesAdapter;
import org.san.tgps.adapter.RunningVehiclesAdapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTECH on 2017-12-18.
 */

public class RunningVehiclesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = RunningVehiclesFragment.class.getSimpleName();
    private RunningVehiclesFragment.OnFragmentInteractionListener mListener;

    List<VehiclesBean> onlineDvcList = new ArrayList<>();

    List<VehiclesBean> onlinedvcList = new ArrayList<>();
    RecyclerView rv_runningvehicles;
    SwipeRefreshLayout sr_runningvehicles;
    EditText et_searchvehicle;
    String strsearch;
    RunningVehiclesAdapter runningadapter;

    public RunningVehiclesFragment() {
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
    public static RunningVehiclesFragment newInstance(String param1, String param2) {
        RunningVehiclesFragment fragment = new RunningVehiclesFragment();
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

        View view = inflater.inflate(R.layout.running_vehicles, container, false);

        rv_runningvehicles = (RecyclerView) view.findViewById(R.id.rv_runningvehicles);
        sr_runningvehicles = (SwipeRefreshLayout) view.findViewById(R.id.sr_runningvehicles);
        et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_runningvehicles.setLayoutManager(llm);

        sr_runningvehicles.setOnRefreshListener(this);

        sr_runningvehicles.post(new Runnable() {
            @Override
            public void run() {
                sr_runningvehicles.setRefreshing(true);
               new GetRunningVehicles().execute();
               // getRunningvehicles();
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
                for (int i = 0; i < onlineDvcList.size(); i++) {
                    final String text = onlineDvcList.get(i).getDevice_Name().toUpperCase();
                    if (text.startsWith(strsearch)) {
                        fltrdVehicleList.add(onlineDvcList.get(i));
                    }
                }

                rv_runningvehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                runningadapter = new RunningVehiclesAdapter(getActivity(), fltrdVehicleList, strsearch);
                rv_runningvehicles.setAdapter(runningadapter);
                runningadapter.notifyDataSetChanged();
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
        if (context instanceof RunningVehiclesFragment.OnFragmentInteractionListener) {
            mListener = (RunningVehiclesFragment.OnFragmentInteractionListener) context;
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
        new GetRunningVehicles().execute();
        //getRunningvehicles();
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

   /* private void getRunningvehicles() {

        onlineDvcList.clear();
        // Toast.makeText(getActivity(),"Online function", Toast.LENGTH_SHORT).show();
        //  String url = GlobalVariables.IPAddress + "getonlinevehicles.php?userid=" + GlobalVariables.Comapny_ID;

        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "getrunningvehicles.php?cUserId=" + GlobalVariables.Comapny_ID;
        } else if (GlobalVariables.role.equalsIgnoreCase("Dealer")) {
            url = GlobalVariables.IPAddress + "getrunningvehiclesbyDealer.php?cUserId=" + GlobalVariables.Comapny_ID;
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
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("items");
                                for (int i = 0; i < ja.length(); i++) {
                                    VehiclesBean moditem = new VehiclesBean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    String DeviceId = jobj.getString("DeviceId");
                                    String name = jobj.getString("cDeviceName");
                                    String dstatus = jobj.getString("vstatus");
                                    String regno = jobj.getString("cRegNumber");
                                    String imei = jobj.getString("imei");


                                    moditem.setRegNumber(regno);

                                    moditem.setDeviceId(DeviceId);
                                    moditem.setDevice_Name(regno);
                                    moditem.setDevice_Status(dstatus);
                                    moditem.setDevice_Imei(imei);

                                    onlineDvcList.add(moditem);
                                }

                                if (onlineDvcList.size() == 0) {
                                    rv_runningvehicles.setAdapter(null);

                                } else {

										*//*for(int i=0;i<OnlineDeviceList.size(); i++){
                                            Toast.makeText(getActivity(), " online " +OnlineDeviceList.get(i).getDevice_Name(),Toast.LENGTH_SHORT).show();
										}*//*

                                    rv_runningvehicles.setAdapter(null);
                                    rv_runningvehicles.setAdapter(new RunningVehiclesAdapter(getActivity(), onlineDvcList, ""));

                                    //onlineDvcList.clear();
                                }
                                sr_runningvehicles.setRefreshing(false);
                            }
                            else if (success == 0) {
                              *//*  Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No Running Vehicles Found", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.GREEN);
                                v.setTextSize(20);
                                //v.setBackgroundColor(0xFFFFFFFF);
                                toast.show();
                                sr_runningvehicles.setRefreshing(false);*//*
                                *//* try {

                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>SORRY!!!</font>"));
                                    alertDialog.setMessage(Html.fromHtml("<font color='#d72a27'>No Running Vehicle Found</font>"));
                                    alertDialog.setIcon(R.drawable.warning);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                           // finish();

                                        }
                                    });

                                    alertDialog.show();
                                }
                                catch (Exception e) {

                                }*//*
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //PD.dismiss();
                sr_runningvehicles.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }*/
    private class GetRunningVehicles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onlineDvcList.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"fetchvehicleList?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&Vehicletype="+"Moving";
            //String url = GlobalVariables.apiserverUrl+"UpdateAccsts?IMEI="+"864502035760990";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            String test=jsonStr;

            if (jsonStr != null) {
                try {


                    JSONArray jsonArr = new JSONArray(jsonStr);   // without check json obj
                    for (int i = 0; i < jsonArr.length(); i++) {
                        VehiclesBean moditem = new VehiclesBean();
                    /*for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj1 = jsonArr.getJSONObject(i);
*/
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String DeviceId = jsonObj.getString("DeviceId");
                        String name = jsonObj.getString("cDeviceName");
                        String dstatus = jsonObj.getString("vstatus");
                        String regno = jsonObj.getString("cRegNumber");
                        String imei = jsonObj.getString("imei");

                        moditem.setRegNumber(regno);

                        moditem.setDeviceId(DeviceId);
                        moditem.setDevice_Name(name);
                        moditem.setRegNumber(regno);
                        moditem.setDevice_Status(dstatus);
                        moditem.setDevice_Imei(imei);
                        // GlobalVariables.imei=imei;

                        onlineDvcList.add(moditem);
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
            if (onlineDvcList.size() > 0) {
                rv_runningvehicles.setAdapter(null);
                rv_runningvehicles.setAdapter(new RunningVehiclesAdapter(getActivity(), onlineDvcList, ""));


                // offlinedvcList.clear();
            } else if (onlineDvcList.size() == 0) {
                rv_runningvehicles.setAdapter(null);
               /* Toast toast = Toast.makeText(getActivity(), "Moving Vehicle Not Found", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(Color.GREEN);
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();*/
            }

            sr_runningvehicles.setRefreshing(false);

        }

    }
}
