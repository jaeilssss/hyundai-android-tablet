package com.obigo.hkmotors.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.network.HttpService;
import com.obigo.hkmotors.common.pref.SharedPreference;
import com.obigo.hkmotors.common.service.ObdService;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.FavoriteDataListItems;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.model.TempTransmission;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;
import com.obigo.hkmotors.module.Result_DrivingInfo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    public static Context mContext;
    private ImageView mainObdLight;
    private ImageButton obdSetBtn;

    private RelativeLayout mLayoutBarChart;
    private RelativeLayout mLayoutRcdation;
    private RelativeLayout mLayoutExpert;

    private ArrayList<FavoriteDataListItems> dataListItems = new ArrayList<>();

    private ToggleButton mMode;

    private ImageView gearBtn;
    private ImageView speakerBtn;
    private ImageView drivingAxleBtn;

    // 고급모드
    private TextView mSeekerEco;
    private TextView mSeekerSport;
    private SeekBar mSbDriving;
    private TextView mSeekerTorque;
    private SeekBar mSbTorque;
    private TextView mSeekerAcc;
    private SeekBar mSbAcc;
    private TextView mSeekerDecel;
    private SeekBar mSbDecel;
    private TextView mSeekerBrake;
    private SeekBar mSbBrake;
    private TextView mSeekerEnergy;
    private SeekBar mSbEnergy;
    private TextView mSeekerSpeed;
    private SeekBar mSbSpeed;
    private TextView mSeekerResponse;
    private SeekBar mSbResponse;

    //추천모드
    private TextView modeTitle;
    private TextView modeTorque;
    private TextView modeAcceleration;
    private TextView modeDeceleration;
    private TextView modeBrake;
    private TextView modeEnergy;
    private TextView modeSpeed;
    private TextView modeResponse;
    private RadarChart modeChart;

    private TextView mAvailableDistance;
    private TextView mAvailableDistanceMax;
    private TextView mAvailableDistanceMin;

    // 상태게이지 (좌)
    private TextView mLeftBattery;
    private ImageView mBattery01;
    private ImageView mBattery02;
    private ImageView mBattery03;
    private ImageView mBattery04;
    private ImageView mBattery05;

    private ImageView mCPwg01;
    private ImageView mCPwg02;
    private ImageView mCPwg03;
    private ImageView mCPwg04;
    private ImageView mCPwg05;

    private ImageView mPwg01;
    private ImageView mPwg02;
    private ImageView mPwg03;
    private ImageView mPwg04;
    private ImageView mPwg05;
    private ImageView mPwg06;
    private ImageView mPwg07;
    private ImageView mPwg08;
    private ImageView mPwg09;
    private ImageView mPwg10;

    // 상태게이지 (중간)
    private TextView mCntrVSpeed;
    private ImageView mCntrLStatus;
    private ImageView mCntrEVReady;
    private ImageView mCntrPaddle;
    private ImageView mCntrDMode;



    // 상태게이지 (우)
    private TextView mMileageInst;
    private ImageView mMileageInst01;
    private ImageView mMileageInst02;
    private ImageView mMileageInst03;
    private ImageView mMileageInst04;
    private ImageView mMileageInst05;
    private ImageView mMileageInst06;
    private ImageView mMileageInst07;
    private ImageView mMileageInst08;
    private ImageView mMileageInst09;
    private ImageView mMileageInst10;
    private ImageView mMileageInst11;
    private ImageView mMileageInst12;
    private ImageView mMileageInst13;
    private ImageView mMileageInst14;
    private ImageView mMileageInst15;
    private ImageView mMileageInst16;

    private TextView mMileageAvg;
    private ImageView mMileageAvg01;
    private ImageView mMileageAvg02;
    private ImageView mMileageAvg03;
    private ImageView mMileageAvg04;
    private ImageView mMileageAvg05;
    private ImageView mMileageAvg06;
    private ImageView mMileageAvg07;
    private ImageView mMileageAvg08;
    private ImageView mMileageAvg09;
    private ImageView mMileageAvg10;
    private ImageView mMileageAvg11;
    private ImageView mMileageAvg12;
    private ImageView mMileageAvg13;
    private ImageView mMileageAvg14;
    private ImageView mMileageAvg15;
    private ImageView mMileageAvg16;



    private Dialog mProgressDialog;
    private ProgressBar mProgress;
    private TextView mProgressValue;
    private int pStatus = 0;
    private int cnt = 0;

    // MPandroidchart or event
    private RadarChart mChart;

    private ObdService mObdsv;
    private ObdDataHandler mObdDataHandler;

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

    private  ImageButton resetBtn;
    private ImageButton carSend;
    private ImageButton saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "== onCreate");


        mModeValue = getIntent().getStringExtra("mode");

        mPref = new SharedPreference(getApplicationContext());
        mObdDataHandler = new ObdDataHandler();
        mContext = this;

        // 추천모드 리스트
        dataListItems = modeListDatabase();

        // UI 초기화
        initUI();

        // 일단 테스트용 으로 만들어놓음
        getOdbData();


        if(Constants.INIT_FLAG) {
            // in case of the initialization
            selectOBD();
            Constants.INIT_FLAG = false;
        } else {
            mObdsv = ObdService.getInstance(getApplicationContext(), mObdDataHandler);
        }

        Constants.COMMAND_MODE = "INIT";

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
        System.out.println("오오오"+CarData.getInstance().getComfortable());

        defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                CarData.getInstance().getPerformance());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.DISPLAY_MODE = "MAIN";
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {
//            case R.id.layout_bar_chart:
/*

                HttpService http = HttpService.getInstance();
                Call<Result_DrivingInfo> call = http.httpService.drivingInfo("1", "10");

                call.enqueue(new Callback<Result_DrivingInfo>() {
                    @Override
                    public void onResponse(Call<Result_DrivingInfo> call, Response<Result_DrivingInfo> response) {
                        if(!response.isSuccessful()){
                            Log.i(TAG,"not Successful");
                            return;
                        }

                        Result_DrivingInfo re = response.body();

                        Log.i(TAG,"Successful");
                    }

                    @Override
                    public void onFailure(Call<Result_DrivingInfo> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), "데이터 조회 실패", Toast.LENGTH_LONG).show();

                        Log.i(TAG,"failure");

                    }
                });
*/


//                goToDTEchartActivity();
//                break;

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

            case R.id.ib_obd_set_btn:
                if(Constants.OBD_STATUS == true) {
                    try {
                        mRespMaxPower = mPref.getFDMaxPower();
                        mRespAcceration = mPref.getFDAcceleration();
                        mRespDeceleration = mPref.getFDDeceleration();
                        mRespResponse = mPref.getFDResponse();
                        mRespEcoLevel = mPref.getFDEcoLevel();

                        // set second default value : 0
                        mPref.setSDResponse(0);
                        mPref.setSDDeceleration(0);
                        mPref.setSDAcceleration(0);
                        mPref.setSDMaxPower(0);
                        mPref.setSDEcoLevel(0);

                        // show default spider chart
                        defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);

                        mObdsv.disconnectDevice();
                    } catch (IOException e) {
                        Log.d(TAG, "블루투스 통신을 끊는 도중에 예외상황이 발생함~");
                    }
                } else {
                    selectOBD();
                }
                break;

            //case R.id.ib_g_reset_btn:
            case R.id.ib_e_reset_btn:



                if(Constants.OBD_STATUS == true) {
                    mObdsv.initializeParam();
                }

//                // set second default value : 0
//                mPref.setSDResponse(0);
//                mPref.setSDDeceleration(0);
//                mPref.setSDAcceleration(0);
//                mPref.setSDMaxPower(0);
//                mPref.setSDEcoLevel(0);
//
//                Constants.COMMAND_MODE = "RESET";
//
//                sendMessage("04" + "000000000000");
//
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("04" + "000000000000");
//                    }
//                }, 1000);


                break;

