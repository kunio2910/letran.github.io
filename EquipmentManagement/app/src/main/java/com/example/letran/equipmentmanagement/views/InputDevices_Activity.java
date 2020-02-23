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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

import es.dmoral.toasty.Toasty;

public class InputDevices_Activity extends DrawerLayout_Activity implements View.OnClickListener {

    EditText edtNameDevice,edtDescription,edtIssue;
    Button btnAdd,btnChoseImage;
    ImageView imageView;
    private ProgressDialog pDialog;
    String image_encode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.inputdevices_activity);
//
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.inputdevices_activity, null, false);
        mDrawer.addView(contentView, 0);

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
                    AddDevice(name,description,issue,image_encode);
                    btnAdd.setEnabled(false);
                } else {
                    Toasty.warning(getApplicationContext(), "Please enter device details...!", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.btnChooseImage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2)
        {
            Uri chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                //resize bitmap
                mBitmap = Bitmap.createScaledBitmap(mBitmap,1200,900,false);
                imageView.setImageBitmap(mBitmap);

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //resize encode
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb,0);
                image_encode = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void AddDevice(final String name,final String description,final String issue,final String url_image){
        // Tag used to cancel the request
        String tag_string_req = "req_createdevice";

        pDialog.setMessage("POST DATA ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.CREATE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toasty.success(getApplicationContext(), "Create Data Completed...!", Toast.LENGTH_SHORT, true).show();
                AppConfig.FLAG = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Magic here
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                }, 1000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Login Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(), "Please input again ...!", Toast.LENGTH_SHORT, true).show();
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
                params.put("url_image", url_image);
                //params.put("create_time", String.valueOf(currentTime));
                params.put("create_time", currentDate);
                params.put("approver", "");
                params.put("creater", AppConfig.NAME_USER);

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
    protected void onResume() {
        super.onResume();
        AppConfig.FLAG = 1;
    }
}
