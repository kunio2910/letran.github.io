package com.example.letran.equipmentmanagement.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.letran.equipmentmanagement.R;

public class ShowDetail_Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdetail_activity);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String description = bundle.getString("description");
        String issue = bundle.getString("issue");
        String url_image = bundle.getString("url_image");
        String create_time = bundle.getString("create_time");
        String approver = bundle.getString("approver");

        Log.e("test","test");
    }
}
