package com.example.letran.equipmentmanagement.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DrawerLayout_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView imageView;
    private TextView txtname;
    private String image_encode_avatar;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();
        setContentView(R.layout.drawerlayout_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //Change color for actionbar
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26ae90")));

        Drawable d = getResources().getDrawable(R.drawable.background_nav_header);
        actionBar.setBackgroundDrawable(d);

        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(DrawerLayout_Activity.this, mDrawer, R.string.ns_menu_open, R.string.ns_menu_close);
        mDrawer.addDrawerListener(drawerToggle);
        //calling sync state is compulsory to avoid unusual behaviour as home button wont show up
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageView = navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
        txtname = navigationView.getHeaderView(0).findViewById(R.id.txtname);
        pDialog = new ProgressDialog(DrawerLayout_Activity.this);

        if (!AppConfig.AVATAR.isEmpty()) {
            SetAvatar();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                //resize bitmap
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 1200, 900, false);
                imageView.setImageBitmap(mBitmap);

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //resize encode
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                byte[] bb = bos.toByteArray();
                String image = Base64.encodeToString(bb, 0);
                image_encode_avatar = image;

                CallUpdateAvatar(AppConfig.ID_USER, image_encode_avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void SetAvatar() {
        if (!AppConfig.AVATAR.isEmpty()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            //decode base64 string to image
            imageBytes = Base64.decode(AppConfig.AVATAR, Base64.DEFAULT);
            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = calculateInSampleSize(options, 400,300);
            //options.inJustDecodeBounds = false;
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Bitmap bMap = Bitmap.createScaledBitmap(decodedImage, 400, 300, true);
            imageView.setImageBitmap(bMap);
        } else {
            imageView.setImageResource(R.drawable.notfoundimage);
        }
        txtname.setText(AppConfig.NAME_USER);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Profile_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(DrawerLayout_Activity.this, SearchDevice_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_piechart) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Chart_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Login_Activity.class);
            startActivity(intent);
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void CallUpdateAvatar(final String id, final String image) {
        // Tag used to cancel the request
        String tag_string_req = "req_updatepassword";


        pDialog.setMessage("Update Avatar...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UPDATE_AVATAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("info", "Update Response: " + response.toString());
                AppConfig.FLAG = 0;
                CallGetAvatar(AppConfig.ID_USER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("infor", "Update Error: " + error.getMessage());
                Toast.makeText(DrawerLayout_Activity.this,
                        "Please click update again ...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("avatar", image);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void CallGetAvatar(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.GET_AVATAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("info", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String avatar_temp = jObj.getString("avatar");
                    AppConfig.AVATAR = avatar_temp;
                    Log.e("info", "Login Response: " + response.toString());

                    Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
        }) {
            @Override
            public Map<String, String> getParams() {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) { //open and close navigation from icon toogle
            return true;
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Profile_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(DrawerLayout_Activity.this, SearchDevice_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_refesh) {
            AppConfig.FLAG = 0;
            Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_piechart) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Chart_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Login_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
