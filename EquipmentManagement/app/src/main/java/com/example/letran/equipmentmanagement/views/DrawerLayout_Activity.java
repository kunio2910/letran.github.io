package com.example.letran.equipmentmanagement.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.utils.AppConfig;

import java.io.ByteArrayOutputStream;


public class DrawerLayout_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView imageView;
    private TextView txtname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();
        setContentView(R.layout.drawerlayout_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //Change color for actionbar
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26ae90")));

        Drawable d=getResources().getDrawable(R.drawable.background_nav_header);
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
        txtname.setText("Mr.NoName");
        if(AppConfig.LST_DEVICES.size() >= 1){
            SetAvatar();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawerLayout_Activity.this,"LongClick",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void SetAvatar(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        //decode base64 string to image
        imageBytes = Base64.decode(AppConfig.LST_DEVICES.get(0).getUrl_image(), Base64.DEFAULT);
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = calculateInSampleSize(options, 400,300);
        //options.inJustDecodeBounds = false;
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap bMap = Bitmap.createScaledBitmap(decodedImage, 400, 300, true);
        imageView.setImageBitmap(bMap);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Personal_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Login_Activity.class);
            startActivity(intent);
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(drawerToggle.onOptionsItemSelected(item)) { //open and close navigation from icon toogle
            return true;
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(DrawerLayout_Activity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_personal) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Personal_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(DrawerLayout_Activity.this, Login_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
