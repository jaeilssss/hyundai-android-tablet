package com.obigo.hkmotors.common;

import android.content.Context;
import android.widget.Toast;


/**
 * Class for pressing the back button
 */
public class BackPressCloseHandler {

    private final String TAG = "BackPressCloseHandler";

	private long backKeyPressedTime = 0;

    private Context mContext;


    public BackPressCloseHandler(Context context) {
        this.mContext = context;
    }
 
    public boolean onBackPressed(Context context) {
        boolean rtnValue = false;

        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            rtnValue = false;
        } else {
            rtnValue = true;
        }
        return rtnValue;
    }
 
    public void showGuide() {
        Toast.makeText(mContext, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }
}
