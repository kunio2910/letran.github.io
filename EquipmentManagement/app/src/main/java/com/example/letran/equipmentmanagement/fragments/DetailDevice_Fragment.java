package com.example.letran.equipmentmanagement.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letran.equipmentmanagement.R;
import com.squareup.picasso.Picasso;

public class DetailDevice_Fragment extends Fragment {

    private String name,description,issue,url_image,create_time,approver;
    private TextView txtname,txtcreate_time,txtdescription,txtissue,txturl_image;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_showdetaildevice,container,false);
        Inititate(rootView);
        GetInfo();
        ShowDetailDevice();
        return rootView;
    }

    private void Inititate(View view){
        txtname = (TextView)view.findViewById(R.id.txtname);
        txtcreate_time = (TextView)view.findViewById(R.id.txtcreatetime);
        txtdescription = (TextView)view.findViewById(R.id.txtdescription);
        txtissue = (TextView)view.findViewById(R.id.txtIssue);
        image = (ImageView)view.findViewById(R.id.image);
    }
    private void GetInfo(){
        Bundle bundle = getActivity().getIntent().getExtras();
        name = bundle.getString("name");
        description = bundle.getString("description");
        issue = bundle.getString("issue");
        url_image = bundle.getString("url_image");
        create_time = bundle.getString("create_time");
        approver = bundle.getString("approver");
    }

    private void ShowDetailDevice(){
        txtname.setText(name);
        txtcreate_time.setText(create_time);
        txtdescription.setText(description);
        txtissue.setText(issue);

        if(!url_image.isEmpty()){
            Picasso.with(getContext()).load(String.valueOf(url_image)).into(image);
        }else{
            image.setImageResource(R.drawable.notfoundimage);
        }
    }
}
