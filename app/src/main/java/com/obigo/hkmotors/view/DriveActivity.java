package com.obigo.hkmotors.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DriveActivity extends BaseActivity implements View.OnClickListener {


    private SwitchCompat switchCompat;

    private RelativeLayout settingLayout;


    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;


    private ImageButton stiffnessLow;
    private ImageButton stiffnessMiddle;
    private ImageButton stiffnessHigh;

    private ImageButton reduceLow;
    private ImageButton reduceMiddle;
    private ImageButton reduceHigh;

    private Button send;


    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;

    private TextView carConnectionState;
    private ImageView carConnectionLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_setting);
        initUI();
    }
    public void initUI(){

        back = findViewById(R.id.ib_axel_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("???????????? ????????????.");
        defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(),CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());

        settingLayout = findViewById(R.id.axel_setting_layout);

        switchCompat = findViewById(R.id.axel_setting_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    settingLayout.setVisibility(View.VISIBLE);
                    Drive.getInstance().setTempIsOn("1");
                    changeChart();
                }else{
                    settingLayout.setVisibility(View.INVISIBLE );
                    Drive.getInstance().setTempIsOn("0");
                    changeChart();
                }

            }
        });

        stiffnessLow = findViewById(R.id.axel_setting_low_check);
        stiffnessMiddle = findViewById(R.id.axel_setting_middle_check);
        stiffnessHigh = findViewById(R.id.axel_setting_high_img);

        stiffnessLow.setOnClickListener(this);
        stiffnessMiddle.setOnClickListener(this);
        stiffnessHigh.setOnClickListener(this);

        reduceLow = findViewById(R.id.axel_reduce_low_img);
        reduceMiddle = findViewById(R.id.axel_setting_reduce_middle_img);
        reduceHigh = findViewById(R.id.axel_setting_reduce_high_img);

        reduceHigh.setOnClickListener(this);
        reduceMiddle.setOnClickListener(this);
        reduceLow.setOnClickListener(this);

        send = findViewById(R.id.axel_data_send);
        send.setOnClickListener(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // something..

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setSettingValue();
                            }
                        });

                    }
                }).start();
            }
        }, 0);

        carConnectionState = findViewById(R.id.ib_obd_set_btn);
        carConnectionLight = findViewById(R.id.iv_favorite_light);

        if(Constants.CONNECTION_STATUS){
            carConnectionLight.setBackgroundResource(R.drawable.ico_light_green);
            carConnectionState.setText("?????? ?????? ON");

        }else{
            carConnectionLight.setBackgroundResource(R.drawable.ico_light_red);
            carConnectionState.setText("???????????? ON");

        }


    }

    private void defaultChart(float d1, float d2, float d3, float d4, float d5) {

        mChart.clear();

        List<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(d1, 0));
        entries.add(new RadarEntry(d2, 1));
        entries.add(new RadarEntry(d3, 2));
        entries.add(new RadarEntry(d4, 3));
        entries.add(new RadarEntry(d5, 4));

        RadarDataSet dataset_comp = new RadarDataSet(entries, "");

        dataset_comp.setColor(Color.GRAY);
        dataset_comp.setDrawFilled(true);

        dataset_comp.setValueTextColor(Color.GRAY); // set the color of real value

        List<IRadarDataSet> dataSetList = new ArrayList<IRadarDataSet>();
        dataSetList.add(dataset_comp);

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("?????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("????????????");

        mChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mChart.getXAxis().setTextSize(13);
        mChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });

        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(8);
        mChart.getYAxis().setEnabled(false);             // disable number

        mChart.getXAxis().setAxisMaximum(8);


        RadarData data = new RadarData(dataSetList);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);

        mChart.getLegend().setEnabled(false);            // remove legend
        // mChart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        mChart.setTouchEnabled(false);                   // disable touch
        mChart.invalidate();
    }

    private void modChart(float d1, float d2, float d3, float d4, float d5,
                          float p1, float p2, float p3, float p4, float p5) {
        mChart.clear();
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        List<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(d1, 0));
        entries.add(new RadarEntry(d2, 1));
        entries.add(new RadarEntry(d3, 2));
        entries.add(new RadarEntry(d4, 3));
        entries.add(new RadarEntry(d5, 4));

        List<RadarEntry> entries2 = new ArrayList<>();
        entries2.add(new RadarEntry(p1, 0));
        entries2.add(new RadarEntry(p2, 1));
        entries2.add(new RadarEntry(p3, 2));
        entries2.add(new RadarEntry(p4, 3));
        entries2.add(new RadarEntry(p5, 4));

        RadarDataSet dataset_comp1 = new RadarDataSet(entries, "");

        RadarDataSet dataset_comp2 = new RadarDataSet(entries2, "");

        dataset_comp1.setColor(Color.GRAY);
        dataset_comp1.setDrawFilled(true);

        dataset_comp2.setColor(Color.RED);
        dataset_comp2.setDrawFilled(true);

        dataset_comp1.setValueTextColor(Color.GRAY); // set the color of real value
        dataset_comp2.setValueTextColor(Color.parseColor("#32A3D9")); // set the color of real value


