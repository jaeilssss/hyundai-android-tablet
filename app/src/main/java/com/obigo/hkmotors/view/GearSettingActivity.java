package com.obigo.hkmotors.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.obigo.hkmotors.model.TempDrive;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GearSettingActivity extends BaseActivity implements View.OnClickListener{

    private int num;

    private SwitchCompat switchCompat;

    private SeekBar gearCountSeekBar;
    private TextView seekBarCountTxt;

    private RelativeLayout settingLayout;

    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;


    private ImageButton atButton ;
    private ImageButton dctButton;
    private ImageButton amtButton;

    private ImageButton gearShortButton;
    private ImageButton gearDefaultButton;
    private ImageButton gearLongButton;

    private ImageButton gearSpeedHigh;
    private ImageButton gearSpeedMiddle;
    private ImageButton gearSpeedLow;

    private ImageButton gearPowerHigh;
    private ImageButton gearPowerMiddle;
    private ImageButton gearPowerLow;

    private ImageButton gearMapNormal;
    private ImageButton gearMapSport;
    private ImageButton gearMapTrack;

    private Button send;

    private String type;
    private String gear;
    private String gearRate;
    private String transmissionSpeed;
    private String transmissionPower;
    private String map;

    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;

    private int tempComfortable;
    private int tempLeading;
    private int tempDynamic;
    private int tempEfficiency;
    private int tempPerformance;


    private TextView atTypeTxt;
    private TextView dctTypeTxt;
    private TextView amtTypeTxt;

    private TextView gearRateShortTxt;
    private TextView gearRateDefaultTxt;
    private TextView gearRateLongTxt;

    private TextView gearSpeedHighTxt;
    private TextView gearSpeedMiddleTxt;
    private TextView gearSpeedLowTxt;

    private TextView gearPowerHighTxt;
    private TextView gearPowerMiddleTxt;
    private TextView gearPowerLowTxt;

    private TextView gearMapNormalTxt;
    private TextView gearMapSportTxt;
    private TextView gearMapTrackTxt;

    private ImageView obdLight;
    private ImageButton obdState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_setting);

        initUI();
    }


    public void initUI(){

        back = findViewById(R.id.ib_gear_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");


        settingLayout = findViewById(R.id.gear_setting_layout);

        switchCompat = findViewById(R.id.gear_setting_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    settingLayout.setVisibility(View.VISIBLE);
                    Transmission.getInstance().setTempIsOn("1");
                    changeChart();
                }else {
                    settingLayout.setVisibility(View.INVISIBLE );
                    Transmission.getInstance().setTempIsOn("0");
                    changeChart();
                }
            }
        });


        amtButton = findViewById(R.id.gear_setting_amt_img);
        dctButton = findViewById(R.id.gear_setting_dct_img);
        atButton = findViewById(R.id.gear_setting_at_check);
        amtButton.setOnClickListener(this);
        dctButton.setOnClickListener(this);
        atButton.setOnClickListener(this);

        gearCountSeekBar = findViewById(R.id.gear_count_seekbar);
        seekBarCountTxt = findViewById(R.id.seekbar_count_txt);


        gearCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               num =  i;

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       seekBarCountTxt.setText(String.valueOf(num+4));
                       if(num+4==4){
                           Transmission.getInstance().setTempGear("000");
                       }else if(num+4==5){
                           Transmission.getInstance().setTempGear("001");
                       }else if(num+4==6){
                           Transmission.getInstance().setTempGear("010");
                       }else if(num+4==7){
                           Transmission.getInstance().setTempGear("011");
                       }else{
                           Transmission.getInstance().setTempGear("100");
                       }
                       changeChart();

                   }
               });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        gearShortButton = findViewById(R.id.gear_setting_short_img);
        gearDefaultButton = findViewById(R.id.gear_setting_default_img);
        gearLongButton = findViewById(R.id.gear_setting_long_img);

        gearShortButton.setOnClickListener(this);
        gearDefaultButton.setOnClickListener(this);
        gearLongButton.setOnClickListener(this);

        gearSpeedHigh = findViewById(R.id.gear_speed_high_img);
        gearSpeedMiddle = findViewById(R.id.gear_speed_middle_img);
        gearSpeedLow = findViewById(R.id.gear_speed_low_img);

        gearSpeedLow.setOnClickListener(this);
        gearSpeedHigh.setOnClickListener(this);
        gearSpeedMiddle.setOnClickListener(this);

        gearPowerHigh = findViewById(R.id.gear_power_high_img);
        gearPowerMiddle = findViewById(R.id.gear_power_middle_img);
        gearPowerLow = findViewById(R.id.gear_power_low_img);

        gearPowerHigh.setOnClickListener(this);
        gearPowerMiddle.setOnClickListener(this);
        gearPowerLow.setOnClickListener(this);


        gearMapNormal = findViewById(R.id.gear_map_normal_img);
        gearMapSport = findViewById(R.id.gear_map_sport_img);
        gearMapTrack = findViewById(R.id.gear_map_track_img);

        gearMapNormal.setOnClickListener(this);
        gearMapSport.setOnClickListener(this);
        gearMapTrack.setOnClickListener(this);


        // 각 설정 텍스트뷰 클릭 리스너


        //
        send = findViewById(R.id.ib_e_send_btn);
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

        obdState = findViewById(R.id.ib_obd_set_btn);
        obdLight = findViewById(R.id.iv_favorite_light);

        if(Constants.OBD_STATUS){
            obdLight.setBackgroundResource(R.drawable.ico_light_green);
            obdState.setBackgroundResource(R.drawable.img_tit_04);

        }else{
            obdLight.setBackgroundResource(R.drawable.ico_light_red);
            obdState.setBackgroundResource(R.drawable.img_tit_03);

        }
    }

    private void setSettingValue(){
        if(Transmission.getInstance().getIsOn().equals("1")){
            switchCompat.setChecked(true);
            settingLayout.setVisibility(View.VISIBLE);

            if(Transmission.getInstance().getTempType().equals("00")){
                typeClick("AT");
            }else if(Transmission.getInstance().getTempType().equals("01")){
                typeClick("DCT");
            }else{
                typeClick("AMT");
            }

            if(Transmission.getInstance().getTempGear().equals("000")){
                gearCountSeekBar.setProgress(0);
                seekBarCountTxt.setText("4");

            }else if(Transmission.getInstance().getTempGear().equals("001")){
                gearCountSeekBar.setProgress(1);
                seekBarCountTxt.setText("5");

            }else if(Transmission.getInstance().getTempGear().equals("010")){
                gearCountSeekBar.setProgress(2);
                seekBarCountTxt.setText("6");
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                gearCountSeekBar.setProgress(3);
                seekBarCountTxt.setText("7");

            }else{
                gearCountSeekBar.setProgress(4);
                seekBarCountTxt.setText("8");

            }

            if(Transmission.getInstance().getTempGearRate().equals("00")){
                gearRate("Short");
            }else if(Transmission.getInstance().getTempGearRate().equals("01")){
                gearRate("Default");
            }else{
                gearRate("Long");
            }

            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                gearSpeed("Low");
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")){
                gearSpeed("Middle");
            }else{
                gearSpeed("High");
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                gearPower("Low");
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                gearPower("Middle");
            }else {
                gearPower("High");
            }

            if(Transmission.getInstance().getTempTransmissionMap().equals("00")){
                map("Normal");
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                map("Sport");
            }else{
                map("Track");
            }


        }else{
            switchCompat.setChecked(false);
            settingLayout.setVisibility(View.INVISIBLE);
            typeClick("AT");
            gearCountSeekBar.setProgress(0);
            seekBarCountTxt.setText("4");
            gearRate("Short");
            gearSpeed("Low");
            gearPower("Low");
            map("Normal");

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
    private void defaultChart(float d1, float d2, float d3, float d4, float d5) {

        mChart.clear();

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(d1));
        entries.add(new RadarEntry(d2));
        entries.add(new RadarEntry(d3));
        entries.add(new RadarEntry(d4));
        entries.add(new RadarEntry(d5));


        RadarDataSet dataset_comp = new RadarDataSet(entries, "");

        dataset_comp.setColor(Color.GRAY);
        dataset_comp.setDrawFilled(true);

        dataset_comp.setValueTextColor(Color.GRAY); // set the color of real value

//        List<IRadarDataSet> dataSetList = new ArrayList<IRadarDataSet>();
//        dataSetList.add(dataset_comp);


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
//        mChart.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return labels.get((int) value % labels.size());
//            }
//        });
        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        //chart.getYAxis().setTextColor(Color.RED);     // change number color
        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(9f);
        mChart.getYAxis().setEnabled(false);              // disable number

//        mChart.getXAxis().setAxisMaximum(9f);
//        mChart.getXAxis().setAxisMinimum(0f);


        RadarData data = new RadarData();
        data.addDataSet(dataset_comp);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);

        mChart.getLegend().setEnabled(false);            // remove legend
        // mChart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        mChart.setTouchEnabled(false);                   // disable touch
