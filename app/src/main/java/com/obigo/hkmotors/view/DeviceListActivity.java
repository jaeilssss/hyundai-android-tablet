package com.obigo.hkmotors.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.adapter.DeviceListViewAdapter;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.model.DeviceDataListItems;
import com.obigo.hkmotors.module.BaseActivity;

import java.lang.reflect.Method;
import java.util.Set;

public class DeviceListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DeviceListActivity";

    private BluetoothAdapter mBtAdapter;
    private DeviceListViewAdapter mPairedDevicesArrayAdapter;

    private ImageButton cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.activity_device_list);

        cancelBtn = (ImageButton)findViewById(R.id.insert_phone_num_cancel_btn);
        cancelBtn.setOnClickListener(this);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new DeviceListViewAdapter();

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.obd_list);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            //findViewById(R.id.obd_list_title).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                try {
                    Method m = device.getClass().getMethod("getAlias");
                    Object res = m.invoke(device);
                    if(res != null) {
                        mPairedDevicesArrayAdapter.addItem(res.toString(), device.getAddress()); // alias name
                    } else {
                        mPairedDevicesArrayAdapter.addItem(device.getName(), device.getAddress()); // device name
                    }
                } catch (Exception e) {
                    Log.e(TAG, "paired device name should have it!!", e);
                    mPairedDevicesArrayAdapter.addItem(device.getName(), device.getAddress());
                }
            }
        } else {
            String noDevices = "No devices have been paired";
            mPairedDevicesArrayAdapter.addItem(noDevices, "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.DISPLAY_MODE = "DEVICE";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);

        Utility.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "============================== 암것도 안함!! ");
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.insert_phone_num_cancel_btn:
                // Create the result Intent and include the MAC address
                Intent intent = new Intent();

                // Set result and finish this Activity
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {

            Object object = parent.getAdapter().getItem(position);
            DeviceDataListItems obdObject = (DeviceDataListItems) object;
            Log.d(TAG, "== onItemClick // obdObject.getObdListMac() :: " + obdObject.getObdListMac() + " // obdObject.getObdListName() :: " + obdObject.getObdListName());

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra("obdMac", obdObject.getObdListMac());
            intent.putExtra("obdName", obdObject.getObdListName());

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and
     * changes the title when discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // When discovery is finished, change the Activity title
                setProgressBarIndeterminateVisibility(false);
                setTitle("select a device to connect");
            }
        }
    };
}
