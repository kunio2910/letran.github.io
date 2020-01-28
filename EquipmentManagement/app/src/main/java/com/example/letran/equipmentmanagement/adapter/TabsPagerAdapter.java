package com.example.letran.equipmentmanagement.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.letran.equipmentmanagement.fragments.Approved_Fragment;
import com.example.letran.equipmentmanagement.fragments.ShowDevices_Fragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ShowDevices_Fragment();
            case 1:
                // Games fragment activity
                return new Approved_Fragment();
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
