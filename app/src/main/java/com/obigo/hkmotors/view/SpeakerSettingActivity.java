package com.obigo.hkmotors.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SpeakerSettingActivity extends BaseActivity implements View.OnClickListener {

    private SwitchCompat switchCompat;

    private RelativeLayout settingLayout;

    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;

    private SeekBar backVolume;

    private ImageButton engin;
    private ImageButton motor;

    private ImageButton enginVolumeLow;
    private ImageButton enginVolumeMiddle;
    private ImageButton enginVolumeHigh;

    private ImageButton speakerSensitivityHigh;
    private ImageButton speakerSensitivityLow;



    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_setting);
        initUI();
    }



    public void initUI(){

        back = findViewById(R.id.ib_speaker_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");
        defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);

        switchCompat = findViewById(R.id.speaker_setting_switch);

        settingLayout = findViewById(R.id.speaker_detail_layout);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    settingLayout.setVisibility(View.VISIBLE);
                }else settingLayout.setVisibility(View.INVISIBLE );

            }
        });

        engin = findViewById(R.id.speaker_engin_type_engin_img);
        motor = findViewById(R.id.speaker_engin_type_motor_img);

        engin.setOnClickListener(this);
        motor.setOnClickListener(this);

        enginVolumeHigh = findViewById(R.id.speaker_volume_high_img);
        enginVolumeLow  = findViewById(R.id.speaker_volume_low_img);
        enginVolumeMiddle = findViewById(R.id.speaker_volume_middle_img);

        enginVolumeHigh.setOnClickListener(this);
        enginVolumeMiddle.setOnClickListener(this);
        enginVolumeLow.setOnClickListener(this);


        speakerSensitivityHigh = findViewById(R.id.speaker_sensitivity_high_img);
        speakerSensitivityLow = findViewById(R.id.speaker_sensitivity_low_img);

        speakerSensitivityLow.setOnClickListener(this);
        speakerSensitivityHigh.setOnClickListener(this);


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
        labels.add("안락감");
        labels.add("주도성");
        labels.add("역동성");
        labels.add("효율성");
        labels.add("동력상능");

        mChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mChart.getXAxis().setTextSize(13);
        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
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

        modChart(3,3,3,1,1,5,5,7,6,3);
    }

    private void modChart(float d1, float d2, float d3, float d4, float d5,
                          float p1, float p2, float p3, float p4, float p5) {

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


        List<IRadarDataSet> dataSetList = new ArrayList<IRadarDataSet>();
        dataSetList.add(dataset_comp1);
        dataSetList.add(dataset_comp2);


        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("안락감");
        labels.add("주도성");
        labels.add("역동성");
        labels.add("효율성");
        labels.add("동력상능");

        mChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mChart.getXAxis().setTextSize(13);
        mChart.getXAxis().setYOffset(0f);
        mChart.getXAxis().setXOffset(0f);
        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });

        //chart.getYAxis().setTextColor(Color.RED);     // change number color
        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(8);

        mChart.getXAxis().setAxisMaximum(8);

        mChart.getYAxis().setEnabled(false);             // disable number

        RadarData data = new RadarData(dataSetList);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);            // remove legend

        mChart.setTouchEnabled(false);                   // disable touch
        mChart.invalidate();

        // TODO : animation makes blink, so it is disabled
        //mChart.setAnimation(animFadeIn);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.speaker_engin_type_engin_img :
                typeClick("Engin");
                break;
            case R.id.speaker_engin_type_motor_img:
                typeClick("Motor");
                break;
            case R.id.speaker_volume_low_img :
                enginVolume("Low");
                break;
            case R.id.speaker_volume_middle_img :
                enginVolume("Middle");
                break;
            case R.id.speaker_volume_high_img :
                enginVolume("High");
                break;
            case R.id.speaker_sensitivity_high_img :
                backSensitivityVolume("High");
                break;
            case R.id.speaker_sensitivity_low_img :
                backSensitivityVolume("Low");
                break;

        }
    }

    public void typeClick(String str){
        if(str.equals("Engin")){

            engin.setBackgroundResource(R.drawable.oval_selected);

            motor.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Motor")){

            motor.setBackgroundResource(R.drawable.oval_selected);
            engin.setBackgroundResource(R.drawable.oval_default);
        }
    }

    public void enginVolume(String str){
        if(str.equals("Low")){

            enginVolumeLow.setBackgroundResource(R.drawable.oval_selected);

            enginVolumeMiddle.setBackgroundResource(R.drawable.oval_default);
            enginVolumeHigh.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Middle")){
            enginVolumeMiddle.setBackgroundResource(R.drawable.oval_selected);

            enginVolumeLow.setBackgroundResource(R.drawable.oval_default);
            enginVolumeHigh.setBackgroundResource(R.drawable.oval_default);
        }else{

            enginVolumeHigh.setBackgroundResource(R.drawable.oval_selected);

            enginVolumeMiddle.setBackgroundResource(R.drawable.oval_default);
            enginVolumeLow.setBackgroundResource(R.drawable.oval_default);
        }
    }

    public void backSensitivityVolume(String str){

        if(str.equals("High")){
            speakerSensitivityHigh.setBackgroundResource(R.drawable.oval_selected);
            speakerSensitivityLow.setBackgroundResource(R.drawable.oval_default);
        }else{
            speakerSensitivityLow.setBackgroundResource(R.drawable.oval_selected);
            speakerSensitivityHigh.setBackgroundResource(R.drawable.oval_default);
        }
    }
}