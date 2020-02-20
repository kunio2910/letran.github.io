package com.example.letran.equipmentmanagement.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.letran.equipmentmanagement.database.DatabaseHelper;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login_Activity extends Activity implements View.OnClickListener {

    private EditText inputName, inputPassword;
    private Button btnLogin, btnRegistration;
    private ProgressDialog pDialog;
    private DatabaseHelper db;
    private boolean isCheckLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Initiate();

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    private void Initiate(){
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegistration = (Button)findViewById(R.id.btnLinkToRegistrerScreen);

        inputName = (EditText)findViewById(R.id.edtName);
        inputPassword = (EditText)findViewById(R.id.edtPassword);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new DatabaseHelper(Login_Activity.this );
        db.getWritableDatabase();

        isCheckLogin = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String name = inputName.getText().toString();
                String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter name and password", Toast.LENGTH_LONG).show();
                    return;
                }else
                    CallLogin(name,password);
                break;
            case R.id.btnLinkToRegistrerScreen:
                Intent intent = new Intent(Login_Activity.this, Registration_Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void CallLogin(final String name,final String password){
        // Tag used to cancel the request
        AppConfig.NAME_USER = "";
        AppConfig.PASSWORD_USER = "";
        AppConfig.PERMISSION_USER = "";
        AppConfig.ID_USER = "";
        AppConfig.AVATAR = "";
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.CHECK_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("users");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json_data = jsonArray.getJSONObject(i);
                        String id_temp = json_data.getString("id");
                        String name_temp = json_data.getString("name");
                        String password_temp = json_data.getString("password");
                        String permission_temp = json_data.getString("permission");
                        String create_time_temp = json_data.getString("create_time");
                        String avatar_temp = json_data.getString("avatar");
                        Log.e("info", "Login Response: " + response.toString());
                        if(name.equals(name_temp) && password.equals(password_temp)){
                            isCheckLogin = true;
                            AppConfig.NAME_USER = name;
                            AppConfig.PERMISSION_USER = permission_temp;
                            AppConfig.PASSWORD_USER = password;
                            AppConfig.ID_USER = id_temp;
                            AppConfig.CREATE_TIME_USER = create_time_temp;
                            AppConfig.AVATAR = avatar_temp;
                            break;
                        }
                    }

                    // Check for error node in json
                    if (isCheckLogin == true) {
                        // user successfully logged in

                        // Launch main activity
                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "The user name or password is incorrect ...", Toast.LENGTH_LONG).show();
                    }
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
