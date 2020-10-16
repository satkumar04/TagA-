package org.san.tgps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class HistoryVehicleTrackMain extends AppCompatActivity implements View.OnClickListener, CurrentFragment.OnFragmentInteractionListener,
        LiveTrackFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    LinearLayout ll_current, ll_livetrack, ll_history, ll_back;
    TextView txtcurrent, txtlivetrack, txthistory, txtback;

    String imei,vehicle ;

    protected PowerManager.WakeLock mWakeLock;

    FragmentManager manager;
    FragmentTransaction transaction;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_vehicle_track_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        ll_current = (LinearLayout) findViewById(R.id.ll_current);
        ll_livetrack = (LinearLayout) findViewById(R.id.ll_livetrack);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        txtcurrent = (TextView) findViewById(R.id.txtcurrent);
        txtlivetrack = (TextView) findViewById(R.id.txtlivetrack);
        txthistory = (TextView) findViewById(R.id.txthistory);
        txtback = (TextView) findViewById(R.id.txtback);

        manager = getSupportFragmentManager();

        ll_current.setOnClickListener(this);
        ll_livetrack.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        imei = (getIntent().getExtras().getString("DeviceId"));
        vehicle = (getIntent().getExtras().getString("vehicle"));
     //   Toast.makeText(this,"IMEI is  " +imei , Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("vehicle", vehicle);
        HistoryFragment historyFragment = new HistoryFragment();
        transaction = manager.beginTransaction();
        historyFragment.setArguments(bundle);
        transaction.replace(R.id.frgmntgroup, historyFragment, "historyFragment");
        transaction.commit();
        /*CurrentFragment currentFragment = new CurrentFragment();
        transaction = manager.beginTransaction();
        transaction.add(R.id.frgmntgroup, currentFragment, "currentFragment");
        transaction.commit();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_current:

                ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.peechgreen);

                // ll_current.setBackgroundColor(getResources().getColor(R.color.skylghtblue));

                ll_current.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.peechgreen));

                txtcurrent.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));

                ll_livetrack.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtlivetrack.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_history.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txthistory.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_back.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtback.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                CurrentFragment currentFragment = new CurrentFragment();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frgmntgroup, currentFragment, "currentFragment");
                transaction.commit();


                break;
            case R.id.ll_livetrack:

                ll_current.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtcurrent.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_livetrack.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.peechgreen));
                txtlivetrack.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));

                ll_history.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txthistory.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_back.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtback.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                LiveTrackFragment livetrckFragment = new LiveTrackFragment();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frgmntgroup, livetrckFragment, "livetrckFragment");
                transaction.commit();

                break;
            case R.id.ll_history:

                ll_current.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtcurrent.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_livetrack.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtlivetrack.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                ll_history.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.peechgreen));
                txthistory.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));

                ll_back.setBackgroundColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.white));
                txtback.setTextColor(ContextCompat.getColor(HistoryVehicleTrackMain.this, R.color.black));

                Bundle bundle = new Bundle();
                bundle.putString("vehicle", vehicle);
                HistoryFragment historyFragment = new HistoryFragment();
                transaction = manager.beginTransaction();
                historyFragment.setArguments(bundle);
                transaction.replace(R.id.frgmntgroup, historyFragment, "historyFragment");
                transaction.commit();

                break;
            case R.id.ll_back:

                Intent backintent = new Intent(HistoryVehicleTrackMain.this, Navigation.class);
                HistoryVehicleTrackMain.this.finish();
                startActivity(backintent);

                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VehicleTrackMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.san.tgps/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "VehicleTrackMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.san.tgps/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(HistoryVehicleTrackMain.this, HistoryVehiclesMain.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
}
