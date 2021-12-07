package com.obigo.hkmotors.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.adapter.FavoriteExpendableListViewAdapter;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.data.Obd2Database;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.service.ObdService;
import com.obigo.hkmotors.model.FavoriteDataListItems;
import com.obigo.hkmotors.module.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for favorite(my setting) lists
 */
public class FavoriteActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FavoriteActivity";

    private ImageView favoriteObdLight;
    private ImageButton obdSetBtn;

    private ObdService mObdsv;

    private FavoriteExpendableListViewAdapter mListAdapter;
    private ListView mExpListView;

    private ImageButton fBackBtn;
    private Button fSendBtn;
    private Button fRemoveBtn;

    private float mRespEcoLevel=2.5f;
    private float mRespMaxPower=2.5f;
    private float mRespAcceration=2.5f;
    private float mRespDeceleration=2.5f;
    private float mRespResponse=2.5f;

    private String[] params;
    private Dialog mProgressDialog;
    private ProgressBar mProgress;
    private TextView mProgressValue;
    private int pStatus = 0;
    private int cnt = 0;


    // right layout
    private TextView torque;
    private TextView acceleration;
    private TextView deceleration;
    private TextView brake;
    private TextView energy;
    private TextView speed;
    private TextView response;
    private RadarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_favorite);


        // UI 초기화
        initUI();

    }


    /**
     * Initialize UI
     */
    public void initUI() {



        // OBD Status 변경
        Log.d(TAG, " ================= OBD Status 변경 :: " + Constants.OBD_STATUS);
//        setOBDMode(Constants.OBD_STATUS);
//        setOBDInitialized(Constants.OBD_INITIALIZED);

    }


    @Override
    protected void onResume() {
        //Constants.DISPLAY_MODE = "FAVORITE";
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        // free list adapter
        if (mListAdapter != null) {
            mListAdapter.recycle();
        }
        */

        Utility.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    /**
     * Event for clicking back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()) {
            // 뒤로가기
            case R.id.ib_favorite_back:
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

//            case R.id.ib_f_send_btn:
//                if(Constants.OBD_STATUS == true) {
//
//                    ArrayList<String> sendCheckedIndex = new ArrayList<String>(mListAdapter.getCheckedId());
//
//                    if (sendCheckedIndex.size() == 0) {
//                        Toast.makeText(getApplicationContext(), "선택된 리스트 정보가 없습니다!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    showProgressDialog();
//
//                    ((MainActivity) MainActivity.mContext).FavoriteResult(params);
//                } else {
//                    Toast.makeText(getApplicationContext(), "App과 OBD가 올바르게 연결되지 않습니다. OBDLink MX와 다시 연결해주세요.", Toast.LENGTH_SHORT).show();
//                    //selectOBD();
//                }

//                break;
//            case R.id.ib_f_remove_btn:
//                ArrayList<String> checkedIndex = new ArrayList<String>(mListAdapter.getCheckedId());
//
//                if(checkedIndex.size() == 0) {
//                    Toast.makeText(getApplicationContext(), "선택된 삭제 리스트 정보가 없습니다!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // remove adapter
//                for(int i=0; i < checkedIndex.size(); i++) {
//                    // mListAdapter.removeGroupItem(checkedIndex.get(i)); // remove group by id
//                    // mListAdapter.removeChildItem(checkedIndex.get(i)); // remove child by id
//                    mListAdapter.removeMitems(checkedIndex.get(i));
//                    mListAdapter.removeCheckedId(checkedIndex.get(i)); // removed index
//                }
//
//                // remove multiple items to database
//                String ids = TextUtils.join(", ", checkedIndex.toArray(new String[checkedIndex.size()])); // ids : 3,2,0
//                DBUtil.deleteMultipleDB(getApplicationContext(), ids);
//
//                mListAdapter.notifyDataSetChanged();
//
//                // if the count of group is zero, activity should be finish
//                if(mListAdapter.getCount() == 0) finish();
//
//                break;
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
            default:
                break;
        }

    }


    private void showProgressDialog() {

        if(mProgressDialog == null) {
            mProgressDialog = new Dialog(FavoriteActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
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
    }

    /**
     * Dismiss progress dialog
     */
    private void dismissProgressDialog() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            finish();
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

        chart.clear();

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

        //ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();
        //dataSets.add(dataset_comp);

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("최대출력");
        labels.add("가속도");
        labels.add("감속도");
        labels.add("응답성");
        labels.add("에코레벨");

        chart.getXAxis().setTextColor(Color.WHITE);     // change label color
        chart.getXAxis().setTextSize(13);
        chart.getXAxis().setYOffset(0f);
        chart.getXAxis().setXOffset(0f);
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value % labels.size());
            }
        });

        chart.getXAxis().setTextColor(Color.WHITE);     // change label color
        chart.getXAxis().setTextSize(10);

        chart.getYAxis().setAxisMinimum(0f);
        chart.getYAxis().setAxisMaximum(5f);
        chart.getYAxis().setEnabled(false);             // disable number

        List<IRadarDataSet> dataSetList = new ArrayList<IRadarDataSet>();
        dataSetList.add(dataset_comp);

        RadarData data = new RadarData(dataSetList);
        chart.setData(data);

        chart.getLegend().setEnabled(false);            // remove legend
        // chart.setDescriptionColor(Color.TRANSPARENT);   // remove description

        chart.setTouchEnabled(false);                   // disable touch
        chart.invalidate();
    }

    /**
     * add the data of database to the adapter
     */
    private void addDataFromDatabase() {

        Obd2DBOpenHelper helper = new Obd2DBOpenHelper(getApplicationContext());
        helper.open();
        Cursor cursor = helper.getAll();
        for(int i=0; i < cursor.getCount(); i ++) {
            cursor.moveToNext();

            FavoriteDataListItems items = new FavoriteDataListItems();
            items.setId(String.valueOf(cursor.getInt(Obd2Database.CreateDB.ID_IDX)));
            items.setDate(cursor.getString(Obd2Database.CreateDB.DATE_IDX));
            items.setTitle(cursor.getString(Obd2Database.CreateDB.TITLE_IDX));
            items.setParam(cursor.getString(Obd2Database.CreateDB.PARAM_IDX));
            items.setResp(cursor.getString(Obd2Database.CreateDB.RESP_IDX));
            items.setChecked(false);
            mListAdapter.addItem(items);  // unique id

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        helper.close();
    }
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

    private void setOBDInitialized(boolean switchFlag) {

        if(switchFlag) {
            favoriteObdLight.setBackgroundResource(R.drawable.ico_light_green);
        } else {
            favoriteObdLight.setBackgroundResource(R.drawable.ico_light_red);
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
}
