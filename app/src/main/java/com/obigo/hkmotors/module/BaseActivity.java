package com.obigo.hkmotors.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;

import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.pref.SharedPreference;
import com.obigo.hkmotors.common.service.BluetoothService;
import com.obigo.hkmotors.dialog.MessageDialog;

/**
 * Class for base activity
 */
public class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";

    private Context context;

    protected BluetoothService mBluetoothService = null;

    public MessageDialog msgDialog;
    public SharedPreference mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkBT();
        context = BaseActivity.this;
        mPref = new SharedPreference(this);

        if(mPref.getDPWD() == null) mPref.setDPWD(Constants.D_PWD);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == Activity.RESULT_CANCELED) {
            //If the request to turn on bluetooth is denied, the app will be finished.
            finish();
            return;
        }

        Log.d(TAG, "== onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Finish activity
     */
    public void finishAct() {
        Log.d(TAG, " finishAct() ");
        Activity activity = (Activity) context;
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * Show message dialog
     *
     * @param msg - message to show
     */
    public void showMessageDialog(String msg) {
        Log.d(TAG, "== showMessageDialog");
        if(msgDialog != null && msgDialog.isShowing()) {
            Log.d(TAG, "== showMessageDialog //dataDialog.isShowing() ==> cancel");
            msgDialog.cancel();
        }
        msgDialog = new MessageDialog(this, msg);
        msgDialog.setCancelable(false);
        msgDialog.show();
    }

    /**
     *  Close message dialog
     */
    public void closeMessageDialog() {
        if(msgDialog != null && msgDialog.isShowing()) {
            Log.d(TAG, "== dataDialog.isShowing() ==> cancel");
            msgDialog.cancel();
        }
    }

    /**
     * Bluetooth Handler
     */
    private final Handler mBluetoothHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    /**
     * Check bluetooth
     */
    protected void checkBT() {
        Log.d(TAG, "== checkBT");
        if(mBluetoothService == null) {
            Log.d(TAG, "== mBluetoothService == null");
            mBluetoothService = new BluetoothService(this, mBluetoothHandler);
        }
    }
}
