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
    private ImageButton cancel;
    private Dialog mProgressDialog;

    private LinearLayout mLayoutGeneral;
    private LinearLayout mLayoutExport;

    private TextView mBlockchainValue;

    private ImageView mStep01;
    private ImageView mStep02;
    private ImageView mStep03;
    private ImageView mStep04;
    private ImageView mStep05;

    private int pStatus = 0;

    private TextView mTorque;
    private TextView mAcc;
    private TextView mDecel;
    private TextView mBrake;
    private TextView mEnergy;
    private TextView mSpeed;
    private TextView mResponse;

    private String mMode;

    private String mParam;
    private String mResp;

    private String signal1;
    private String signal2;

    private String isInit;
    private String editTitle;
    private int editId;

    private int isEdit;
    private boolean check;

    private LoadingDialog loadingDialog;





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

//               mParam = getIntent().getStringExtra("param");
//        mResp = getIntent().getStringExtra("resp");
//
//
//        String[] params = mParam.split(":");
//
//        mMode = params[0]; // 20181113 지금 프로그램에서는 필요없음
//



         loadingDialog  = new LoadingDialog(ParamSettingActivity.this);
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

//
//
//        mTorque = (TextView) findViewById(R.id.tv_torque_value);
//        mTorque.setText(params[1] + "%");
//
//        mAcc = (TextView) findViewById(R.id.tv_acc_value);
//        mAcc.setText(params[2]);
//
//        mDecel = (TextView) findViewById(R.id.tv_decel_value);
//        mDecel.setText(params[3]);
//
//        mBrake = (TextView) findViewById(R.id.tv_brake_value);
//        mBrake.setText(params[4]);
//
//        mEnergy = (TextView) findViewById(R.id.tv_energy_value);
//        if(params[5].equals("0")) mEnergy.setText("Off");
//        else if(params[5].equals("1")) mEnergy.setText("Eco");
//        else if(params[5].equals("2")) mEnergy.setText("Normal");
//
//        mSpeed = (TextView) findViewById(R.id.tv_speed_value);
//        mSpeed.setText(params[6] + "kph");
//
//        mResponse = (TextView) findViewById(R.id.tv_response_value);
//        mResponse.setText(params[7]);
//
//
//        ImageButton cancelBtn = (ImageButton) findViewById(R.id.ib_cancel_btn);
//        cancelBtn.setOnClickListener(this);
//
        ImageButton confirmBtn = (ImageButton) findViewById(R.id.ib_confirm_btn);
        confirmBtn.setOnClickListener(this);
        cancel = findViewById(R.id.ib_cancel_btn);
        cancel.setOnClickListener(this);


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

        pStatus = 0;

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
        finish();
    }
}
