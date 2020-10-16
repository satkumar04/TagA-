package org.san.tgps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.san.tgps.adapter.OfflineVehiclesAdapter;
import org.san.tgps.app.AppController;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfflineVehiclesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfflineVehiclesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfflineVehiclesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = OfflineVehiclesFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    List<VehiclesBean> offlinedvcList = new ArrayList<>();
    RecyclerView rv_offlinevehicles;
    SwipeRefreshLayout sr_offlinevehicles;
    EditText et_searchvehicle;
    String strsearch;
    OfflineVehiclesAdapter offlineadapter;

    public OfflineVehiclesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfflineVehiclesFragment.
     */
    public static OfflineVehiclesFragment newInstance(String param1, String param2) {
        OfflineVehiclesFragment fragment = new OfflineVehiclesFragment();
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
        View view = inflater.inflate(R.layout.offline_vehicles, container, false);

        widgetsInitializations(view);
        rv_offlinevehicles.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_offlinevehicles.setLayoutManager(llm);
        // Toast.makeText(getActivity(), "Inside OFFLINE", Toast.LENGTH_SHORT).show();


        sr_offlinevehicles.setOnRefreshListener(this);

        sr_offlinevehicles.post(new Runnable() {
            @Override
            public void run() {
                sr_offlinevehicles.setRefreshing(true);
                new GetOfflineVehicles().execute();
                //getofflinevehicles();
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
                for (int i = 0; i < offlinedvcList.size(); i++) {
                    final String text = offlinedvcList.get(i).getDevice_Name().toUpperCase();
                    if (text.startsWith(strsearch)) {
                        fltrdVehicleList.add(offlinedvcList.get(i));
                    }
                }

                rv_offlinevehicles.setLayoutManager(new LinearLayoutManager(getActivity()));
                offlineadapter = new OfflineVehiclesAdapter(getActivity(), fltrdVehicleList, strsearch);
                rv_offlinevehicles.setAdapter(offlineadapter);
                offlineadapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void widgetsInitializations(View view) {
        rv_offlinevehicles = (RecyclerView) view.findViewById(R.id.rv_offlinevehicles);
        sr_offlinevehicles = (SwipeRefreshLayout) view.findViewById(R.id.sr_offlinevehicles);
        et_searchvehicle = (EditText) view.findViewById(R.id.et_searchvehicle);
    }



    private class GetOfflineVehicles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            offlinedvcList.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = GlobalVariables.apiserverUrl+"fetchvehicleList?UserId="+GlobalVariables.userid+"&Role="+GlobalVariables.role+"&Vehicletype="+"Offline";
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

                        offlinedvcList.add(moditem);
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
            if (offlinedvcList.size() > 0) {
                rv_offlinevehicles.setAdapter(null);
                rv_offlinevehicles.setAdapter(new OfflineVehiclesAdapter(getActivity(), offlinedvcList, ""));


                // offlinedvcList.clear();
            } else if (offlinedvcList.size() == 0) {
                rv_offlinevehicles.setAdapter(null);
            }

            sr_offlinevehicles.setRefreshing(false);

        }

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
       // getofflinevehicles();
        new GetOfflineVehicles().execute();
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
}