//        List<IRadarDataSet> dataSetList = new ArrayList<IRadarDataSet>();
//        dataSetList.add(dataset_comp1);
//        dataSetList.add(dataset_comp2);

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("?????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("????????????");

        mChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mChart.getXAxis().setTextSize(13);
        mChart.getXAxis().setYOffset(0f);
        mChart.getXAxis().setXOffset(0f);
        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
//        mChart.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return labels.get((int) value % labels.size());
//            }
//        });

        mChart.getYAxis().setTextColor(Color.RED);     // change number color
        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(9f);
        mChart.getYAxis().setEnabled(false);  // disable number
//
//        mChart.getXAxis().setAxisMaximum(9f);
//        mChart.getXAxis().setAxisMinimum(0f);

        RadarData data = new RadarData();
        data.addDataSet(dataset_comp1);
        data.addDataSet(dataset_comp2);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);            // remove legend

        mChart.setTouchEnabled(false);                   // disable touch
//        mChart.invalidate();

        // TODO : animation makes blink, so it is disabled
        mChart.setAnimation(animFadeIn);
    }

    private void setSettingValue() {

        if(Drive.getInstance().getTempIsOn().equals("1")){
            switchCompat.setChecked(true);
            settingLayout.setVisibility(View.VISIBLE);

            if(Drive.getInstance().getTempStiffness().equals("00")){
                setStiffness("Low");
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                setStiffness("Middle");
            }else{
                setStiffness("High");
            }

            if(Drive.getInstance().getTempReducer().equals("00")){
                setReduce("Low");
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                setReduce("Middle");
            }else{
                setReduce("High");
            }

        }else{
            switchCompat.setChecked(false);
            settingLayout.setVisibility(View.INVISIBLE);
            setStiffness("Low");
            setReduce("Low");
        }

        changeChart();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.axel_setting_low_check :
                setStiffness("Low");
                Drive.getInstance().setTempStiffness("00");
                changeChart();
                break;
            case R.id.axel_setting_middle_check :
                setStiffness("Middle");
                Drive.getInstance().setTempStiffness("01");
                changeChart();
                break;
            case R.id.axel_setting_high_img :
                setStiffness("High");
                Drive.getInstance().setTempStiffness("10");
                changeChart();
                break;
            ////
            case R.id.axel_reduce_low_img :
                setReduce("Low");
                Drive.getInstance().setTempReducer("00");
                changeChart();
                break;
            case R.id.axel_setting_reduce_middle_img :
                setReduce("Middle");
                Drive.getInstance().setTempReducer("01");
                changeChart();
                break;
            case R.id.axel_setting_reduce_high_img :
                setReduce("High");
                Drive.getInstance().setTempReducer("10");
                changeChart();
                break;
            case R.id.axel_data_send:
                send();
                break;
            case R.id.ib_axel_setting_back :
                onBackPressed();
                break;
                ////
        }
    }
    public void send(){

        Intent intent = new Intent();
        intent.putExtra("change",true);
        setResult(Constants.REQUEST_DRIVING_SETTING,intent);
        finish();
    }
    public void setStiffness(String str){
        if(str.equals("Low")){


            stiffnessLow.setImageResource(R.drawable.setting_check_main_color);

            stiffnessMiddle.setImageResource(0);
            stiffnessHigh.setImageResource(0);
        }else if(str.equals("Middle")){


            stiffnessMiddle.setImageResource(R.drawable.setting_check_main_color);

            stiffnessHigh.setImageResource(0);
            stiffnessLow.setImageResource(0);
        }else {

            stiffnessHigh.setImageResource(R.drawable.setting_check_main_color);

            stiffnessMiddle.setImageResource(0);
            stiffnessLow.setImageResource(0);
        }

    }

    public void setReduce(String str){

        if(str.equals("Low")){

            reduceLow.setImageResource(R.drawable.setting_check_main_color);

            reduceMiddle.setImageResource(0);
            reduceHigh.setImageResource(0);
        }else if(str.equals("Middle")){

            reduceMiddle.setImageResource(R.drawable.setting_check_main_color);

            reduceLow.setImageResource(0);
            reduceHigh.setImageResource(0);
        }else{
            reduceHigh.setImageResource(R.drawable.setting_check_main_color);

            reduceMiddle.setImageResource(0);
            reduceLow.setImageResource(0);
        }


    }

    public void changeChart(){
        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempPerformance();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempLeading();

        modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
    }

    @Override
    public void onBackPressed() {
        Drive.getInstance().reset();
        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempPerformance();
        Intent intent = new Intent();
        intent.putExtra("change",false);
        setResult(Constants.REQUEST_DRIVING_SETTING,intent);
        finish();
    }
}