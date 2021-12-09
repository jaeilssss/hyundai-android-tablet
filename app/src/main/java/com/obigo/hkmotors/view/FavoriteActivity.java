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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.obigo.hkmotors.adapter.MyFavoriteRecyclerAdapter;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.OnItemClickListener;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.data.Obd2Database;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.service.ObdService;
import com.obigo.hkmotors.model.FavoriteData;
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

    private ArrayList<FavoriteData> FavoriteList = new ArrayList<>();
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
    private RecyclerView rcv;

    // 변속기 Transmission
    private TextView transmissionIsOn;
    private TextView transmissionType;
    private TextView transmissionGear;
    private TextView transmissionGearRate;
    private TextView transmissionSpeed;
    private TextView transmissionPower;
    private TextView transmissionMap;


    // 구동축 Drive
    private TextView driveIsOn;
    private TextView driveStiffness;
    private TextView driveReducer;

    // 음향 Sound

    private TextView soundIsOn;
    private TextView soundDriveType;
    private TextView soundVolume;
    private TextView soundBackVolume;
    private TextView soundBackSensitive;


    private LinearLayout driveLayout;
    private LinearLayout transmissionLayout;
    private LinearLayout soundLayout;


    private ImageButton obdState;
    private ImageView obdLight;

    private MyFavoriteRecyclerAdapter adapter ;
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
        addDataFromDatabase();
        rcv = findViewById(R.id.favorite_rcv);
        adapter = new MyFavoriteRecyclerAdapter(FavoriteList);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {
            setDetailData(position);

            }
        });


        // 변속기 Transmission
         transmissionIsOn = findViewById(R.id.favorite_transmission_is_on);
        transmissionType = findViewById(R.id.favorite_transmission_type);
        transmissionGear = findViewById(R.id.favorite_transmission_gear);
        transmissionGearRate = findViewById(R.id.favorite_transmission_gear_rate);
        transmissionSpeed = findViewById(R.id.favorite_transmission_speed);
        transmissionPower = findViewById(R.id.favorite_transmission_power);
        transmissionMap = findViewById(R.id.favorite_transmission_map);
        transmissionLayout = findViewById(R.id.transmission_layout);


        // 구동축 Drive
        driveIsOn = findViewById(R.id.favorite_drive_is_on);
        driveStiffness = findViewById(R.id.favorite_drive_stiffness);
        driveReducer = findViewById(R.id.favorite_drive_reducer);
        driveLayout = findViewById(R.id.drive_layout);

        // 음향 Sound

        soundIsOn = findViewById(R.id.favorite_sound_is_on);
        soundDriveType = findViewById(R.id.favorite_sound_type);
        soundVolume = findViewById(R.id.favorite_sound_volume);
         soundBackVolume = findViewById(R.id.favorite_sound_back_volume);
       soundBackSensitive = findViewById(R.id.favorite_sound_back_sensitive);
       soundLayout = findViewById(R.id.sound_layout);
        if(!FavoriteList.isEmpty()){
            setDetailData(0);
        }


        obdState = findViewById(R.id.ib_obd_set_btn);
        obdLight = findViewById(R.id.iv_favorite_light);

        if(Constants.OBD_INITIALIZED){
            obdLight.setBackgroundResource(R.drawable.ico_light_green);
            obdState.setBackgroundResource(R.drawable.img_tit_04);

        }else{
            obdLight.setBackgroundResource(R.drawable.ico_light_red);
            obdState.setBackgroundResource(R.drawable.img_tit_03);

        }


    }


    public void setDetailData(int index){
        String signal1 = FavoriteList.get(index).getSignal1();
        String signal2 = FavoriteList.get(index).getSignal2();

        String [] signal1Array = signal1.split(" ");
        String [] signal2Array = signal2.split(" ");

        if(signal1Array[1].equals("1")){
            soundIsOn.setText("ON");
           soundLayout.setVisibility(View.VISIBLE);

        }else {
            soundIsOn.setText("OFF");
            soundLayout.setVisibility(View.GONE);

        }
        if(signal1Array[2].equals("1")){
            soundDriveType.setText("엔진");
        }else{
            soundDriveType.setText("모터");
        }

        if(signal1Array[3].equals("00")){
            soundVolume.setText("소");
        }else if(signal1Array[3].equals("01")){
            soundVolume.setText("중");
        }else{
            soundVolume.setText("대");
        }

        if(signal1Array[4].equals("00")){
            soundBackVolume.setText("OFF");
        }else if(signal1Array[4].equals("01")){
            soundBackVolume.setText("소");
        }else if(signal1Array[4].equals("10")) {
            soundBackVolume.setText("중");
        }else{
            soundBackVolume.setText("대");
        }

        if(signal1Array[5].equals("1")){
            soundBackSensitive.setText("고");
        }else{
            soundBackSensitive.setText("저");
        }

        if(signal1Array[6].equals("1")){
            driveIsOn.setText("On");
            driveLayout.setVisibility(View.VISIBLE);
        }else{
            driveIsOn.setText("OFF");
            driveLayout.setVisibility(View.GONE);
        }

        if(signal1Array[7].equals("00")){
            driveStiffness.setText("하");
        }else if(signal1Array[7].equals("01")){
            driveStiffness.setText("중");
        }else{
            driveStiffness.setText("상");
        }

        if(signal1Array[8].equals("00")){
            driveReducer.setText("하");
        }else if(signal1Array[8].equals("01")){
            driveReducer.setText("중");
        }else{
            driveReducer.setText("상");
        }

        if(signal2Array[0].equals("1")){
            transmissionIsOn.setText("ON");
            transmissionLayout.setVisibility(View.VISIBLE);
        }else{
            transmissionIsOn.setText("OFF");
            transmissionLayout.setVisibility(View.GONE);
        }

        if(signal2Array[1].equals("00")){
            transmissionType.setText("AT");
        }else if(signal2Array[1].equals("01")){
            transmissionType.setText("DCT");
        }else {
            transmissionType.setText("AMT");
        }

        if(signal2Array[2].equals("000")){
            transmissionGear.setText("4");
        }else if(signal2Array[2].equals("001")){
            transmissionGear.setText("5");
        }else if(signal2Array[2].equals("010")){
            transmissionGear.setText("6");
        }else if(signal2Array[2].equals("011")){
            transmissionGear.setText("7");
        }else{
            transmissionGear.setText("8");
        }

        if(signal2Array[3].equals("00")){
            transmissionGearRate.setText("Short");
        }else if(signal2Array[3].equals("01")){
            transmissionGearRate.setText("Default");
        }else{
            transmissionGearRate.setText("Long");
        }

        if(signal2Array[4].equals("00")){
            transmissionSpeed.setText("하");
        }else if(signal2Array[4].equals("01")){
            transmissionSpeed.setText("중");
        }else{
            transmissionSpeed.setText("상");
        }

        if(signal2Array[5].equals("00")){
            transmissionPower.setText("하");
        }else if(signal2Array.equals("01")){
            transmissionPower.setText("중");
        }else{
            transmissionPower.setText("상");
        }

        if(signal2Array[6].equals("00")){
            transmissionMap.setText("Normal");
        }else if(signal2Array[6].equals("01")){
            transmissionMap.setText("Sport");
        }else{
            transmissionMap.setText("Track");
        }





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

//            FavoriteDataListItems items = new FavoriteDataListItems();
//            items.setId(String.valueOf(cursor.getInt(Obd2Database.CreateDB.ID_IDX)));
//            items.setDate(cursor.getString(Obd2Database.CreateDB.DATE_IDX));
//            items.setTitle(cursor.getString(Obd2Database.CreateDB.TITLE_IDX));
//            items.setParam(cursor.getString(Obd2Database.CreateDB.PARAM_IDX));
//            items.setResp(cursor.getString(Obd2Database.CreateDB.RESP_IDX));
//            items.setChecked(false);
//            mListAdapter.addItem(items);  // unique id


            FavoriteData data = new FavoriteData();
            data.setTitle(cursor.getString(Obd2Database.CreateDB.TITLE_IDX));
            data.setDate(cursor.getString(Obd2Database.CreateDB.DATE_IDX));
            data.setSignal1(cursor.getString(Obd2Database.CreateDB.SIGNAL1_IDX));
            data.setSignal2(cursor.getString(Obd2Database.CreateDB.SIGNAL2_IDX));
            FavoriteList.add(data);

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
