package com.example.letran.equipmentmanagement.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.fragments.DetailDevice_Fragment;
import com.example.letran.equipmentmanagement.fragments.TreeDevice_Fragment;

import java.util.ArrayList;
import java.util.List;

public class ShowDetail_Activity extends DrawerLayout_Activity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerDetailDeviceAdapter mAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.showdetail_activity);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.showdetail_activity, null, false);
        mDrawer.addView(contentView, 0);

        Initiate();
    }

    private void Initiate() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        mAdapter = new TabsPagerDetailDeviceAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setupViewPager();
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("DETAILS");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_black_18dp, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TREE");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person_black_18dp, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setupViewPager() {
        mAdapter = new TabsPagerDetailDeviceAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new DetailDevice_Fragment(), "DETAILS");
        mAdapter.addFragment(new TreeDevice_Fragment(), "TREE");
        viewPager.setAdapter(mAdapter);

        setIcon();
        //setupTabIcons();
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.detail);
        tabLayout.getTabAt(1).setIcon(R.drawable.treeview);
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

    public class TabsPagerDetailDeviceAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabsPagerDetailDeviceAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
