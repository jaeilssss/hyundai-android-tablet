package com.obigo.hkmotors.view;

import android.graphics.Color;
import android.media.Image;
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

public class AxelSettingActivity extends BaseActivity implements View.OnClickListener {


    private SwitchCompat switchCompat;

    private RelativeLayout settingLayout;


    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;


    private ImageButton axelTypeAt;
    private ImageButton axelTypeDct;
    private ImageButton axelTypeAmt;

    private ImageButton gearRateShort;
    private ImageButton gearRateDefault;
    private ImageButton gearRateLong;


    // MPandroidchart or event
    private RadarChart mChart;

    private ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axel_setting);
        initUI();
    }
    public void initUI(){

        back = findViewById(R.id.ib_axel_setting_back);
        back.setOnClickListener(this);
        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");
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
                }else settingLayout.setVisibility(View.INVISIBLE );

            }
        });

        axelTypeAmt = findViewById(R.id.axel_setting_amt_img);
        axelTypeDct = findViewById(R.id.axel_setting_dct_img);
        axelTypeAt = findViewById(R.id.axel_setting_at_check);

        axelTypeAt.setOnClickListener(this);
        axelTypeDct.setOnClickListener(this);
        axelTypeAmt.setOnClickListener(this);

        gearRateShort = findViewById(R.id.axel_short_img);
        gearRateDefault = findViewById(R.id.axel_default_img);
        gearRateLong = findViewById(R.id.axel_long_img);

        gearRateLong.setOnClickListener(this);
        gearRateDefault.setOnClickListener(this);
        gearRateShort.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.axel_setting_at_check :
                axelType("At");
                break;
            case R.id.axel_setting_dct_img :
                axelType("Dct");
                break;
            case R.id.axel_setting_amt_img :
                axelType("Amt");
                break;
            ////
            case R.id.axel_short_img :
                gearRate("Short");
                break;
            case R.id.axel_default_img :
                gearRate("Default");
                break;
            case R.id.axel_long_img :
                gearRate("Long");
                break;
                ////
        }
    }

    public void axelType(String str){
        if(str.equals("At")){
            axelTypeAt.setBackgroundResource(R.drawable.oval_selected);

            axelTypeDct.setBackgroundResource(R.drawable.oval_default);
            axelTypeAmt.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Dct")){

            axelTypeDct.setBackgroundResource(R.drawable.oval_selected);

            axelTypeAmt.setBackgroundResource(R.drawable.oval_default);
            axelTypeAt.setBackgroundResource(R.drawable.oval_default);
        }else {

            axelTypeAmt.setBackgroundResource(R.drawable.oval_selected);

            axelTypeAt.setBackgroundResource(R.drawable.oval_default);
            axelTypeDct.setBackgroundResource(R.drawable.oval_default);
        }

    }

    public void gearRate(String str){

        if(str.equals("Short")){

            gearRateShort.setBackgroundResource(R.drawable.oval_selected);

            gearRateLong.setBackgroundResource(R.drawable.oval_default);
            gearRateDefault.setBackgroundResource(R.drawable.oval_default);
        }else if(str.equals("Default")){

            gearRateDefault.setBackgroundResource(R.drawable.oval_selected);

            gearRateShort.setBackgroundResource(R.drawable.oval_default);
            gearRateLong.setBackgroundResource(R.drawable.oval_default);
        }else{
            gearRateLong.setBackgroundResource(R.drawable.oval_selected);

            gearRateShort.setBackgroundResource(R.drawable.oval_default);
            gearRateDefault.setBackgroundResource(R.drawable.oval_default);
        }
    }
}