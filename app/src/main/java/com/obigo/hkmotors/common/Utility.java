package com.obigo.hkmotors.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Class for the general functionality
 */
public class Utility {

    private static final String TAG = "Utility";

    /**
     * Convert serial integer to hex (6bytes)
     */
    public static String convertParamIntToHex(int torque, int acc, int decel, int brake, int energy, int speed, int response, int ecoSport) {

        String command = "";

        if(torque <= 15) {
            command +=  "0" + Integer.toHexString(torque);
        } else {
            command += Integer.toHexString(torque);
        }

        // TODO : need to consideration
        command += Integer.toHexString(decel);

        command += Integer.toHexString(acc);

        command += Integer.toHexString(energy);

        command += Integer.toHexString(brake);

        if(speed <= 15) {
            command +=  "0" + Integer.toHexString(speed);
        } else {
            command += Integer.toHexString(speed);
        }

        if(response <= 15) {
            command +=  "0" + Integer.toHexString(response);
        } else {
            command += Integer.toHexString(response);
        }

/*
        if(ecoSport <= 15) {
            command +=  "0" + Integer.toHexString(ecoSport);
        } else {
            command += Integer.toHexString(ecoSport);
        }
*/


        Log.d("*************************** msg : ", command);
        return command;
    }

    /**
     * check null
     */
    public static boolean checkNullSpace(String str) {

        boolean rtnStr;
        if(str == null || (str.length()==0)) {
            rtnStr = false;
        }
        else {
            rtnStr = true;
        }
        return rtnStr;
    }

    /**
     * To make date to the format like 2017:09:27:17:35:32
     */
    public static String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
        Date date = new Date( );
        return formatter.format( date );
    }

    /**
     * Release and Recycle bitmap at the image view when view is exited
     *
     * @param view - view to release and recycle
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void recursiveRecycle(View view) {

        if (view == null) return;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(null);
        } else {
            view.setBackground(null);
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                recursiveRecycle(group.getChildAt(i));
            }

            if (!(view instanceof AdapterView)) {
                group.removeAllViews();
            }
        }

        view = null;

        return;
    }

    /**
     * Set specific timer for the toast
     *
     * @param c - context
     * @param msg - message
     * @param millisec - timer
     */
    static public void toast(Context c, String msg, int millisec) {
        Handler handler = null;
        final Toast[] toasts = new Toast[1];
        for(int i = 0; i < millisec; i+=2000) {
            toasts[0] = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
            toasts[0].show();
            if(handler == null) {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toasts[0].cancel();
                    }
                }, millisec);
            }
        }
    }

    /**
     * Convert dp to sp
     *
     * @param resources - resource
     * @param dp - dp
     * @return
     */
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    /**
     * Convert sp to pixel
     *
     * @param resources - resource
     * @param sp - sp
     * @return
     */
    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * Convert int to dp
     *
     * @param c - context
     * @param sizeInDP - size int
     * @return - converted dp
     */
    static public int marginDP (Context c, int sizeInDP) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, c.getResources().getDisplayMetrics());
    }

    /**
     * Set Margin
     *
     * @param view - view object
     * @param left - left margin
     * @param top - top margin
     * @param right - right margin
     * @param bottom - bottom margin
     */
    static public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /**
     * Get current date & time
     */
    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    /**
     * Get device serial number
     */
    public static String getDeviceSerialNumber() {
        try {
            return (String) Build.class.getField("SERIAL").get(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Check whether network is avaliable or not
     *
     * @param act - activity
     */
    public static boolean isUseNetwork(Activity act) {
        //if(isMobile(act) || isWifi(act) || isWIMAX(act)){
        if(isNetWork(act)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Check network (getNetworkInfo()가 사용되지 않아 getActiveNetworkInfo()으로 네트워크 상태 체크)
     *
     * @param act - activity
     */
    public static boolean isNetWork(Activity act){
        ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        // 네트워크 상태 값
        Log.d(TAG, info.getTypeName());

        if(info != null)
            return info.isConnected();
        else
            return false;
    }

    /*

    *//**
     * Check WiBro(Mobile WiMAX)
     *
     * @param act - activity
     *//*
    public static boolean isWIMAX(Activity act){

        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX) != null
                && cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX).getState() == NetworkInfo.State.CONNECTED){
            return true;
        }else{
            return false;
        }
    }

    *//**
     * Check whether mobile data is connecter or not
     *
     * @param act - activity
     *//*
    public static boolean isMobile(Activity act){
        ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(info != null)
            return info.isConnected();
        else
            return false;
    }

    */
}
