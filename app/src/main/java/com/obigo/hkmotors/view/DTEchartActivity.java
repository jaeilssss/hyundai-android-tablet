package com.obigo.hkmotors.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.module.BaseActivity;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Class for the set of parameters and responses
 */
public class DTEchartActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ParamSettingActivity";

    public static Context mContext;

    private ImageView mDTEExit;

    // MPandroidchart or event
    private PieChart mPieChart;
    private CandleStickChart mCandleStickChart;
    private BarChart mStackedBarChart;

    private TextView mAvailableDistance;
    private TextView test;


    // availableDistance
    private int mRespAvailableDistance=0;
    private float mRespAvailableDistanceBase=42.5f;

    private float mRespAvailableDistanceBaseMax=62.5f;
    private float mRespAvailableDistanceBaseMin=-7.0f;

    private float mRespDriverPatternDTEMax=12.5f;
    private float mRespDriverPatternDTEMin=-7.0f;
    private float mRespHarmonizationDTEMax=-1.0f;
    private float mRespHarmonizationDTEMin=-8.0f;
    private float mRespHarmonizationDTE=-3.0f;
    private float mRespVehicleSpeedDTEMax=32.5f;
    private float mRespVehicleSpeedDTEMin=4.0f;

    private float mRespVehicleSpeedDTE=22.5f;
    private float mRespDrivingFeelingDTE=12.5f;
    private float mRespDrivingFeelingDTEMax=19.5f;
    private float mRespDrivingFeelingDTEMin=3.0f;
    private float mRespDriverPatternDTE=0.5f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");
        //mObdDataHandler = new MainActivity.ObdDataHandler();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setDimAmount(0.9f);

        setContentView(R.layout.activity_dte_chart);

        mContext = this;


        initUI();

        PieChart(mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE);
