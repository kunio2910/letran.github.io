package com.example.letran.equipmentmanagement.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.letran.equipmentmanagement.views.Registration_Activity;
import com.example.letran.equipmentmanagement.views.ShowDetail_Activity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DetailDevice_Fragment extends Fragment implements View.OnClickListener {

    private String name_device, description, issue, url_image, create_time, approver,creater, id, name_temp, description_temp, issue_temp;
    private EditText edtname, edtcreate_time, edtdescription, edtissue, edtcreater;
    private TextView txturl_image,txtnote;
    private ImageView imageview;
    private Button btnapprove, btnchange, btndelete, btnchoseimage;
    private ProgressDialog pDialog;
    private String image_encode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_showdetaildevice, container, false);

        GetInfor();
        Inititate(rootView);
        ShowDetailDevice();
        return rootView;
    }

    private void Inititate(View view) {
        AppConfig.FLAG = 1;
        edtname = (EditText) view.findViewById(R.id.txtname);
        edtcreate_time = (EditText) view.findViewById(R.id.txtcreatetime);
        edtdescription = (EditText) view.findViewById(R.id.txtdescription);
        edtissue = (EditText) view.findViewById(R.id.txtIssue);
        edtcreater = (EditText) view.findViewById(R.id.txtCreater);
        imageview = (ImageView) view.findViewById(R.id.image);
        btnapprove = (Button) view.findViewById(R.id.btnapprove);
        btnchange = (Button) view.findViewById(R.id.btnchange);
        btndelete = (Button) view.findViewById(R.id.btndelete);
        btnchoseimage = (Button) view.findViewById(R.id.btnChooseImage);
        txtnote = (TextView)view.findViewById(R.id.note);

        pDialog = new ProgressDialog(getContext());
        txtnote.setVisibility(View.INVISIBLE);
        btnchoseimage.setVisibility(View.INVISIBLE);

        image_encode = "";

        if (AppConfig.NAME_USER.equals(creater)) {
            btndelete.setEnabled(true);
        }

        if(AppConfig.PERMISSION_USER.equals("1") && approver.isEmpty()){
            btnapprove.setEnabled(true);
        }

        //device duoc approve se khong duoc change
        if(approver.isEmpty() && AppConfig.NAME_USER.equals(creater))
            btnchange.setEnabled(true);

        btnapprove.setOnClickListener(this);
        btnchange.setOnClickListener(this);
        btndelete.setOnClickListener(this);
        btnchoseimage.setOnClickListener(this);
    }

    private void GetInfor() {
        Bundle bundle = getActivity().getIntent().getExtras();
        id = bundle.getString("id");
        name_device = bundle.getString("name");
        description = bundle.getString("description");
        issue = bundle.getString("issue");
        url_image = bundle.getString("url_image");
        create_time = bundle.getString("create_time");
        approver = bundle.getString("approver");
        creater = bundle.getString("creater");
    }

    private void ShowDetailDevice() {
        edtname.setHint(name_device);
        edtcreate_time.setHint(create_time);
        edtdescription.setHint(description);
        edtissue.setHint(issue);
        edtcreater.setHint(creater);

        if (!url_image.isEmpty()) {
            //Picasso.with(getContext()).load(String.valueOf(url_image)).into(image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            //decode base64 string to image
            imageBytes = Base64.decode(url_image, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Bitmap bMap = Bitmap.createScaledBitmap(decodedImage, 1200, 900, true);
            imageview.setImageBitmap(bMap);
        } else {
            imageview.setImageResource(R.drawable.notfoundimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnapprove:
                CallApprove(approver, id);
                break;
            case R.id.btnchange:
                if(btnchange.getText().equals("CHANGE")) {
                    edtname.setEnabled(true);
                    edtdescription.setEnabled(true);
                    edtissue.setEnabled(true);
                    btnchoseimage.setVisibility(View.VISIBLE);
                    btnchange.setText("UPDATE");
                }else if(btnchange.getText().equals("UPDATE")){
                    btnchange.setText("CHANGE");
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                    //if(!image_encode.isEmpty())
                        //CallUpdateImage(AppConfig.NAME_USER,edtname.getText().toString(),image_encode);

                    if(edtname.getText().toString().isEmpty())
                         name_temp = name_device;
                    else
                        name_temp = edtname.getText().toString();

                    if(edtdescription.getText().toString().isEmpty())
                         description_temp = description;
                    else
                        description_temp = edtdescription.getText().toString();

                    if(edtissue.getText().toString().isEmpty())
                         issue_temp = issue;
                    else
                        issue_temp = edtissue.getText().toString();

                    CallChangeInfor(id, name_temp, description_temp, issue_temp, image_encode, currentDate, "");
                }
                break;
            case R.id.btndelete:
                CallDelete(id);
                break;
            case R.id.btnChooseImage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), chosenImageUri);
                mBitmap = Bitmap.createScaledBitmap(mBitmap,1200,900,false);
                imageview.setImageBitmap(mBitmap);

                BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb,0);
                image_encode = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void CallApprove(final String approver, final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatedevice";
        AppConfig.FLAG = 0;
        pDialog.setMessage("Approve...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.APPROVE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();

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

        AppConfig.FLAG = 0;
        pDialog.setMessage("Approve...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.DELETE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();

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

        AppConfig.FLAG = 0;
        pDialog.setMessage("Update Infor...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());


                image_encode = "";
                edtname.setEnabled(false);
                edtdescription.setEnabled(false);
                edtissue.setEnabled(false);
                edtcreate_time.setText(create_time);
                btnchange.setEnabled(false);
                btnchoseimage.setVisibility(View.INVISIBLE);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();

                //decode base64 string to image
                imageBytes = Base64.decode(url_image, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Bitmap bMap = Bitmap.createScaledBitmap(decodedImage, 400, 300, true);
                imageview.setImageBitmap(bMap);
                //txtnote.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppConfig.FLAG = 1;
                        hideDialog();
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                }, 1000);

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

    private void CallUpdateImage(final String name_user,final String name_image, final String image) {
        // Tag used to cancel the request
        String tag_string_req = "req_updateimage";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.CREATE_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("info", "Update Response: " + response.toString());

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("name_user", name_user);
                params.put("name_image", name_image);
                params.put("image", image);

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
