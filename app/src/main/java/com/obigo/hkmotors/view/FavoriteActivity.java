package com.obigo.hkmotors.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.obigo.hkmotors.R;
import com.obigo.hkmotors.adapter.FavoriteExpendableListViewAdapter;
import com.obigo.hkmotors.adapter.MyFavoriteRecyclerAdapter;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.OnItemClickListener;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.common.db.DBUtil;
import com.obigo.hkmotors.common.db.data.Database;
import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;
import com.obigo.hkmotors.common.service.ObdService;
import com.obigo.hkmotors.custom.LoadingDialog;
import com.obigo.hkmotors.model.CarData;
import com.obigo.hkmotors.model.Drive;
import com.obigo.hkmotors.model.FavoriteData;
import com.obigo.hkmotors.model.Sound;
import com.obigo.hkmotors.model.Transmission;
import com.obigo.hkmotors.module.BaseActivity;
import com.obigo.hkmotors.module.Network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Class for favorite(my setting) lists
 */
public class FavoriteActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FavoriteActivity";

    private int id;

    private ArrayList<FavoriteData> FavoriteList = new ArrayList<>();
    private ImageView favoriteObdLight;


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

    // ????????? Transmission
    private TextView transmissionIsOn;
    private TextView transmissionType;
    private TextView transmissionGear;
    private TextView transmissionGearRate;
    private TextView transmissionSpeed;
    private TextView transmissionPower;
    private TextView transmissionMap;


    // ????????? Drive
    private TextView driveIsOn;
    private TextView driveStiffness;
    private TextView driveReducer;

    // ?????? Sound

    private TextView soundIsOn;
    private TextView soundDriveType;
    private TextView soundVolume;
    private TextView soundBackVolume;
    private TextView soundBackSensitive;


    private LinearLayout driveLayout;
    private LinearLayout transmissionLayout;
    private LinearLayout soundLayout;


    private TextView carConnectionState;
    private ImageView carConnectionLight;

    private MyFavoriteRecyclerAdapter adapter ;

    private Button delete;
    private Button edit;
    private Button send;
    private ImageButton back;

    private Dialog dialog ;
    private Dialog editDialog;

    LoadingDialog loadingDialog ;

    PrintWriter sendWriter;

    SharedPreferences preference;
    final static String foldername = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Motors";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_favorite);
        preference = (SharedPreferences) getSharedPreferences("Motors",MODE_PRIVATE);
        // UI ?????????
        initUI();

    }


    /**
     * Initialize UI
     */
    public void initUI() {

        // OBD Status ??????

//        setOBDMode(Constants.OBD_STATUS);
//        setOBDInitialized(Constants.OBD_INITIALIZED);
        send = findViewById(R.id.favorite_info_send);
        send.setOnClickListener(this);
        addDataFromDatabase();
        rcv = findViewById(R.id.favorite_rcv);
        adapter = new MyFavoriteRecyclerAdapter(FavoriteList);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {

                int temp = adapter.clickIndex;
                adapter.clickIndex = position;
            setDetailData(position);
                adapter.notifyItemChanged(temp);
            adapter.notifyItemChanged(position);

            }
        });


        // ????????? Transmission
         transmissionIsOn = findViewById(R.id.favorite_transmission_is_on);
        transmissionType = findViewById(R.id.favorite_transmission_type);
        transmissionGear = findViewById(R.id.favorite_transmission_gear);
        transmissionGearRate = findViewById(R.id.favorite_transmission_gear_rate);
        transmissionSpeed = findViewById(R.id.favorite_transmission_speed);
        transmissionPower = findViewById(R.id.favorite_transmission_power);
        transmissionMap = findViewById(R.id.favorite_transmission_map);
        transmissionLayout = findViewById(R.id.transmission_layout);


        // ????????? Drive
        driveIsOn = findViewById(R.id.favorite_drive_is_on);
        driveStiffness = findViewById(R.id.favorite_drive_stiffness);
        driveReducer = findViewById(R.id.favorite_drive_reducer);
        driveLayout = findViewById(R.id.drive_layout);

        // ?????? Sound

        soundIsOn = findViewById(R.id.favorite_sound_is_on);
        soundDriveType = findViewById(R.id.favorite_sound_type);
        soundVolume = findViewById(R.id.favorite_sound_volume);
         soundBackVolume = findViewById(R.id.favorite_sound_back_volume);
       soundBackSensitive = findViewById(R.id.favorite_sound_back_sensitive);
       soundLayout = findViewById(R.id.sound_layout);
        if(!FavoriteList.isEmpty()){
            setDetailData(0);
        }


        carConnectionState = findViewById(R.id.ib_obd_set_btn);
        carConnectionLight = findViewById(R.id.iv_favorite_light);

        if(Constants.CONNECTION_STATUS){
            carConnectionLight.setBackgroundResource(R.drawable.ico_light_green);
            carConnectionState.setText("?????? ?????? ON");

        }else{
            carConnectionLight.setBackgroundResource(R.drawable.ico_light_red);
            carConnectionState.setText("???????????? ON");

        }

        dialog =  new Dialog(FavoriteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        editDialog = new Dialog(FavoriteActivity.this);

        editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editDialog.setContentView(R.layout.custom_dialog_edit);


        delete = findViewById(R.id.favorite_info_delete);
        send = findViewById(R.id.favorite_info_send);

        delete.setOnClickListener(this);


        back = findViewById(R.id.ib_favorite_back);
        back.setOnClickListener(this);

    }


    public void setDetailData(int index){
        if(FavoriteList.size()==0){
            onBackPressed();
            Toast.makeText(getApplicationContext(),"????????? ?????????????????????",Toast.LENGTH_SHORT).show();
            return;
        }
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
            soundDriveType.setText("??????");
        }else{
            soundDriveType.setText("??????");
        }

        if(signal1Array[3].equals("00")){
            soundVolume.setText("???");
        }else if(signal1Array[3].equals("01")){
            soundVolume.setText("???");
        }else{
            soundVolume.setText("???");
        }

        if(signal1Array[4].equals("00")){
            soundBackVolume.setText("OFF");
        }else if(signal1Array[4].equals("01")){
            soundBackVolume.setText("???");
        }else if(signal1Array[4].equals("10")) {
            soundBackVolume.setText("???");
        }else{
            soundBackVolume.setText("???");
        }

        if(signal1Array[5].equals("1")){
            soundBackSensitive.setText("???");
        }else{
            soundBackSensitive.setText("???");
        }

        if(signal1Array[6].equals("1")){
            driveIsOn.setText("On");
            driveLayout.setVisibility(View.VISIBLE);
        }else{
            driveIsOn.setText("OFF");
            driveLayout.setVisibility(View.GONE);
        }

        if(signal1Array[7].equals("00")){
            driveStiffness.setText("???");
        }else if(signal1Array[7].equals("01")){
            driveStiffness.setText("???");
        }else{
            driveStiffness.setText("???");
        }

        if(signal1Array[8].equals("00")){
            driveReducer.setText("???");
        }else if(signal1Array[8].equals("01")){
            driveReducer.setText("???");
        }else{
            driveReducer.setText("???");
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
            transmissionSpeed.setText("???");
        }else if(signal2Array[4].equals("01")){
            transmissionSpeed.setText("???");
        }else{
            transmissionSpeed.setText("???");
        }

        if(signal2Array[5].equals("00")){
            transmissionPower.setText("???");
        }else if(signal2Array.equals("01")){
            transmissionPower.setText("???");
        }else{
            transmissionPower.setText("???");
        }

        if(signal2Array[6].equals("00")){
            transmissionMap.setText("Normal");
        }else if(signal2Array[6].equals("01")){
            transmissionMap.setText("Sport");
        }else{
            transmissionMap.setText("Track");
        }

    edit = findViewById(R.id.favorite_info_edit);
        edit.setOnClickListener(this);



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
        intent.putExtra("check",false);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()) {
            // ????????????
            case R.id.ib_favorite_back:
                onBackPressed();

                break;

            case R.id.favorite_info_delete:
                showDialog01();
                break;
            case R.id.favorite_info_edit  :
                showDialog02();
                break;
            case R.id.favorite_info_send:
                sendCarData();

                break;

            default:
                break;
        }

    }

    private void showDialog02(){
        editDialog.show(); // ??????????????? ?????????


        Button noBtn = editDialog.findViewById(R.id.custom_edit_cancel);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????? ?????? ??????
                editDialog.dismiss(); // ??????????????? ??????
            }
        });
        // ??? ??????
        editDialog.findViewById(R.id.custom_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setEditData(adapter.list.get(adapter.clickIndex).getSignal1(),adapter.list.get(adapter.clickIndex).getSignal2());
                        Intent intent = new Intent();
                        intent.putExtra("check",true);
                        intent.putExtra("title",adapter.list.get(adapter.clickIndex).getTitle());
                        intent.putExtra("id",adapter.list.get(adapter.clickIndex).getId());
                        setResult(Constants.REQUEST_FAVORITE,intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"?????? ?????? ?????????",Toast.LENGTH_SHORT).show();
                        editDialog.dismiss();
                    }
                });
            }
        });
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
        labels.add("????????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("?????????");
        labels.add("????????????");

        chart.getXAxis().setTextColor(Color.WHITE);     // change label color
        chart.getXAxis().setTextSize(13);
        chart.getXAxis().setYOffset(0f);
        chart.getXAxis().setXOffset(0f);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
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



            FavoriteData data = new FavoriteData();
            data.setTitle(cursor.getString(Database.CreateDB.TITLE_IDX));
            data.setDate(cursor.getString(Database.CreateDB.DATE_IDX));
            data.setSignal1(cursor.getString(Database.CreateDB.SIGNAL1_IDX));
            data.setSignal2(cursor.getString(Database.CreateDB.SIGNAL2_IDX));
            data.setId(cursor.getInt(Database.CreateDB.ID_IDX));
            FavoriteList.add(data);

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        helper.close();
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

    public void showDialog01(){
        dialog.show(); // ??????????????? ?????????


        Button noBtn = dialog.findViewById(R.id.custom_cancel);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????? ?????? ??????
                dialog.dismiss(); // ??????????????? ??????
            }
        });
        // ??? ??????
        dialog.findViewById(R.id.custom_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBUtil.deleteDB(getApplicationContext(),adapter.list.get(adapter.clickIndex).getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.list.remove(adapter.clickIndex);
                        adapter.clickIndex=0;
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"????????? ?????????????????????",Toast.LENGTH_SHORT).show();
                        setDetailData(0);
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    public void setEditData(String signal1 , String signal2){

        String [] signal1Array = signal1.split(" ");
        String [] signal2Array = signal2.split(" ");


        if(signal1Array[1].equals("1")){
            Sound.getInstance().setTempIsOn("1");

        }else {
            Sound.getInstance().setTempIsOn("0");

        }
        if(signal1Array[2].equals("1")){
            Sound.getInstance().setTempDriveType("1");
        }else{
            Sound.getInstance().setTempDriveType("0");
        }

        if(signal1Array[3].equals("00")){
            Sound.getInstance().setTempVolume("00");
        }else if(signal1Array[3].equals("01")){
            Sound.getInstance().setTempVolume("01");
        }else{
            Sound.getInstance().setTempVolume("10");
        }

        if(signal1Array[4].equals("00")){
            Sound.getInstance().setTempBackVolume("00");
        }else if(signal1Array[4].equals("01")){
            Sound.getInstance().setTempBackVolume("01");
        }else if(signal1Array[4].equals("10")) {
            Sound.getInstance().setTempBackVolume("10");
        }else{
            Sound.getInstance().setTempBackVolume("11");
        }

        if(signal1Array[5].equals("1")){
            Sound.getInstance().setTempBackSensitive("1");
        }else{
            Sound.getInstance().setTempBackSensitive("0");
        }

        if(signal1Array[6].equals("1")){
            Drive.getInstance().setTempIsOn("1");
        }else{
            Drive.getInstance().setTempIsOn("0");
        }

        if(signal1Array[7].equals("00")){
            Drive.getInstance().setTempStiffness("00");
        }else if(signal1Array[7].equals("01")){
            Drive.getInstance().setTempStiffness("01");
        }else{
            Drive.getInstance().setTempStiffness("10");
        }

        if(signal1Array[8].equals("00")){
            Drive.getInstance().setTempReducer("00");
        }else if(signal1Array[8].equals("01")){
            Drive.getInstance().setTempReducer("01");
        }else{
            Drive.getInstance().setTempReducer("10");
        }

        if(signal2Array[0].equals("1")){
            Transmission.getInstance().setTempIsOn("1");
        }else{
            Transmission.getInstance().setTempIsOn("0");
        }

        if(signal2Array[1].equals("00")){
            Transmission.getInstance().setTempType("00");
        }else if(signal2Array[1].equals("01")){
            Transmission.getInstance().setTempType("01");
        }else {
            Transmission.getInstance().setTempType("10");
        }

        if(signal2Array[2].equals("000")){
            Transmission.getInstance().setTempGear("000");
        }else if(signal2Array[2].equals("001")){
            Transmission.getInstance().setTempGear("001");
        }else if(signal2Array[2].equals("010")){
            Transmission.getInstance().setTempGear("010");
        }else if(signal2Array[2].equals("011")){
            Transmission.getInstance().setTempGear("011");
        }else{
            Transmission.getInstance().setTempGear("100");
        }

        if(signal2Array[3].equals("00")){
            Transmission.getInstance().setTempGearRate("00");
        }else if(signal2Array[3].equals("01")){
            Transmission.getInstance().setTempGearRate("01");
        }else{
            Transmission.getInstance().setTempGearRate("10");
        }

        if(signal2Array[4].equals("00")){
            Transmission.getInstance().setTempTransmissionSpeed("00");
        }else if(signal2Array[4].equals("01")){
            Transmission.getInstance().setTempTransmissionSpeed("01");
        }else{
            Transmission.getInstance().setTempTransmissionSpeed("10");
        }

        if(signal2Array[5].equals("00")){
            Transmission.getInstance().setTempTransmissionPower("00");
        }else if(signal2Array.equals("01")){
            Transmission.getInstance().setTempTransmissionPower("01");
        }else{
            Transmission.getInstance().setTempTransmissionPower("10");
        }

        if(signal2Array[6].equals("00")){

            Transmission.getInstance().setTempTransmissionMap("00");
        }else if(signal2Array[6].equals("01")){

            Transmission.getInstance().setTempTransmissionMap("01");
        }else{

            Transmission.getInstance().setTempTransmissionMap("10");
        }


//        if(Constants.OBD_INITIALIZED){
//            if(Transmission.getInstance().getTempIsOn().equals("0") &&
//                    Sound.getInstance().getTempIsOn().equals("0") &&
//                    Drive.getInstance().getTempIsOn().equals("0")){
//                CarData.getInstance().setTempEVMode();
//
//            }else{
//                CarData.getInstance().setTempComfortable();
//                CarData.getInstance().setTempDynamic();
//                CarData.getInstance().setTempEfficiency();
//                CarData.getInstance().setTempLeading();
//                CarData.getInstance().setTempPerformance();
//            }
//
//
//        }else{
//            // ?????? ????????? ????????????? ?????? ?????? ?????? ?????? ????????? ....
//            if(Transmission.getInstance().getTempIsOn().equals("0") &&
//                    Sound.getInstance().getTempIsOn().equals("0") &&
//                    Drive.getInstance().getTempIsOn().equals("0")){
//                CarData.getInstance().setTempEVMode();
//
//            }else{
//                CarData.getInstance().setTempComfortable();
//                CarData.getInstance().setTempDynamic();
//                CarData.getInstance().setTempEfficiency();
//                CarData.getInstance().setTempLeading();
//                CarData.getInstance().setTempPerformance();
//            }
//
//        }


    }
    public void WriteTextFile(String foldername, String filename, String contents){
        try{
            File dir = new File (foldername);
            //???????????? ????????? ????????? ?????????
            if(!dir.exists()){
                dir.mkdir();
            }
            //?????? output stream ??????
            FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, true);
            //????????????
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sendCarData(){

         String tempSignal1 =  FavoriteList.get(adapter.clickIndex).getSignal1();
         String tempSignal2 =  FavoriteList.get(adapter.clickIndex).getSignal2();

        setEditData(tempSignal1,tempSignal2);
        String signal1 =  tempSignal1.replace(" ","");
       String signal2 = tempSignal2.replace(" ","");
        int decimalSignal1 = Integer.parseInt(signal1,2);
        int decimalSignal2 = Integer.parseInt(signal2,2);


        if(Network.getInstance()==null){
            SharedPreferences.Editor editor = preference.edit();

            editor.putString("Signal1",tempSignal1);
            editor.putString("Signal2",tempSignal2);

            editor.commit();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String contents = "Signal1 : " +signal1+"->"+decimalSignal1+"\n"
                    +"Signal2 : "+signal2+"->"+decimalSignal2+"\n";
            String filename = now+ "log.txt";
            WriteTextFile(foldername,filename,contents);
        }else{
            final String data = " { " +
                    "  \"TPCANMsg\" : {      " +
                    "\"DATA\" : ["+decimalSignal1+"," + decimalSignal2+ ", 0, 0, 0, 0, 0, 0 ]," +

                    "  \"ID\" : 1,     " +
                    " \"LEN\" : 2,      " +
                    "\"MSGTYPE\" : 1   },   " +
                    "\"TPCANTimestamp\" : {" +
                    "  \"micros\" : 1,      " +
                    "\"millis\" : 1,      " +
                    "\"millis_overflow\" : 1   " +
                    "}" +
                    "}";

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        if(Network.getInstance().isConnected()){

                            sendWriter = Network.getInstance().sendWriter;

                            sendWriter.println(data);
                            sendWriter.flush();


                        }else{
                            Toast.makeText(getApplicationContext(),"?????? ?????? ????????? ??????????????????",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


        loadingDialog = new LoadingDialog(FavoriteActivity.this,2);
        loadingDialog.show();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setNotTouch();

        Drive.getInstance().update();
        Transmission.getInstance().update();
        Sound.getInstance().update();

        CarData.getInstance().setComfortable();
        CarData.getInstance().setDynamic();
        CarData.getInstance().setEfficiency();
        CarData.getInstance().setLeading();
        CarData.getInstance().setPerformance();

        CarData.getInstance().setTempComfortable();
        CarData.getInstance().setTempDynamic();
        CarData.getInstance().setTempEfficiency();
        CarData.getInstance().setTempLeading();
        CarData.getInstance().setTempPerformance();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.hide();
                loadingDialog.setTouch();

                if(Transmission.getInstance().getTempIsOn().equals("0") &&
                        Sound.getInstance().getTempIsOn().equals("0") &&
                        Drive.getInstance().getTempIsOn().equals("0")){
                    CarData.getInstance().setTempEVMode();
                    CarData.getInstance().setEVMode();
                }



                Toast.makeText(getApplicationContext(),"?????? ????????? ?????????????????????",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("check",false);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        },2500);

    }


    public String setSignal1(){
        return "101"+""+Sound.getInstance().getTempIsOn()+""+
                Sound.getInstance().getTempDriveType()+""+
                Sound.getInstance().getTempVolume()+""+
                Sound.getInstance().getBackVolume()+""+
                Sound.getInstance().getBackSensitive()+""+
                Drive.getInstance().getTempIsOn()+""+
                Drive.getInstance().getTempStiffness()+""+
                Drive.getInstance().getTempReducer();

    }
    public String setSignal2(){
        return Transmission.getInstance().getTempIsOn()+""+
                Transmission.getInstance().getTempType()+""+
                Transmission.getInstance().getTempGear()+""+
                Transmission.getInstance().getTempGearRate()+""+
                Transmission.getInstance().getTempTransmissionSpeed()+""+
                Transmission.getInstance().getTempTransmissionPower()+""+
                Transmission.getInstance().getTempTransmissionMap();

    }


}
