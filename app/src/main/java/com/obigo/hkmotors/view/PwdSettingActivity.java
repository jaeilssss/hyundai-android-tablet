package com.obigo.hkmotors.view;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.custom.CustomKeyboardView;
import com.obigo.hkmotors.module.BaseActivity;


/**
 * Class for the set of password
 */
public class PwdSettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PwdSettingActivity";

    // origin widget
    private TextView mNewPw;
    private ImageView mNewPwChk;
    private ImageView mNewPwLine;

    private TextView mPwConfirm;
    private boolean editState = true;

    private ImageView mPwConfirmChk;
    private ImageView mPwConfirmLine;

    private TextView mTextPwd1;
    private TextView mTextPwd2;
    private TextView mTextPwd3;
    private TextView mTextPwd4;
    private TextView mTextPwd5;
    private TextView mTextPwd6;
    private TextView mTextPwd7;
    private TextView mTextPwd8;
    private TextView mTextPwd9;
    private TextView mTextPwd0;
    private TextView mTextPwdDel;

    private ImageButton mSaveBtn;

    private Keyboard mKeyboard;

    // custom widget or event
    private CustomKeyboardView mKeyboardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_setting);
        Log.d(TAG, "== onCreate");

        // initialize UI
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.DISPLAY_MODE = "PWDSET";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Utility.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    /**
     * Event for clicking
     *
     * @param v - view to click
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_save_btn:
                if(mNewPw.getText().length() == 4) {
                    mPref.setPWD(mNewPw.getText().toString());
                    Toast.makeText(getApplicationContext(), "신규 비밀번호가 저장되었습니다!!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "신규 비밀번호는 4자리 숫자입니다!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.et_new_pw:
                editState = true;
                break;

            case R.id.et_pw_confirm:

                editState = false;
                break;

            case R.id.tv_pwd_1:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "1");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "1");
                }
                break;

            case R.id.tv_pwd_2:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "2");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "2");
                }
                break;

            case R.id.tv_pwd_3:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "3");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "3");
                }
                break;

            case R.id.tv_pwd_4:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "4");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "4");
                }
                break;

            case R.id.tv_pwd_5:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "5");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "5");
                }
                break;

            case R.id.tv_pwd_6:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "6");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "6");
                }
                break;

            case R.id.tv_pwd_7:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "7");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "7");
                }
                break;

            case R.id.tv_pwd_8:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "8");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "8");
                }
                break;

            case R.id.tv_pwd_9:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "9");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "9");
                }
                break;

            case R.id.tv_pwd_0:

                if(editState){
                    mNewPw.setText(mNewPw.getText() + "0");
                }else{
                    mPwConfirm.setText(mPwConfirm.getText() + "0");
                }
                break;

            case R.id.tv_pwd_del:


                if(editState){
                    String txt = mNewPw.getText().toString();
                    mNewPw.setText(txt.substring(0, txt.length()-1));
                }else{
                    String txt = mPwConfirm.getText().toString();
                    mPwConfirm.setText(txt.substring(0, txt.length()-1));
                }
                break;

            default:
                // nothing to this
                break;
        }
    }

    /**
     * Change focus
     *
     * @param field - EditText view
     */
    private void changeFocus(EditText field){
        field.setFocusable(true);
        field.setFocusableInTouchMode(true);
        field.requestFocus();
    }

    /**
     *  Initialize UI
     */
    private void initUI() {

        mNewPw = (TextView) findViewById(R.id.et_new_pw);
        //mNewPw.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mNewPwChk = (ImageView) findViewById(R.id.iv_new_pw_chk);

        mNewPw.setOnClickListener(this);
/*
        mNewPw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mPwConfirm.getText().length() > 0 && mPwConfirm.getText().length() != 4) {
                    Utility.toast(getApplication(), "신규 비밀번호 확인은 4자리 숫자로 되어 있습니다!", 500);
                } else {
                    changeFocus(mNewPw);
                }
                return true;
            }
        });*/
/*
        // TODO :
        // currently, cursor is not visible
        // so I can't know where the cursor is
        mNewPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 4) {

                    if(mNewPw.getText().toString().equals(Constants.D_PWD)) {
                        mNewPw.setText("");
                        mWrongPw.setVisibility(View.GONE);

                        mNewPwChk.setVisibility(View.GONE);
                        mNewPwLine.setSelected(false);
                        mSaveBtn.setActivated(false);
                        mSaveBtn.setClickable(false);
                        Utility.toast(getApplication(), "관리자용 패스워드는 사용하실 수 없습니다", 1000);
                        return;
                    }

                    if(mPwConfirm.getText().length() == 4) {
                        if(mPwConfirm.getText().toString().equals(mNewPw.getText().toString())) {
                            mWrongPw.setVisibility(View.GONE);
                            mSaveBtn.setActivated(true);
                            mSaveBtn.setClickable(true);
                        } else {
                            mWrongPw.setVisibility(View.VISIBLE);
                            mSaveBtn.setActivated(false);
                            mSaveBtn.setClickable(false);
                        }
                    }

                    mNewPwChk.setVisibility(View.VISIBLE);
                    mNewPwLine.setSelected(true);
                } else {
                    mWrongPw.setVisibility(View.GONE);

                    mNewPwChk.setVisibility(View.GONE);
                    mNewPwLine.setSelected(false);
                    mSaveBtn.setActivated(false);
                    mSaveBtn.setClickable(false);
                }
            }
        });*/

        mPwConfirm = (TextView) findViewById(R.id.et_pw_confirm);
        //mPwConfirm.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPwConfirmChk = (ImageView) findViewById(R.id.iv_pw_confirm_chk);

        mPwConfirm.setOnClickListener(this);
/*
        mPwConfirm.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mNewPw.getText().length() > 0 && mNewPw.getText().length() != 4) {
                    Utility.toast(getApplication(), "패스워드는 4자리 숫자로 되어 있습니다!", 500);
                } else {
                    changeFocus(mPwConfirm);
                }
                return true;
            }
        });*/

        /*
        mPwConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {

                    if(mNewPw.getText().length() == 4) {
                        if(mNewPw.getText().toString().equals(mPwConfirm.getText().toString())) {
                            mWrongPw.setVisibility(View.GONE);
                            mSaveBtn.setActivated(true);
                            mSaveBtn.setClickable(true);
                        } else {
                            mWrongPw.setVisibility(View.VISIBLE);
                            mSaveBtn.setActivated(false);
                            mSaveBtn.setClickable(false);
                        }
                    }
                    mPwConfirmChk.setVisibility(View.VISIBLE);
                    mPwConfirmLine.setSelected(true);
                } else {
                    mWrongPw.setVisibility(View.GONE);

                    mPwConfirmChk.setVisibility(View.GONE);
                    mPwConfirmLine.setSelected(false);
                    mSaveBtn.setActivated(false);
                    mSaveBtn.setClickable(false);
                }
            }
        });*/


        ImageButton backBtn = (ImageButton) findViewById(R.id.ib_back);
        backBtn.setOnClickListener(this);

        mSaveBtn = (ImageButton) findViewById(R.id.ib_save_btn);
        mSaveBtn.setOnClickListener(this);
        //mSaveBtn.setClickable(false);

        // handle for custom keyboard
        /*
        mKeyboard = new Keyboard(this, R.xml.keyboard);
        mKeyboardView = (CustomKeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(this));
*/
        mTextPwd1 = (TextView) findViewById(R.id.tv_pwd_1);
        mTextPwd2 = (TextView) findViewById(R.id.tv_pwd_2);
        mTextPwd3 = (TextView) findViewById(R.id.tv_pwd_3);
        mTextPwd4 = (TextView) findViewById(R.id.tv_pwd_4);
        mTextPwd5 = (TextView) findViewById(R.id.tv_pwd_5);
        mTextPwd6 = (TextView) findViewById(R.id.tv_pwd_6);
        mTextPwd7 = (TextView) findViewById(R.id.tv_pwd_7);
        mTextPwd8 = (TextView) findViewById(R.id.tv_pwd_8);
        mTextPwd9 = (TextView) findViewById(R.id.tv_pwd_9);
        mTextPwd0 = (TextView) findViewById(R.id.tv_pwd_0);
        mTextPwdDel = (TextView) findViewById(R.id.tv_pwd_del);
        mTextPwd1.setOnClickListener(this);
        mTextPwd2.setOnClickListener(this);
        mTextPwd3.setOnClickListener(this);
        mTextPwd5.setOnClickListener(this);
        mTextPwd4.setOnClickListener(this);
        mTextPwd5.setOnClickListener(this);
        mTextPwd6.setOnClickListener(this);
        mTextPwd7.setOnClickListener(this);
        mTextPwd8.setOnClickListener(this);
        mTextPwd9.setOnClickListener(this);
        mTextPwd0.setOnClickListener(this);
        mTextPwdDel.setOnClickListener(this);

        if(mPref.getDPWD() != null) {
            System.out.println("-------");
            System.out.println(mPref.getDPWD());
            Toast.makeText(this, "이미 등록한 비밀번호가 있습니다. 초기화하고 재등록 하시겠습니까?", Toast.LENGTH_SHORT).show();
        }
    }
}