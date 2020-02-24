package com.example.letran.equipmentmanagement.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChart_Fragment extends Fragment {

    PieChartView pieChartView;
    private int ratioApproved, ratioWait;
    private PieChartOnValueSelectListener selectListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.piechart_fragment, container, false);

        pieChartView = (PieChartView)rootView.findViewById(R.id.chart);
        GetRatio();

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(ratioApproved, Color.BLUE).setLabel("APPROVED(" + ratioApproved + "%)"));
        pieData.add(new SliceValue(ratioWait, Color.RED).setLabel("WAIT(" + ratioWait + "%)"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(13);
        //pieChartData.setHasCenterCircle(true).setCenterText1("Sales in million").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        selectListener = new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                if(arcIndex == 0){
                    ShowDialog(0);
                }else{
                    ShowDialog(1);
                }
            }

            @Override
            public void onValueDeselected() {

            }
        };

        pieChartView.setOnValueTouchListener(selectListener);
        return rootView;
    }

    private void GetRatio(){
        ratioApproved = Math.round(AppConfig.LST_DEVICES_APPROVED.size() * 100 / AppConfig.LST_DEVICES.size());
        ratioWait = 100 - ratioApproved;
        Log.e("Test","Test");
    }

    private void ShowDialog(int arcIndex){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.piechart_dialog, null);
        final EditText edtTotal = (EditText) alertLayout.findViewById(R.id.edtTotal);
        final EditText edtCurrent = (EditText) alertLayout.findViewById(R.id.edtCurrent);
        final EditText edtRatio = (EditText) alertLayout.findViewById(R.id.edtRatio);

        edtTotal.setText(String.valueOf(AppConfig.LST_DEVICES.size()));
        if(arcIndex == 1) {
            edtCurrent.setText(String.valueOf(AppConfig.LST_DEVICES_WAIT_APPROVED.size()));
            edtRatio.setText(String.valueOf(ratioWait) + "%");
        }else{
            edtCurrent.setText(String.valueOf(AppConfig.LST_DEVICES_APPROVED.size()));
            edtRatio.setText(String.valueOf(ratioApproved) + "%");
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
