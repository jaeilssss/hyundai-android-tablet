package com.obigo.hkmotors.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.network.HttpService;
import com.obigo.hkmotors.common.pref.SharedPreference;
import com.obigo.hkmotors.common.service.ObdService;
import com.obigo.hkmotors.custom.LoadingDialog;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.FavoriteData;
import com.obigo.hkmotors.model.FavoriteDataListItems;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.model.TempTransmission;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;
import com.obigo.hkmotors.module.Network;
import com.obigo.hkmotors.module.Result_DrivingInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    public static Context mContext;
    private ImageView  mainConnectionLight;
    private TextView carConnectionBtn;

    private RelativeLayout mLayoutBarChart;
    private RelativeLayout mLayoutRcdation;
    private RelativeLayout mLayoutExpert;

    private ArrayList<FavoriteDataListItems> dataListItems = new ArrayList<>();

    private ConstraintLayout mMode;

    private ImageView gearBtn;
    private ImageView speakerBtn;
    private ImageView drivingAxleBtn;



    private Dialog mProgressDialog;
    private ProgressBar mProgress;
    private TextView mProgressValue;
    private int pStatus = 0;
    private int cnt = 0;

    // MPandroidchart or event
    private RadarChart mChart;

    private ObdService mObdsv;


    //private BackPressCloseHandler backPressCloseHandler;
    //public SharedPreference mPref;

    private String obdMac = "";

    private String mModeValue;


    // initial value
    private int mParamDriving=50;
    private int mParamTorque=50;
    private int mParamAcceleration=0;
    private int mParamDeceleration=0;
    private int mParamBrake=0;
    private int mParamEnergy=0;
    private int mParamSpeed=60;
    private int mParamResponse=0;

    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;


    private int mRespVehicleSpeedDTE=3;
    private int mRespHarmonizationDTE=3;
    private int mRespDrivingFeelingDTE=3;
    private int mRespDriverPatternDTE=3;
    private int mRespAvailableDistanceBase=3;

    private int mRespVehicleSpeedDTEMax=3;
    private int mRespVehicleSpeedDTEMin=3;
    private int mRespHarmonizationDTEMax=3;
    private int mRespHarmonizationDTEMin=3;
    private int mRespDrivingFeelingDTEMax=3;
    private int mRespDrivingFeelingDTEMin=3;
    private int mRespDriverPatternDTEMax=3;
    private int mRespDriverPatternDTEMin=3;

    // availableDistance
    private int mRespAvailableDistance=0;
    private int mRespAvailableDistanceMin=0;
    private int mRespAvailableDistanceMax=0;

    // left
    private int mRespPGauge=0;
    private int mRespBtrSOC=0;

    // center
    private int mRespVSpeed=0;
    private int mRespLStatus=0;
    private int mRespEVReady=0;
    private int mRespPaddle=0;
    private int mRespDMode=0;

    // rigth
    private int mRespInstFlefc=0;
    private int mRespAvgFlefc=0;

    // graph
    private int aa ;

    // 차량 데이터 값
    private int comfortable;    //안락감
    private int leading;        //주도성
    private int dynamic;        //역동성
    private int Efficiency;     //효율성
    private int performance;    // 동력성능

    // newSettingData

    private int nComfortable;
    private int nLeading;
    private int nDynamic;
    private int nEfficiency;
    private int nPerformance;

    private  Button resetBtn;
    private Button carSend;
    private Button saveBtn;

    private Button commercial;
    private Button ev;
    private Button vip;
    private Button sport;
    private Button passenger;
    
    private TextView mainModeTxt;

    private ArrayList<FavoriteData> recommentList;

    private Dialog dialog;

    private int isEdit;
    private String editTitle;
    private int editId;

    private String ip = "192.168.0.3";
    private Handler mHandler;
    InetAddress serverAddr;
    PrintWriter sendWriter;
    BufferedReader input;
    String read;
    MainActivity activity = this;
    Button ex,rc;
    private int port = 1234;

    private Button rcSend;
    LoadingDialog loadingDialog ;

    boolean isTransmissionSetting = false;
    boolean isDrivingSetting = false;
    boolean isSoundSetting = false;

    String selectedPreset="";
    Boolean isConnected  = false;
    ArrayList<String> signalList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "== onCreate");

        mModeValue = getIntent().getStringExtra("mode");

        mPref = new SharedPreference(getApplicationContext());

        mContext = this;

        // 추천모드 리스트
        recommentList = modeListDatabase();

        // UI 초기화
        initUI();
        if(Constants.CONNECTION_STATUS){
            mainConnectionLight.setBackgroundResource(R.drawable.ico_light_green);
            carConnectionBtn.setText("차량 연결 ON");
        }else{
            mainConnectionLight.setBackgroundResource(R.drawable.ico_light_red);
            carConnectionBtn.setText("차량 연결 OFF");
            connectedWifi();
        }
        isEdit = 1;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // something..
                getOdbData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                                        CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                                        CarData.getInstance().getPerformance());
                            }
                        });

                    }
                }).start();
            }
        }, 0);

