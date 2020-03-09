package com.example.letran.equipmentmanagement.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.DevicesAdapter;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.MyDividerItemDecoration;
import com.example.letran.equipmentmanagement.utils.RecyclerTouchListener;
import com.example.letran.equipmentmanagement.views.ShowDetail_Activity;

public class Approved_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private DevicesAdapter mAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waitapprove_fragment,container,false);
        Initiate(rootView);
        context = container.getContext();
        return rootView;
    }

    private void Initiate(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(),ShowDetail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", AppConfig.LST_DEVICES_APPROVED.get(position).getId());
                bundle.putString("name", AppConfig.LST_DEVICES_APPROVED.get(position).getName());
                bundle.putString("description", AppConfig.LST_DEVICES_APPROVED.get(position).getDescription());
                bundle.putString("issue", AppConfig.LST_DEVICES_APPROVED.get(position).getIssue());
                bundle.putString("url_image", AppConfig.LST_DEVICES_APPROVED.get(position).getUrl_image());
                bundle.putString("create_time", AppConfig.LST_DEVICES_APPROVED.get(position).getCreate_time());
                bundle.putString("approver", AppConfig.LST_DEVICES_APPROVED.get(position).getApprover());
                bundle.putString("creater", AppConfig.LST_DEVICES_APPROVED.get(position).getCreater());
                bundle.putString("note", AppConfig.LST_DEVICES_APPROVED.get(position).getNote());
                bundle.putString("date_approve", AppConfig.LST_DEVICES_APPROVED.get(position).getDateApprove());
                bundle.putString("approver_1", AppConfig.LST_DEVICES_APPROVED.get(position).getApprover_1());
                bundle.putString("note_1", AppConfig.LST_DEVICES_APPROVED.get(position).getNote_1());
                bundle.putString("date_approve_1", AppConfig.LST_DEVICES_APPROVED.get(position).getDateApprove_1());

                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(),"LongClick",Toast.LENGTH_LONG).show();
            }
        }));
    }

    public void ShowDevices(){
        if (AppConfig.LST_DEVICES_APPROVED.size() >= 1) {
            mAdapter = new DevicesAdapter(AppConfig.LST_DEVICES_APPROVED, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //Adding RecyclerView Divider / Separator
            recyclerView.addItemDecoration(new MyDividerItemDecoration(context,LinearLayoutManager.VERTICAL,16));
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ShowDevices();
    }
}
