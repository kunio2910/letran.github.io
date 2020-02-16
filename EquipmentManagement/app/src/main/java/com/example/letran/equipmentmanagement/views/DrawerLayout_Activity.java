package com.example.letran.equipmentmanagement.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.letran.equipmentmanagement.R;


public class DrawerLayout_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;

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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26ae90")));

        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(DrawerLayout_Activity.this, mDrawer, R.string.ns_menu_open, R.string.ns_menu_close);
        mDrawer.addDrawerListener(drawerToggle);
        //calling sync state is compulsory to avoid unusual behaviour as home button wont show up
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