//        connectedSocket();

    }

    private void setEditMode(){
        if(isEdit==1){
            resetBtn.setText("초기화");
            carSend.setVisibility(View.VISIBLE);
            mMode.setVisibility(View.VISIBLE);
            mainModeTxt.setVisibility(View.INVISIBLE);

        }else{
            resetBtn.setText("편집 취소");
            carSend.setVisibility(View.INVISIBLE);
            mMode.setVisibility(View.INVISIBLE);
            mainModeTxt.setVisibility(View.VISIBLE);
        }
    }
    private void getOdbData(){
        // 최초 접속 시 차량 데이터 가저오기

        // 변속
        Transmission.getInstance().setIsOn("1");
        Transmission.getInstance().setGear("011");
        Transmission.getInstance().setType("00");
        Transmission.getInstance().setGearRate("01");
        Transmission.getInstance().setTransmissionSpeed("00");
        Transmission.getInstance().setTransmissionPower("00");
        Transmission.getInstance().setTransmissionMap("00");


        // 음향

        Sound.getInstance().setIsOn("1");
        Sound.getInstance().setDriveType("1");
        Sound.getInstance().setVolume("00");
        Sound.getInstance().setBackVolume("00");
        Sound.getInstance().setBackSensitive("0");

        // 구동축

        Drive.getInstance().setIsOn("1");
        Drive.getInstance().setStiffness("01");
        Drive.getInstance().setReducer("01");


        CarData.getInstance().setComfortable();
        CarData.getInstance().setDynamic();
        CarData.getInstance().setEfficiency();
        CarData.getInstance().setLeading();
        CarData.getInstance().setPerformance();

        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempPerformance();

        defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());

    }


    @Override
    protected void onResume() {
        super.onResume();

        Constants.DISPLAY_MODE = "MAIN";
    }


    public void setBtnAnimation(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.turn_anim);

        gearBtn.clearAnimation();
        speakerBtn.clearAnimation();
        drivingAxleBtn.clearAnimation();

        gearBtn.startAnimation(animation);
        speakerBtn.startAnimation(animation);
        drivingAxleBtn.startAnimation(animation);
        gearBtn.setBackgroundResource(R.drawable.no_setting_click);
        speakerBtn.setBackgroundResource(R.drawable.no_setting_click);
        drivingAxleBtn.setBackgroundResource(R.drawable.no_setting_click);
        gearBtn.setImageResource(R.drawable.ic_setting_grey);
        speakerBtn.setImageResource(R.drawable.ic_setting_grey);
        drivingAxleBtn.setImageResource(R.drawable.ic_setting_grey);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.gear_box_btn :
            {
                goToGearSettingActivity();
                break;
            }

            case R.id.speaker_btn :{
                goToSpeakerSettingActivity();
                break;
            }
            case R.id.driving_axle_btn :{
                goToDrivingSettingActivity();
                break;
            }
            case R.id.ib_favorite_btn:

                if(DBUtil.getRowCount(getApplicationContext()) == 0) {
                    Toast.makeText(getApplicationContext(), "나의 설정 정보가 없습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    goToFavoriteActivity();
                }

                break;



            //case R.id.ib_g_reset_btn:
            case R.id.ib_e_reset_btn:


                if(isEdit==1){
                    setBtnAnimation();
                    Transmission.getInstance().reset();
                    Drive.getInstance().reset();
                    Sound.getInstance().reset();
                    CarData.getInstance().setTempLeading();
                    CarData.getInstance().setTempComfortable();
                    CarData.getInstance().setTempEfficiency();
                    CarData.getInstance().setTempPerformance();
                    CarData.getInstance().setTempDynamic();

                    defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                            CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                            CarData.getInstance().getPerformance());

                }else{
                    showDialog01();

                }



                break;


            case R.id.ib_e_submit_btn:

                if(isDrivingSetting && isTransmissionSetting && isSoundSetting){

                    if(Network.getInstance()!=null){
                        sendCarData();
                    }else{
                        Toast.makeText(getApplicationContext(),"차량과 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "모든 세팅을 완료 해주세요",Toast.LENGTH_SHORT).show();

                }
                break;

            //case R.id.ib_g_submit_btn:
            case R.id.ib_e_save_btn:

                Constants.COMMAND_MODE = "SUBMIT";
                goToParamSettingActivity();
                break;

            case R.id.ib_confirm_btn:
                // progressbar dialog confirm
                dismissProgressDialog();
                break;
            case R.id.rcdation_ev :
                selectedPreset = "000";
                clickRecomment(0);
                break;

            case R.id.rcdation_vip :
                selectedPreset = "001";
                clickRecomment(1);
                break;
            case R.id.rcdation_passenger :
                selectedPreset = "010";
                clickRecomment(2);
                break;

            case R.id.rcdation_sport :
                selectedPreset = "011";
                clickRecomment(3);
                break;
            case R.id.rcdation_commercial :
                selectedPreset = "100";
                clickRecomment(4);

                break;
            case R.id.rcdation_submit :

                if(Network.getInstance()!=null){
                    if(selectedPreset.length()==0){
                        return;
                    }
                    sendCarData();
                }else {
                    Toast.makeText(getApplicationContext(),"차량과 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tb_setting :


            case R.id.expert_mode :
                if(mModeValue.equals(Constants.MODE_RCDATION)){
                    mModeValue = Constants.MODE_EXPERT;
                    setMode();
                    selectedPreset = "101";
                }
                break;
            case R.id.rc_mode :
                if(mModeValue.equals(Constants.MODE_EXPERT)){
                    mModeValue = Constants.MODE_RCDATION;
                    setMode();
                    setBtnAnimation();
                    isDrivingSetting = false;
                    isSoundSetting = false;
                    isTransmissionSetting = false;
                    selectedPreset ="";
                }
                break;
            case R.id.car_connection_btn:
                connectedWifi();

                break;
            default:
                break;
        }
    }



    public void setEditData(String signal1 , String signal2){

        String [] signal1Array = signal1.split(" ");
        String [] signal2Array = signal2.split(" ");

        if(signal1Array[1].equals("1")){
            Sound.getInstance().setIsOn("1");
        }else {
            Sound.getInstance().setIsOn("0");
        }
        if(signal1Array[2].equals("1")){
            Sound.getInstance().setDriveType("1");
        }else{
            Sound.getInstance().setDriveType("0");
        }

        if(signal1Array[3].equals("00")){
            Sound.getInstance().setVolume("00");
        }else if(signal1Array[3].equals("01")){
            Sound.getInstance().setVolume("01");
        }else{
            Sound.getInstance().setVolume("10");
        }

        if(signal1Array[4].equals("00")){
            Sound.getInstance().setBackVolume("00");
        }else if(signal1Array[4].equals("01")){
            Sound.getInstance().setBackVolume("01");
        }else if(signal1Array[4].equals("10")) {
            Sound.getInstance().setBackVolume("10");
        }else{
            Sound.getInstance().setBackVolume("11");
        }

        if(signal1Array[5].equals("1")){
            Sound.getInstance().setBackSensitive("1");
        }else{
            Sound.getInstance().setBackSensitive("0");
        }

        if(signal1Array[6].equals("1")){
            Drive.getInstance().setIsOn("1");
        }else{
            Drive.getInstance().setIsOn("0");
        }

        if(signal1Array[7].equals("00")){
            Drive.getInstance().setStiffness("00");
        }else if(signal1Array[7].equals("01")){
            Drive.getInstance().setStiffness("01");
        }else{
            Drive.getInstance().setStiffness("10");
        }

        if(signal1Array[8].equals("00")){
            Drive.getInstance().setReducer("00");
        }else if(signal1Array[8].equals("01")){
            Drive.getInstance().setReducer("01");
        }else{
            Drive.getInstance().setReducer("10");
        }

        if(signal2Array[0].equals("1")){
            Transmission.getInstance().setIsOn("1");
        }else{
            Transmission.getInstance().setIsOn("0");
        }

        if(signal2Array[1].equals("00")){
            Transmission.getInstance().setType("00");
        }else if(signal2Array[1].equals("01")){
            Transmission.getInstance().setType("01");
        }else {
            Transmission.getInstance().setType("10");
        }

        if(signal2Array[2].equals("000")){
            Transmission.getInstance().setGear("000");
        }else if(signal2Array[2].equals("001")){
            Transmission.getInstance().setGear("001");
        }else if(signal2Array[2].equals("010")){
            Transmission.getInstance().setGear("010");
        }else if(signal2Array[2].equals("011")){
            Transmission.getInstance().setGear("011");
        }else{
            Transmission.getInstance().setGear("100");
        }

        if(signal2Array[3].equals("00")){
            Transmission.getInstance().setGearRate("00");
        }else if(signal2Array[3].equals("01")){
            Transmission.getInstance().setGearRate("01");
        }else{
            Transmission.getInstance().setGearRate("10");
        }

        if(signal2Array[4].equals("00")){
            Transmission.getInstance().setTransmissionSpeed("00");
        }else if(signal2Array[4].equals("01")){
            Transmission.getInstance().setTransmissionSpeed("01");
        }else{
            Transmission.getInstance().setTransmissionSpeed("10");
        }

        if(signal2Array[5].equals("00")){
            Transmission.getInstance().setTransmissionPower("00");
        }else if(signal2Array.equals("01")){
            Transmission.getInstance().setTransmissionPower("01");
        }else{
            Transmission.getInstance().setTransmissionPower("10");
        }

        if(signal2Array[6].equals("00")){

            Transmission.getInstance().setTransmissionMap("00");
        }else if(signal2Array[6].equals("01")){

            Transmission.getInstance().setTransmissionMap("01");
        }else{

            Transmission.getInstance().setTransmissionMap("10");
        }


        if(Constants.OBD_INITIALIZED){
            if(Transmission.getInstance().getTempIsOn().equals("0") &&
                    Sound.getInstance().getTempIsOn().equals("0") &&
                    Drive.getInstance().getTempIsOn().equals("0")){
                CarData.getInstance().setTempEVMode();
                CarData.getInstance().setEVMode();

            }else{
                CarData.getInstance().setComfortable();
                CarData.getInstance().setTempComfortable();
                CarData.getInstance().setDynamic();
                CarData.getInstance().setTempDynamic();
                CarData.getInstance().setEfficiency();
                CarData.getInstance().setTempEfficiency();
                CarData.getInstance().setLeading();
                CarData.getInstance().setTempLeading();
                CarData.getInstance().setPerformance();
                CarData.getInstance().setTempPerformance();
            }


        }else{
            // 이거 어떻게 해야할까? 만약 연결 되어 있지 않다면 ....
            if(Transmission.getInstance().getTempIsOn().equals("0") &&
                    Sound.getInstance().getTempIsOn().equals("0") &&
                    Drive.getInstance().getTempIsOn().equals("0")){
                CarData.getInstance().setTempEVMode();
                CarData.getInstance().setEVMode();
            }else{
                CarData.getInstance().setComfortable();
                CarData.getInstance().setTempComfortable();
                CarData.getInstance().setDynamic();
                CarData.getInstance().setTempDynamic();
                CarData.getInstance().setEfficiency();
                CarData.getInstance().setTempEfficiency();
                CarData.getInstance().setLeading();
                CarData.getInstance().setTempLeading();
                CarData.getInstance().setPerformance();
                CarData.getInstance().setTempPerformance();
            }

        }

        defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());
    }



    public void clickRecomment(int index){

        recommendChangeBackGround(index);
        String signal1 = recommentList.get(index).getSignal1();
        String signal2 = recommentList.get(index).getSignal2();

        String [] signal1Array = signal1.split(" ");
        String [] signal2Array = signal2.split(" ");

        if(signal1Array[1].equals("1")){
            Sound.getInstance().setTempIsOn("1");

        }else {
            Sound.getInstance().setTempIsOn("0");

        }
        if(signal1Array[2].equals("1")){
            Sound.getInstance().setTempDriveType("1");
        }else{
            Sound.getInstance().setTempDriveType("0");
        }

        if(signal1Array[3].equals("00")){
            Sound.getInstance().setTempVolume("00");
        }else if(signal1Array[3].equals("01")){
            Sound.getInstance().setTempVolume("01");
        }else{
            Sound.getInstance().setTempVolume("10");
        }

        if(signal1Array[4].equals("00")){
            Sound.getInstance().setTempBackVolume("00");
        }else if(signal1Array[4].equals("01")){
            Sound.getInstance().setTempBackVolume("01");
        }else if(signal1Array[4].equals("10")) {
            Sound.getInstance().setTempBackVolume("10");
        }else{
            Sound.getInstance().setTempBackVolume("11");
        }

        if(signal1Array[5].equals("1")){
            Sound.getInstance().setTempBackSensitive("1");
        }else{
            Sound.getInstance().setTempBackSensitive("0");
        }

        if(signal1Array[6].equals("1")){
            Drive.getInstance().setTempIsOn("1");
        }else{
            Drive.getInstance().setTempIsOn("0");
        }

        if(signal1Array[7].equals("00")){
            Drive.getInstance().setTempStiffness("00");
        }else if(signal1Array[7].equals("01")){
            Drive.getInstance().setTempStiffness("01");
        }else{
            Drive.getInstance().setTempStiffness("10");
        }

        if(signal1Array[8].equals("00")){
            Drive.getInstance().setTempReducer("00");
        }else if(signal1Array[8].equals("01")){
            Drive.getInstance().setTempReducer("01");
        }else{
            Drive.getInstance().setTempReducer("10");
        }

        if(signal2Array[0].equals("1")){
            Transmission.getInstance().setTempIsOn("1");
        }else{
            Transmission.getInstance().setTempIsOn("0");
        }

        if(signal2Array[1].equals("00")){
            Transmission.getInstance().setTempType("00");
        }else if(signal2Array[1].equals("01")){
            Transmission.getInstance().setTempType("01");
        }else {
            Transmission.getInstance().setTempType("10");
        }

        if(signal2Array[2].equals("000")){
            Transmission.getInstance().setTempGear("000");
        }else if(signal2Array[2].equals("001")){
            Transmission.getInstance().setTempGear("001");
        }else if(signal2Array[2].equals("010")){
            Transmission.getInstance().setTempGear("010");
        }else if(signal2Array[2].equals("011")){
            Transmission.getInstance().setTempGear("011");
        }else{
            Transmission.getInstance().setTempGear("100");
        }

        if(signal2Array[3].equals("00")){
            Transmission.getInstance().setTempGearRate("00");
        }else if(signal2Array[3].equals("01")){
            Transmission.getInstance().setTempGearRate("01");
        }else{
            Transmission.getInstance().setTempGearRate("10");
        }

        if(signal2Array[4].equals("00")){
            Transmission.getInstance().setTempTransmissionSpeed("00");
        }else if(signal2Array[4].equals("01")){
            Transmission.getInstance().setTempTransmissionSpeed("01");
        }else{
            Transmission.getInstance().setTempTransmissionSpeed("10");
        }

        if(signal2Array[5].equals("00")){
            Transmission.getInstance().setTempTransmissionPower("00");
        }else if(signal2Array.equals("01")){
            Transmission.getInstance().setTempTransmissionPower("01");
        }else{
            Transmission.getInstance().setTempTransmissionPower("10");
        }

        if(signal2Array[6].equals("00")){

            Transmission.getInstance().setTempTransmissionMap("00");
        }else if(signal2Array[6].equals("01")){

            Transmission.getInstance().setTempTransmissionMap("01");
        }else{

            Transmission.getInstance().setTempTransmissionMap("10");
        }


        if(Constants.OBD_INITIALIZED){
        if(Transmission.getInstance().getTempIsOn().equals("0") &&
            Sound.getInstance().getTempIsOn().equals("0") &&
            Drive.getInstance().getTempIsOn().equals("0")){
            CarData.getInstance().setTempEVMode();

        }else{
            CarData.getInstance().setTempComfortable();
            CarData.getInstance().setTempDynamic();
            CarData.getInstance().setTempEfficiency();
            CarData.getInstance().setTempLeading();
            CarData.getInstance().setTempPerformance();
        }



            modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                    CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                    CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                    CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());

        }else{
            // 이거 어떻게 해야할까? 만약 연결 되어 있지 않다면 ....
            if(Transmission.getInstance().getTempIsOn().equals("0") &&
                    Sound.getInstance().getTempIsOn().equals("0") &&
                    Drive.getInstance().getTempIsOn().equals("0")){
                CarData.getInstance().setTempEVMode();

            }else{
                CarData.getInstance().setTempComfortable();
                CarData.getInstance().setTempDynamic();
                CarData.getInstance().setTempEfficiency();
                CarData.getInstance().setTempLeading();
                CarData.getInstance().setTempPerformance();
            }

            modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                    CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                    CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                    CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
        }

    }

    public void recommendChangeBackGround(int num){
        if(num==-1){
            ev.setBackgroundResource(R.drawable.shape_rc_border_10);
            vip.setBackgroundResource(R.drawable.shape_rc_border_10);
            passenger.setBackgroundResource(R.drawable.shape_rc_border_10);
            sport.setBackgroundResource(R.drawable.shape_rc_border_10);
            commercial.setBackgroundResource(R.drawable.shape_rc_border_10);
        } else if(num==0){
            ev.setBackgroundResource(R.drawable.button_shape_radius_10);

            vip.setBackgroundResource(R.drawable.shape_rc_border_10);
            passenger.setBackgroundResource(R.drawable.shape_rc_border_10);
            sport.setBackgroundResource(R.drawable.shape_rc_border_10);
            commercial.setBackgroundResource(R.drawable.shape_rc_border_10);
        }else if(num==1){
            vip.setBackgroundResource(R.drawable.button_shape_radius_10);

            ev.setBackgroundResource(R.drawable.shape_rc_border_10);
            passenger.setBackgroundResource(R.drawable.shape_rc_border_10);
            sport.setBackgroundResource(R.drawable.shape_rc_border_10);
            commercial.setBackgroundResource(R.drawable.shape_rc_border_10);
        }else if(num==2){

            passenger.setBackgroundResource(R.drawable.button_shape_radius_10);

            ev.setBackgroundResource(R.drawable.shape_rc_border_10);
            vip.setBackgroundResource(R.drawable.shape_rc_border_10);
            sport.setBackgroundResource(R.drawable.shape_rc_border_10);
            commercial.setBackgroundResource(R.drawable.shape_rc_border_10);

        }else if(num==3){

            sport.setBackgroundResource(R.drawable.button_shape_radius_10);

            ev.setBackgroundResource(R.drawable.shape_rc_border_10);
            vip.setBackgroundResource(R.drawable.shape_rc_border_10);
            passenger.setBackgroundResource(R.drawable.shape_rc_border_10);
            commercial.setBackgroundResource(R.drawable.shape_rc_border_10);

        }else{
            
            commercial.setBackgroundResource(R.drawable.button_shape_radius_10);
            
            ev.setBackgroundResource(R.drawable.shape_rc_border_10);
            vip.setBackgroundResource(R.drawable.shape_rc_border_10);
            passenger.setBackgroundResource(R.drawable.shape_rc_border_10);
            sport.setBackgroundResource(R.drawable.shape_rc_border_10);

        }

    }

    /**
     * Initialize UI
     */
    public void initUI() {

        TextView favoriteBtn =  findViewById(R.id.ib_favorite_btn);
        favoriteBtn.setOnClickListener(this);

        gearBtn = findViewById(R.id.gear_box_btn);
        gearBtn.setOnClickListener(this);
        speakerBtn = findViewById(R.id.speaker_btn);
        speakerBtn.setOnClickListener(this);
        drivingAxleBtn = findViewById(R.id.driving_axle_btn);
        drivingAxleBtn.setOnClickListener(this);


        carConnectionBtn =  findViewById(R.id.car_connection_btn);
        carConnectionBtn.setOnClickListener(this);

        mainConnectionLight = (ImageView) findViewById(R.id.iv_main_light);

        mMode = findViewById(R.id.tb_setting);
        ex = findViewById(R.id.expert_mode);
        rc = findViewById(R.id.rc_mode);
        ex.setOnClickListener(this);
        rc.setOnClickListener(this);

        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");

        mLayoutRcdation = (RelativeLayout) findViewById(R.id.layout_rcdation);
        mLayoutExpert = (RelativeLayout) findViewById(R.id.layout_expert);

        saveBtn = findViewById(R.id.ib_e_save_btn);
        saveBtn.setOnClickListener(this);
        carSend = findViewById(R.id.ib_e_submit_btn);
        carSend.setOnClickListener(this);
        resetBtn = findViewById(R.id.ib_e_reset_btn);
        resetBtn.setOnClickListener(this);

        // OBD Status 변경

        vip = findViewById(R.id.rcdation_vip);
        vip.setOnClickListener(this);
        ev = findViewById(R.id.rcdation_ev);
        ev.setOnClickListener(this);
        passenger = findViewById(R.id.rcdation_passenger);
        passenger.setOnClickListener(this);
        sport = findViewById(R.id.rcdation_sport);
        sport.setOnClickListener(this);
        commercial = findViewById(R.id.rcdation_commercial);
        commercial.setOnClickListener(this);
        
        mainModeTxt = findViewById(R.id.main_mode_text);
        mainModeTxt.setVisibility(View.INVISIBLE);

        dialog =  new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_cancel);

        rcSend = findViewById(R.id.rcdation_submit);
        rcSend.setOnClickListener(this);
        if(mModeValue.equals(Constants.MODE_RCDATION)){

            setMode();

        } else if (mModeValue.equals(Constants.MODE_EXPERT)){

            setMode();

        }


        setBtnAnimation();



    }


    public void setMode(){

        if(mModeValue.equals(Constants.MODE_EXPERT)){

            ex.setTextColor(Color.WHITE);
            rc.setTextColor(Color.GRAY);
            ex.setBackgroundResource(R.drawable.shape_toggle_expert_on);
            rc.setBackgroundResource(R.drawable.shape_toggle_rc_off);

            mLayoutExpert.setVisibility(View.VISIBLE);
            mLayoutRcdation.setVisibility(View.GONE);

        }else{
            ex.setTextColor(Color.GRAY);
            rc.setTextColor(Color.WHITE);
            findViewById(R.id.expert_mode).setBackgroundResource(R.drawable.shape_toggle_expert_off);
            findViewById(R.id.rc_mode).setBackgroundResource(R.drawable.shape_toggle_rc_on);
            mLayoutExpert.setVisibility(View.GONE);
            mLayoutRcdation.setVisibility(View.VISIBLE);
            recommendChangeBackGround(-1);
        }

        defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getInstance().getDynamic(),
                CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance());
        Transmission.getInstance().reset();
        Sound.getInstance().reset();
        Drive.getInstance().reset();
    }


    // =============================================================================================
    /**
     * Set OBD mode
     *
     * @param switchFlag - odb mode about whether switch is on or off
     */


    /**
     * Result from another activity
     *
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - data
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult // requestCode :: " + requestCode + " // resultCode :: " + resultCode);
        switch (requestCode) {
            case Constants.REQUEST_CONNECT_WIFI :
                if(Constants.CONNECTION_STATUS){
                    mainConnectionLight.setBackgroundResource(R.drawable.ico_light_green);
                    carConnectionBtn.setText("차량 연결 ON");
                }else{
                    mainConnectionLight.setBackgroundResource(R.drawable.ico_light_red);
                    carConnectionBtn.setText("차량 연결 OFF");
                }
                break;


            case Constants.REQUEST_FAVORITE:

                if(data.getBooleanExtra("check",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
                    isEdit = 2;
                    setEditMode();
                    editTitle = data.getStringExtra("title");
                    editId = data.getIntExtra("id",-1);
                }else{
                    defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance());
                    isEdit = 1;
                    setEditMode();
                }

                break;
            case Constants.REQUEST_GEAR_SETTING :
                // 차량 obd가 연결되어있느냐에 따라서 다르게 처리해야함..
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());

                    gearBtn.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.gear_after_anim);
                    gearBtn.setImageResource(R.drawable.setting_check_main_color);
                    gearBtn.setBackgroundResource(R.drawable.setting_click);
                    gearBtn.startAnimation(animation);

                    isTransmissionSetting  = true;

                }
                break;
            case Constants.REQUEST_SPEAKER_SETTING :
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());

                    speakerBtn.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.gear_after_anim);
                    speakerBtn.setImageResource(R.drawable.setting_check_main_color);
                    speakerBtn.setBackgroundResource(R.drawable.setting_click);
                    speakerBtn.startAnimation(animation);

                    isSoundSetting = true;
                }


                break;
            case Constants.REQUEST_DRIVING_SETTING :
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());

                    drivingAxleBtn.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.driving_after_anim);
                    drivingAxleBtn.setImageResource(R.drawable.setting_check_main_color);
                    drivingAxleBtn.setBackgroundResource(R.drawable.setting_click);
                    drivingAxleBtn.startAnimation(animation);

                    isDrivingSetting = true;

                }
                break;
            case Constants.REQUEST_EDIT_CUSTOM_RESULT :
                if(data.getBooleanExtra("change",false)){
                    if(isEdit==2){
                        Toast.makeText(getApplicationContext(),"편집이 완료되었습니다",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"새로운 커스텀 모드를 저장했습니다",Toast.LENGTH_SHORT).show();
                    }
                    isEdit=1;
                    setEditMode();
                }else{

                }

                break;
            default:
                break;
        }
    }



    public void sendCarData(){



        final String signal1 = setSignal1();
        final String signal2 = setSignal2();

        signal1.replace(" ","");
        int decimalSignal1 = Integer.parseInt(signal1,2);
        int decimalSignal2 = Integer.parseInt(signal2,2);
        final String data = " { " +
                "  \"TPCANMsg\" : {      " +
                "\"DATA\" : ["+decimalSignal1+"," + decimalSignal2+ ", 0, 0, 0, 0, 0, 0 ]," +

                "  \"ID\" : 1,     " +
                " \"LEN\" : 2,      " +
                "\"MSGTYPE\" : 1   },   " +
                "\"TPCANTimestamp\" : {" +
                "  \"micros\" : 1,      " +
                "\"millis\" : 1,      " +
                "\"millis_overflow\" : 1   " +
                "}" +
                "}";



        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if(Network.getInstance().isConnected()){

                        sendWriter = Network.getInstance().sendWriter;

                        sendWriter.println(data);
                        sendWriter.flush();
                    }else{
                        Toast.makeText(getApplicationContext(),"차량 연결 상태를 확인해주세요",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        loadingDialog = new LoadingDialog(MainActivity.this,2);
        loadingDialog.show();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setNotTouch();

        Drive.getInstance().update();
        Transmission.getInstance().update();
        Sound.getInstance().update();

        CarData.getInstance().setComfortable();
        CarData.getInstance().setDynamic();
        CarData.getInstance().setEfficiency();
        CarData.getInstance().setLeading();
        CarData.getInstance().setPerformance();

        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempPerformance();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.hide();
                loadingDialog.setTouch();

                if(Transmission.getInstance().getTempIsOn().equals("0") &&
                        Sound.getInstance().getTempIsOn().equals("0") &&
                        Drive.getInstance().getTempIsOn().equals("0")){
                    CarData.getInstance().setTempEVMode();
                    CarData.getInstance().setEVMode();
                }
                defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                        CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                        CarData.getInstance().getPerformance());
                setBtnAnimation();

                Toast.makeText(getApplicationContext(),"차량 전송이 완료되었습니다",Toast.LENGTH_SHORT).show();
            }
        },2500);

    }

    public String setSignal1(){
        return selectedPreset+""+Sound.getInstance().getTempIsOn()+""+
                Sound.getInstance().getTempDriveType()+""+
                Sound.getInstance().getTempVolume()+""+
                Sound.getInstance().getBackVolume()+""+
                Sound.getInstance().getBackSensitive()+""+
                Drive.getInstance().getTempIsOn()+""+
                Drive.getInstance().getTempStiffness()+""+
                Drive.getInstance().getTempReducer();

    }
    public String setSignal2(){
        return Transmission.getInstance().getTempIsOn()+""+
                Transmission.getInstance().getTempType()+""+
                Transmission.getInstance().getTempGear()+""+
                Transmission.getInstance().getTempGearRate()+""+
                Transmission.getInstance().getTempTransmissionSpeed()+""+
                Transmission.getInstance().getTempTransmissionPower()+""+
                Transmission.getInstance().getTempTransmissionMap();

    }

    /**
     * Sends a message to obd
     *
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {

        Log.d(TAG, "=======================> sendMessage : " + message);

        if(mObdsv == null) {
            Utility.toast(getApplicationContext(), "App과 OBD가 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", 1000);
            return;
        }
        // Check that we're actually connected before trying anything
        if (mObdsv.getState() != Constants.STATE_CONNECTED) {
            Utility.toast(getApplicationContext(), "App과 OBD가 올바르게 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", 1000);
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            if(message.substring(0, 2).equals("00") && message.length() > 5){
                Constants.SEND_MSG_00 = message.substring(2);
            } else if (message.substring(0, 2).equals("01") && message.length() > 5){
                Constants.SEND_MSG_01 = message.substring(2);
            }
            message = message + "\r";
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mObdsv.write(send);
        }


    }

    /**
     * OBD(device list) popup for the selection
     */
    private void connectedWifi() {
        Intent intent = new Intent(this,connectionActivity.class);

        startActivityForResult(intent,Constants.REQUEST_CONNECT_WIFI);

    }




    /**
     * Show progress dialog
     */
    private void showProgressDialog() {

        if(mProgressDialog == null) {
            mProgressDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        }
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mProgressDialog.getWindow().setDimAmount(0.9f);

        mProgressDialog.setContentView(R.layout.custom_progress);
        mProgressDialog.setCanceledOnTouchOutside(false); // not in touch with background activity

        mProgress = (ProgressBar) mProgressDialog.findViewById(R.id.pb_progress);

        mProgressValue = (TextView) mProgressDialog.findViewById(R.id.tv_progress_value);

        ImageButton confirmBtn = (ImageButton) mProgressDialog.findViewById(R.id.ib_confirm_btn);
        confirmBtn.setOnClickListener(this);

        pStatus = 0;
        cnt = 32;

        mProgressValue.setText("0%");

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Handler hd = new Handler(Looper.getMainLooper());

                while (pStatus <= 100) {
                    pStatus += 1;

                    if(!Constants.SEND_MSG_01.equals("")){
                        if(pStatus == 50){
                            cnt = cnt + 32;
                        } else if(pStatus == 70){
                            cnt = cnt + 32;
                        } else if(pStatus == 90){
                            cnt = cnt + 32;
                        }
                    } else {
                        cnt = 32;
                    }


                    if(pStatus == 100) {
                        dismissProgressDialog();
                        pStatus= 0;
                        cnt=0;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPref.setSDResponse(mRespResponse);
                                mPref.setSDDeceleration(mRespDeceleration);
                                mPref.setSDAcceleration(mRespAcceration);
                                mPref.setSDMaxPower(mRespMaxPower);
                                mPref.setSDEcoLevel(mRespEcoLevel);
                                modChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel,
                                        mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);
                            }
                        });
                        return;
                    }

                    hd.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);
                            mProgressValue.setText(pStatus + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(cnt); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        mProgressDialog.show();

        sendMessage("01" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));

        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage("01" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
            }
        }, 1000);
    }


    /**
     * Dismiss progress dialog
     */
    private void dismissProgressDialog() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Set and Show default chart
     *
     * @param d1 - max power
     * @param d2 - acceleration
     * @param d3 - deceleration
     * @param d4 - charging time
     * @param d5 - eco level
     */
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



        RadarData data = new RadarData();
        data.addDataSet(dataset_comp);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);

        mChart.getLegend().setEnabled(false);            // remove legend
        // mChart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        mChart.setTouchEnabled(false);                   // disable touch




    }



    /**
     * Set and Show default and response chart
     *
     * @param d1 - max power (default)
     * @param d2 - acceleration (default)
     * @param d3 - deceleration (default)
     * @param d4 - charging time (default)
     * @param d5 - eco level (default)
     *
     * @param p1 - max power
     * @param p2 - acceleration
     * @param p3 - deceleration
     * @param p4 - charging time
     * @param p5 - eco level
     */

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

        mChart.getYAxis().setTextColor(Color.RED);     // change number color
        mChart.getYAxis().setAxisMinimum(0f);
        mChart.getYAxis().setAxisMaximum(9f);
        mChart.getYAxis().setEnabled(false);  // disable number
