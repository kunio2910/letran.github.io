package com.example.letran.equipmentmanagement.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.letran.equipmentmanagement.fragments.DetailDevice_Fragment;
import com.example.letran.equipmentmanagement.fragments.TreeDevice_Fragment;

public class TabsPagerDetailDeviceAdapter extends FragmentPagerAdapter {

    public TabsPagerDetailDeviceAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DetailDevice_Fragment();
            case 1:
                return new TreeDevice_Fragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
