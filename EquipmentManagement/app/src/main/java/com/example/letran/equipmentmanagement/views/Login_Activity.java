package com.example.letran.equipmentmanagement.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.letran.equipmentmanagement.utils.Encrypte;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class Login_Activity extends Activity implements View.OnClickListener {

    private EditText inputName, inputPassword;
    private Button btnLogin, btnRegistration,btnShowPassword;
    private CheckBox cbRememberLogin;
    private ProgressDialog pDialog;
    private DatabaseHelper db;
    private boolean isCheckLogin,saveLogin,isShow = false;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            Initiate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);

        btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow == false){
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnShowPassword.setBackgroundResource(R.drawable.close_password);
                }else if(isShow == true){
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnShowPassword.setBackgroundResource(R.drawable.open_password);
                }
                isShow = !isShow;
            }
        });
    }

    private void Initiate() throws Exception {
        AppConfig.NAME_USER = "";
        AppConfig.PASSWORD_USER = "";
        AppConfig.PERMISSION_USER = "";
        AppConfig.ID_USER = "";
        AppConfig.AVATAR = "";

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegistration = (Button)findViewById(R.id.btnLinkToRegistrerScreen);
        btnShowPassword = (Button)findViewById(R.id.btnEye);
        cbRememberLogin = (CheckBox)findViewById(R.id.cbRememberLogin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        inputName = (EditText)findViewById(R.id.edtName);
        inputPassword = (EditText)findViewById(R.id.edtPassword);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new DatabaseHelper(Login_Activity.this );
        db.getWritableDatabase();

        isCheckLogin = false;
        CallRememberLogin();

    }

    private void CallRememberLogin() throws Exception {
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            showDialog();
            inputName.setText(loginPreferences.getString("name", ""));
            String password = loginPreferences.getString("password", "");
            String decrypt_password = Encrypte.decrypt(password);
            inputPassword.setText(decrypt_password);

            AppConfig.NAME_USER = loginPreferences.getString("username", "");
            AppConfig.PERMISSION_USER = loginPreferences.getString("permission", "");
            AppConfig.PASSWORD_USER = loginPreferences.getString("password", "");
            AppConfig.ID_USER = loginPreferences.getString("id", "");
            AppConfig.CREATE_TIME_USER = loginPreferences.getString("create_time", "");
            AppConfig.AVATAR = loginPreferences.getString("avatar", "");

            cbRememberLogin.setChecked(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Magic here
                    hideDialog();
                    Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                    startActivity(intent);
                }
            }, 700);
        }else{
            cbRememberLogin.setChecked(false);
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }

    @Override
    public void onClick(View v) {
        String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();
        switch (v.getId()){
            case R.id.btnLogin:
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password)){
                    Toasty.warning(getApplicationContext(), "Enter name and password", Toast.LENGTH_SHORT, true).show();
                    return;
                }else
                    CallLogin(name, password);
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

                        String decrypt_password = Encrypte.decrypt(password_temp);
                        if(name.equals(name_temp) && password.equals(decrypt_password)){
                            isCheckLogin = true;
                            AppConfig.NAME_USER = name;
                            AppConfig.PERMISSION_USER = permission_temp;
                            AppConfig.PASSWORD_USER = password;
                            AppConfig.ID_USER = id_temp;
                            AppConfig.CREATE_TIME_USER = create_time_temp;
                            AppConfig.AVATAR = avatar_temp;

                            if(cbRememberLogin.isChecked()){
                                loginPrefsEditor.putBoolean("saveLogin",true);
                                loginPrefsEditor.putString("name",name_temp);
                                loginPrefsEditor.putString("password",password_temp);
                                loginPrefsEditor.putString("id",id_temp);
                                loginPrefsEditor.putString("permission",permission_temp);
                                loginPrefsEditor.putString("create_time",create_time_temp);
                                loginPrefsEditor.putString("avatar",avatar_temp);
                                loginPrefsEditor.commit();
                            }
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
                        Toasty.error(getApplicationContext(), "The user name or password is incorrect ...", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toasty.error(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toasty.error(getApplicationContext(), "Please login again ...", Toast.LENGTH_SHORT, true).show();
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