//            case R.id.ib_r_send_btn:
//
//                if(mObdsv == null) {
//                    Toast.makeText(getApplicationContext(), "App과 OBD가 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // Check that we're actually connected before trying anything
//                if (mObdsv.getState() != Constants.STATE_CONNECTED) {
//                    Toast.makeText(getApplicationContext(), "App과 OBD가 올바르게 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Constants.COMMAND_MODE = "SEND";
//                showProgressDialogMode();
//                break;

            case R.id.ib_e_send_btn:

                if(mObdsv == null) {
                    Toast.makeText(getApplicationContext(), "App과 OBD가 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check that we're actually connected before trying anything
                if (mObdsv.getState() != Constants.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "App과 OBD가 올바르게 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Constants.COMMAND_MODE = "SEND";
                showProgressDialog();
                break;

            //case R.id.ib_g_submit_btn:
            case R.id.ib_e_submit_btn:

                Constants.COMMAND_MODE = "SUBMIT";
                goToParamSettingActivity();
                break;

            case R.id.ib_confirm_btn:
                // progressbar dialog confirm
                dismissProgressDialog();
                break;

            default:
                break;
        }
    }

    /**
     * Handler about the result of OBDII
     */
    public class ObdDataHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constants.HANDLE_OBD_RESPONSE_VCUPs1:
                    //Log.d(TAG, "결과값 처리 // ");
                    if(msg == null || msg.obj == null) return;

                    HashMap<String, Float> responseValue = new HashMap<String, Float>();
                    Object responseObject = msg.obj;
                    responseValue = (HashMap<String, Float>) responseObject;

                    // TODO : for testing , it should be disabled
                    mRespMaxPower = responseValue.get(Constants.RESP_MAX_POWER);
                    mRespAcceration = responseValue.get(Constants.RESP_ACCELERATION);
                    mRespDeceleration = responseValue.get(Constants.RESP_DECELERATION);
                    mRespResponse = responseValue.get(Constants.RESP_RESPONSE);
                    mRespEcoLevel = responseValue.get(Constants.RESP_ECO_LEVEL);

                    // at first
                    if(mPref.getFD()) {
                        Log.d(TAG, "=== response : FD");
                        // save first default value
                        mPref.setFDMaxPower(mRespMaxPower);
                        mPref.setFDAcceleration(mRespAcceration);
                        mPref.setFDDeceleration(mRespDeceleration);
                        mPref.setFDEcoLevel(mRespEcoLevel);
                        mPref.setFDResponse(mRespResponse);

                        // set second default value : 0
                        mPref.setSDResponse(0);
                        mPref.setSDDeceleration(0);
                        mPref.setSDAcceleration(0);
                        mPref.setSDMaxPower(0);
                        mPref.setSDEcoLevel(0);

                        defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);

                        Toast.makeText(getApplicationContext(), "초기화가 완료 되었습니다.!", Toast.LENGTH_SHORT).show();

                    } else {

                        if(Constants.COMMAND_MODE == "RESET") { // select reset button
                            Log.d(TAG, "=== response : RESET");
                            defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel); // VCU send value
                            //defaultChart(mPref.getFDMaxPower(), mPref.getFDAcceleration(), mPref.getFDDeceleration(), mPref.getFDResponse(), mPref.getFDEcoLevel());
                        } else if(Constants.COMMAND_MODE == "SEND") { // select send button
                            Log.d(TAG, "=== response : SEND");
                            mPref.setSDResponse(mRespResponse);
                            mPref.setSDDeceleration(mRespDeceleration);
                            mPref.setSDAcceleration(mRespAcceration);
                            mPref.setSDMaxPower(mRespMaxPower);
                            mPref.setSDEcoLevel(mRespEcoLevel);

                            modChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel,
                                    mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);
                        } else if(Constants.COMMAND_MODE == "PARAM") { // select seek bar
                            Log.d(TAG, "=== response : PARAM");
                            if(mPref.getSDMaxPower() == 0 && mPref.getSDAcceleration() == 0 && mPref.getSDDeceleration() == 0 &&
                                    mPref.getSDResponse() == 0 &&  mPref.getSDEcoLevel() == 0) { // without selecing send button
                                modChart(mPref.getFDMaxPower(), mPref.getFDAcceleration(), mPref.getFDDeceleration(), mPref.getFDResponse(), mPref.getFDEcoLevel(),
                                        mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);
                            } else { // with selecting send button
                                modChart(mPref.getSDMaxPower(), mPref.getSDAcceleration(), mPref.getSDDeceleration(), mPref.getSDResponse(), mPref.getSDEcoLevel(),
                                        mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);
                            }
                        }
                    }
                    break;
                case Constants.HANDLE_OBD_PARAMETER:
                    //Log.d(TAG, "파라미터 처리 // ");

                    if(msg == null || msg.obj == null) return;

                    HashMap<String, Integer> paramValue = new HashMap<String, Integer>();
                    Object paramObject = msg.obj;
                    paramValue = (HashMap<String, Integer>) paramObject;


                    mParamTorque = paramValue.get(Constants.PARAM_TORQUE);
                    if(mParamTorque >= 50 && mParamTorque <= 100){
                        mSeekerTorque.setText(Integer.toString(mParamTorque) + "%");
                        mSbTorque.setProgress(-50);
                    }

                    mParamAcceleration = paramValue.get(Constants.PARAM_ACCELERATION);
                    if(mParamAcceleration >= 0 && mParamAcceleration <= 5){
                        mSeekerAcc.setText(Integer.toString(mParamAcceleration));
                        mSbAcc.setProgress(mParamAcceleration);
                    }

                    mParamDeceleration = paramValue.get(Constants.PARAM_DECELERATION);
                    if(mParamDeceleration >= 0 && mParamDeceleration <= 5){
                        mSeekerDecel.setText(Integer.toString(mParamDeceleration));
                        mSbDecel.setProgress(mParamDeceleration);
                    }

                    mParamBrake = paramValue.get(Constants.PARAM_BRAKE);
                    if(mParamBrake >= 0 && mParamBrake <= 4){
                        mSeekerBrake.setText(Integer.toString(mParamBrake));
                        mSbBrake.setProgress(mParamBrake);
                    }

                    mParamEnergy = paramValue.get(Constants.PARAM_ENERGY);
                    if(mParamEnergy >= 0 && mParamEnergy <= 2){
                        if(mParamEnergy==0) mSeekerEnergy.setText("Off");
                        else if(mParamEnergy==1) mSeekerEnergy.setText("Eco");
                        else if(mParamEnergy==2) mSeekerEnergy.setText("Normal");
                        mSbEnergy.setProgress(mParamEnergy);
                    }

                    mParamSpeed = paramValue.get(Constants.PARAM_SPEED);
                    if(mParamSpeed >= 60 && mParamSpeed <= 160){
                        mSeekerSpeed.setText(Integer.toString(mParamSpeed) + "kph");
                        mSbSpeed.setProgress(mParamSpeed-60);
                    }

                    mParamResponse = paramValue.get(Constants.PARAM_RESPONSE);
                    if(mParamResponse >= 0 && mParamResponse <= 2){
                        mSeekerResponse.setText(Integer.toString(mParamResponse));
                        mSbResponse.setProgress(mParamResponse);
                    }

                    break;
                case Constants.HANDLE_OBD_CONN_LOST:
                    Log.i(TAG, "OBD 연결끊어졌을때");
                    setOBDMode(false);
                    //Toast.makeText(getApplicationContext(), "OBDLink MX와의 연결이 끊어졌습니다!", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.HANDLE_OBD_USER_DISCONN:
                    Log.i(TAG, "OBD 연결을 의도적으로 끊을 경우");
                    setOBDMode(false);
                    Toast.makeText(getApplicationContext(), "요청하신 OBDLink MX와의 연결이 종료 되었습니다!", Toast.LENGTH_SHORT).show();
                    break;

                case Constants.HANDLE_OBD_RESPONSE_VCUPs2:
                    if(msg == null || msg.obj == null) return;

                    HashMap<String, Integer> responseValueVCUPs2 = new HashMap<String, Integer>();
                    Object responseObjectVCUPs2 = msg.obj;
                    responseValueVCUPs2 = (HashMap<String, Integer>) responseObjectVCUPs2;

                    // 차속
                    mRespVSpeed = responseValueVCUPs2.get(Constants.RESP_VEHICLE_SPEED);
                    mCntrVSpeed.setText(Integer.toString(mRespVSpeed));


                    mRespLStatus = responseValueVCUPs2.get(Constants.RESP_LEVER_STATUS);

                    // 래버 상태
                    if(mRespLStatus == 0){
                        mCntrLStatus.setBackgroundResource(R.drawable.ico_drvm_p);
                    }else if(mRespLStatus == 5) {
                        mCntrLStatus.setBackgroundResource(R.drawable.ico_drvm_d);
                    }else if(mRespLStatus == 6) {
                        mCntrLStatus.setBackgroundResource(R.drawable.ico_drvm_n);
                    }else if(mRespLStatus == 7) {
                        mCntrLStatus.setBackgroundResource(R.drawable.ico_drvm_r);
                    }else{
                        /**
                         *  1 : B
                         *  A~D : reserved
                         *  E : Intermediate Position
                         *  F : Fault
                         *  etc : Not used
                         */
                        Log.d(TAG, "레버상태 값 : " + mRespLStatus);
                    }

                    // EV 모드
                    mRespEVReady = responseValueVCUPs2.get(Constants.RESP_EV_READY);

                    if(mRespEVReady == 0){
                        mCntrEVReady.setBackgroundResource(R.drawable.ico_evready_off);
                    }else if(mRespEVReady == 1) {
                        mCntrEVReady.setBackgroundResource(R.drawable.ico_evready_on);
                    }else{
                        Log.d(TAG, "EV Ready 값 : " + mRespEVReady);
                    }

                    // 패들 쉬프트
                    mRespPaddle = responseValueVCUPs2.get(Constants.RESP_PADDLE_SHIFT);

                    if(mRespPaddle == 0){
                        mCntrPaddle.setBackgroundResource(R.drawable.ico_paddle_0);
                    }else if(mRespPaddle == 1) {
                        mCntrPaddle.setBackgroundResource(R.drawable.ico_paddle_1);
                    }else if(mRespPaddle == 2) {
                        mCntrPaddle.setBackgroundResource(R.drawable.ico_paddle_2);
                    }else if(mRespPaddle == 3) {
                        mCntrPaddle.setBackgroundResource(R.drawable.ico_paddle_3);
                    }else{
                        /**
                         *  4 : 4th
                         *  5 : 5th
                         *  6 : 6th
                         *  F : Fault
                         *  etc : reserved
                         */
                    }

                    // 드라이버 모드
                    mRespDMode = responseValueVCUPs2.get(Constants.RESP_DRIVER_MODE);

                    if(mRespDMode == 0){
                        mCntrDMode.setBackgroundResource(R.drawable.ico_userm_03);
                    }else if(mRespDMode == 1) {
                        mCntrDMode.setBackgroundResource(R.drawable.ico_userm_01);
                    }else if(mRespDMode == 2) {
                        mCntrDMode.setBackgroundResource(R.drawable.ico_userm_02);
                    }else if(mRespDMode == 5) {
                        mCntrDMode.setBackgroundResource(R.drawable.ico_userm_04);
                    }else{
                        /**
                         *  3 : Eco+
                         *  4 : Rest
                         *  5~6 : reserved
                         *  7 : Invalid
                         */
                    }


                    // 공조 Max(MPandroidChart)
                    mRespHarmonizationDTEMax = responseValueVCUPs2.get(Constants.RESP_HARMONIZATION_DTE_MAX);

                    if(Constants.DTE_MODE_STATUS == 3){

                        ((DTEchartActivity)DTEchartActivity.mContext).DTEchart(mRespAvailableDistance, mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE,
                                mRespAvailableDistanceMax, mRespDriverPatternDTEMax, mRespHarmonizationDTEMax, mRespVehicleSpeedDTEMax, mRespDrivingFeelingDTEMax, mRespAvailableDistanceMin, mRespDriverPatternDTEMin, mRespHarmonizationDTEMin, mRespVehicleSpeedDTEMin, mRespDrivingFeelingDTEMin );
                    }


                    // 파워 게이지
                    mRespPGauge = responseValueVCUPs2.get(Constants.RESP_POWER_GAUGE);

                    if(mRespPGauge == 100){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.GONE)) mPwg06.setVisibility(View.VISIBLE);
                        if ((mPwg07.getVisibility() == View.GONE)) mPwg07.setVisibility(View.VISIBLE);
                        if ((mPwg08.getVisibility() == View.GONE)) mPwg08.setVisibility(View.VISIBLE);
                        if ((mPwg09.getVisibility() == View.GONE)) mPwg09.setVisibility(View.VISIBLE);
                        if ((mPwg10.getVisibility() == View.GONE)) mPwg10.setVisibility(View.VISIBLE);

                    } else if(mRespPGauge < 100 && mRespPGauge >= 90){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.GONE)) mPwg06.setVisibility(View.VISIBLE);
                        if ((mPwg07.getVisibility() == View.GONE)) mPwg07.setVisibility(View.VISIBLE);
                        if ((mPwg08.getVisibility() == View.GONE)) mPwg08.setVisibility(View.VISIBLE);
                        if ((mPwg09.getVisibility() == View.GONE)) mPwg09.setVisibility(View.VISIBLE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 90 && mRespPGauge >= 80){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.GONE)) mPwg06.setVisibility(View.VISIBLE);
                        if ((mPwg07.getVisibility() == View.GONE)) mPwg07.setVisibility(View.VISIBLE);
                        if ((mPwg08.getVisibility() == View.GONE)) mPwg08.setVisibility(View.VISIBLE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 80 && mRespPGauge >= 70){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.GONE)) mPwg06.setVisibility(View.VISIBLE);
                        if ((mPwg07.getVisibility() == View.GONE)) mPwg07.setVisibility(View.VISIBLE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 70 && mRespPGauge >= 60){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.GONE)) mPwg06.setVisibility(View.VISIBLE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 60 && mRespPGauge >= 50){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.GONE)) mPwg05.setVisibility(View.VISIBLE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 50 && mRespPGauge >= 40){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.GONE)) mPwg04.setVisibility(View.VISIBLE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 40 && mRespPGauge >= 30){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.GONE)) mPwg03.setVisibility(View.VISIBLE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 30 && mRespPGauge >= 20){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.GONE)) mPwg02.setVisibility(View.VISIBLE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 20 && mRespPGauge >= 10){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.GONE)) mPwg01.setVisibility(View.VISIBLE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 10 && mRespPGauge >= 0){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.VISIBLE)) mCPwg05.setVisibility(View.GONE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < 0 && mRespPGauge >= -10){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.VISIBLE)) mCPwg04.setVisibility(View.GONE);
                        if ((mCPwg05.getVisibility() == View.GONE)) mCPwg05.setVisibility(View.VISIBLE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < -10 && mRespPGauge >= -20){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.VISIBLE)) mCPwg03.setVisibility(View.GONE);
                        if ((mCPwg04.getVisibility() == View.GONE)) mCPwg04.setVisibility(View.VISIBLE);
                        if ((mCPwg05.getVisibility() == View.GONE)) mCPwg05.setVisibility(View.VISIBLE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < -20 && mRespPGauge >= -30){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.VISIBLE)) mCPwg02.setVisibility(View.GONE);
                        if ((mCPwg03.getVisibility() == View.GONE)) mCPwg03.setVisibility(View.VISIBLE);
                        if ((mCPwg04.getVisibility() == View.GONE)) mCPwg04.setVisibility(View.VISIBLE);
                        if ((mCPwg05.getVisibility() == View.GONE)) mCPwg05.setVisibility(View.VISIBLE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < -30 && mRespPGauge >= -40){

                        if ((mCPwg01.getVisibility() == View.VISIBLE)) mCPwg01.setVisibility(View.GONE);
                        if ((mCPwg02.getVisibility() == View.GONE)) mCPwg02.setVisibility(View.VISIBLE);
                        if ((mCPwg03.getVisibility() == View.GONE)) mCPwg03.setVisibility(View.VISIBLE);
                        if ((mCPwg04.getVisibility() == View.GONE)) mCPwg04.setVisibility(View.VISIBLE);
                        if ((mCPwg05.getVisibility() == View.GONE)) mCPwg05.setVisibility(View.VISIBLE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    } else if(mRespPGauge < -40){

                        if ((mCPwg01.getVisibility() == View.GONE)) mCPwg01.setVisibility(View.VISIBLE);
                        if ((mCPwg02.getVisibility() == View.GONE)) mCPwg02.setVisibility(View.VISIBLE);
                        if ((mCPwg03.getVisibility() == View.GONE)) mCPwg03.setVisibility(View.VISIBLE);
                        if ((mCPwg04.getVisibility() == View.GONE)) mCPwg04.setVisibility(View.VISIBLE);
                        if ((mCPwg05.getVisibility() == View.GONE)) mCPwg05.setVisibility(View.VISIBLE);

                        if ((mPwg01.getVisibility() == View.VISIBLE)) mPwg01.setVisibility(View.GONE);
                        if ((mPwg02.getVisibility() == View.VISIBLE)) mPwg02.setVisibility(View.GONE);
                        if ((mPwg03.getVisibility() == View.VISIBLE)) mPwg03.setVisibility(View.GONE);
                        if ((mPwg04.getVisibility() == View.VISIBLE)) mPwg04.setVisibility(View.GONE);
                        if ((mPwg05.getVisibility() == View.VISIBLE)) mPwg05.setVisibility(View.GONE);
                        if ((mPwg06.getVisibility() == View.VISIBLE)) mPwg06.setVisibility(View.GONE);
                        if ((mPwg07.getVisibility() == View.VISIBLE)) mPwg07.setVisibility(View.GONE);
                        if ((mPwg08.getVisibility() == View.VISIBLE)) mPwg08.setVisibility(View.GONE);
                        if ((mPwg09.getVisibility() == View.VISIBLE)) mPwg09.setVisibility(View.GONE);
                        if ((mPwg10.getVisibility() == View.VISIBLE)) mPwg10.setVisibility(View.GONE);

                    }

                    mRespBtrSOC = responseValueVCUPs2.get(Constants.RESP_BATTERY_SOC);

                    if(mRespBtrSOC == 100){

                        if ((mBattery01.getVisibility() == View.GONE)) mBattery01.setVisibility(View.VISIBLE);
                        if ((mBattery02.getVisibility() == View.GONE)) mBattery02.setVisibility(View.VISIBLE);
                        if ((mBattery03.getVisibility() == View.GONE)) mBattery03.setVisibility(View.VISIBLE);
                        if ((mBattery04.getVisibility() == View.GONE)) mBattery04.setVisibility(View.VISIBLE);
                        if ((mBattery05.getVisibility() == View.GONE)) mBattery05.setVisibility(View.VISIBLE);

                    } else if(mRespBtrSOC < 100 && mRespBtrSOC >= 80){

                        if ((mBattery01.getVisibility() == View.GONE)) mBattery01.setVisibility(View.VISIBLE);
                        if ((mBattery02.getVisibility() == View.GONE)) mBattery02.setVisibility(View.VISIBLE);
                        if ((mBattery03.getVisibility() == View.GONE)) mBattery03.setVisibility(View.VISIBLE);
                        if ((mBattery04.getVisibility() == View.GONE)) mBattery04.setVisibility(View.VISIBLE);
                        if ((mBattery05.getVisibility() == View.VISIBLE)) mBattery05.setVisibility(View.GONE);

                    } else if(mRespBtrSOC < 80 && mRespBtrSOC >= 60){

                        if ((mBattery01.getVisibility() == View.GONE)) mBattery01.setVisibility(View.VISIBLE);
                        if ((mBattery02.getVisibility() == View.GONE)) mBattery02.setVisibility(View.VISIBLE);
                        if ((mBattery03.getVisibility() == View.GONE)) mBattery03.setVisibility(View.VISIBLE);
                        if ((mBattery04.getVisibility() == View.VISIBLE)) mBattery04.setVisibility(View.GONE);
                        if ((mBattery05.getVisibility() == View.VISIBLE)) mBattery05.setVisibility(View.GONE);

                    } else if(mRespBtrSOC < 60 && mRespBtrSOC >= 40){

                        if ((mBattery01.getVisibility() == View.GONE)) mBattery01.setVisibility(View.VISIBLE);
                        if ((mBattery02.getVisibility() == View.GONE)) mBattery02.setVisibility(View.VISIBLE);
                        if ((mBattery03.getVisibility() == View.VISIBLE)) mBattery03.setVisibility(View.GONE);
                        if ((mBattery04.getVisibility() == View.VISIBLE)) mBattery04.setVisibility(View.GONE);
                        if ((mBattery05.getVisibility() == View.VISIBLE)) mBattery05.setVisibility(View.GONE);

                    } else if(mRespBtrSOC < 40 && mRespBtrSOC >= 20){

                        if ((mBattery01.getVisibility() == View.GONE)) mBattery01.setVisibility(View.VISIBLE);
                        if ((mBattery02.getVisibility() == View.VISIBLE)) mBattery02.setVisibility(View.GONE);
                        if ((mBattery03.getVisibility() == View.VISIBLE)) mBattery03.setVisibility(View.GONE);
                        if ((mBattery04.getVisibility() == View.VISIBLE)) mBattery04.setVisibility(View.GONE);
                        if ((mBattery05.getVisibility() == View.VISIBLE)) mBattery05.setVisibility(View.GONE);

                    } else {

                        if ((mBattery01.getVisibility() == View.VISIBLE)) mBattery01.setVisibility(View.GONE);
                        if ((mBattery02.getVisibility() == View.VISIBLE)) mBattery02.setVisibility(View.GONE);
                        if ((mBattery03.getVisibility() == View.VISIBLE)) mBattery03.setVisibility(View.GONE);
                        if ((mBattery04.getVisibility() == View.VISIBLE)) mBattery04.setVisibility(View.GONE);
                        if ((mBattery05.getVisibility() == View.VISIBLE)) mBattery05.setVisibility(View.GONE);

                    }

                    mLeftBattery.setText(String.valueOf(mRespBtrSOC));


                    break;

                case Constants.HANDLE_OBD_RESPONSE_VCUPs3:
                    if(msg == null || msg.obj == null) return;

                    HashMap<String, Integer> responseValueVCUPs3 = new HashMap<String, Integer>();
                    Object responseObjectVCUPs3 = msg.obj;
                    responseValueVCUPs3 = (HashMap<String, Integer>) responseObjectVCUPs3;

                    // TODO : for testing , it should be disabled
                    mRespAvailableDistance = responseValueVCUPs3.get(Constants.RESP_AVAILABLE_DISTANCE);
                    mAvailableDistance.setText(Integer.toString(mRespAvailableDistance));

                    mRespAvailableDistanceMin = responseValueVCUPs3.get(Constants.RESP_AVAILABLE_DISTANCE_MIN);
                    mAvailableDistanceMin.setText(Integer.toString(mRespAvailableDistanceMin));

                    mRespAvailableDistanceMax = responseValueVCUPs3.get(Constants.RESP_AVAILABLE_DISTANCE_MAX);
                    mAvailableDistanceMax.setText(Integer.toString(mRespAvailableDistanceMax));

                    mRespAvailableDistanceBase = responseValueVCUPs3.get(Constants.RESP_AVAILABLE_DISTANCE_BASE);

                    if(Constants.DTE_MODE_STATUS == 3){

                        ((DTEchartActivity)DTEchartActivity.mContext).DTEchart(mRespAvailableDistance, mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE,
                                mRespAvailableDistanceMax, mRespDriverPatternDTEMax, mRespHarmonizationDTEMax, mRespVehicleSpeedDTEMax, mRespDrivingFeelingDTEMax, mRespAvailableDistanceMin, mRespDriverPatternDTEMin, mRespHarmonizationDTEMin, mRespVehicleSpeedDTEMin, mRespDrivingFeelingDTEMin );
                    }


                    // Constants.respAvailableDistanceBase =  Integer.toString(mRespAvailableDistance);

                    //DTEchartActivity DTEActivity = new DTEchartActivity("1");
                    //DTEActivity.doStart();

                    break;

                case Constants.HANDLE_OBD_RESPONSE_VCUPs4:
                    if(msg == null || msg.obj == null) return;

                    HashMap<String, Integer> responseValueVCUPs4 = new HashMap<String, Integer>();
                    Object responseObjectVCUPs4 = msg.obj;
                    responseValueVCUPs4 = (HashMap<String, Integer>) responseObjectVCUPs4;

                    mRespDriverPatternDTEMax = responseValueVCUPs4.get(Constants.RESP_DRIVER_PATTERN_DTE_MAX);
                    mRespDriverPatternDTEMin = responseValueVCUPs4.get(Constants.RESP_DRIVER_PATTERN_DTE_MIN);
                    mRespHarmonizationDTEMin = responseValueVCUPs4.get(Constants.RESP_HARMONIZATION_DTE_MIN);
                    mRespHarmonizationDTE = responseValueVCUPs4.get(Constants.RESP_HARMONIZATION_DTE);
                    mRespDrivingFeelingDTEMax = responseValueVCUPs4.get(Constants.RESP_DRIVING_FEELING_DTE_MAX);
                    mRespDrivingFeelingDTEMin = responseValueVCUPs4.get(Constants.RESP_DRIVING_FEELING_DTE_MIN);

                    if(Constants.DTE_MODE_STATUS == 3){

                        ((DTEchartActivity)DTEchartActivity.mContext).DTEchart(mRespAvailableDistance, mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE,
                                mRespAvailableDistanceMax, mRespDriverPatternDTEMax, mRespHarmonizationDTEMax, mRespVehicleSpeedDTEMax, mRespDrivingFeelingDTEMax, mRespAvailableDistanceMin, mRespDriverPatternDTEMin, mRespHarmonizationDTEMin, mRespVehicleSpeedDTEMin, mRespDrivingFeelingDTEMin );
                    }

                    break;

                case Constants.HANDLE_OBD_RESPONSE_VCUPs5:
                    if(msg == null || msg.obj == null) return;


                    HashMap<String, Integer> responseValueVCUPs5 = new HashMap<String, Integer>();
                    Object responseObjectVCUPs5 = msg.obj;
                    responseValueVCUPs5 = (HashMap<String, Integer>) responseObjectVCUPs5;

                    mRespVehicleSpeedDTE = responseValueVCUPs5.get(Constants.RESP_VEHICLE_SPEED_DTE);
                    mRespDrivingFeelingDTE = responseValueVCUPs5.get(Constants.RESP_DRIVING_FEELING_DTE);
                    mRespVehicleSpeedDTEMax = responseValueVCUPs5.get(Constants.RESP_VEHICLE_SPEED_DTE_MAX);
                    mRespVehicleSpeedDTEMin = responseValueVCUPs5.get(Constants.RESP_VEHICLE_SPEED_DTE_MIN);
                    mRespDriverPatternDTE = responseValueVCUPs5.get(Constants.RESP_DRIVER_PATTERN_DTE);


                    if(Constants.DTE_MODE_STATUS == 3){

                        ((DTEchartActivity)DTEchartActivity.mContext).DTEchart(mRespAvailableDistance, mRespAvailableDistanceBase, mRespDriverPatternDTE, mRespHarmonizationDTE, mRespVehicleSpeedDTE, mRespDrivingFeelingDTE,
                                mRespAvailableDistanceMax, mRespDriverPatternDTEMax, mRespHarmonizationDTEMax, mRespVehicleSpeedDTEMax, mRespDrivingFeelingDTEMax, mRespAvailableDistanceMin, mRespDriverPatternDTEMin, mRespHarmonizationDTEMin, mRespVehicleSpeedDTEMin, mRespDrivingFeelingDTEMin );
                    }

                    mRespInstFlefc = responseValueVCUPs5.get(Constants.RESP_INSTANT_FUEL_EFFICIENCY);

                    DecimalFormat dfm1 = new DecimalFormat("0.#");
                    String instFlefc = dfm1.format((float) (mRespInstFlefc*0.1));

                    mMileageInst.setText(instFlefc);

                    //mMileageInst.setText(String.valueOf((float) mRespInstFlefc*0.1));

                    if(mRespInstFlefc == 255){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.GONE)) mMileageInst12.setVisibility(View.VISIBLE);
                        if ((mMileageInst13.getVisibility() == View.GONE)) mMileageInst13.setVisibility(View.VISIBLE);
                        if ((mMileageInst14.getVisibility() == View.GONE)) mMileageInst14.setVisibility(View.VISIBLE);
                        if ((mMileageInst15.getVisibility() == View.GONE)) mMileageInst15.setVisibility(View.VISIBLE);
                        if ((mMileageInst16.getVisibility() == View.GONE)) mMileageInst16.setVisibility(View.VISIBLE);

                    } else if(mRespInstFlefc < 255 && mRespInstFlefc > 239){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.GONE)) mMileageInst12.setVisibility(View.VISIBLE);
                        if ((mMileageInst13.getVisibility() == View.GONE)) mMileageInst13.setVisibility(View.VISIBLE);
                        if ((mMileageInst14.getVisibility() == View.GONE)) mMileageInst14.setVisibility(View.VISIBLE);
                        if ((mMileageInst15.getVisibility() == View.GONE)) mMileageInst15.setVisibility(View.VISIBLE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 239 && mRespInstFlefc > 223){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.GONE)) mMileageInst12.setVisibility(View.VISIBLE);
                        if ((mMileageInst13.getVisibility() == View.GONE)) mMileageInst13.setVisibility(View.VISIBLE);
                        if ((mMileageInst14.getVisibility() == View.GONE)) mMileageInst14.setVisibility(View.VISIBLE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 223 && mRespInstFlefc > 207){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.GONE)) mMileageInst12.setVisibility(View.VISIBLE);
                        if ((mMileageInst13.getVisibility() == View.GONE)) mMileageInst13.setVisibility(View.VISIBLE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 207 && mRespInstFlefc > 191){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.GONE)) mMileageInst12.setVisibility(View.VISIBLE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 191 && mRespInstFlefc > 175){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.GONE)) mMileageInst11.setVisibility(View.VISIBLE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 175 && mRespInstFlefc > 159){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.GONE)) mMileageInst10.setVisibility(View.VISIBLE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 159 && mRespInstFlefc > 143){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.GONE)) mMileageInst09.setVisibility(View.VISIBLE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 143 && mRespInstFlefc > 127){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.GONE)) mMileageInst08.setVisibility(View.VISIBLE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 127 && mRespInstFlefc > 111){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.GONE)) mMileageInst07.setVisibility(View.VISIBLE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 111 && mRespInstFlefc > 95){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.GONE)) mMileageInst06.setVisibility(View.VISIBLE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 95 && mRespInstFlefc > 79){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.GONE)) mMileageInst05.setVisibility(View.VISIBLE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 79 && mRespInstFlefc > 63){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.GONE)) mMileageInst04.setVisibility(View.VISIBLE);
                        if ((mMileageInst05.getVisibility() == View.VISIBLE)) mMileageInst05.setVisibility(View.GONE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 63 && mRespInstFlefc > 47){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.GONE)) mMileageInst03.setVisibility(View.VISIBLE);
                        if ((mMileageInst04.getVisibility() == View.VISIBLE)) mMileageInst04.setVisibility(View.GONE);
                        if ((mMileageInst05.getVisibility() == View.VISIBLE)) mMileageInst05.setVisibility(View.GONE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 47 && mRespInstFlefc > 31){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.GONE)) mMileageInst02.setVisibility(View.VISIBLE);
                        if ((mMileageInst03.getVisibility() == View.VISIBLE)) mMileageInst03.setVisibility(View.GONE);
                        if ((mMileageInst04.getVisibility() == View.VISIBLE)) mMileageInst04.setVisibility(View.GONE);
                        if ((mMileageInst05.getVisibility() == View.VISIBLE)) mMileageInst05.setVisibility(View.GONE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else if(mRespInstFlefc < 31 && mRespInstFlefc > 15){

                        if ((mMileageInst01.getVisibility() == View.GONE)) mMileageInst01.setVisibility(View.VISIBLE);
                        if ((mMileageInst02.getVisibility() == View.VISIBLE)) mMileageInst02.setVisibility(View.GONE);
                        if ((mMileageInst03.getVisibility() == View.VISIBLE)) mMileageInst03.setVisibility(View.GONE);
                        if ((mMileageInst04.getVisibility() == View.VISIBLE)) mMileageInst04.setVisibility(View.GONE);
                        if ((mMileageInst05.getVisibility() == View.VISIBLE)) mMileageInst05.setVisibility(View.GONE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    } else {

                        if ((mMileageInst01.getVisibility() == View.VISIBLE)) mMileageInst01.setVisibility(View.GONE);
                        if ((mMileageInst02.getVisibility() == View.VISIBLE)) mMileageInst02.setVisibility(View.GONE);
                        if ((mMileageInst03.getVisibility() == View.VISIBLE)) mMileageInst03.setVisibility(View.GONE);
                        if ((mMileageInst04.getVisibility() == View.VISIBLE)) mMileageInst04.setVisibility(View.GONE);
                        if ((mMileageInst05.getVisibility() == View.VISIBLE)) mMileageInst05.setVisibility(View.GONE);
                        if ((mMileageInst06.getVisibility() == View.VISIBLE)) mMileageInst06.setVisibility(View.GONE);
                        if ((mMileageInst07.getVisibility() == View.VISIBLE)) mMileageInst07.setVisibility(View.GONE);
                        if ((mMileageInst08.getVisibility() == View.VISIBLE)) mMileageInst08.setVisibility(View.GONE);
                        if ((mMileageInst09.getVisibility() == View.VISIBLE)) mMileageInst09.setVisibility(View.GONE);
                        if ((mMileageInst10.getVisibility() == View.VISIBLE)) mMileageInst10.setVisibility(View.GONE);
                        if ((mMileageInst11.getVisibility() == View.VISIBLE)) mMileageInst11.setVisibility(View.GONE);
                        if ((mMileageInst12.getVisibility() == View.VISIBLE)) mMileageInst12.setVisibility(View.GONE);
                        if ((mMileageInst13.getVisibility() == View.VISIBLE)) mMileageInst13.setVisibility(View.GONE);
                        if ((mMileageInst14.getVisibility() == View.VISIBLE)) mMileageInst14.setVisibility(View.GONE);
                        if ((mMileageInst15.getVisibility() == View.VISIBLE)) mMileageInst15.setVisibility(View.GONE);
                        if ((mMileageInst16.getVisibility() == View.VISIBLE)) mMileageInst16.setVisibility(View.GONE);

                    }



                    mRespAvgFlefc = responseValueVCUPs5.get(Constants.RESP_AVERAGE_FUEL_EFFICIENCY);


                    DecimalFormat dfm2 = new DecimalFormat("0.#");
                    String avgFlefc = dfm2.format((float) (mRespAvgFlefc*0.1));

                    mMileageAvg.setText(avgFlefc);

                    if(mRespAvgFlefc == 255){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.GONE)) mMileageAvg12.setVisibility(View.VISIBLE);
                        if ((mMileageAvg13.getVisibility() == View.GONE)) mMileageAvg13.setVisibility(View.VISIBLE);
                        if ((mMileageAvg14.getVisibility() == View.GONE)) mMileageAvg14.setVisibility(View.VISIBLE);
                        if ((mMileageAvg15.getVisibility() == View.GONE)) mMileageAvg15.setVisibility(View.VISIBLE);
                        if ((mMileageAvg16.getVisibility() == View.GONE)) mMileageAvg16.setVisibility(View.VISIBLE);

                    } else if(mRespAvgFlefc < 255 && mRespAvgFlefc > 239){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.GONE)) mMileageAvg12.setVisibility(View.VISIBLE);
                        if ((mMileageAvg13.getVisibility() == View.GONE)) mMileageAvg13.setVisibility(View.VISIBLE);
                        if ((mMileageAvg14.getVisibility() == View.GONE)) mMileageAvg14.setVisibility(View.VISIBLE);
                        if ((mMileageAvg15.getVisibility() == View.GONE)) mMileageAvg15.setVisibility(View.VISIBLE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 239 && mRespAvgFlefc > 223){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.GONE)) mMileageAvg12.setVisibility(View.VISIBLE);
                        if ((mMileageAvg13.getVisibility() == View.GONE)) mMileageAvg13.setVisibility(View.VISIBLE);
                        if ((mMileageAvg14.getVisibility() == View.GONE)) mMileageAvg14.setVisibility(View.VISIBLE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 223 && mRespAvgFlefc > 207){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.GONE)) mMileageAvg12.setVisibility(View.VISIBLE);
                        if ((mMileageAvg13.getVisibility() == View.GONE)) mMileageAvg13.setVisibility(View.VISIBLE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 207 && mRespAvgFlefc > 191){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.GONE)) mMileageAvg12.setVisibility(View.VISIBLE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 191 && mRespAvgFlefc > 175){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.GONE)) mMileageAvg11.setVisibility(View.VISIBLE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 175 && mRespAvgFlefc > 159){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.GONE)) mMileageAvg10.setVisibility(View.VISIBLE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 159 && mRespAvgFlefc > 143){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.GONE)) mMileageAvg09.setVisibility(View.VISIBLE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 143 && mRespAvgFlefc > 127){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.GONE)) mMileageAvg08.setVisibility(View.VISIBLE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 127 && mRespAvgFlefc > 111){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.GONE)) mMileageAvg07.setVisibility(View.VISIBLE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 111 && mRespAvgFlefc > 95){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.GONE)) mMileageAvg06.setVisibility(View.VISIBLE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 95 && mRespAvgFlefc > 79){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.GONE)) mMileageAvg05.setVisibility(View.VISIBLE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 79 && mRespAvgFlefc > 63){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.GONE)) mMileageAvg04.setVisibility(View.VISIBLE);
                        if ((mMileageAvg05.getVisibility() == View.VISIBLE)) mMileageAvg05.setVisibility(View.GONE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 63 && mRespAvgFlefc > 47){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.GONE)) mMileageAvg03.setVisibility(View.VISIBLE);
                        if ((mMileageAvg04.getVisibility() == View.VISIBLE)) mMileageAvg04.setVisibility(View.GONE);
                        if ((mMileageAvg05.getVisibility() == View.VISIBLE)) mMileageAvg05.setVisibility(View.GONE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 47 && mRespAvgFlefc > 31){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.GONE)) mMileageAvg02.setVisibility(View.VISIBLE);
                        if ((mMileageAvg03.getVisibility() == View.VISIBLE)) mMileageAvg03.setVisibility(View.GONE);
                        if ((mMileageAvg04.getVisibility() == View.VISIBLE)) mMileageAvg04.setVisibility(View.GONE);
                        if ((mMileageAvg05.getVisibility() == View.VISIBLE)) mMileageAvg05.setVisibility(View.GONE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else if(mRespAvgFlefc < 31 && mRespAvgFlefc > 15){

                        if ((mMileageAvg01.getVisibility() == View.GONE)) mMileageAvg01.setVisibility(View.VISIBLE);
                        if ((mMileageAvg02.getVisibility() == View.VISIBLE)) mMileageAvg02.setVisibility(View.GONE);
                        if ((mMileageAvg03.getVisibility() == View.VISIBLE)) mMileageAvg03.setVisibility(View.GONE);
                        if ((mMileageAvg04.getVisibility() == View.VISIBLE)) mMileageAvg04.setVisibility(View.GONE);
                        if ((mMileageAvg05.getVisibility() == View.VISIBLE)) mMileageAvg05.setVisibility(View.GONE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    } else {

                        if ((mMileageAvg01.getVisibility() == View.VISIBLE)) mMileageAvg01.setVisibility(View.GONE);
                        if ((mMileageAvg02.getVisibility() == View.VISIBLE)) mMileageAvg02.setVisibility(View.GONE);
                        if ((mMileageAvg03.getVisibility() == View.VISIBLE)) mMileageAvg03.setVisibility(View.GONE);
                        if ((mMileageAvg04.getVisibility() == View.VISIBLE)) mMileageAvg04.setVisibility(View.GONE);
                        if ((mMileageAvg05.getVisibility() == View.VISIBLE)) mMileageAvg05.setVisibility(View.GONE);
                        if ((mMileageAvg06.getVisibility() == View.VISIBLE)) mMileageAvg06.setVisibility(View.GONE);
                        if ((mMileageAvg07.getVisibility() == View.VISIBLE)) mMileageAvg07.setVisibility(View.GONE);
                        if ((mMileageAvg08.getVisibility() == View.VISIBLE)) mMileageAvg08.setVisibility(View.GONE);
                        if ((mMileageAvg09.getVisibility() == View.VISIBLE)) mMileageAvg09.setVisibility(View.GONE);
                        if ((mMileageAvg10.getVisibility() == View.VISIBLE)) mMileageAvg10.setVisibility(View.GONE);
                        if ((mMileageAvg11.getVisibility() == View.VISIBLE)) mMileageAvg11.setVisibility(View.GONE);
                        if ((mMileageAvg12.getVisibility() == View.VISIBLE)) mMileageAvg12.setVisibility(View.GONE);
                        if ((mMileageAvg13.getVisibility() == View.VISIBLE)) mMileageAvg13.setVisibility(View.GONE);
                        if ((mMileageAvg14.getVisibility() == View.VISIBLE)) mMileageAvg14.setVisibility(View.GONE);
                        if ((mMileageAvg15.getVisibility() == View.VISIBLE)) mMileageAvg15.setVisibility(View.GONE);
                        if ((mMileageAvg16.getVisibility() == View.VISIBLE)) mMileageAvg16.setVisibility(View.GONE);

                    }



                    break;

                case Constants.HANDLE_OBD_OTHER:
                    // Log.d(TAG, "이외의 상황은?");

                    if(Constants.OBD_INITIALIZED){
                        mainObdLight.setBackgroundResource(R.drawable.ico_light_green);
                    }else{
                        mainObdLight.setBackgroundResource(R.drawable.ico_light_red);
                    }

                    break;
            }




        }
    }

    /**
     * Initialize UI
     */
    public void initUI() {

        ImageButton favoriteBtn = (ImageButton) findViewById(R.id.ib_favorite_btn);
        favoriteBtn.setOnClickListener(this);

        gearBtn = findViewById(R.id.gear_box_btn);
        gearBtn.setOnClickListener(this);
        speakerBtn = findViewById(R.id.speaker_btn);
        speakerBtn.setOnClickListener(this);
        drivingAxleBtn = findViewById(R.id.driving_axle_btn);
        drivingAxleBtn.setOnClickListener(this);


        obdSetBtn = (ImageButton) findViewById(R.id.ib_obd_set_btn);
        obdSetBtn.setOnClickListener(this);

        mainObdLight = (ImageView) findViewById(R.id.iv_main_light);

        mMode = (ToggleButton) findViewById(R.id.tb_setting);

        if(mModeValue.equals(Constants.MODE_RCDATION)){
            mMode.setChecked(true);
        } else if (mModeValue.equals(Constants.MODE_EXPERT)){
            mMode.setChecked(false);
        }

        mChart = (RadarChart) findViewById(R.id.chart);
        mChart.setNoDataText("데이터가 없습니다.");

        defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);

        mLayoutRcdation = (RelativeLayout) findViewById(R.id.layout_rcdation);
        mLayoutExpert = (RelativeLayout) findViewById(R.id.layout_expert);