//        mChart.invalidate();


//            ArrayList<RadarEntry> dataVals = new ArrayList<>();
//            dataVals.add(new RadarEntry(d1));
//            dataVals.add(new RadarEntry(d2));
//            dataVals.add(new RadarEntry(d3));
//            dataVals.add(new RadarEntry(d4));
//            dataVals.add(new RadarEntry(d5));
//
//        RadarDataSet dataSet = new RadarDataSet(dataVals, "DATA");
//        dataSet.setColor(Color.GRAY);
//        dataSet.setDrawFilled(true);
//
//
//        RadarData data = new RadarData();
//        data.addDataSet(dataSet);
//        String[] labels =  {"안락감", "주도성", "역동성", "효율성", "동력성능"};
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTextColor(Color.WHITE);
//        xAxis.setTextSize(13);
//
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        mChart.setData(data);
//        mChart.invalidate();




    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_gear_setting_back :
                onBackPressed();
                break;
            case R.id.gear_setting_at_check :
                typeClick("AT");
                Transmission.getInstance().setTempType("00");
                changeChart();
                break;
            case R.id.gear_setting_dct_img :
                typeClick("DCT");
                Transmission.getInstance().setTempType("01");
                changeChart();
                break;
            case R.id.gear_setting_amt_img:
                Transmission.getInstance().setTempType("10");
                typeClick("AMT");
                changeChart();
                break;
            case R.id.gear_setting_short_img :
                Transmission.getInstance().setTempGearRate("00");
                gearRate("Short");
                changeChart();
                break;
            case R.id.gear_setting_default_img:
                Transmission.getInstance().setTempGearRate("01");
                gearRate("Default");
                changeChart();
                break;
            case R.id.gear_setting_long_img :
                Transmission.getInstance().setTempGearRate("10");
                gearRate("Long");
                changeChart();
                break;
            case R.id.gear_speed_high_img :
                Transmission.getInstance().setTempTransmissionSpeed("10");

                gearSpeed("High");
                changeChart();
                break;
            case R.id.gear_speed_middle_img :
                Transmission.getInstance().setTempTransmissionSpeed("01");
                gearSpeed("Middle");
                changeChart();
                break;
            case R.id.gear_speed_low_img :
                Transmission.getInstance().setTempTransmissionSpeed("00");
                gearSpeed("Low");
                changeChart();
                break;
            case R.id.gear_power_high_img :
                Transmission.getInstance().setTempTransmissionPower("10");
                gearPower("High");
                changeChart();
                break;
            case R.id.gear_power_middle_img :
                Transmission.getInstance().setTempTransmissionPower("01");
                gearPower("Middle");
                changeChart();
                break;
            case R.id.gear_power_low_img :
                Transmission.getInstance().setTempTransmissionPower("00");
                gearPower("Low");
                changeChart();
                break;
            case R.id.gear_map_sport_img :
                Transmission.getInstance().setTempTransmissionMap("01");
                map("Sport");
                changeChart();
                break;
            case R.id.gear_map_normal_img :
                Transmission.getInstance().setTempTransmissionMap("00");
                map("Normal");
                changeChart();
                break;
            case R.id.gear_map_track_img :
                Transmission.getInstance().setTempTransmissionMap("10");
                map("Track");
                changeChart();
                break;
            case R.id.ib_e_send_btn :
                send();

                break;


        }
    }

    public void send(){

        Intent intent = new Intent();
        intent.putExtra("change",true);
        setResult(Constants.REQUEST_GEAR_SETTING,intent);
        finish();
    }
    public void map(String str){
        if(str.equals("Normal")){

            gearMapNormal.setImageResource(R.drawable.oval_selected);

            gearMapSport.setImageResource(R.drawable.oval_default);
            gearMapTrack.setImageResource(R.drawable.oval_default);
        }else if(str.equals("Sport")){


            gearMapSport.setImageResource(R.drawable.oval_selected);
            gearMapTrack.setImageResource(R.drawable.oval_default);
            gearMapNormal.setImageResource(R.drawable.oval_default);
        }else{

            gearMapTrack.setImageResource(R.drawable.oval_selected);
            gearMapSport.setImageResource(R.drawable.oval_default);
            gearMapNormal.setImageResource(R.drawable.oval_default);
        }
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




    public void typeClick(String str){

        if(str.equals("AT")){
            atButton.setImageResource(R.drawable.oval_selected);

            dctButton.setImageResource(R.drawable.oval_default);
            amtButton.setImageResource(R.drawable.oval_default);



        }else if(str.equals("DCT")){
            dctButton.setImageResource(R.drawable.oval_selected);

            atButton.setImageResource(R.drawable.oval_default);
            amtButton.setImageResource(R.drawable.oval_default);

        }else{
            amtButton.setImageResource(R.drawable.oval_selected);

            atButton.setImageResource(R.drawable.oval_default);
            dctButton.setImageResource(R.drawable.oval_default);
        }

    }

    public void gearRate(String str){
        if(str.equals("Long")){

            gearLongButton.setImageResource(R.drawable.oval_selected);

            gearShortButton.setImageResource(R.drawable.oval_default);
            gearDefaultButton.setImageResource(R.drawable.oval_default);
        }else if(str.equals("Default")){

            gearDefaultButton.setImageResource(R.drawable.oval_selected);

            gearLongButton.setImageResource(R.drawable.oval_default);
            gearShortButton.setImageResource(R.drawable.oval_default);
        }else{

            gearShortButton.setImageResource(R.drawable.oval_selected);

            gearDefaultButton.setImageResource(R.drawable.oval_default);
            gearLongButton.setImageResource(R.drawable.oval_default);
        }
    }

    public void gearSpeed(String str){
        if(str.equals("High")){

            gearSpeedHigh.setImageResource(R.drawable.oval_selected);

            gearSpeedMiddle.setImageResource(R.drawable.oval_default);
            gearSpeedLow.setImageResource(R.drawable.oval_default);
        }else if(str.equals("Middle")){

            gearSpeedMiddle.setImageResource(R.drawable.oval_selected);

            gearSpeedHigh.setImageResource(R.drawable.oval_default);
            gearSpeedLow.setImageResource(R.drawable.oval_default);

        }else if(str.equals("Low")){

            gearSpeedLow.setImageResource(R.drawable.oval_selected);

            gearSpeedHigh.setImageResource(R.drawable.oval_default);
            gearSpeedMiddle.setImageResource(R.drawable.oval_default);
        }
    }

    public void gearPower(String str){
        if(str.equals("High")){

            gearPowerHigh.setImageResource(R.drawable.oval_selected);

            gearPowerMiddle.setImageResource(R.drawable.oval_default);
            gearPowerLow.setImageResource(R.drawable.oval_default);
        }else if(str.equals("Middle")){
            gearPowerMiddle.setImageResource(R.drawable.oval_selected);

            gearPowerHigh.setImageResource(R.drawable.oval_default);
            gearPowerLow.setImageResource(R.drawable.oval_default);
        }else{
            gearPowerLow.setImageResource(R.drawable.oval_selected);

            gearPowerHigh.setImageResource(R.drawable.oval_default);
            gearPowerMiddle.setImageResource(R.drawable.oval_default);
        }
    }

    @Override
    public void onBackPressed() {
        Transmission.getInstance().reset();
        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempPerformance();
        Intent intent = new Intent();
        intent.putExtra("change",true);
        setResult(Constants.REQUEST_GEAR_SETTING,intent);
        finish();
    }
}