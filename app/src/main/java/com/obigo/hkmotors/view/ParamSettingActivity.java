package com.obigo.hkmotors.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.obigo.hkmotors.module.BaseActivity;


/**
 * Class for the set of parameters and responses
 */
public class ParamSettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ParamSettingActivity";

    private TextView mTitle;
    private ImageButton mDeleteBtn;

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
//        mTitle = (EditText) findViewById(R.id.et_title);
//        mTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if(s.length() > 0) {
//                    mDeleteBtn.setVisibility(View.VISIBLE);
//                } else {
//                    mDeleteBtn.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        mDeleteBtn = (ImageButton) findViewById(R.id.ib_delete_btn);
//        mDeleteBtn.setOnClickListener(this);
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
//        ImageButton confirmBtn = (ImageButton) findViewById(R.id.ib_confirm_btn);
//        confirmBtn.setOnClickListener(this);

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
                finish();
                break;
            case R.id.ib_confirm_btn:
                if(mTitle.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "저장할 설정의 이름이 입력되지 않았습니다. 입력후 다시 확인을 선택해 주십시오!", Toast.LENGTH_SHORT).show();
                } else {
                    DBUtil.insertDB(getApplicationContext(), mTitle.getText().toString(), Utility.getCurrentDateTime(), mParam, mResp);

/*
                    try {

                        AutoledgerClient autoledgerClient = new AutoledgerClient(); // 블록체인 클라이언트 객체 생성
                        String blockchainEndPoint = "http://10.6.220.103:3000"; // 블록체인 Endpoint
                        String contractAddress = "189zBY3A5g5Ry5PqbrNPH7amCgXsN9SFDt"; // 스마트 컨트랙트 주소

                        autoledgerClient.connectBlockchain(blockchainEndPoint); // 블록체인에 연결 (연결 후 스마트컨트랙트 호출 가능)

                        */
/*
                         * 스마트컨트랙트 execute
                         *     executeSmartContract 함수는 블록체인의 state database를 변경하는 경우 사용합니다. ex) 저장, 수정, 삭제
                         *     블록체인의 상태를 변경하므로, 트랜잭션을 생성하고 트랜잭션 ID를 리턴받습니다.
                         *//*

                        int mt_max_torq = 0;    //모터최대토크
                        int acc = 0;            //발진가속감
                        int decel = 0;          //감속감
                        int reg_brake = 0;      //회생제동량
                        int ch_engy = 0;        //냉난방 에너지
                        int max_sped_limt = 0;  //최고속도 제한
                        int rspns = 0;          //응답성

                        String executionCode = "call(\"setDrivingInfo\", "
                                + mt_max_torq + ", "
                                + acc + ", "
                                + decel + ", "
                                + reg_brake + ", "
                                + ch_engy + ", "
                                + max_sped_limt + ", "
                                + rspns + ")"; // SmartContract Execute 함수 실행 코드
                        String privateKey = "KwEo9ia9YaTDqE6PByBCBPDLspFsRaS4V8P2xHbpfDyi3Diy2DM6"; // 블록체인 지갑 개인키
                        String executedTransactionId = autoledgerClient.executeSmartContract(executionCode, privateKey, contractAddress); // execute 실행
                        Log.d(TAG, "트랜잭션 ID : " + executedTransactionId);

                        // 데이터가 저장되어있는지 확인 요청
                        Transaction transaction = autoledgerClient.getTransaction(executedTransactionId);
                        Log.d(TAG, "0이 아니면 저장완료 : " + transaction);

                    }catch (Exception e){

                        Log.d(TAG, "연결안될때 : " + e);
                    }
*/

                    showProgressDialog();
                }
                break;
            default:
                break;
        }

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
}
