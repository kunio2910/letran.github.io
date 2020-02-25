package com.example.letran.equipmentmanagement.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.views.ShowDetail_Activity;

public class TreeDevice_Fragment extends Fragment implements View.OnClickListener {

    private String name_device, description, issue, url_image, create_time, approver,creater,note,date_approve, id;
    Button btnstar_1,btnstar_2,btnstar_3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_treedevice,container,false);

        GetInfor();
        Initiate(rootView);
        return rootView;
    }

    private void Initiate(View view){
        btnstar_1 = (Button)view.findViewById(R.id.btnstar_1);
        btnstar_2 = (Button)view.findViewById(R.id.btnstar_2);
        btnstar_3 = (Button)view.findViewById(R.id.btnstar_3);

        if(!approver.isEmpty()) {
            btnstar_2.setBackgroundResource(R.drawable.star_yellow);
        }

        btnstar_1.setOnClickListener(this);
        btnstar_2.setOnClickListener(this);
        btnstar_3.setOnClickListener(this);
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
        note = bundle.getString("note");
        date_approve = bundle.getString("date_approve");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnstar_1:
                ShowDialog(1);
                break;
            case R.id.btnstar_2:
                ShowDialog(2);
                break;
            case R.id.btnstar_3:
                ShowDialog(3);
                break;
        }
    }

    private void ShowDialog(int star){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final EditText edtApprover = (EditText) alertLayout.findViewById(R.id.edtApprover);
        final EditText edtDate = (EditText) alertLayout.findViewById(R.id.edtDate);
        final EditText edtNote = (EditText) alertLayout.findViewById(R.id.edtNotes);

        if(star == 1){
            edtApprover.setText(creater);
            edtDate.setText(create_time);
            edtNote.setText("Empty");
        }else if(star == 2){
            if (approver.isEmpty()) {
                edtApprover.setText("Empty");
                edtDate.setText("Empty");
                edtNote.setText("Empty");
            }else{
                edtApprover.setText(approver);
                edtDate.setText(date_approve);
                if(note.isEmpty() || note == null){
                    edtNote.setText("Empty");
                }else
                    edtNote.setText(note);
            }
        }else if(star == 3){
            if (approver.isEmpty()) {
                edtApprover.setText("Empty");
                edtDate.setText("Empty");
                edtNote.setText("Empty");
            }else{
                edtApprover.setText("Empty");
                edtDate.setText("Empty");
                edtNote.setText("Empty");

            }
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("INFORMATION");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
}
