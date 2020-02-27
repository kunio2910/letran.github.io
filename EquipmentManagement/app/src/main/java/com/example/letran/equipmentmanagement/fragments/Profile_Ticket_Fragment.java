package com.example.letran.equipmentmanagement.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.DevicesAdapter;
import com.example.letran.equipmentmanagement.models.Device;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.MyDividerItemDecoration;
import com.example.letran.equipmentmanagement.utils.RecyclerTouchListener;
import com.example.letran.equipmentmanagement.views.ShowDetail_Activity;

import java.util.ArrayList;
import java.util.List;

public class Profile_Ticket_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private DevicesAdapter mAdapter;
    private Context context;
    private List<Device> lstMyTicket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_ticket_fragment,container,false);
        Initiate(rootView);
        context = container.getContext();
        GetTicket();
        ShowDevices();
        return rootView;
    }

    private void Initiate(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        lstMyTicket = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(),ShowDetail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", lstMyTicket.get(position).getId());
                bundle.putString("name", lstMyTicket.get(position).getName());
                bundle.putString("description", lstMyTicket.get(position).getDescription());
                bundle.putString("issue", lstMyTicket.get(position).getIssue());
                bundle.putString("url_image", lstMyTicket.get(position).getUrl_image());
                bundle.putString("create_time", lstMyTicket.get(position).getCreate_time());
                bundle.putString("approver", lstMyTicket.get(position).getApprover());
                bundle.putString("creater", lstMyTicket.get(position).getCreater());
                bundle.putString("note", lstMyTicket.get(position).getNote());
                bundle.putString("date_approve", lstMyTicket.get(position).getDateApprove());

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(),"LongClick",Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void GetTicket(){
        for(int i = 0; i < AppConfig.LST_DEVICES.size(); i++){
            if(AppConfig.LST_DEVICES.get(i).getCreater().equals(AppConfig.NAME_USER)){
                lstMyTicket.add(AppConfig.LST_DEVICES.get(i));
            }
        }
    }
    public void ShowDevices(){
        if (lstMyTicket.size() >= 1) {
            mAdapter = new DevicesAdapter(lstMyTicket, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //Adding RecyclerView Divider / Separator
            recyclerView.addItemDecoration(new MyDividerItemDecoration(context,LinearLayoutManager.VERTICAL,16));
            recyclerView.setAdapter(mAdapter);
        }
    }
}
