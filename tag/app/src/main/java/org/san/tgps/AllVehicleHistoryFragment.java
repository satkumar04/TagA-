package org.san.tgps;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.san.tgps.adapter.AllVehiclesAdapter;
import org.san.tgps.adapter.AllVehiclesHistoryAdapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllVehicleHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllVehicleHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllVehicleHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    List<VehiclesBean> deviceList = new ArrayList<>();
    RecyclerView rv_allvehicles;
    SwipeRefreshLayout sr_allvehicles;
    EditText et_searchvehicle;

    Handler handler = new Handler();
    Runnable runnablelive = null;
    String strsearch;
    private String TAG = AllVehicleHistoryFragment.class.getSimpleName();
    AllVehiclesHistoryAdapter allvhadapter;
    private OnFragmentInteractionListener mListener;

    public AllVehicleHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllVehicleFragment.
     */
    public static AllVehicleHistoryFragment newInstance(String param1, String param2) {
        AllVehicleHistoryFragment fragment = new AllVehicleHistoryFragment();
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
        View view = inflater.inflate(R.layout.all_vehiclehistory, container, false);

        viewInitializations(view);

        rv_allvehicles.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_allvehicles.setLayoutManager(llm);
        //  getallvehicles();
        //searchvehicle.setOnQueryTextListener(this);
        sr_allvehicles.setOnRefreshListener(this);
        sr_allvehicles.post(new Runnable() {
            @Override
            public void run() {
                sr_allvehicles.setRefreshing(true);
                //getallvehicles();
                new GetAllVehicles().execute();
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
                for (int i = 0; i < deviceList.size(); i++) {
                    final String text = deviceList.get(i).getDevice_Name().toUpperCase();
                    if (text.startsWith(strsearch)) {
                        fltrdVehicleList.add(deviceList.get(i));
                    }
                }

                rv_allvehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                allvhadapter = new AllVehiclesHistoryAdapter(getActivity(), fltrdVehicleList, strsearch);
                rv_allvehicles.setAdapter(allvhadapter);
                allvhadapter.notifyDataSetChanged();
            }
        });

        et_searchvehicle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                et_searchvehicle.setCursorVisible(true);

                return false;
            }
        });
        sr_allvehicles.setOnRefreshListener(this);


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
    public void onRefresh() {
        //getallvehicles();
        new GetAllVehicles().execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toUpperCase();
        final List<VehiclesBean> fltrdVehicleList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            final String text = deviceList.get(i).getDevice_Name().toUpperCase();
            if (text.startsWith(query)) {
                fltrdVehicleList.add(deviceList.get(i));
            }
        }

        rv_allvehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
        allvhadapter = new AllVehiclesHistoryAdapter(getActivity(), fltrdVehicleList, query);
        rv_allvehicles.setAdapter(allvhadapter);
        allvhadapter.notifyDataSetChanged();
        return true;
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


    private void viewInitializations(View view) {
        rv_allvehicles = (RecyclerView) view.findViewById(R.id.rv_allvehicles);
        sr_allvehicles = (SwipeRefreshLayout) view.findViewById(R.id.sr_allvehicles);
        // searchvehicle = (SearchView) view.findViewById(R.id.searchvehicle);
        et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);
    }

   /* private void getallvehicles() {
        deviceList.clear();
        rv_allvehicles.setAdapter(null);
        // ((SimpleItemAnimator) rv_allvehicles.getItemAnimator()).setSupportsChangeAnimations(‌​false);
        String url = null;
        if (GlobalVariables.role.equalsIgnoreCase("User")) {
            url = GlobalVariables.IPAddress + "getallvehicles.php?cUserId=" + GlobalVariables.Comapny_ID;
        } else if (GlobalVariables.role.equalsIgnoreCase("Dealer")) {
            url = GlobalVariables.IPAddress + "getallvehiclesbyDealer.php?cUserId=" + GlobalVariables.Comapny_ID;
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
                                for (int i = 0; i < ja.length(); i++) {
                                    VehiclesBean vehiclebin = new VehiclesBean();
                                    JSONObject jobj = ja.getJSONObject(i);

                                    String DeviceId = jobj.getString("DeviceId");
                                    String name = jobj.getString("cDeviceName");
                                    String dstatus = jobj.getString("vstatus");
                                    String regno = jobj.getString("cRegNumber");
                                    String imei = jobj.getString("imei");

                                    vehiclebin.setRegNumber(regno);
                                    vehiclebin.setDeviceId(DeviceId);
                                    vehiclebin.setDevice_Name(regno);
                                    vehiclebin.setDevice_Status(dstatus);
                                    GlobalVariables.imei=imei;
                                    deviceList.add(vehiclebin);
                                }

                                if (deviceList.size() > 0) {

                                    rv_allvehicles.setAdapter(null);

                                    *//*RecyclerView.ItemAnimator animator = rv_allvehicles.getItemAnimator();

                                    if (animator instanceof SimpleItemAnimator) {
                                        ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                                    }*//*


                                    rv_allvehicles.setAdapter(new AllVehiclesHistoryAdapter(getActivity(), deviceList, ""));

                                    //deviceList.clear();
                                } else if (deviceList.size() == 0) {
                                    rv_allvehicles.setAdapter(null);
                                }
                                sr_allvehicles.setRefreshing(false);
                            } else if (success == 0) {
                                Toast.makeText(getActivity(), "No Vehicles found...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //PD.dismiss();
                sr_allvehicles.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jreq);
    }*/
    private class GetAllVehicles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deviceList.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"fetchvehicleList?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&Vehicletype="+"All";
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

                        deviceList.add(moditem);
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
            if (deviceList.size() > 0) {
                rv_allvehicles.setAdapter(null);
                rv_allvehicles.setAdapter(new AllVehiclesHistoryAdapter(getActivity(), deviceList, ""));


                // offlinedvcList.clear();
            } else if (deviceList.size() == 0) {
                rv_allvehicles.setAdapter(null);
            }

            sr_allvehicles.setRefreshing(false);

        }

    }

}