/*

        CandleStickChart(mRespAvailableDistanceBaseMax, mRespAvailableDistanceBaseMin, mRespAvailableDistanceBase
                , mRespDriverPatternDTEMax, mRespDriverPatternDTEMin, mRespDriverPatternDTE
                , mRespHarmonizationDTEMax, mRespHarmonizationDTEMin, mRespHarmonizationDTE
                , mRespVehicleSpeedDTEMax, mRespVehicleSpeedDTEMin, mRespVehicleSpeedDTE
                , mRespDrivingFeelingDTEMax, mRespDrivingFeelingDTEMin, mRespDrivingFeelingDTE);
*/

        StackedBarChart(mRespAvailableDistanceBaseMax, mRespAvailableDistanceBaseMin, mRespAvailableDistanceBase
                , mRespDriverPatternDTEMax, mRespDriverPatternDTEMin, mRespDriverPatternDTE
                , mRespHarmonizationDTEMax, mRespHarmonizationDTEMin, mRespHarmonizationDTE
                , mRespVehicleSpeedDTEMax, mRespVehicleSpeedDTEMin, mRespVehicleSpeedDTE
                , mRespDrivingFeelingDTEMax, mRespDrivingFeelingDTEMin, mRespDrivingFeelingDTE);


        Constants.DTE_MODE_STATUS = 3;
    }

    public void DTEchart(int Distance, int valC01, int valC02, int valC03, int valC04, int valC05
                    ,int max01, int maxC02, int maxC03, int maxC04, int maxC05, int min01, int minC02, int minC03, int minC04, int minC05){

        mAvailableDistance.setText(String.valueOf(Distance));



        mRespAvailableDistanceBase = (float)valC01;
        mRespDriverPatternDTE = (float)valC02;
        mRespHarmonizationDTE = (float)valC03;
        mRespVehicleSpeedDTE = (float)valC04;
        mRespDrivingFeelingDTE = (float)valC05;
        PieChart(mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE);

        // BASE 최소, 최대
        mRespAvailableDistanceBaseMax = (float)(max01);
        mRespAvailableDistanceBaseMin = (float)(min01);

        // 드라이브패턴 최소, 최대
        mRespDriverPatternDTEMax = (float)maxC02;
        mRespDriverPatternDTEMin = (float)minC02;

        Log.d(TAG, "드라이브 패턴값 : " + mRespDriverPatternDTE);

        Log.d(TAG, "드라이브 패턴값Max : " + mRespDriverPatternDTEMax);
        Log.d(TAG, "드라이브 패턴값Min : " + mRespDriverPatternDTEMin);

        // mRespDriverPatternDTE = -10.0f;

        // 공조 최소, 최대
        mRespHarmonizationDTEMax = (float)maxC03;
        mRespHarmonizationDTEMin = (float)minC03;

        // 운전감 최소, 최대
        mRespDrivingFeelingDTEMax = (float)maxC04;
        mRespDrivingFeelingDTEMin = (float)minC04;

        // 차량속도 최소, 최대
        mRespVehicleSpeedDTEMax = (float)maxC05;
        mRespVehicleSpeedDTEMin = (float)minC05;
        StackedBarChart(mRespAvailableDistanceBaseMax, mRespAvailableDistanceBaseMin, mRespAvailableDistanceBase
                , mRespDriverPatternDTEMax, mRespDriverPatternDTEMin, mRespDriverPatternDTE
                , mRespHarmonizationDTEMax, mRespHarmonizationDTEMin, mRespHarmonizationDTE
                , mRespVehicleSpeedDTEMax, mRespVehicleSpeedDTEMin, mRespVehicleSpeedDTE
                , mRespDrivingFeelingDTEMax, mRespDrivingFeelingDTEMin, mRespDrivingFeelingDTE);

        /*
        CandleStickChart(mRespAvailableDistanceBaseMax, mRespAvailableDistanceBaseMin, mRespAvailableDistanceBase
                , mRespDriverPatternDTEMax, mRespDriverPatternDTEMin, mRespDriverPatternDTE
                , mRespHarmonizationDTEMax, mRespHarmonizationDTEMin, mRespHarmonizationDTE
                , mRespVehicleSpeedDTEMax, mRespVehicleSpeedDTEMin, mRespVehicleSpeedDTE
                , mRespDrivingFeelingDTEMax, mRespDrivingFeelingDTEMin, mRespDrivingFeelingDTE);
*/
    }

   /* private Handler mhandler;
    private Message message;

    public DTEchartActivity(Handler DTEhandler){
        mhandler = DTEhandler;
    }
    public void doStart(){
        mhandler.sendMessage(mhandler.obtainMessage());
    }
*/

    @Override
    protected void onResume() {
        super.onResume();

        Constants.DISPLAY_MODE = "PARAM";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Utility.recursiveRecycle(getWindow().getDecorView());
        System.gc();
        Constants.DTE_MODE_STATUS = 0;
    }

    @Override
    public void onClick(View v) {

            switch(v.getId()) {
                case R.id.iv_dte_exit:
                    finish();
                    break;
            default:
                break;
        }

    }


    public void PieChart(float d1, float d2, float d3, float d4, float d5 ) {

        mPieChart.clear();

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(Math.abs(d1), "BASE"));
        entries.add(new PieEntry(Math.abs(d2), "운전페턴"));
        entries.add(new PieEntry(Math.abs(d3), "공조"));
        entries.add(new PieEntry(Math.abs(d4), "최대차속"));
        entries.add(new PieEntry(Math.abs(d5), "운전감"));

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(new int[] {ColorTemplate.rgb("#506bfe"), ColorTemplate.rgb("#529ffa"), ColorTemplate.rgb("#33cccc"), ColorTemplate.rgb("#e8485c"), ColorTemplate.rgb("#f69235")});

        PieData barData = new PieData(set);
        barData.setDrawValues(false);

        mPieChart.setData(barData);
        mPieChart.invalidate();
    }


    public void CandleStickChart(float maxC01, float minC01, float valC01, float maxC02, float minC02, float valC02,
                     float maxC03, float minC03, float valC03, float maxC04, float minC04, float valC04, float maxC05, float minC05, float valC05) {

        mCandleStickChart.clear();

        float[] arrayMin = {minC01, minC02, minC03, minC04, minC05};

        ArrayList<CandleEntry> entries = new ArrayList<>();
        entries.add(new CandleEntry(0, maxC01, minC01, 0, valC01));
        entries.add(new CandleEntry(1, maxC02, minC02, 0, valC02));
        entries.add(new CandleEntry(2, maxC03, minC03, 0, valC03));
        entries.add(new CandleEntry(3, maxC04, minC04, 0, valC04));
        entries.add(new CandleEntry(4, maxC05, minC05, 0, valC05));

        int chData = 0;
        for(float minData : arrayMin){

            if(chData > (int)minData){
                chData = (int)minData;
            }

        }

        mCandleStickChart.getAxisLeft().setAxisMinimum((float)chData);
        // mCandleStickChart.getAxisLeft().setAxisMaximum(maxC01);

        CandleDataSet set = new CandleDataSet(entries, "");
        set.setColors(new int[] {ColorTemplate.rgb("#506bfe"), ColorTemplate.rgb("#529ffa"), ColorTemplate.rgb("#33cccc"), ColorTemplate.rgb("#e8485c"), ColorTemplate.rgb("#f69235")});

        set.setShadowColor(Color.WHITE);
        set.setShadowWidth(0.7f);
        //set.setBarSpace(400f);
        set.setDecreasingColor(ColorTemplate.rgb("#00ff00"));
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(ColorTemplate.rgb("#00ff00"));
        set.setIncreasingPaintStyle(Paint.Style.FILL);

        CandleData barData = new CandleData(set);
        barData.setDrawValues(false);

        mCandleStickChart.setData(barData);
        mCandleStickChart.invalidate();
    }


    public void StackedBarChart(float maxC01, float minC01, float valC01, float maxC02, float minC02, float valC02,
                                 float maxC03, float minC03, float valC03, float maxC04, float minC04, float valC04, float maxC05, float minC05, float valC05) {

        mStackedBarChart.clear();

        test.setText("BASEmin : " + String.valueOf(minC01)+ "\n" + "BASEmax : " + String.valueOf(maxC01)+ "\n" + "BASEval : " + String.valueOf(valC01) + "\n"
        + "운전패턴min : " + String.valueOf(minC02)+ "\n" + "운전패턴max : " + String.valueOf(maxC02)+ "\n" + "운전패턴val : " + String.valueOf(valC02) + "\n"
        + "공조min : " + String.valueOf(minC03)+ "\n" + "공조max : " + String.valueOf(maxC03)+ "\n" + "공조val : " + String.valueOf(valC03) + "\n"
        + "최대차속min : " + String.valueOf(minC04)+ "\n" + "최대차속max : " + String.valueOf(maxC04)+ "\n" + "최대차속val : " + String.valueOf(valC04) + "\n"
        + "운전감min : " + String.valueOf(minC05)+ "\n" + "운전감max : " + String.valueOf(maxC05)+ "\n" + "운전감val : " + String.valueOf(valC05) );

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, new float[]{minC01, formetter(minC01, valC01), formetterMax(minC01, formetter(minC01, valC01), maxC01)}));
        entries.add(new BarEntry(1, new float[]{minC02, formetter(minC02, valC02), formetterMax(minC02, formetter(minC02, valC02), maxC02)}));
        entries.add(new BarEntry(2, new float[]{minC03, formetter(minC03, valC03), formetterMax(minC03, formetter(minC03, valC03), maxC03)}));
        entries.add(new BarEntry(3, new float[]{minC04, formetter(minC04, valC04), formetterMax(minC04, formetter(minC04, valC04), maxC04)}));
        entries.add(new BarEntry(4, new float[]{minC05, formetter(minC05, valC05), formetterMax(minC05, formetter(minC05, valC05), maxC05)}));



        BarDataSet set;

        if(mStackedBarChart.getData() != null && mStackedBarChart.getData().getDataSetCount() > 0){
            set = (BarDataSet) mStackedBarChart.getData().getDataSetByIndex(0);
            set.setValues(entries);
            mStackedBarChart.getData().notifyDataChanged();
            mStackedBarChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(entries, "");
            set.setStackLabels(new String[]{"MIN", "CUSTOM", "MAX"});

            set.setValueFormatter(new IValueFormatter() {
                float TotalData = 0.0f;
                float data = 0.0f;
                int xAxisCnt = 0;
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                    if(xAxisCnt == 0){
                        xAxisCnt++;
                        data = value;
                        return String.valueOf(data);
                    } else if(xAxisCnt == 1){
                        xAxisCnt++;
                        if(data > 0){
                            data = data + value;
                        } else {
                            data = value;
                        }
                        return String.valueOf(data);
                    } else if(xAxisCnt == 2){
                        xAxisCnt = 0;
                        if(data > 0){
                            data = data + value;
                        } else {
                            data = value;
                        }
                        return String.valueOf(data);
                    } else{
                        // 여기로 들어오는 경우는 없어야 된다.
                        return String.valueOf(value);
                    }
                }

            });
            set.setColors(new int[] {ColorTemplate.rgb("#cd0000"), ColorTemplate.rgb("#40a940"), ColorTemplate.rgb("#3232ff")});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            mStackedBarChart.setData(data);

        }
        mStackedBarChart.invalidate();
    }


    private void initUI() {

        final ArrayList<String> labels = new ArrayList<String>();

        labels.add("BASE");
        labels.add("운전페턴");
        labels.add("공조");
        labels.add("최대차속");
        labels.add("운전감");


        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mPieChart.setNoDataText("데이터가 없습니다.");

        mStackedBarChart = (BarChart) findViewById(R.id.stacked_bar_chart);
        mStackedBarChart.setNoDataText("데이터가 없습니다.");

        mCandleStickChart = (CandleStickChart) findViewById(R.id.candel_stick_chart);
        mCandleStickChart.setNoDataText("데이터가 없습니다.");

        mDTEExit = (ImageView) findViewById(R.id.iv_dte_exit);
        mDTEExit.setOnClickListener(this);

        mAvailableDistance = (TextView) findViewById(R.id.tv_dte_dstn);
        test = (TextView) findViewById(R.id.tv_dte_test);


        // 원형 그래프
        mPieChart.setDrawEntryLabels(false);
        mPieChart.setDrawHoleEnabled(false);

        mPieChart.getLegend().setEnabled(true);
        mPieChart.getLegend().setTextColor(Color.WHITE);
        mPieChart.getLegend().setTextSize(18f);
        mPieChart.getLegend().setFormSize(18f);
        mPieChart.getLegend().setXEntrySpace(10f);
        mPieChart.getLegend().setYEntrySpace(10f);
        mPieChart.getLegend().setFormLineWidth(100f);
        mPieChart.getLegend().setWordWrapEnabled(true);


        mPieChart.getDescription().setEnabled(false);
        mPieChart.setTouchEnabled(false);


        // 막대 그래프
        mStackedBarChart.getXAxis().setTextColor(Color.WHITE);
        mStackedBarChart.getXAxis().setTextSize(15f);
        mStackedBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mStackedBarChart.getXAxis().setDrawGridLines(false);

        mStackedBarChart.getXAxis().setLabelCount(5, false);
        mStackedBarChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return labels.get((int) value % labels.size());
            }
        });

        mStackedBarChart.getAxisLeft().setTextColor(Color.WHITE);
        mStackedBarChart.getAxisLeft().setTextSize(15f);
        mStackedBarChart.getAxisRight().setEnabled(false);

        mStackedBarChart.getLegend().setTextColor(Color.WHITE);
        mStackedBarChart.getLegend().setTextSize(15f);
        mStackedBarChart.getLegend().setFormSize(15f);
        mStackedBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        mStackedBarChart.setExtraBottomOffset(15f);
        mStackedBarChart.getDescription().setEnabled(false);
        mStackedBarChart.setTouchEnabled(false);




        // 캔들막대 그래프
        mCandleStickChart.getXAxis().setTextColor(Color.WHITE);     // change label color
        mCandleStickChart.getXAxis().setDrawGridLines(false);
        mCandleStickChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mCandleStickChart.getXAxis().setLabelCount(5, false);

        mCandleStickChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return labels.get((int) value % labels.size());
            }
        });

        mCandleStickChart.getLegend().setEnabled(false);            // remove legend

        //mCandleStickChart.getAxisLeft().setDrawGridLines(false);

        mCandleStickChart.getAxisLeft().setTextColor(Color.WHITE);
        mCandleStickChart.getAxisLeft().setDrawAxisLine(false);
        mCandleStickChart.getAxisLeft().setGridColor(Color.GRAY);

        //mCandleStickChart.getAxisRight().setDrawGridLines(false);
        mCandleStickChart.getAxisRight().setEnabled(false);

        mCandleStickChart.getDescription().setEnabled(false);
        mCandleStickChart.setTouchEnabled(false);                   // disable touch

    }

    private float formetter(float a1, float b1){
        if(a1 < 0.0f ){
            return b1;
        } else {
            return b1 - a1;
        }
    }



    private float formetterMax(float a2, float b2, float c2){
        if(a2 < 0.0f ){
            if(b2 < 0.0f){
                return c2;
            }else{
                return c2 - b2;
            }
        } else {
            if(b2 < 0.0f){
                return c2;
            }else{
                return c2 - b2 - a2;
            }
        }
    }
}
