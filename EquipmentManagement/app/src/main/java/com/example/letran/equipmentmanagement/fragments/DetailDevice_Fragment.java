package com.example.letran.equipmentmanagement.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;
import com.example.letran.equipmentmanagement.views.Login_Activity;
import com.example.letran.equipmentmanagement.views.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailDevice_Fragment extends Fragment implements View.OnClickListener {

    private String name, description, issue, url_image, create_time, approver, id;
    private TextView txtname, txtcreate_time, txtdescription, txtissue, txturl_image;
    private ImageView image;
    private Button btnapprove, btnchange, btndelete;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_showdetaildevice, container, false);
        Inititate(rootView);
        GetInfo();
        ShowDetailDevice();
        return rootView;
    }

    private void Inititate(View view) {
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtcreate_time = (TextView) view.findViewById(R.id.txtcreatetime);
        txtdescription = (TextView) view.findViewById(R.id.txtdescription);
        txtissue = (TextView) view.findViewById(R.id.txtIssue);
        image = (ImageView) view.findViewById(R.id.image);
        btnapprove = (Button) view.findViewById(R.id.btnapprove);
        btnchange = (Button) view.findViewById(R.id.btnchange);
        btndelete = (Button) view.findViewById(R.id.btndelete);
        pDialog = new ProgressDialog(getContext());

        //if (AppConfig.PERMISSION_USER.equals("1")) {
            btnapprove.setEnabled(true);
            btnchange.setEnabled(true);
            btndelete.setEnabled(true);
        //}

        btnapprove.setOnClickListener(this);
        btnchange.setOnClickListener(this);
        btndelete.setOnClickListener(this);
    }

    private void GetInfo() {
        Bundle bundle = getActivity().getIntent().getExtras();
        id = bundle.getString("id");
        name = bundle.getString("name");
        description = bundle.getString("description");
        issue = bundle.getString("issue");
        url_image = bundle.getString("url_image");
        create_time = bundle.getString("create_time");
        approver = bundle.getString("approver");
    }

    private void ShowDetailDevice() {
        txtname.setText(name);
        txtcreate_time.setText(create_time);
        txtdescription.setText(description);
        txtissue.setText(issue);

        if (!url_image.isEmpty()) {
            Picasso.with(getContext()).load(String.valueOf(url_image)).into(image);
        } else {
            image.setImageResource(R.drawable.notfoundimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnapprove:
                CallApprove(approver, id);
                break;
            case R.id.btnchange:
                CallChangeInfor(id, name, description, issue, url_image, create_time, approver);
                break;
            case R.id.btndelete:
                CallDelete(id);
                break;
            default:
                break;
        }
    }

    private void CallApprove(final String approver, final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatedevice";

        pDialog.setMessage("Approve...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.APPROVE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Approve Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Please click approve again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Date currentTime = Calendar.getInstance().getTime();
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("approver", AppConfig.NAME_USER);
                params.put("id", id);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(getContext()).addToRequestQueue(strReq, tag_string_req);
    }

    private void CallDelete(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatedevice";

        pDialog.setMessage("Approve...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.DELETE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Approve Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Please click approve again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Date currentTime = Calendar.getInstance().getTime();
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(getContext()).addToRequestQueue(strReq, tag_string_req);
    }


    private void CallChangeInfor(final String id, final String name, final String description, final String issue, final String url_image, final String create_time, final String approver) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatedevice";

        pDialog.setMessage("Approve...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.DELETE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Approve Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        "Please click approve again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Date currentTime = Calendar.getInstance().getTime();
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("name", name);
                params.put("description", description);
                params.put("issue", issue);
                params.put("url_image", url_image);
                params.put("create_time", create_time);
                params.put("approver", approver);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(getContext()).addToRequestQueue(strReq, tag_string_req);
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
