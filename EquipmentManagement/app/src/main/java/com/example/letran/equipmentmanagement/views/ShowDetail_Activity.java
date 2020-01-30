package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.TabsPagerAdapter;
import com.example.letran.equipmentmanagement.adapter.TabsPagerDetailDeviceAdapter;

public class ShowDetail_Activity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private TabsPagerDetailDeviceAdapter mAdapter;
    //Tab titles
    private String[] tabs = {"Infor Device", "Tree Device"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdetail_activity);

        Initiate();
    }

    private void Initiate(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerDetailDeviceAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Adding Tabs
        for(String tab_name : tabs){
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(ShowDetail_Activity.this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
