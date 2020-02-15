package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;

import java.util.HashMap;
import java.util.Map;

public class Personal_Activity extends DrawerLayout_Activity implements View.OnClickListener {

    private EditText edtName, edtPassword, edtCreate_time;
    private Button btnCancel, btnChangePassword;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.personal_activity);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.personal_activity, null, false);
        mDrawer.addView(contentView, 0);

        Initiate();
        GetInfor();
    }

    private void Initiate() {
        AppConfig.FLAG = 1;
        edtName = (EditText) findViewById(R.id.edtname);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        edtCreate_time = (EditText) findViewById(R.id.edtcreatetime);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        pDialog = new ProgressDialog(this);

        btnCancel.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
    }

    private void GetInfor(){
        edtName.setText(AppConfig.NAME_USER);
        edtPassword.setText(AppConfig.PASSWORD_USER);
        edtCreate_time.setText(AppConfig.CREATE_TIME_USER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                Intent intent = new Intent(Personal_Activity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnChangePassword:
                ShowDialog();
                break;
            default:
                break;
        }
    }

    private void ShowDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.changepassword_dialog, null);
        final EditText edtOldPassword = (EditText)alertLayout.findViewById(R.id.edtOldPassword);
        final EditText edtNewPassword = (EditText)alertLayout.findViewById(R.id.edtNewPassword);
        final EditText edtConfirmPassword = (EditText)alertLayout.findViewById(R.id.edtConfirmPassword);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("CHANGE PASSWORD");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldpassword = edtOldPassword.getText().toString();
                String newpassword = edtNewPassword.getText().toString();
                String confirmpassword = edtConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(oldpassword) || TextUtils.isEmpty(oldpassword) || TextUtils.isEmpty(oldpassword)){
                    Toast.makeText(Personal_Activity.this, "Enter oldpassword and newpassword and confirmpassword again", Toast.LENGTH_LONG).show();
                    return;
                }else if(!oldpassword.equals(AppConfig.PASSWORD_USER)){
                    Toast.makeText(Personal_Activity.this,"OldPassword is not correct !",Toast.LENGTH_LONG).show();
                }else if(!newpassword.equals(confirmpassword)){
                    Toast.makeText(Personal_Activity.this,"Password and confirmpassword is not same !",Toast.LENGTH_LONG).show();
                }else{
                    CallUpdatePassword(AppConfig.ID_USER, newpassword);
                }
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void CallUpdatePassword(final String id,final String newpassword) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatepassword";

        AppConfig.FLAG = 0;
        pDialog.setMessage("Update Password...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Update Response: " + response.toString());
                hideDialog();
                Intent intent = new Intent(Personal_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Update Error: " + error.getMessage());
                Toast.makeText(Personal_Activity.this,
                        "Please click update again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("password", newpassword);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
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
    public void onStop() {
        super.onStop();
        AppConfig.FLAG = 1;
    }
}
