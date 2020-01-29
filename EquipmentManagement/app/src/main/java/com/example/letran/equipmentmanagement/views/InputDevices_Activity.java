package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputDevices_Activity extends Activity implements View.OnClickListener {

    EditText edtNameDevice,edtDescription,edtIssue;
    Button btnAdd,btnChoseImage;
    ImageView imageView;
    private ProgressDialog pDialog;
    String image_encode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputdevices_activity);
        Initiate();
    }

    private void Initiate(){
        edtNameDevice = (EditText)findViewById(R.id.edtNameDevice);
        edtDescription = (EditText)findViewById(R.id.edtDescription);
        edtIssue = (EditText)findViewById(R.id.edtIssue);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnChoseImage = (Button)findViewById(R.id.btnChooseImage);
        imageView = (ImageView)findViewById(R.id.image);

        pDialog = new ProgressDialog(InputDevices_Activity.this);

        btnChoseImage.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                String name = edtNameDevice.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String issue = edtIssue.getText().toString().trim();

                if (!name.isEmpty() && !description.isEmpty() && !issue.isEmpty()) {
                    AddImage(AppConfig.NAME_USER,name,image_encode);
                    AddDevice(name,description,issue);
                    btnAdd.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter device details!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.btnChooseImage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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
                imageView.setImageBitmap(mBitmap);

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb,0);
                image_encode = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void AddDevice(final String name,final String description,final String issue){
        // Tag used to cancel the request
        String tag_string_req = "req_createdevice";

        pDialog.setMessage("POST DATA ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.CREATE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(InputDevices_Activity.this,"Create Data Completed !",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                        Intent intent = new Intent(InputDevices_Activity.this,MainActivity.class);
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
                btnAdd.setEnabled(true);
                hideDialog();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                //Date currentTime = Calendar.getInstance().getTime();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("description", description);
                params.put("issue", issue);
                params.put("url_image", "");
                //params.put("create_time", String.valueOf(currentTime));
                params.put("create_time", currentDate);
                params.put("approver", "");

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void AddImage(final String name_user,final String name,final String image){
        // Tag used to cancel the request
        String tag_string_req = "req_createdevice";

        //pDialog.setMessage("POST DATA ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.CREATE_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //hideDialog();
                //Toast.makeText(InputDevices_Activity.this,"Create Image Completed !",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Please post again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name_user", name_user);
                params.put("name_image", name);
                params.put("image", image_encode);

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
