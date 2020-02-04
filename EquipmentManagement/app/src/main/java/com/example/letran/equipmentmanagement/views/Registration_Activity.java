package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Registration_Activity extends Activity implements View.OnClickListener {

    private EditText inputName, inputPassword, inputPasswordAgain;
    private Button btnRegistration;
    private ProgressDialog  pDialog;
    private boolean isExitName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Initiate();
        btnRegistration.setOnClickListener(this);
    }

    private void Initiate(){
        inputName = (EditText)findViewById(R.id.edtName);
        inputPassword = (EditText)findViewById(R.id.edtPassword);
        inputPasswordAgain = (EditText)findViewById(R.id.edtPassword_again);
        btnRegistration = (Button)findViewById(R.id.btnRegistration);

        pDialog = new ProgressDialog(Registration_Activity.this);
        pDialog.setCancelable(false);

        isExitName = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistration:
                String name = inputName.getText().toString();
                String password = inputPassword.getText().toString();
                String password_again = inputPasswordAgain.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password_again)){
                    Toast.makeText(getApplicationContext(), "Enter name and password and password again", Toast.LENGTH_LONG).show();
                    return;
                }else if(!password.equals(password_again)){
                    Toast.makeText(getApplicationContext(), "Password and password again is dissimilar", Toast.LENGTH_LONG).show();
                    return;
                }else
                    CheckName(name,password);

                break;
            default:
                break;
        }
    }

    //Kiem tra name da ton tai hay chua
    private void CheckName(final String name, final String password){
        // Tag used to cancel the request
        String tag_string_req = "req_checkname";

        pDialog.setMessage("Check name ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.CHECK_USER_NAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("users");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_data = jsonArray.getJSONObject(i);
                        String name_temp = json_data.getString("name");
                        Log.e("info", "Login Response: " + response.toString());
                        if (name.equals(name_temp)) {
                            isExitName = false;
                            break;
                        } else
                            isExitName = true;
                    }

                    if(isExitName == false){
                        Toast.makeText(getApplicationContext(), "Name is exited ! ", Toast.LENGTH_LONG).show();
                    }else
                        CallRegistration(name,password);
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Please login again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void CallRegistration(final String name, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_registration";

        pDialog.setMessage("Registration ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(Registration_Activity.this,"Registration Completed !",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                        Intent intent = new Intent(Registration_Activity.this,Login_Activity.class);
                        startActivity(intent);
                    }
                }, 3000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Please login again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("password", password);
                params.put("permission", "0");
                params.put("create_time", String.valueOf(currentDate));

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
}
