package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.utils.AppConfig;
import com.example.letran.equipmentmanagement.utils.AppController;
import com.example.letran.equipmentmanagement.utils.Encrypte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class Registration_Activity extends Activity implements View.OnClickListener {

    private EditText inputName, inputPassword, inputPasswordAgain;
    private Button btnRegistration,btnChoseAvatar,btnShowPassword_1,btnShowPassword_2;
    private ImageView imageAvatar;
    private ProgressDialog  pDialog;
    private boolean isExitName,isShow_1,isShow_2;
    private String image_encode_avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Initiate();
        btnRegistration.setOnClickListener(this);
        btnChoseAvatar.setOnClickListener(this);
    }

    private void Initiate(){
        AppConfig.FLAG = 1;
        image_encode_avatar = "";
        isShow_1 = false;
        isShow_2 = false;
        inputName = (EditText)findViewById(R.id.edtName);
        inputPassword = (EditText)findViewById(R.id.edtPassword);
        inputPasswordAgain = (EditText)findViewById(R.id.edtPassword_again);
        btnRegistration = (Button)findViewById(R.id.btnRegistration);
        btnChoseAvatar = (Button)findViewById(R.id.btnChooseAvatar);
        btnShowPassword_1 = (Button)findViewById(R.id.btnEye_1);
        btnShowPassword_2 = (Button)findViewById(R.id.btnEye_2);
        imageAvatar = (ImageView)findViewById(R.id.imgAvatar);

        pDialog = new ProgressDialog(Registration_Activity.this);
        pDialog.setCancelable(false);

        isExitName = false;

        btnShowPassword_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow_1 == false){
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnShowPassword_1.setBackgroundResource(R.drawable.close_password);
                }else if(isShow_1 == true){
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnShowPassword_1.setBackgroundResource(R.drawable.open_password);
                }
                isShow_1 = !isShow_1;
            }
        });

        btnShowPassword_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow_2 == false){
                    inputPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnShowPassword_2.setBackgroundResource(R.drawable.close_password);
                }else if(isShow_2 == true){
                    inputPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnShowPassword_2.setBackgroundResource(R.drawable.open_password);
                }
                isShow_2 = !isShow_2;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistration:
                String name = inputName.getText().toString();
                String password = inputPassword.getText().toString();
                String password_again = inputPasswordAgain.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password_again)){
                    Toasty.warning(getApplicationContext(), "Enter name and password and password again", Toast.LENGTH_SHORT, true).show();
                    return;
                }else if(!password.equals(password_again)){
                    Toasty.error(getApplicationContext(), "Password and password again is dissimilar", Toast.LENGTH_SHORT, true).show();
                    return;
                }else
                    CheckName(name,password);

                break;
            case R.id.btnChooseAvatar:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                //resize bitmap
                mBitmap = Bitmap.createScaledBitmap(mBitmap,1200,900,false);
                imageAvatar.setImageBitmap(mBitmap);

                BitmapDrawable drawable = (BitmapDrawable) imageAvatar.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //resize encode
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb,0);
                image_encode_avatar = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        Toasty.error(getApplicationContext(), "Name is exited...! ", Toast.LENGTH_SHORT, true).show();
                    }else {
                        String encrypt_password = Encrypte.encrypt(password);
                        CallRegistration(name, encrypt_password, image_encode_avatar);
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
                Toasty.warning(getApplicationContext(), "Please login again...!", Toast.LENGTH_SHORT, true).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void CallRegistration(final String name, final String password, final String image_encode_avatar) {
        // Tag used to cancel the request
        String tag_string_req = "req_registration";

        pDialog.setMessage("Registration ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppConfig.FLAG = 0;
                hideDialog();
                Toasty.success(getApplicationContext(), "Registration Completed...!", Toast.LENGTH_SHORT, true).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                        Intent intent = new Intent(Registration_Activity.this,Login_Activity.class);
                        startActivity(intent);
                    }
                }, 1000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(), "Please registration again...!", Toast.LENGTH_SHORT, true).show();
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
                params.put("avatar", image_encode_avatar);

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