//


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



    /**
     * Move to favorite activity
     */
    private void goToDTEchartActivity() {

        Intent DTEchart = new Intent(this, DTEchartActivity.class);
        startActivity(DTEchart);
    }

    /**
     * Move to favorite activity
     */
    private void goToFavoriteActivity() {

        Intent favorite = new Intent(this, FavoriteActivity.class);
        startActivityForResult(favorite, Constants.REQUEST_FAVORITE);
    }

    private void goToGearSettingActivity(){
        Intent gear = new Intent(getApplicationContext(), GearSettingActivity.class);
        startActivityForResult(gear , Constants.REQUEST_GEAR_SETTING);
    }

    private void goToSpeakerSettingActivity(){
        Intent speaker = new Intent(this,SpeakerSettingActivity.class);
        startActivityForResult(speaker,Constants.REQUEST_SPEAKER_SETTING);
    }

    private void goToDrivingSettingActivity(){
        Intent driving = new Intent(getApplicationContext() , AxelSettingActivity.class);
        startActivityForResult(driving , Constants.REQUEST_DRIVING_SETTING);
    }
    /**
     * Move to main activity
     */
    private void goToParamSettingActivity() {

        Intent paramSetting = new Intent(this, ParamSettingActivity.class);
        paramSetting.putExtra("isEdit" , isEdit);
        paramSetting.putExtra("title",editTitle);
        paramSetting.putExtra("id",editId);
        startActivityForResult(paramSetting,Constants.REQUEST_EDIT_CUSTOM_RESULT);
    }


    private ArrayList<FavoriteData> modeListDatabase() {

        Obd2DBOpenHelper helper = new Obd2DBOpenHelper(getApplicationContext());

        ArrayList<FavoriteData> list = new ArrayList<>();
        helper.open();
        Cursor cursor = helper.getModeAll();


        // 추천모드 초기에 데이터가 없는 경우 호출
        if(helper.getModeRowCount() == 0) {
            // 초기값 팅

            //EV Mode
            String evSignal1 = "000 0 0 00 00 0 0 00 00";
            String evSignal2 = "0 00 000 00 00 00 00";
            DBUtil.insertModeDB(getApplicationContext(),"EV",evSignal1,evSignal2);


            // VIP
            String vipSignal1 = "001 0 0 00 00 0 1 00 10 ";
            String vipSignal2 = "0 00 000 00 00 00 00";
            DBUtil.insertModeDB(getApplicationContext(),"VIP", vipSignal1, vipSignal2);


            // Passenger
            String passengerSignal1 = "010 1 1 00 00 0 1 01 01";
            String passengerSignal2 = "1 00 011 01 00 00 00";
            DBUtil.insertModeDB(getApplicationContext(),"Passenger", passengerSignal1, passengerSignal2);

            // Sport
            String sportSignal1 = "011 1 1 10 11 1 1 10 01";
            String sportSignal2 = "1 01 010 00 10 10 01";
            DBUtil.insertModeDB(getApplicationContext(),"Sport", sportSignal1, sportSignal2);

            // Commercial
            String commercialSignal1 = "100 1 1 01 00 0 1 01 00";
            String commercialSignal2 = "1 10 001 10 01 10 00";
            DBUtil.insertModeDB(getApplicationContext(),"Commercial", commercialSignal1, commercialSignal2);


        }

        for(int i=0; i < cursor.getCount(); i ++) {
            cursor.moveToNext();

            FavoriteData items = new FavoriteData();
            items.setId(cursor.getInt(0));
            items.setTitle(cursor.getString(1));
            items.setSignal1(cursor.getString(2));
            items.setSignal2(cursor.getString(3));
            list.add(items);

            // mListAdapter.addItem(items);  // unique id
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            return list;
        }
        helper.close();
        return list;
    }

    private void modeInit(FavoriteDataListItems listItems){

        String[] params = listItems.getParam().split(":");

//        modeTitle.setText(listItems.getTitle());
//        modeTorque.setText(params[0] + "%");
//        modeAcceleration.setText(params[1]);
//        modeDeceleration.setText(params[2]);
//        modeBrake.setText(params[3]);
//        if (params[4].equals("0")) modeEnergy.setText("Off");
//        else if (params[4].equals("1")) modeEnergy.setText("Eco");
//        else if (params[4].equals("2")) modeEnergy.setText("Normal");
//
//        modeSpeed.setText(params[5] + "kph");
//        modeResponse.setText(params[6]);
//
//        String[] resps = listItems.getResp().split(":");
//
//        defaultChart( Float.parseFloat(resps[0]), Float.parseFloat(resps[1]), Float.parseFloat(resps[2]), Float.parseFloat(resps[3]), Float.parseFloat(resps[4]));
    }

    public void showDialog01(){
        dialog.show(); // 다이얼로그 띄우기


        Button noBtn = dialog.findViewById(R.id.custom_cancel_no);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        dialog.findViewById(R.id.custom_cancel_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transmission.getInstance().reset();
                Drive.getInstance().reset();
                Sound.getInstance().reset();
                CarData.getInstance().setTempLeading();
                CarData.getInstance().setTempComfortable();
                CarData.getInstance().setTempEfficiency();
                CarData.getInstance().setTempPerformance();
                CarData.getInstance().setTempDynamic();

                defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                        CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                        CarData.getInstance().getPerformance());
                isEdit = 1;

                setEditMode();
                dialog.dismiss();
            }
        });
    }

}
