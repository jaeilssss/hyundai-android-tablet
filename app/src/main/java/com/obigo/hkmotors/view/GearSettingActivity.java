package com.obigo.hkmotors.view;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.model.CarData;
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

    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;

    private int tempComfortable;
    private int tempLeading;
    private int tempDynamic;
    private int tempEfficiency;
    private int tempPerformance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_setting);
        initUI();
    }


    public void initUI(){

        tempComfortable = CarData.getInstance().getComfortable();
        tempDynamic = CarData.getInstance().getDynamic();
        tempEfficiency = CarData.getInstance().getEfficiency();
        tempLeading = CarData.getInstance().getLeading();
        tempPerformance = CarData.getInstance().getPerformance();

        back = findViewById(R.id.ib_gear_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");
//        defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);

        defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(),CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());

        settingLayout = findViewById(R.id.gear_setting_layout);

        switchCompat = findViewById(R.id.gear_setting_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    settingLayout.setVisibility(View.VISIBLE);
                }else settingLayout.setVisibility(View.INVISIBLE );

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
                       seekBarCountTxt.setText(String.valueOf(num));

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
        labels.add("동력성능");

        mChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mChart.getXAxis().setTextSize(13);
        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });

        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(5f);
        mChart.getYAxis().setEnabled(false);             // disable number

        RadarData data = new RadarData(dataSetList);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);

        mChart.getLegend().setEnabled(false);            // remove legend
        // mChart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        mChart.setTouchEnabled(false);                   // disable touch
        mChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_gear_setting_back :
                onBackPressed();
                break;
            case R.id.gear_setting_at_check :
                typeClick("AT");
                break;
            case R.id.gear_setting_dct_img :
                typeClick("DCT");
                break;
            case R.id.gear_setting_amt_img:
                typeClick("AMT");
                break;
            case R.id.gear_setting_short_img :
                gearRate("Short");
                break;
            case R.id.gear_setting_default_img:
                gearRate("Default");
                break;
            case R.id.gear_setting_long_img :
                gearRate("Long");
                break;
            case R.id.gear_speed_high_img :
                gearSpeed("High");
                break;
            case R.id.gear_speed_middle_img :
                gearSpeed("Middle");
                break;
            case R.id.gear_speed_low_img :
                gearSpeed("Low");
                break;
            case R.id.gear_power_high_img :
                gearPower("High");
                break;
            case R.id.gear_power_middle_img :
                gearPower("Middle");
                break;
            case R.id.gear_power_low_img :
                gearPower("Low");
                break;



        }
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
        labels.add("최대출력");
        labels.add("가속도");
        labels.add("감속도");
        labels.add("응답성");
        labels.add("에코레벨");

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
        mChart.getYAxis().setAxisMaximum(5f);
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

    public void typeClick(String str){

        if(str.equals("AT")){
            atButton.setBackgroundResource(R.drawable.oval_selected);

            dctButton.setBackgroundResource(R.drawable.oval_default);
            amtButton.setBackgroundResource(R.drawable.oval_default);



        }else if(str.equals("DCT")){
            dctButton.setBackgroundResource(R.drawable.oval_selected);

            atButton.setBackgroundResource(R.drawable.oval_default);
            amtButton.setBackgroundResource(R.drawable.oval_default);

        }else{
            amtButton.setBackgroundResource(R.drawable.oval_selected);

            atButton.setBackgroundResource(R.drawable.oval_default);
            dctButton.setBackgroundResource(R.drawable.oval_default);
        }

    }

    public void gearRate(String str){
        if(str.equals("Long")){

            gearLongButton.setBackgroundResource(R.drawable.oval_selected);

            gearShortButton.setBackgroundResource(R.drawable.oval_default);
            gearDefaultButton.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Default")){

            gearDefaultButton.setBackgroundResource(R.drawable.oval_selected);

            gearLongButton.setBackgroundResource(R.drawable.oval_default);
            gearShortButton.setBackgroundResource(R.drawable.oval_default);
        }else{

            gearShortButton.setBackgroundResource(R.drawable.oval_selected);

            gearDefaultButton.setBackgroundResource(R.drawable.oval_default);
            gearLongButton.setBackgroundResource(R.drawable.oval_default);
        }
    }

    public void gearSpeed(String str){
        if(str.equals("High")){

            gearSpeedHigh.setBackgroundResource(R.drawable.oval_selected);

            gearSpeedMiddle.setBackgroundResource(R.drawable.oval_default);
            gearSpeedLow.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Middle")){

            gearSpeedMiddle.setBackgroundResource(R.drawable.oval_selected);

            gearSpeedHigh.setBackgroundResource(R.drawable.oval_default);
            gearSpeedLow.setBackgroundResource(R.drawable.oval_default);

        }else if(str.equals("Low")){

            gearSpeedLow.setBackgroundResource(R.drawable.oval_selected);

            gearSpeedHigh.setBackgroundResource(R.drawable.oval_default);
            gearSpeedMiddle.setBackgroundResource(R.drawable.oval_default);
        }
    }

    public void gearPower(String str){
        if(str.equals("High")){

            gearPowerHigh.setBackgroundResource(R.drawable.oval_selected);

            gearPowerMiddle.setBackgroundResource(R.drawable.oval_default);
            gearPowerLow.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Middle")){
            gearPowerMiddle.setBackgroundResource(R.drawable.oval_selected);

            gearPowerHigh.setBackgroundResource(R.drawable.oval_default);
            gearPowerLow.setBackgroundResource(R.drawable.oval_default);
        }else{
            gearPowerLow.setBackgroundResource(R.drawable.oval_selected);

            gearPowerHigh.setBackgroundResource(R.drawable.oval_default);
            gearPowerMiddle.setBackgroundResource(R.drawable.oval_default);
        }
    }
}