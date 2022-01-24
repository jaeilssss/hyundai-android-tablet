package com.obigo.hkmotors.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.custom.LoadingDialog;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;


/**
 * Class for the set of parameters and responses
 */
public class ParamSettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ParamSettingActivity";

    private TextView mTitle;
    private ImageButton mDeleteBtn;
    private Button cancel;
    private Dialog mProgressDialog;

    private LinearLayout mLayoutGeneral;
    private LinearLayout mLayoutExport;

    private TextView mBlockchainValue;

    private ImageView mStep01;
    private ImageView mStep02;
    private ImageView mStep03;
    private ImageView mStep04;
    private ImageView mStep05;


    private String signal1;
    private String signal2;

    private String isInit;
    private String editTitle;
    private int editId;

    private int isEdit;
    private boolean check;

    private LoadingDialog loadingDialog;



    private TextView transmissionIsOn;
    private TextView transmissionType;
    private TextView transmissionGear;
    private TextView transmissionGearRate;
    private TextView transmissionGearSpeed;
    private TextView transmissionGearPower;
    private TextView transmissionMap;

    private TextView soundIsOn;
    private TextView soundType;
    private TextView soundVolume;
    private TextView backSoundVolume;
    private TextView backSoundSensitive;

    private TextView driveIsOn;
    private TextView driveStiffness;
    private TextView driveReducer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setDimAmount(0.9f);

        setContentView(R.layout.activity_param_setting);
        init();



         loadingDialog  = new LoadingDialog(ParamSettingActivity.this,0);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //로딩창을 투명하게




        mTitle = (EditText) findViewById(R.id.et_title);
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 0) {
                    mDeleteBtn.setVisibility(View.VISIBLE);
                } else {
                    mDeleteBtn.setVisibility(View.GONE);
                }
            }
        });
