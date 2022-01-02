package com.obigo.hkmotors.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SpeakerSettingActivity extends BaseActivity implements View.OnClickListener {

    private SwitchCompat switchCompat;

    private RelativeLayout settingLayout;

    private int num;

    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;



    private ImageButton engin;
    private ImageButton motor;

    private ImageButton enginVolumeLow;
    private ImageButton enginVolumeMiddle;
    private ImageButton enginVolumeHigh;

    private ImageButton speakerSensitivityHigh;
    private ImageButton speakerSensitivityLow;



    private Button send;

    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;

    private ImageButton obdState;
    private ImageView obdLight;

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_setting);
        audioManager =  (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        initUI();
    }



    public void initUI(){

        back = findViewById(R.id.ib_speaker_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");
        defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(),CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());

        switchCompat = findViewById(R.id.speaker_setting_switch);

        settingLayout = findViewById(R.id.speaker_detail_layout);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    settingLayout.setVisibility(View.VISIBLE);
                    Sound.getInstance().setTempIsOn("1");
                    changeChart();
                }else{
                    settingLayout.setVisibility(View.INVISIBLE );
                    Sound.getInstance().setTempIsOn("0");
                    changeChart();
                }

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




        send = findViewById(R.id.speaker_data_send);
        send.setOnClickListener(this);
        setSettingValue();

        obdLight = findViewById(R.id.iv_favorite_light);
        obdState = findViewById(R.id.ib_obd_set_btn);

        if(Constants.OBD_STATUS){
            obdLight.setBackgroundResource(R.drawable.ico_light_green);
            obdState.setBackgroundResource(R.drawable.img_tit_04);

        }else{
            obdLight.setBackgroundResource(R.drawable.ico_light_red);
            obdState.setBackgroundResource(R.drawable.img_tit_03);

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
        labels.add("안락감");
        labels.add("주도성");
        labels.add("역동성");
        labels.add("효율성");
        labels.add("동력상능");

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
        labels.add("안락감");
        labels.add("주도성");
        labels.add("역동성");
        labels.add("효율성");
        labels.add("동력성능");

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
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.speaker_engin_type_engin_img :
                engin.setSoundEffectsEnabled(true);
                audioManager.playSoundEffect(SoundEffectConstants.CLICK);
                Sound.getInstance().setTempDriveType("1");
                typeClick("Engin");
                changeChart();
                break;
            case R.id.speaker_engin_type_motor_img:
                Sound.getInstance().setTempDriveType("0");
                typeClick("Motor");
                changeChart();
                break;
            case R.id.speaker_volume_low_img :
                Sound.getInstance().setVolume("00");
                enginVolume("Low");
                changeChart();
                break;
            case R.id.speaker_volume_middle_img :
                Sound.getInstance().setVolume("01");
                enginVolume("Middle");
                changeChart();
                break;
            case R.id.speaker_volume_high_img :
                Sound.getInstance().setVolume("10");
                enginVolume("High");
                changeChart();
                break;
            case R.id.speaker_sensitivity_high_img :
                Sound.getInstance().setTempBackSensitive("1");
                backSensitivityVolume("High");
                changeChart();
                break;
            case R.id.speaker_sensitivity_low_img :
                Sound.getInstance().setTempBackSensitive("0");
                backSensitivityVolume("Low");
                changeChart();
                break;
            case R.id.speaker_data_send :
                send();
                break;
            case R.id.ib_speaker_setting_back:
                onBackPressed();
                break;
                //
        }
    }
    public void send(){

        Intent intent = new Intent();
        intent.putExtra("change",true);
        setResult(Constants.REQUEST_SPEAKER_SETTING,intent);
        finish();
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

    private void setSettingValue() {


        if (Sound.getInstance().getTempIsOn().equals("1")){
            switchCompat.setChecked(true);
            if (Sound.getInstance().getTempDriveType().equals("0")){
                typeClick("Motor");
            }else{
                typeClick("Engin");
            }

            if(Sound.getInstance().getTempVolume().equals("00")){
                enginVolume("Low");
            }else if(Sound.getInstance().getTempVolume().equals("01")){
                enginVolume("Middle");
            }else{
                enginVolume("High");
            }

            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                backSensitivityVolume("Low");
            }else{
                backSensitivityVolume("High");
            }

            if(Sound.getInstance().getTempVolume().equals("00")){


            }else if(Sound.getInstance().getTempVolume().equals("01")){


            }else if(Sound.getInstance().getTempVolume().equals("10")){


            }else{


            }


        }else{
            switchCompat.setChecked(false);
            settingLayout.setVisibility(View.INVISIBLE);
        }

        changeChart();
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
        Sound.getInstance().reset();
        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempPerformance();
        Intent intent = new Intent();
        intent.putExtra("change",true);
        setResult(Constants.REQUEST_SPEAKER_SETTING,intent);
        finish();
    }
    }