package com.example.letran.equipmentmanagement.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.DevicesAdapter;
import com.example.letran.equipmentmanagement.fragments.Approved_Fragment;
import com.example.letran.equipmentmanagement.fragments.DetailDevice_Fragment;
import com.example.letran.equipmentmanagement.fragments.ShowDevices_Fragment;
import com.example.letran.equipmentmanagement.fragments.WaitApprove_Fragment;
import com.example.letran.equipmentmanagement.models.Device;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;
import com.example.letran.equipmentmanagement.utils.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends DrawerLayout_Activity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private TabLayout tabLayout;
    private ProgressDialog pDialog;

    ShowDevices_Fragment showDevices_fragment;
    Approved_Fragment approved_fragment;
    WaitApprove_Fragment waitApprove_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawer.addView(contentView, 0);

        Initiate();
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setIcon();

//        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
//        ShowDevices_Fragment showDevices_fragment = (ShowDevices_Fragment)page;
//        ((ShowDevices_Fragment) page).Test();
        showDevices_fragment = (ShowDevices_Fragment)mAdapter.mFragmentList.get(0);
        approved_fragment = (Approved_Fragment) mAdapter.mFragmentList.get(1);
        waitApprove_fragment = (WaitApprove_Fragment) mAdapter.mFragmentList.get(2);
    }

    private void Initiate(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        pDialog = new ProgressDialog(MainActivity.this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupViewPager() {
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new ShowDevices_Fragment(), "ALL");
        mAdapter.addFragment(new Approved_Fragment(), "APPROVED");
        mAdapter.addFragment(new WaitApprove_Fragment(), "WAIT");
        viewPager.setAdapter(mAdapter);
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.home_black_18dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.person_black_18dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.person_black_18dp);
    }

    private void GetAllDevices(final Context context){
        // Tag used to cancel the request
        AppConfig.LST_DEVICES.clear();
        AppConfig.LST_DEVICES_APPROVED.clear();
        AppConfig.LST_DEVICES_WAIT_APPROVED.clear();
        String tag_string_req = "req_login";

        pDialog.setMessage("Getting Data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.GET_ALL_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("devices");
                    int success = jObj.getInt("success");
                    if(success == 1) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Device device = new Device();
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            device.setId(json_data.getString("id"));
                            device.setName(json_data.getString("name"));
                            device.setDescription(json_data.getString("description"));
                            device.setIssue(json_data.getString("issue"));
                            device.setUrl_image(json_data.getString("url_image"));
                            device.setCreate_time(json_data.getString("create_time"));
                            device.setApprover(json_data.getString("approver"));
                            device.setCreater(json_data.getString("creater"));
                            Log.e("info", "Login Response: " + response.toString());

                            AppConfig.LST_DEVICES.add(device);
                            if(device.getApprover().isEmpty()){
                                AppConfig.LST_DEVICES_WAIT_APPROVED.add(device);
                            }else
                                AppConfig.LST_DEVICES_APPROVED.add(device);

                            Log.e("test","test");
                        }

                        showDevices_fragment.ShowDevices();
                        approved_fragment.ShowDevices();
                        //waitApprove_fragment.ShowDevices();
                        SetAvatar();
                        AppConfig.FLAG = 1;
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Get data Error: " + error.getMessage());
                Toast.makeText(context,
                        "Please get data again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance(context).addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppConfig.FLAG == 0) {
            GetAllDevices(this);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {


    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabsPagerAdapter(FragmentManager fm) {
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
