package com.example.letran.equipmentmanagement.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.fragments.Profile_Fragment;
import com.example.letran.equipmentmanagement.fragments.Profile_Ticket_Fragment;

import java.util.ArrayList;
import java.util.List;

public class Profile_Activity extends DrawerLayout_Activity implements ActionBar.TabListener {
    private ViewPager viewPager;
    private TabsPagerProfileAdapter mAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.profile_activity, null, false);
        mDrawer.addView(contentView, 0);
        Initiate();
    }

    private void Initiate(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager();

    }

    private void setupViewPager() {
        mAdapter = new TabsPagerProfileAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new Profile_Fragment(), "Profile");
        mAdapter.addFragment(new Profile_Ticket_Fragment(), "My Ticket");
        viewPager.setAdapter(mAdapter);
        setIcon();
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.profile);
        tabLayout.getTabAt(1).setIcon(R.drawable.gallery);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public class TabsPagerProfileAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabsPagerProfileAdapter(FragmentManager fm) {
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
