package org.san.tgps;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.san.tgps.adapter.LiveVehiclesAdapter;
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
 * Created by SANTECH on 2017-12-19.
 */

public class LiveVehiclesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = LiveVehiclesFragment.class.getSimpleName();
    String DeviceId,name,dstatus,regno,imei;
    private LiveVehiclesFragment.OnFragmentInteractionListener mListener;

    List<VehiclesBean> onlineDvcList = new ArrayList<>();

    List<VehiclesBean> onlinedvcList = new ArrayList<>();
    RecyclerView rv_runningvehicles;
    SwipeRefreshLayout sr_runningvehicles;
    EditText et_searchvehicle;
    String strsearch;
    LiveVehiclesAdapter liveadapter;

    public LiveVehiclesFragment() {
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
    public static LiveVehiclesFragment newInstance(String param1, String param2) {
        LiveVehiclesFragment fragment = new LiveVehiclesFragment();
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
                new GetRunningLiveVehicles().execute();
                //getlivevehicles();
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
                liveadapter = new LiveVehiclesAdapter(getActivity(), fltrdVehicleList, strsearch);
                rv_runningvehicles.setAdapter(liveadapter);
                liveadapter.notifyDataSetChanged();
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
        if (context instanceof LiveVehiclesFragment.OnFragmentInteractionListener) {
            mListener = (LiveVehiclesFragment.OnFragmentInteractionListener) context;
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
        new GetRunningLiveVehicles().execute();
        //getlivevehicles();
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


    private class GetRunningLiveVehicles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onlineDvcList.clear();
            DeviceId=null;
            name = null;
            dstatus = null;
            regno = null;
            imei = null;
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
                        DeviceId = jsonObj.getString("DeviceId");
                        name = jsonObj.getString("cDeviceName");
                        dstatus = jsonObj.getString("vstatus");
                        regno = jsonObj.getString("cRegNumber");
                        imei = jsonObj.getString("imei");

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
                rv_runningvehicles.setAdapter(new LiveVehiclesAdapter(getActivity(), onlineDvcList, ""));


                // offlinedvcList.clear();
            } else if (onlineDvcList.size() == 0) {
                rv_runningvehicles.setAdapter(null);
                Toast toast = Toast.makeText(getActivity(), "Moving Vehicle Not Found", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                v1.setTextColor(Color.GREEN);
                v1.setTextSize(15);
                //v.setBackgroundColor(0xFFFFFFFF);
                toast.show();
            }

            sr_runningvehicles.setRefreshing(false);

        }

    }
}