//
        mDeleteBtn = (ImageButton) findViewById(R.id.ib_delete_btn);
        mDeleteBtn.setOnClickListener(this);

        isEdit = getIntent().getIntExtra("isEdit",1);
        if(isEdit==2){
            editTitle = getIntent().getStringExtra("title");
            mTitle.setText(editTitle);
            editId = getIntent().getIntExtra("id",-1);
        }

        Button confirmBtn = (Button) findViewById(R.id.ib_confirm_btn);
        confirmBtn.setOnClickListener(this);
        cancel = findViewById(R.id.ib_cancel_btn);
        cancel.setOnClickListener(this);


    }



    public void init(){
        transmissionIsOn = findViewById(R.id.transmission_is_on);
        transmissionType = findViewById(R.id.transmission_type);
        transmissionGear = findViewById(R.id.transmission_gear);
        transmissionGearRate = findViewById(R.id.transmission_gear_rate);
        transmissionGearSpeed = findViewById(R.id.transmission_speed);
        transmissionGearPower = findViewById(R.id.transmission_power);
        transmissionMap = findViewById(R.id.transmission_map);

        soundIsOn = findViewById(R.id.sound_is_on);
        soundType = findViewById(R.id.sound_type);
        soundVolume = findViewById(R.id.sound_volume);
        backSoundVolume = findViewById(R.id.sound_back_volume);
        backSoundSensitive = findViewById(R.id.sound_back_sensitive);

        driveIsOn = findViewById(R.id.drive_is_on);
        driveStiffness = findViewById(R.id.drive_stiffness);
        driveReducer = findViewById(R.id.drive_reduce);



        soundIsOn.setText(Sound.getInstance().getTempIsOn());
        driveIsOn.setText(Drive.getInstance().getTempIsOn());


        if(Transmission.getInstance().getTempIsOn().equals("1")){
            transmissionIsOn.setText("ON");
            if(Transmission.getInstance().getTempType().equals("00")){
                transmissionType.setText("AT");
            }else if(Transmission.getInstance().getTempType().equals("01")){
                transmissionType.setText("DCT");
            }else{
                transmissionType.setText("AMT");
            }
            if(Transmission.getInstance().getTempGear().equals("000")){
                transmissionGear.setText("4");
            }else if(Transmission.getInstance().getTempGear().equals("001")){
                transmissionGear.setText("5");
            }else if(Transmission.getInstance().getTempGear().equals("010")){
                transmissionGear.setText("6");
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                transmissionGear.setText("7");
            }else{
                transmissionGear.setText("8");
            }

            if(Transmission.getInstance().getTempGearRate().equals("00")){
                transmissionGearRate.setText("Short");
            }else if(Transmission.getInstance().getTempGearRate().equals("01")){
                transmissionGearRate.setText("Default");
            }else{
                transmissionGearRate.setText("Long");
            }

            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                transmissionGearSpeed.setText("하");
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")) {
                transmissionGearSpeed.setText("중");
            }else{
                transmissionGearSpeed.setText("상");
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                transmissionGearPower.setText("하");
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                transmissionGearPower.setText("중");
            }else{
                transmissionGearPower.setText("상");
            }

            if(Transmission.getInstance().getTempTransmissionMap().equals("00")){
                transmissionMap.setText("Normal");
            }else if(Transmission.getInstance().getTempTransmissionMap().equals("01")){
                transmissionMap.setText("Sport");
            }else {
                transmissionMap.setText("Track");
            }

        }else{
            transmissionIsOn.setText("OFF");
           findViewById(R.id.transmission_type_layout).setVisibility(View.INVISIBLE);
           findViewById(R.id.transmission_gear_layout).setVisibility(View.INVISIBLE);
           findViewById(R.id.transmission_gear_rate_layout).setVisibility(View.INVISIBLE);
           findViewById(R.id.transmission_speed_layout).setVisibility(View.INVISIBLE);
           findViewById(R.id.transmission_power_layout).setVisibility(View.INVISIBLE);
           findViewById(R.id.transmission_map_layout).setVisibility(View.INVISIBLE);
        }

        if(Sound.getInstance().getTempIsOn().equals("1")){
            soundIsOn.setText("ON");

            if(Sound.getInstance().getTempDriveType().equals("0")){
                soundType.setText("모터");
            }else {

            soundType.setText("엔진");
            }

            if(Sound.getInstance().getTempVolume().equals("00")){
                soundVolume.setText("소");
            }else if(Sound.getInstance().getTempVolume().equals("01")){
                soundVolume.setText("중");
            }else {
                soundVolume.setText("대");
            }

            if(Sound.getInstance().getTempBackVolume().equals("00")){
                backSoundVolume.setText("OFF");
            }else if(Sound.getInstance().getTempBackVolume().equals("01")){
                backSoundVolume.setText("소");
            }else if(Sound.getInstance().getTempBackVolume().equals("10")){
                backSoundVolume.setText("중");
            }else {
                backSoundVolume.setText("대");
            }

            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                backSoundSensitive.setText("저");
            }else{
                backSoundSensitive.setText("고");
            }
        }else{
            soundIsOn.setText("OFF");
            findViewById(R.id.sound_type_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.sound_volume_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.sound_back_volme_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.sound_back_sensitive_layout).setVisibility(View.INVISIBLE);
        }

        if(Drive.getInstance().getTempIsOn().equals("1")){
            driveIsOn.setText("ON");

            if(Drive.getInstance().getTempStiffness().equals("00")){
                driveStiffness.setText("하");
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                driveStiffness.setText("중");
            }else{
                driveStiffness.setText("상");
            }

            if(Drive.getInstance().getTempReducer().equals("00")){
                driveReducer.setText("하");
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                driveReducer.setText("중");
            }else{
                driveReducer.setText("상");
            }
        }else{
            driveIsOn.setText("OFF");
            findViewById(R.id.drive_stiffness_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.drive_reduce_layout).setVisibility(View.INVISIBLE);

        }
    }
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
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.ib_delete_btn:
                mTitle.setText("");
                break;
            case R.id.ib_cancel_btn:
                exit(false);
                finish();
                break;
            case R.id.ib_confirm_btn:
                if(mTitle.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "저장할 설정의 이름이 입력되지 않았습니다. 입력후 다시 확인을 선택해 주십시오!", Toast.LENGTH_SHORT).show();
                } else {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                    loadingDialog.show();
                    if(isEdit==1){
                        signal1 = "101"+" "+ Sound.getInstance().getTempIsOn()+" "+Sound.getInstance().getTempDriveType()+" "+Sound.getInstance().getTempVolume()+" "
                                +Sound.getInstance().getTempBackVolume()+" "+Sound.getInstance().getTempBackSensitive()+ " "+
                                Drive.getInstance().getTempIsOn()+" "+Drive.getInstance().getTempStiffness()+" "+Drive.getInstance().getTempReducer();
                        signal2 = Transmission.getInstance().getTempIsOn()+" "+Transmission.getInstance().getTempType()+" "+Transmission.getInstance().getTempGear()+" "+
                                Transmission.getInstance().getTempGearRate()+" "+Transmission.getInstance().getTempTransmissionSpeed()+" "+Transmission.getInstance().getTempTransmissionPower()+" "+
                                Transmission.getInstance().getTempTransmissionMap();
                        DBUtil.insertDB(getApplicationContext(), mTitle.getText().toString(), Utility.getCurrentDateTime(), signal1, signal2);
                        check = false;
                    }else{

                        signal1 = "101"+" "+ Sound.getInstance().getTempIsOn()+" "+Sound.getInstance().getTempDriveType()+" "+Sound.getInstance().getTempVolume()+" "
                                +Sound.getInstance().getTempBackVolume()+" "+Sound.getInstance().getTempBackSensitive()+ " "+
                                Drive.getInstance().getTempIsOn()+" "+Drive.getInstance().getTempStiffness()+" "+Drive.getInstance().getTempReducer();
                        signal2 = Transmission.getInstance().getTempIsOn()+" "+Transmission.getInstance().getTempType()+" "+Transmission.getInstance().getTempGear()+" "+
                                Transmission.getInstance().getTempGearRate()+" "+Transmission.getInstance().getTempTransmissionSpeed()+" "+Transmission.getInstance().getTempTransmissionPower()+" "+
                                Transmission.getInstance().getTempTransmissionMap();

                        DBUtil.updateDB(getApplicationContext(),editId,mTitle.getText().toString(),signal1,signal2);
                    check = true;
                    }
//                    exit(true);
                    showLoading();
//                    showProgressDialog();
                }
                break;
            default:
                break;
        }

    }

    public void showLoading(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                loadingDialog=null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exit(true);
                    }
                });

            }
        }, 2500);

    }
    /**
     * Show progress dialog
     */
    private void showProgressDialog() {

        if(mProgressDialog == null) {
            mProgressDialog = new Dialog(ParamSettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        }
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mProgressDialog.getWindow().setDimAmount(0.9f);

        mProgressDialog.setContentView(R.layout.blockchain_progress);
        mProgressDialog.setCanceledOnTouchOutside(false); // not in touch with background activity

        mBlockchainValue = (TextView) mProgressDialog.findViewById(R.id.tv_blockchain_value);

        mStep01 = (ImageView) mProgressDialog.findViewById(R.id.step_01);
        mStep02 = (ImageView) mProgressDialog.findViewById(R.id.step_02);
        mStep03 = (ImageView) mProgressDialog.findViewById(R.id.step_03);
        mStep04 = (ImageView) mProgressDialog.findViewById(R.id.step_04);
        mStep05 = (ImageView) mProgressDialog.findViewById(R.id.step_05);



        mProgressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStep01.setBackgroundResource(R.drawable.step_01_on);
                mBlockchainValue.setText("20");
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStep02.setBackgroundResource(R.drawable.step_02_on);
                mBlockchainValue.setText("40");
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStep03.setBackgroundResource(R.drawable.step_03_on);
                mBlockchainValue.setText("60");
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStep04.setBackgroundResource(R.drawable.step_04_on);
                mBlockchainValue.setText("80");
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mStep05.setBackgroundResource(R.drawable.step_05_on);
                mBlockchainValue.setText("100");
                Toast.makeText(getApplicationContext(), "설정이 저장되었습니다 나의 설정에서 확인하실 수 있습니다!", Toast.LENGTH_SHORT).show();
            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                finish();
            }
        }, 5500);

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

    @Override
    public void onBackPressed() {
        exit(false);
    }

    public void exit(boolean change){
        Intent intent = new Intent();
        intent.putExtra("change",change);
        setResult(Constants.REQUEST_EDIT_CUSTOM_RESULT,intent);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        finish();
    }
}
