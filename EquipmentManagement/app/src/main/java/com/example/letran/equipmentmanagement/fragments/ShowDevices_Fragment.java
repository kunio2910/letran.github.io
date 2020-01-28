package com.example.letran.equipmentmanagement.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.adapter.DevicesAdapter;
import com.example.letran.equipmentmanagement.models.Device;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowDevices_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private DevicesAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alldevices,container,false);
        Initiate(rootView);
        pDialog = new ProgressDialog(container.getContext());
        GetAllDevices(container.getContext());
        return rootView;
    }

    private void Initiate(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
    }

    private void GetAllDevices(final Context context){
        // Tag used to cancel the request
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
                            Log.e("info", "Login Response: " + response.toString());

                            AppConfig.LST_DEVICES.add(device);
                            Log.e("test","test");
                        }

                        //Dua data vao recycleview
                        if (AppConfig.LST_DEVICES.size() >= 1) {
                            mAdapter = new DevicesAdapter(AppConfig.LST_DEVICES, context);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            //recyclerView.addItemDecoration(new MyDividerItemDecoration(ShowProducts_Activity.this,LinearLayoutManager.VERTICAL,16));
                            recyclerView.setAdapter(mAdapter);
                        }
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
}