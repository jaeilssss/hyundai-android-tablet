package com.obigo.hkmotors.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.BackPressCloseHandler;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.custom.AsteriskPasswordTransformationMethod;
import com.obigo.hkmotors.custom.BasicOnKeyboardActionListener;
import com.obigo.hkmotors.custom.CustomKeyboardView;
import com.obigo.hkmotors.module.BaseActivity;

import java.lang.ref.WeakReference;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private final IntroHandler mHandler = new IntroHandler(this);

    // origin widget
    private ImageView mLogo;
    private LinearLayout mLayoutMode;
    private ToggleButton mMode;
    private EditText mPwd;
    private ImageView mPwdChk;
    private ImageView mPwdLine;
    private Keyboard mKeyboard;
    private LinearLayout mLayoutKeyboard;
    private ImageButton mLoginBtn;

    // custom widget or event
    private CustomKeyboardView mKeyboardView;
    private BackPressCloseHandler backPressCloseHandler;

    private String mModeValue= Constants.MODE_EXPERT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "== onCreate");

        // initialize UI
        initUI();

        // exit handler for back button with two times
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "== onResume");

        Constants.DISPLAY_MODE = "INTRO";

        checkPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 메모리 관리하기위해 사용되지만 사용여부에 대하여 고민해봐야 한다.20181030
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
            case R.id.ib_login_btn:
                if(mPwd.getText().toString().equals(mPref.getDPWD()) || mPwd.getText().toString().equals(mPref.getPWD())) {
                    goToMainActivity();
                } else {
                    Toast.makeText(this, "입력하신 비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_pw_setting:
                goToPwdSettingActivity();
                break;
            default:
                // nothing to this
                break;
        }
    }

    /**
     * Event for clicking back button(left or right button on the bottom of the screen)
     */

    @Override
    public void onBackPressed() {

        // if custom keyboard is showing, original view will be shown
        if (mLayoutKeyboard.getVisibility() == View.VISIBLE) {
            mLayoutKeyboard.setVisibility(View.GONE);
            mLogo.setVisibility(View.VISIBLE);
            Utility.setMargins(mLayoutMode, 0, Utility.marginDP(getApplication(), 52), 0, 0);
            return;
        }

        if(backPressCloseHandler.onBackPressed(LoginActivity.this)) {
            Log.d(TAG, "=== Exit application");
            Handler handler = new Handler();
            handler.postDelayed(runnable, 500);
        } else {
            Log.d(TAG, "=== Need one more back for exit application");
        }
    }

    /**
     *  Initialize UI
     */

    private void initUI() {

        mLogo = (ImageView) findViewById(R.id.iv_logo);

        mLayoutMode = (LinearLayout) findViewById(R.id.layout_mode);

        mMode = (ToggleButton) findViewById(R.id.tb_mode);

        mMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                //
                if(!isChecked) {
                    mModeValue = Constants.MODE_EXPERT;
                } else {
                    mModeValue = Constants.MODE_RCDATION;
                }
            }
        });

        mPwd = (EditText) findViewById(R.id.et_pwd);

        mPwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        mPwd.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                showKeyboard();
                return true;
            }
        });

        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    mPwdChk.setVisibility(View.VISIBLE);
                    mPwdLine.setSelected(true);
                    mLoginBtn.setActivated(true);
                    mLoginBtn.setClickable(true);
                } else {
                    mPwdChk.setVisibility(View.GONE);
                    mPwdLine.setSelected(false);
                    mLoginBtn.setActivated(false);
                    mLoginBtn.setClickable(false);
                }
            }
        });

        mPwdChk = (ImageView) findViewById(R.id.iv_pwd_chk);
        mPwdLine = (ImageView) findViewById(R.id.iv_pwd_line);

        ImageButton pwSettingBtn = (ImageButton) findViewById(R.id.ib_pw_setting);
        pwSettingBtn.setOnClickListener(this);

        mKeyboard = new Keyboard(this, R.xml.keyboard);
        mLayoutKeyboard = (LinearLayout) findViewById(R.id.layout_keyboard);
        mKeyboardView = (CustomKeyboardView) findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(this));

        mLoginBtn = (ImageButton) findViewById(R.id.ib_login_btn);
        mLoginBtn.setOnClickListener(this);
        mLoginBtn.setClickable(false);


        // event about edittext outside touching
        RelativeLayout touchInterceptor = (RelativeLayout) findViewById(R.id.touchInterceptor);

        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mPwd.isFocused()) {
                        Rect outRect = new Rect();
                        mPwd.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            mPwd.clearFocus();
                            hideKeyboard();
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * Handler for intro activity for the bluetooth
     */
    private static class IntroHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        public IntroHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
                Log.d(TAG, "== public void handleMessage(Message msg)");
                activity.handleMessage(msg);
            }
        }
    }


    /**
     * Handler to exit or continue after selecting bluetooth on/off
     *
     * @param msg - message for handler
     */
    private void handleMessage(Message msg) {
        Log.d(TAG, "== Handler 에서 호출하는 함수 :: msg.what :: " + msg.what);
        switch (msg.what) {
            case 1:
                Log.d(TAG, "== 앱 을 종료시킨다.");
                //closeDialog();
                finish();
                break;
            case 3:
                Log.d(TAG, "== 블루투스 on 확인되면 이쪽.");
                //closeDialog();
                initData();
                break;
            default :
                break;
        }
    }

    /**
     * Check the permission for the only external storage.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "requestPermissions 를 실행합니다.");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_STORAGE);
            }
            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant
        } else {
            Log.d(TAG, "다음 부분은 항상 허용일 경우에 해당이 됩니다..");

            initData();
        }
    }


    /**
     * Initialize setting value into the prefence
     */
    private void initData() {

        // 블루투스 체크
        Handler hd2 = new Handler(Looper.getMainLooper());
        hd2.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mBluetoothService.getDeviceState()) {
                    // true -- Bluetooth ON
                    // false -- Bluetooth OFF
                    if(!mBluetoothService.bluetoothFlag()) {

                        // 확인, 취소 버튼 여부에 따라 handler로 넘긴다.
                        mBluetoothService.enableBluetooth(mHandler);
                    }
                } else {

                    finish();
                }
            }
        }, 2000);
    }

    /**
     * Finish activity when clicking twice back button continuously
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finishAct();
        }
    };

    /**
     * Show custom keypad with animation
     */
    private void showKeyboard() {
        if (mLayoutKeyboard.getVisibility() == View.GONE) {
            //mLogo.setVisibility(View.GONE);
            Utility.setMargins(mLayoutMode, 0,  Utility.marginDP(getApplication(), 21), 0, 0);
            mLayoutKeyboard.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide custom keypad with animation
     */
    private void hideKeyboard() {
        if (mLayoutKeyboard.getVisibility() == View.VISIBLE) {
            //mLogo.setVisibility(View.VISIBLE);
            Utility.setMargins(mLayoutMode, 0,  Utility.marginDP(getApplication(), 52), 0, 0);
            mLayoutKeyboard.setVisibility(View.GONE);
        }
    }

    /**
     * Move to main activity
     */
    private void goToMainActivity() {

        Intent obd = new Intent(this, MainActivity.class);
        obd.putExtra("mode", mModeValue);
        startActivity(obd);
        finish();
    }

    /**
     * Move to set pwd activity
     */
    private void goToPwdSettingActivity() {

        Intent pwdSetting = new Intent(this, PwdSettingActivity.class);
        startActivity(pwdSetting);
    }
}
