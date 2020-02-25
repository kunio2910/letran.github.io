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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChart_Fragment extends Fragment {

    private LineChartView lineChartView;
    private String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    private int[] yAxisData = new int[12];
    private LineChartOnValueSelectListener selectListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.linechart_fragment, container, false);

        lineChartView = rootView.findViewById(R.id.chart);

        Initiate();
        GetData();
        DrawLineChart();

        selectListener = new LineChartOnValueSelectListener() {


            @Override
            public void onValueDeselected() {

            }

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                int month = (int)value.getX();
                int total = (int)value.getY();
                ShowDialog(month,total);
                //Toast.makeText(getContext(),"lineIndex " + lineIndex + " " + "pointindex " + pointIndex + " " + "value " + value,Toast.LENGTH_LONG).show();
            }
        };

        lineChartView.setOnValueTouchListener(selectListener);
        return rootView;
    }

    private void Initiate(){
        yAxisData[0] = 0;
        yAxisData[1] = 0;
        yAxisData[2] = 0;
        yAxisData[3] = 0;
        yAxisData[4] = 0;
        yAxisData[5] = 0;
        yAxisData[6] = 0;
        yAxisData[7] = 0;
        yAxisData[8] = 0;
        yAxisData[9] = 0;
        yAxisData[10] = 0;
        yAxisData[11] = 0;
    }

    private void GetData(){
        int jan = 0;
        for (int i = 0 ;i < AppConfig.LST_DEVICES.size(); i++){
            String[] arrayString = AppConfig.LST_DEVICES.get(i).getCreate_time().split("-");
            String month = arrayString[1];
            SetValueForMonth(month);
            Log.e("Test","Test");
        }
    }

    private void SetValueForMonth(String month){
        if(month.equals("01")){
            yAxisData[0] ++;
        }else  if(month.equals("02")){
            yAxisData[1] ++;
        }else  if(month.equals("03")){
            yAxisData[2] ++;
        }else  if(month.equals("04")){
            yAxisData[3] ++;
        }else  if(month.equals("05")){
            yAxisData[4] ++;
        }else  if(month.equals("06")){
            yAxisData[5] ++;
        }else  if(month.equals("07")){
            yAxisData[6] ++;
        }else  if(month.equals("08")){
            yAxisData[7] ++;
        }else  if(month.equals("09")){
            yAxisData[8] ++;
        }else  if(month.equals("10")){
            yAxisData[9] ++;
        }else  if(month.equals("11")){
            yAxisData[10] ++;
        }else  if(month.equals("12")){
            yAxisData[11] ++;
        }
        Log.e("Test","Test");
    }

    private void DrawLineChart() {
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#FF0000"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#000000"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        //yAxis.setName("Sales in millions");
        yAxis.setTextColor(Color.parseColor("#000000"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 100;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }

    private void ShowDialog(int month, int total){
        int ratio = 0;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.linechart_dialog, null);
        final EditText edtMonth = (EditText) alertLayout.findViewById(R.id.edtMonth);
        final EditText edtTotal = (EditText) alertLayout.findViewById(R.id.edtTotal);
        final EditText edtRatio = (EditText) alertLayout.findViewById(R.id.edtRatio);

        edtMonth.setText(axisData[month]);
        edtTotal.setText(String.valueOf(total));
        if(month >= 1) {
            if(total > 0) {
                ratio = Math.round(yAxisData[month] * 100 / yAxisData[month - 1]);
            }else{
                ratio = Math.round(yAxisData[month-1] * 100 / 1);
            }

            if (yAxisData[month] > yAxisData[month - 1]) {
                edtRatio.setText("+" + String.valueOf(ratio) + "%");
            } else if (yAxisData[month] <= yAxisData[month - 1]) {
                edtRatio.setText("-" + String.valueOf(ratio) + "%");
            }
        }else if(month == 0){
            edtRatio.setText("0%");
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
