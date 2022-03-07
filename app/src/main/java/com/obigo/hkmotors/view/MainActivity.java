package com.obigo.hkmotors.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.pref.SharedPreference;
import com.obigo.hkmotors.custom.LoadingDialog;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.FavoriteData;
import com.obigo.hkmotors.model.FavoriteDataListItems;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;
import com.obigo.hkmotors.module.Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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



    private String mModeValue;


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

    String sampleSignal1="";
    String sampleSignal2 = "";

    SharedPreferences preference;
    final static String foldername = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Motors";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "== onCreate");

        mModeValue = getIntent().getStringExtra("mode");

        mPref = new SharedPreference(getApplicationContext());


        preference = (SharedPreferences) getSharedPreferences("Motors",MODE_PRIVATE);
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
            carConnectionBtn.setText("가상모드 ON");
            connectedWifi();

            sampleSignal1 = preference.getString("Signal1","101 1 1 01 01 1 0 10 01");
            sampleSignal2 = preference.getString("Signal2","1 01 011 01 01 10 01");

        }
        isEdit = 1;





//        connectedSocket();

    }


    public void WriteTextFile(String foldername, String filename, String contents){
        try{
            File dir = new File (foldername);
            //디렉토리 폴더가 없으면 생성함
            if(!dir.exists()){
                dir.mkdir();
            }
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void getCarData(){
        loadingDialog = new LoadingDialog(MainActivity.this,1);
        loadingDialog.show();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setNotTouch();

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
                                loadingDialog.hide();
                                loadingDialog.setTouch();
                                // 실제 차량 데이터 가저오는 메소드 필요
//                                getOdbData();
                                defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                                        CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                                        CarData.getInstance().getPerformance());
                            }
                        });


                    }
                }).start();
            }
        }, 1000);


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

                        sendCarData();
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


                    sendCarData();


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
        System.out.println(signal1Array[0]);
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
            selectedPreset = "101";
        }else{
            ex.setTextColor(Color.GRAY);
            rc.setTextColor(Color.WHITE);
            findViewById(R.id.expert_mode).setBackgroundResource(R.drawable.shape_toggle_expert_off);
            findViewById(R.id.rc_mode).setBackgroundResource(R.drawable.shape_toggle_rc_on);
            mLayoutExpert.setVisibility(View.GONE);
            mLayoutRcdation.setVisibility(View.VISIBLE);
            recommendChangeBackGround(-1);
            selectedPreset = "";
        }

        defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getInstance().getDynamic(),
                CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance());
        Transmission.getInstance().reset();
        Sound.getInstance().reset();
        Drive.getInstance().reset();
    }


    public void setOffSetting(){

        loadingDialog = new LoadingDialog(MainActivity.this,1);
        setBtnAnimation();
        loadingDialog.show();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setNotTouch();
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
                                loadingDialog.hide();
                                loadingDialog.setTouch();
                                setEditData(sampleSignal1,sampleSignal2);
                                defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                                        CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                                        CarData.getInstance().getPerformance());
                            }
                        });


                    }
                }).start();
            }
        }, 1000);

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
                    // 차량 데이터 가저오기 기능 여기서 구현
                    getCarData();
                }else{
                    mainConnectionLight.setBackgroundResource(R.drawable.ico_light_red);
                    carConnectionBtn.setText("가상모드 ON");
                    setOffSetting();
                }


                break;


            case Constants.REQUEST_FAVORITE:

                if(data.getBooleanExtra("check",false)){

                    isEdit = 2;
                    setEditMode();
                    editTitle = data.getStringExtra("title");
                    editId = data.getIntExtra("id",-1);
                    changeChart();
                }else{
                    defaultChart(CarData.getInstance().getComfortable(),CarData.getInstance().getLeading(),CarData.getInstance().getInstance().getDynamic(),
                            CarData.getInstance().getEfficiency(),CarData.getInstance().getPerformance());
                    isEdit = 1;
                    setEditMode();
                    setBtnAnimation();
                    Transmission.getInstance().reset();
                    Sound.getInstance().reset();
                    Drive.getInstance().reset();
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
                        setBtnAnimation();
                        Transmission.getInstance().reset();
                        Sound.getInstance().reset();
                        Drive.getInstance().reset();
                    }else{
                        Toast.makeText(getApplicationContext(),"새로운 커스텀 모드를 저장했습니다",Toast.LENGTH_SHORT).show();
                        setBtnAnimation();
                        Transmission.getInstance().reset();
                        Sound.getInstance().reset();
                        Drive.getInstance().reset();
                        defaultChart(CarData.getInstance().getComfortable(), CarData.getInstance().getLeading(),
                                CarData.getInstance().getDynamic(), CarData.getInstance().getEfficiency(),
                                CarData.getInstance().getPerformance());
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
        String sgn1 = signal1.replace(" ","");
        String sgn2 = signal2.replace(" ","");
        int decimalSignal1 = Integer.parseInt(sgn1,2);
        int decimalSignal2 = Integer.parseInt(sgn2,2);

        if(Network.getInstance()==null){
            SharedPreferences.Editor editor = preference.edit();

            editor.putString("Signal1",signal1);
            editor.putString("Signal2",signal2);

            editor.commit();

            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String contents = "Signal1 : " +signal1+"->"+decimalSignal1+"\n"
                    +"Signal2 : "+signal2+"->"+decimalSignal2+"\n";
            String filename = now+ "log.txt";
//                        Toast.makeText(getApplicationContext(),"차량과 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
            WriteTextFile(foldername,filename,contents);



        }else{
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

            System.out.println("-----Send JSON-----");
            System.out.println(data);

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
        }


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
        return selectedPreset+" "+Sound.getInstance().getTempIsOn()+" "+
                Sound.getInstance().getTempDriveType()+" "+
                Sound.getInstance().getTempVolume()+" "+
                Sound.getInstance().getTempBackVolume()+" "+
                Sound.getInstance().getTempBackSensitive()+" "+
                Drive.getInstance().getTempIsOn()+" "+
                Drive.getInstance().getTempStiffness()+" "+
                Drive.getInstance().getTempReducer();

    }
    public String setSignal2(){
        return Transmission.getInstance().getTempIsOn()+" "+
                Transmission.getInstance().getTempType()+" "+
                Transmission.getInstance().getTempGear()+" "+
                Transmission.getInstance().getTempGearRate()+" "+
                Transmission.getInstance().getTempTransmissionSpeed()+" "+
                Transmission.getInstance().getTempTransmissionPower()+" "+
                Transmission.getInstance().getTempTransmissionMap();

    }


    /**
     * OBD(device list) popup for the selection
     */
    private void connectedWifi() {
        Intent intent = new Intent(this,connectionActivity.class);

        startActivityForResult(intent,Constants.REQUEST_CONNECT_WIFI);

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

        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
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
    private void goToFavoriteActivity() {

        Intent favorite = new Intent(this, FavoriteActivity.class);
        startActivityForResult(favorite, Constants.REQUEST_FAVORITE);
    }

    private void goToGearSettingActivity(){
        Intent gear = new Intent(getApplicationContext(), TransmissionActivity.class);
        startActivityForResult(gear , Constants.REQUEST_GEAR_SETTING);
    }

    private void goToSpeakerSettingActivity(){
        Intent speaker = new Intent(this, SoundActivity.class);
        startActivityForResult(speaker,Constants.REQUEST_SPEAKER_SETTING);
    }

    private void goToDrivingSettingActivity(){
        Intent driving = new Intent(getApplicationContext() , DriveActivity.class);
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
                setBtnAnimation();
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