//        mLayoutBarChart = (RelativeLayout) findViewById(R.id.layout_bar_chart);
//        mLayoutBarChart.setOnClickListener(this);

        //mSeekerEco = (TextView) findViewById(R.id.tv_eco);
        //mSeekerSport = (TextView) findViewById(R.id.tv_sport);

        //mSbDriving = (SeekBar)findViewById(R.id.sb_driving);
        /*mSbDriving.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "mParamDriving : " + mParamDriving);

                Constants.COMMAND_MODE = "PARAM";

                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));

                // TODO : it hase timing issue, so send it by twice
                Handler hd2 = new Handler(Looper.getMainLooper());
                hd2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
                    }
                }, 1000);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mParamDriving = progress;
                mSeekerEco.setText(Integer.toString(100 - mParamDriving));
                mSeekerSport.setText(Integer.toString(mParamDriving));
            }
        });*/

//        mSeekerTorque = (TextView) findViewById(R.id.tv_seeker_torque);
//        mSbTorque = (SeekBar)findViewById(R.id.sb_torque);
//        mSbTorque.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamTorque : " + mParamTorque);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamTorque = progress + 50;
//                mSeekerTorque.setText(Integer.toString(mParamTorque) + "%");
//            }
//        });

//        mSeekerAcc = (TextView) findViewById(R.id.tv_seeker_acc);
//        mSbAcc = (SeekBar)findViewById(R.id.sb_acc);
//        mSbAcc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamAcceleration : " + mParamAcceleration);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamAcceleration = progress;
//                mSeekerAcc.setText(Integer.toString(mParamAcceleration));
//            }
//        });
//
//        mSeekerDecel = (TextView) findViewById(R.id.tv_seeker_decel);
//        mSbDecel = (SeekBar)findViewById(R.id.sb_decel);
//        mSbDecel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamDeceleration : " + mParamDeceleration);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamDeceleration = progress;
//                mSeekerDecel.setText(Integer.toString(mParamDeceleration));
//            }
//        });
//
//        mSeekerBrake = (TextView) findViewById(R.id.tv_seeker_brake);
//        mSbBrake = (SeekBar)findViewById(R.id.sb_brake);
//        mSbBrake.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamBrake : " + mParamBrake);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamBrake = progress;
//                mSeekerBrake.setText(Integer.toString(mParamBrake));
//            }
//        });
//
//        mSeekerEnergy = (TextView) findViewById(R.id.tv_seeker_energy);
//        mSbEnergy = (SeekBar)findViewById(R.id.sb_energy);
//        mSbEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamEnergy : " + mParamEnergy);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamEnergy = progress;
//                if(mParamEnergy==0) mSeekerEnergy.setText("Off");
//                else if(mParamEnergy==1) mSeekerEnergy.setText("Eco");
//                else if(mParamEnergy==2) mSeekerEnergy.setText("Normal");
//            }
//        });
//
//        mSeekerSpeed = (TextView) findViewById(R.id.tv_seeker_speed);
//        mSbSpeed = (SeekBar)findViewById(R.id.sb_speed);
//        mSbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamSpeed : " + mParamSpeed);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamSpeed = progress + 60;
//                mSeekerSpeed.setText(Integer.toString(mParamSpeed) + "kph");
//            }
//        });
//
//        mSeekerResponse = (TextView) findViewById(R.id.tv_seeker_response);
//        mSbResponse = (SeekBar)findViewById(R.id.sb_response);
//        mSbResponse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "mParamResponse : " + mParamResponse);
//
//                Constants.COMMAND_MODE = "PARAM";
//                sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                        mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//
//                // TODO : it hase timing issue, so send it by twice
//                Handler hd2 = new Handler(Looper.getMainLooper());
//                hd2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMessage("00" + Utility.convertParamIntToHex(mParamTorque, mParamAcceleration,
//                                mParamDeceleration, mParamBrake, mParamEnergy, mParamSpeed, mParamResponse, mParamDriving));
//                    }
//                }, 500);
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mParamResponse = progress;
//                mSeekerResponse.setText(Integer.toString(mParamResponse));
//            }
//        });

        // button for general
        //ImageButton gResetBtn = (ImageButton) findViewById(R.id.ib_g_reset_btn);
        //gResetBtn.setOnClickListener(this);

        //ImageButton gSendBtn = (ImageButton) findViewById(R.id.ib_g_send_btn);
        //gSendBtn.setOnClickListener(this);

        //ImageButton gSubmitBtn = (ImageButton) findViewById(R.id.ib_g_submit_btn);
        //gSubmitBtn.setOnClickListener(this);

        // button for expert
        ImageButton eResetBtn = (ImageButton) findViewById(R.id.ib_e_reset_btn);
        eResetBtn.setOnClickListener(this);

        ImageButton eSendBtn = (ImageButton) findViewById(R.id.ib_e_send_btn);
        eSendBtn.setOnClickListener(this);

        ImageButton eSubmitBtn = (ImageButton) findViewById(R.id.ib_e_submit_btn);
        eSubmitBtn.setOnClickListener(this);


        resetBtn = findViewById(R.id.ib_e_reset_btn);
        resetBtn.setOnClickListener(this);


        mMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                //
                if(!isChecked) {
                    mModeValue = Constants.MODE_EXPERT;
                    defaultChart(mRespMaxPower, mRespAcceration, mRespDeceleration, mRespResponse, mRespEcoLevel);
                } else {
                    mModeValue = Constants.MODE_RCDATION;
                }
                setParamMode(mModeValue);
            }
        }) ;

        setParamMode(mModeValue);

        // OBD Status 변경
        Log.d(TAG, " ================= OBD Status 변경 :: " + Constants.OBD_STATUS);
        setOBDMode(Constants.OBD_STATUS);


    }

    // =============================================================================================
    /**
     * Set OBD mode
     *
     * @param switchFlag - odb mode about whether switch is on or off
     */
    private void setOBDMode(boolean switchFlag) {

        if(switchFlag) {
            obdSetBtn.setBackgroundResource(R.drawable.img_tit_04);
            Constants.OBD_STATUS = true;
        } else {
            obdSetBtn.setBackgroundResource(R.drawable.img_tit_03);
            Constants.OBD_STATUS = false;
        }
    }

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
            case Constants.REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {

                    String obdName = data.getExtras().getString("obdName");
                    obdMac = data.getExtras().getString("obdMac");

                    Log.d(TAG, "onActivityResult // obdName :: " + obdName + " // obdMac :: " + obdMac);

                    if(!obdName.contains("OBDLink MX")) {
                        showDialog("App과 OBD가 올바르게 연결되지 않았습니다. OBDLink MX를 선택했는지 다시 확인해주세요.\n", true);
                        Constants.OBD_STATUS = false;
                    } else {
                        String msg = "OBDLink MX와 Bluetooth 연결 중....";
                        showMessageDialog(msg);
                        createOBDSingleton(data, true);
                    }
                } else if(resultCode == Activity.RESULT_CANCELED) {
                    showDialogCancelData();
                }
                break;
            case Constants.REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    createOBDSingleton(data, false);
                }
                break;
            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "블루투스를 사용할수 없습니다 않습니다. 블루투스를 다시 설정하십시오");
                    finish();
                }
                break;
            case Constants.REQUEST_FAVORITE:

                if (resultCode == Activity.RESULT_OK) {

                    String param = data.getExtras().getString("param");
                    String[] params = param.split(":");

                    if(params[0].equals(Constants.MODE_RCDATION)){
                        mMode.setChecked(false);
                    } else if (params[0].equals(Constants.MODE_EXPERT)){
                        mMode.setChecked(true);
                    }

                    mParamTorque = Integer.parseInt(params[1]);
                    mSeekerTorque.setText(Integer.toString(mParamTorque) + "%");
                    mSbTorque.setProgress(mParamTorque-50);

                    mParamAcceleration = Integer.parseInt(params[2]);
                    mSeekerAcc.setText(Integer.toString(mParamAcceleration));
                    mSbAcc.setProgress(mParamAcceleration);

                    mParamDeceleration = Integer.parseInt(params[3]);
                    mSeekerDecel.setText(Integer.toString(mParamDeceleration));
                    mSbDecel.setProgress(mParamDeceleration);

                    mParamBrake = Integer.parseInt(params[4]);
                    mSeekerBrake.setText(Integer.toString(mParamBrake));
                    mSbBrake.setProgress(mParamBrake);

                    mParamEnergy = Integer.parseInt(params[5]);
                    if(mParamEnergy==0) mSeekerEnergy.setText("Off");
                    else if(mParamEnergy==1) mSeekerEnergy.setText("Eco");
                    else if(mParamEnergy==2) mSeekerEnergy.setText("Normal");
                    mSbEnergy.setProgress(mParamEnergy);

                    mParamSpeed = Integer.parseInt(params[6]);
                    mSeekerSpeed.setText(Integer.toString(mParamSpeed) + "kph");
                    mSbSpeed.setProgress(mParamSpeed-60);

                    mParamResponse = Integer.parseInt(params[7]);
                    mSeekerResponse.setText(Integer.toString(mParamResponse));
                    mSbResponse.setProgress(mParamResponse);

                    mParamDriving = Integer.parseInt(params[8]);
                    mSeekerEco.setText(Integer.toString(100 - mParamDriving));
                    mSeekerSport.setText(Integer.toString(mParamDriving));
                    mSbDriving.setProgress(mParamDriving);

                    if(mObdsv == null) {
                        Toast.makeText(getApplicationContext(), "App과 OBD가 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Check that we're actually connected before trying anything
                    if (mObdsv.getState() != Constants.STATE_CONNECTED) {
                        Toast.makeText(getApplicationContext(), "App과 OBD가 올바르게 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Constants.COMMAND_MODE = "SEND";

                    showProgressDialog();
                } else if(resultCode == Activity.RESULT_CANCELED) {
                    // do nothing
                }
                break;
            case Constants.REQUEST_GEAR_SETTING :
                // 차량 obd가 연결되어있느냐에 따라서 다르게 처리해야함..
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
                }
            case Constants.REQUEST_SPEAKER_SETTING :
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
                }

                break;
            case Constants.REQUEST_DRIVING_SETTING :
                if(data.getBooleanExtra("change",false)){
                    modChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance(),
                            CarData.getInstance().getTempComfortable(),CarData.getInstance().getTempLeading(),
                            CarData.getInstance().getTempDynamic(),CarData.getInstance().getTempEfficiency(),CarData.getInstance().getTempPerformance());
                }
            default:
                break;
        }
    }

    /**
     * Dialog about that obd name is wrong
     */
    private void showDialog(String popStrChk_1, final boolean moveFlag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(popStrChk_1)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        if (moveFlag) {
                            selectOBD();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_bg);
        dialog.show();
    }

    /**
     * Create singleton OBD instance
     *
     * @param data - data about obd name and mac
     * @param secure - method whether it is secure or not
     */
    private void createOBDSingleton(Intent data, boolean secure) {
        Log.d(TAG, "=== createOBDSingleton");

        mObdsv = ObdService.getInstance(getApplicationContext(), mObdDataHandler);

        mObdsv.connectDevice(data, secure);

        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {

                // in case of no connection
                if(mObdsv.getState() != Constants.STATE_CONNECTED) {
                    Log.d(TAG, "=== createOBDSingleton :: != Constants.STATE_CONNECTED :: 연결안댐!!");
                    showDialogRetry();
                    // OBD Status 변경
                    //Constants.OBD_STATUS = false;
                    setOBDMode(false);
                } else {
                    // connected
                    successConnectOBD();
                }
            }
        }, 5000);
    }

    /**
     * Connection with OBD is the success
     */
    private void successConnectOBD() {

        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {

                closeMessageDialog();
                //closeDialog();

                Log.d(TAG, "=== createOBDSingleton :: == Constants.STATE_CONNECTED :: 연결댐!!");
                String successStr = "App과 OBD의 Bluetooth 연결에 성공하였습니다.";
                showDialog(successStr, false);

                // OBD Status 변경
                // Constants.OBD_STATUS = true;
                setOBDMode(true);

                // set mode to obd service
                // mObdsv.setOBDServiceMode(mModeValue);

                // start send message it
                sendMessage("AT Z");

            }
        }, 1000);
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
    private void selectOBD() {
        Intent serverIntent = null;
        serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
        startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_SECURE);
    }

    /**
     * Show or cancel dialog
     */
    private void showDialogCancelData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("App과 OBD를 연결하지 않으면 App 사용에 제한이 있을 수 있습니다. OBD 연결을 하지 않고 사용하시겠습니까?\n")        // 메세지 설정
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        selectOBD();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_bg);
        dialog.show();
    }

    /**
     * Retry dialog
     */
    private void showDialogRetry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("App과 OBD의 Bluetooth 연결에 실패하였습니다. 다시 시도하시겠습니까?\n")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        selectOBD();

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeMessageDialog();
                        dialog.cancel();
                        showDialogCancelData();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_bg);
        dialog.show();
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

    private void showProgressDialogMode() {

        sendMessage("ATSH530");
        FavoriteDataListItems listItems = dataListItems.get(Constants.MODE_STATUS);

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
                        cnt = 0;

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

        final String[] params = listItems.getParam().split(":");

        sendMessage("01" + Utility.convertParamIntToHex(Integer.valueOf(params[0]), Integer.valueOf(params[1]),
                Integer.valueOf(params[2]), Integer.valueOf(params[3]), Integer.valueOf(params[4]), Integer.valueOf(params[5]), Integer.valueOf(params[6]), mParamDriving));

        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {

                mMode.setChecked(false);
                mModeValue = Constants.MODE_EXPERT;
                setParamMode(mModeValue);
                sendMessage("01" + Utility.convertParamIntToHex(Integer.valueOf(params[0]), Integer.valueOf(params[1]),
                        Integer.valueOf(params[2]), Integer.valueOf(params[3]), Integer.valueOf(params[4]), Integer.valueOf(params[5]), Integer.valueOf(params[6]), mParamDriving));
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

    public void FavoriteResult(String[] resp){


        sendMessage("ATSH530");

        final String [] respParams = resp;

        mMode.setChecked(false);
        mModeValue = Constants.MODE_EXPERT;
        setParamMode(mModeValue);


        sendMessage("01" + Utility.convertParamIntToHex(Integer.valueOf(respParams[1]), Integer.valueOf(respParams[2]),
                Integer.valueOf(respParams[3]), Integer.valueOf(respParams[4]), Integer.valueOf(respParams[5]), Integer.valueOf(respParams[6]), Integer.valueOf(respParams[7]), mParamDriving));

        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage("01" + Utility.convertParamIntToHex(Integer.valueOf(respParams[1]), Integer.valueOf(respParams[2]),
                        Integer.valueOf(respParams[3]), Integer.valueOf(respParams[4]), Integer.valueOf(respParams[5]), Integer.valueOf(respParams[6]), Integer.valueOf(respParams[7]), mParamDriving));

            }
        }, 1000);


        sendMessage("0000");
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
        mChart.getYAxis().setAxisMaximum(8f);
        mChart.getYAxis().setEnabled(false);             // disable number

        RadarData data = new RadarData(dataSetList);
        mChart.setData(data);
        mChart.getDescription().setEnabled(false);

        mChart.getLegend().setEnabled(false);            // remove legend
        // mChart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        mChart.setTouchEnabled(false);                   // disable touch
        mChart.invalidate();
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
        labels.add("동력성능");

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
        mChart.getYAxis().setAxisMaximum(8f);
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


    /**
     * Set mode(setting)
     *
     * @param mode general or expert
     */
    private void setParamMode(String mode){
        if(mode.equals(Constants.MODE_RCDATION)) {
            mLayoutExpert.setVisibility(View.GONE);
            mLayoutRcdation.setVisibility(View.VISIBLE);

            sendMessage("ATSH531");

            Handler hd2 = new Handler(Looper.getMainLooper());
            hd2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage("ATSH531");
                }
            }, 1000);

            // 추천모드 실행
            modeInit(dataListItems.get(Constants.MODE_STATUS));

        } else if(mode.equals(Constants.MODE_EXPERT)) {
            mLayoutExpert.setVisibility(View.VISIBLE);
            mLayoutRcdation.setVisibility(View.GONE);

            sendMessage("ATSH530");

            Handler hd2 = new Handler(Looper.getMainLooper());
            hd2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage("ATSH530");
                }
            }, 1000);

            //initUI();

        }
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
        Intent gear = new Intent(this, GearSettingActivity.class);
        startActivityForResult(gear , Constants.REQUEST_GEAR_SETTING);
    }

    private void goToSpeakerSettingActivity(){
        Intent speaker = new Intent(this,SpeakerSettingActivity.class);
        startActivityForResult(speaker,Constants.REQUEST_SPEAKER_SETTING);
    }

    private void goToDrivingSettingActivity(){
        Intent driving = new Intent(this , AxelSettingActivity.class);
        startActivityForResult(driving , Constants.REQUEST_DRIVING_SETTING);
    }
    /**
     * Move to main activity
     */
    private void goToParamSettingActivity() {

        Intent paramSetting = new Intent(this, ParamSettingActivity.class);

        // 20181109_강동필
        paramSetting.putExtra("resp", String.valueOf(mRespMaxPower) + ":" + String.valueOf(mRespAcceration) + ":" +
                String.valueOf(mRespDeceleration) + ":" + String.valueOf(mRespResponse) + ":" + String.valueOf(mRespEcoLevel));

        paramSetting.putExtra("param", "e:" + String.valueOf(mParamTorque) + ":" + String.valueOf(mParamAcceleration) + ":" +
                String.valueOf(mParamDeceleration) + ":" + String.valueOf(mParamBrake) + ":" + String.valueOf(mParamEnergy) + ":" +
                String.valueOf(mParamSpeed) + ":" + String.valueOf(mParamResponse) + ":" + String.valueOf(mParamDriving));

        startActivity(paramSetting);
    }


    private ArrayList<FavoriteDataListItems> modeListDatabase() {

        Obd2DBOpenHelper helper = new Obd2DBOpenHelper(getApplicationContext());

        ArrayList<FavoriteDataListItems> list = new ArrayList<>();
        helper.open();
        Cursor cursor = helper.getModeAll();

        // 추천모드 초기에 데이터가 없는 경우 호출
        if(helper.getModeRowCount() == 0) {
            // 초기값 팅
            // 산악
            String param = "100:3:5:4:2:60:1";
            String resp = "5.0:3.0:5.0:1.0:2.88";
            DBUtil.insertModeDB(getApplicationContext(),"산악", param, resp);
            // 해변가
            String param2 = "90:3:3:3:2:80:2";
            String resp2 = "4.0:3.0:3.0:3.0:2.88";
            DBUtil.insertModeDB(getApplicationContext(),"해변가", param2, resp2);
            // 실버
            String param3 = "60:1:5:4:2:60:0";
            String resp3 = "1.0:1.0:5.0:2.0:3.75";
            DBUtil.insertModeDB(getApplicationContext(),"실버", param3, resp3);
            // 도심
            String param4 = "100:5:5:4:1:120:2";
            String resp4 = "1.0:5.0:5.0:5.0:2.5";
            DBUtil.insertModeDB(getApplicationContext(),"도심", param4, resp4);
        }

        for(int i=0; i < cursor.getCount(); i ++) {
            cursor.moveToNext();

            FavoriteDataListItems items = new FavoriteDataListItems();
            items.setId(String.valueOf(cursor.getInt(0)));
            items.setTitle(cursor.getString(1));
            items.setParam(cursor.getString(2));
            items.setResp(cursor.getString(3));

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

}
