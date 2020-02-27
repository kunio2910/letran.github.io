package com.example.letran.equipmentmanagement.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.letran.equipmentmanagement.utils.Encrypte;
import com.example.letran.equipmentmanagement.views.MainActivity;
import com.example.letran.equipmentmanagement.views.Personal_Activity;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Profile_Fragment extends Fragment implements View.OnClickListener {

    private EditText edtName, edtPassword, edtCreate_time;
    private Button btnCancel, btnChangePassword;
    private ProgressDialog pDialog;
    boolean isShow_1,isShow_2,isShow_3;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment,container,false);
        context = container.getContext();
        Initiate(rootView);
        GetInfor();
        return rootView;
    }

    private void Initiate(View view) {
        AppConfig.FLAG = 1;
        isShow_1 =false;
        isShow_2 =false;
        isShow_3 =false;
        edtName = (EditText)view.findViewById(R.id.edtname);
        edtPassword = (EditText)view.findViewById(R.id.edtpassword);
        edtCreate_time = (EditText)view.findViewById(R.id.edtcreatetime);
        btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnChangePassword = (Button)view.findViewById(R.id.btnChangePassword);
        pDialog = new ProgressDialog(context);

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
                Intent intent = new Intent(getContext(),MainActivity.class);
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
        final Button btnEye_1 = (Button)alertLayout.findViewById(R.id.btnEye_1);
        final Button btnEye_2 = (Button)alertLayout.findViewById(R.id.btnEye_2);
        final Button btnEye_3 = (Button)alertLayout.findViewById(R.id.btnEye_3);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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
                    Toasty.warning(getContext(), "Enter oldpassword and newpassword and confirmpassword again...!", Toast.LENGTH_SHORT, true).show();
                    return;
                }else if(!oldpassword.equals(AppConfig.PASSWORD_USER)){
                    Toasty.error(getContext(), "OldPassword is not correct...!", Toast.LENGTH_SHORT, true).show();
                }else if(!newpassword.equals(confirmpassword)){
                    Toasty.error(getContext(), "Password and confirmpassword is not same...!", Toast.LENGTH_SHORT, true).show();
                }else{
                    try {
                        newpassword = Encrypte.encrypt(newpassword);
                        CallUpdatePassword(AppConfig.ID_USER, newpassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        btnEye_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow_1 == false){
                    edtOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnEye_1.setBackgroundResource(R.drawable.close_password);
                }else if(isShow_1 == true){
                    edtOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnEye_1.setBackgroundResource(R.drawable.open_password);
                }
                isShow_1 = !isShow_1;
            }
        });

        btnEye_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow_2 == false){
                    edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnEye_2.setBackgroundResource(R.drawable.close_password);
                }else if(isShow_2 == true){
                    edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnEye_2.setBackgroundResource(R.drawable.open_password);
                }
                isShow_2 = !isShow_2;
            }
        });

        btnEye_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow_3 == true){
                    edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnEye_3.setBackgroundResource(R.drawable.close_password);
                }else if(isShow_3 == false){
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnEye_3.setBackgroundResource(R.drawable.open_password);
                }
                isShow_3 = !isShow_3;
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void CallUpdatePassword(final String id,final String newpassword) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatepassword";


        pDialog.setMessage("Update Password...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("info", "Update Response: " + response.toString());
                AppConfig.FLAG = 0;
                hideDialog();
                Toasty.success(getContext(), "Update Completed...!", Toast.LENGTH_SHORT, true).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, 1000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Update Error: " + error.getMessage());
                Toasty.warning(getContext(), "Please click update again...!", Toast.LENGTH_SHORT, true).show();
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

    @Override
    public void onStop() {
        super.onStop();
        AppConfig.FLAG = 1;
    }
}
