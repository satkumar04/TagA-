package org.san.tgps;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class AccAlarmMain extends AppCompatActivity implements AccAlarmFragment.OnFragmentInteractionListener{
    ViewPager viewPager;
    Toolbar toolbar;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_alarm_main);
     /*   toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupaccalarmViewPager(viewPager);

       // tabLayout = (TabLayout) findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(viewPager);

    }
    public void onBackPressed() {

        Intent i=new Intent(AccAlarmMain.this,Navigation.class);
        startActivity(i);

    }


    private void setupaccalarmViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccAlarmFragment(), "ACC ALARM");

        viewPager.setAdapter(adapter);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
