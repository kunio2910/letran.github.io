package com.example.letran.equipmentmanagement.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.DevicesAdapter;
import com.example.letran.equipmentmanagement.models.Device;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.MyDividerItemDecoration;
import com.example.letran.equipmentmanagement.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class SearchDevice_Activity extends DrawerLayout_Activity {

    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private DevicesAdapter mAdapter;
    private ArrayList<Device> deviceList = new ArrayList<>();
    private ArrayList<Device> deviceList_temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.searchdevice_activity);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.searchdevice_activity, null, false);
        mDrawer.addView(contentView, 0);

        Initiate();
        ShowDevices();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(SearchDevice_Activity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                deviceList.clear();
                deviceList.addAll(deviceList_temp);
                Intent intent = new Intent(SearchDevice_Activity.this,ShowDetail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", deviceList.get(position).getId());
                bundle.putString("name", deviceList.get(position).getName());
                bundle.putString("description", deviceList.get(position).getDescription());
                bundle.putString("issue", deviceList.get(position).getIssue());
                bundle.putString("url_image", deviceList.get(position).getUrl_image());
                bundle.putString("create_time", deviceList.get(position).getCreate_time());
                bundle.putString("approver", deviceList.get(position).getApprover());
                bundle.putString("creater", deviceList.get(position).getCreater());

                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(),"LongClick",Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void Initiate(){
        deviceList.clear();
        deviceList_temp.clear();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        editTextSearch = (EditText)findViewById(R.id.editTextSearch);

        deviceList = new ArrayList<>(AppConfig.LST_DEVICES);
    }


    public void ShowDevices(){
        if (AppConfig.LST_DEVICES.size() >= 1) {
            mAdapter = new DevicesAdapter(AppConfig.LST_DEVICES, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //Adding RecyclerView Divider / Separator
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this,LinearLayoutManager.VERTICAL,16));
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Device> filterdNames = new ArrayList<>();
        //looping through existing elements
        for(int i = 0;i < deviceList.size();i++){
            String name = deviceList.get(i).getName();
            String date = deviceList.get(i).getCreate_time();
                if (name.toLowerCase().contains(text.toLowerCase()) || date.toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(deviceList.get(i));
                }

        }

        //calling a method of the adapter class and passing the filtered list
        mAdapter.filterList(filterdNames);

        //use get item position new for reccycleview
        deviceList_temp.clear();
        deviceList_temp.addAll(filterdNames);
    }
}
