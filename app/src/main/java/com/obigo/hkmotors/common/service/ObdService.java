package com.obigo.hkmotors.common.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.github.pires.obd.commands.ObdCommand;
import com.obigo.hkmotors.common.Constants;
import com.obigo.hkmotors.common.obd.AbstractGatewayService;
import com.obigo.hkmotors.common.obd.MockObdGatewayService;
import com.obigo.hkmotors.common.obd.ObdCommandJob;
import com.obigo.hkmotors.common.obd.ObdConfig;
import com.obigo.hkmotors.common.obd.ObdGatewayService;
import com.obigo.hkmotors.common.pref.SharedPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;


/**
 * Class for OBD service
 */
public class ObdService {

    private static final String TAG = "ObdService";

    private static Context mContext;
    private static SharedPreference mPref;
    private static Handler mCallBackMsg;

    private ResultHandler mResultHandler;

    private final BluetoothAdapter mAdapter;
    private AcceptThread mSecureAcceptThread;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    public BluetoothSocket socket;

    private int mState;

    private boolean mUserDisconnect=false;

    private String[] mInitializeCommands;

    private String[] mInitializeCommands2;

    private int mWhichCommand = 0;

    // return message
    private String mPrevMsg="";
    private String mPrevRespS1Msg="";
    private String mPrevRespS2Msg="";
    private String mPrevRespS3Msg="";
    private String mPrevRespS4Msg="";
    private String mPrevRespS5Msg="";
    private String mPrevParamMsg="";

    // response (VCUPs1)
    private String mRespELevel="";
    private String mRespMPower="";
    private String mRespAcc="";
    private String mRespDecel="";
    private String mRespResponse="";

    // response (VCUPs2)
    private String mRespVSpeed="";
    private String mRespLStatus="";
    private String mRespEVReady="";
    private String mRespPaddle="";
    private String mRespDMode="";
    private String mRespPGauge="";
    private String mRespBtrSOC="";

    // response (VCUPs3)
    private String mRespADstn="";
    private String mRespADstnBASE="";
    private String mRespADstnMax="";
    private String mRespADstnMin="";

    // response (VCUPs4)
    private String mRespDPtrnDTEMax="";
    private String mRespDPtrnDTEMin="";
    private String mRespHarmoDTEMax="";
    private String mRespHarmoDTEMin="";
    private String mRespHarmoDTE="";
    private String mRespVSpeedDTEMax="";
    private String mRespVSpeedDTEMin="";

    // response (VCUPs5)
    private String mRespInstFlefc="";
    private String mRespAvgFlefc="";
    private String mRespVSpeedDTE="";
    private String mRespDFeelingDTE="";
    private String mRespDFeelingDTEMax="";
    private String mRespDFeelingDTEMin="";
    private String mRespDPtrnDTE="";

    // parameters
    private String mParamTorque="";
    private String mParamAcc="";
    private String mParamDecel="";
    private String mParamBrake="";
    private String mParamEnergy="";
    private String mParamSpeed="";
    private String mParamResponse="";
    private String mParamDriving=""; // reserved

    private volatile static ObdService uniqueInstance = new ObdService();


    /**
     *  counstructor
     */
    public ObdService() {
        Log.d(TAG, "=== ObdService 생성자 호출");
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = Constants.STATE_NONE;
        mResultHandler = new ResultHandler();

        // ATS0   : S0, S1 ( printing of Spaces off or on)
        // ATL0   : L0, L1 (Linefeeds off or on)
        // ATAT0  : AT0, 1, 2 (Adaptive Timing off, auto1, auto2)
        // ATST10 : ST hh (Set Timeout to hh * 4 msec)  <===== TODO : timing control
        // ATE0   : E0, E1 (Echo off or on)
        // ATPBC008 : user CAN protocol B, set baudrate 500kbps
        // ATSH5F0 : set header "5F0"
        // ATDP   : DP (Describe the current protocol)
        // ATCF5F4 : CAN Filter
        // ATCM5F3 : CAN Mask
        // ATCRA5FX : receive only address header "5F0" ~ "5FF"
        // ATH0   : H0, H1 (Headers off or on)

        // TODO : order is tested
        // if you test other handphone, command order can be changed
        mInitializeCommands = new String[]{"ATAT0", "ATST30", "ATS0", "ATPBC008", "ATE0", "ATSH530", "ATH0", "ATCRA53X", "ATL0"};
        //mInitializeCommands = new String[]{"ATAT0", "ATST30", "ATS0", "ATPBC008", "ATE0", "ATSH530", "ATH0", "ATCRA53X", "ATL0"};
        //mInitializeCommands = new String[]{"ATAT0", "ATST30", "ATS0", "ATPBC008", "ATE0", "ATH0", "ATSH530", "ATCF53A", "ATL0", "ATCM53B"};

        mWhichCommand = 0;
    }

    /**
     *  Create singleton
     *
     * @param context
     * @return
     */
    public static ObdService getInstance(Context context) {
        Log.d(TAG, "=== 핸들러를 제외한 생성자");

        mContext = context;
        mPref = new SharedPreference(mContext);

        if (uniqueInstance == null) {
            Log.d(TAG, "=== 이렇게 하면 처음에만 동기화 된다");
            synchronized (ObdService.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ObdService();
                }
            }
        }
        else {
            Log.d(TAG, "=== 음... 일로오는건 기존에 instance 가 있는 경우.");
        }
        return uniqueInstance;
    }

    /**
     *  Create singleton with handler
     *
     * @param context -  context
     * @param resultHandler - callback handler
     * @return
     */
    public static ObdService getInstance(Context context, Handler resultHandler) {
        Log.d(TAG, "=== 핸들러를 포함한 생성자");

        mContext = context;
        mCallBackMsg = resultHandler;

        mPref = new SharedPreference(mContext);

        if (uniqueInstance == null) {
            Log.d(TAG, "=== 이렇게 하면 처음에만 동기화 된다");

            synchronized (ObdService.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ObdService();
                }
            }
        }
        else {
            Log.d(TAG, "=== 음... 일로오는건 기존에 instance 가 있는 경우.");
        }
        return uniqueInstance;
    }

    /**
     * Initialize parameters
     */
    public void initializeParam() {

        mPrevMsg="";
        mPrevRespS1Msg="";
        mPrevRespS2Msg="";
        mPrevRespS3Msg="";
        mPrevRespS4Msg="";
        mPrevRespS5Msg="";
        mPrevParamMsg="";

        // response (VCUPs1)
        mRespELevel = "";
        mRespMPower="";
        mRespAcc="";
        mRespDecel="";
        mRespResponse="";

        // response (VCUPs2)
        mRespVSpeed="";
        mRespLStatus="";
        mRespEVReady="";
        mRespPaddle="";
        mRespDMode="";
        mRespPGauge="";
        mRespBtrSOC="";

        // response (VCUPs3)
        mRespADstn="";
        mRespADstnBASE="";
        mRespADstnMax="";
        mRespADstnMin="";

        // response (VCUPs4)
        mRespDPtrnDTEMax="";
        mRespDPtrnDTEMin="";
        mRespHarmoDTEMax="";
        mRespHarmoDTEMin="";
        mRespHarmoDTE="";
        mRespVSpeedDTEMax="";
        mRespVSpeedDTEMin="";

        // response (VCUPs5)
        mRespInstFlefc="";
        mRespAvgFlefc="";
        mRespVSpeedDTE="";
        mRespDFeelingDTE="";
        mRespDFeelingDTEMax="";
        mRespDFeelingDTEMin="";
        mRespDPtrnDTE="";

        mParamTorque="";
        mParamAcc="";
        mParamDecel="";
        mParamBrake="";
        mParamEnergy="";
        mParamSpeed="";
        mParamResponse="";

        mPref.setFD(false);
    }

    // =============================================================================================
    private boolean isServiceBound;
    private AbstractGatewayService service;
    private boolean preRequisites = true;
    private String obdMac = "";

    public void queueCommands() {
        if (isServiceBound) {
            for (ObdCommand Command : ObdConfig.getCommands()) {
                service.queueJob(new ObdCommandJob(Command));
            }
        }
    }

    public void doBindService(String obdMac) {
        this.obdMac = obdMac;
        if (!isServiceBound) {
            Log.d(TAG, "Binding OBD service..");
            if (preRequisites) {
                Intent serviceIntent = new Intent(mContext, ObdGatewayService.class);
                mContext.bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);
            } else {
                Intent serviceIntent = new Intent(mContext, MockObdGatewayService.class);
                mContext.bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private void doUnbindService() {
        if (isServiceBound) {
            if (service.isRunning()) {
                service.stopService();
                if (preRequisites) {}
            }
            Log.d(TAG, "Unbinding OBD service..");
            mContext.unbindService(serviceConn);
            isServiceBound = false;
        }
    }

    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, className.toString() + " service is bound");
            isServiceBound = true;
            service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
            service.setContext(mContext);
            Log.d(TAG, "Starting live data");
            try {
                Log.d(TAG, "serviceConn // socket.isConnected() :: " + socket.isConnected());
                service.startService(obdMac, mResultHandler, socket);
                if (preRequisites) {}
            } catch (IOException ioe) {
                Log.e(TAG, "Failure Starting live data");
                doUnbindService();
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        // This method is *only* called when the connection to the service is lost unexpectedly
        // and *not* when the client unbinds (http://developer.android.com/guide/components/bound-services.html)
        // So the isServiceBound attribute should also be set to false when we unbind from the service.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, className.toString() + " service is unbound");
            isServiceBound = false;
        }
    };

    public final Runnable mQueueCommands = new Runnable() {
        public void run() {
            if (service != null && service.isRunning() && service.queueEmpty()) {
                queueCommands();
            }
            new Handler().post(mQueueCommands);
        }
    };


    // =============================================================================================
    /**
     * Connect device
     *
     * @param data - data
     * @param secure - whether it is secure or not
     */
    public void connectDevice(Intent data, boolean secure) {
        Log.d(TAG, "=== connectDevice");

        mPrevMsg="";
        mPrevRespS1Msg="";
        mPrevRespS2Msg="";
        mPrevRespS3Msg="";
        mPrevRespS4Msg="";
        mPrevRespS5Msg="";
        mPrevParamMsg="";

        mUserDisconnect = true;

        // Get the device MAC address
        String address = data.getExtras().getString("obdMac");
        Log.d(TAG, "=== connectDevice // address :: " + address);

        // Get the BluetoothDevice object
        BluetoothDevice device = mAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        connect(device, secure);
    }

    /**
     * Disconnect bluetooth socket connection for handling OBD connection or disconnection.
     *
     * @throws IOException
     */
    public void disconnectDevice() throws IOException {

        Constants.INITIALIZED = false;
        Constants.OBD_STATUS = false;

        mWhichCommand = 0;

        // initialize parameters and response
        initializeParam();

        // set user disconnect
        mUserDisconnect = true;

        if (service != null && service.isRunning()) {
            service.stopService();
            if (preRequisites) {}
        }

        if(socket != null && socket.isConnected()) {
            socket.close();
            socket = null;
        }

        // when IOException on ConnectedThread is happened, connectionLost() will handled
        //connectionLost(true);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == Constants.STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(Constants.STATE_CONNECTING);
    }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
//        Log.d(TAG, "getState :: mState :: " + mState);
        return mState;
    }

    /**
     * Set the current state of the connection
     *
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

//        // Give the new state to the Handler so the UI Activity can update
//        mHandler.obtainMessage(BluetoothChatActivity.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType) {
        Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        setState(Constants.STATE_CONNECTED);
    }

    /**
     * Start the service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        setState(Constants.STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(false);
            mInsecureAcceptThread.start();
        }
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        Log.i(TAG, "connectionFailed");
        Constants.OBD_STATUS = false;

//        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BluetoothChatActivity.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BluetoothChatActivity.TOAST, "Unable to connect device");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);

        // TODO : if multiple connection is needed, enable it. but I don't know whether this position is correct or not
        //        currently hyundai-kia project doesn't need it
        // Start the service over to restart listening mode
        //ObdService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost(boolean user) {
        Log.i(TAG, "connectionLost");
        Constants.OBD_STATUS = false;

        Message msg = user ? Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_USER_DISCONN) : Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_CONN_LOST);
        mCallBackMsg.sendMessage(msg);
        Log.i(TAG, "msg 실행");
        Constants.INITIALIZED = false;
        mWhichCommand = 0;

        // TODO : if multiple connection is needed, enable it. but I don't know whether this position is correct or not
        //        currently hyundai-kia project doesn't need it
        // Start the service over to restart listening mode
        //ObdService.this.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
//        Log.d(TAG, "=== write");
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != Constants.STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure":"Insecure";

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(Constants.NAME_SECURE, Constants.MY_UUID_SECURE);
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(Constants.NAME_INSECURE, Constants.MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != Constants.STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (ObdService.this) {
                        switch (mState) {
                            case Constants.STATE_LISTEN:
                            case Constants.STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice(),mSocketType);
                                break;
                            case Constants.STATE_NONE:
                            case Constants.STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
        }

        public void cancel() {
            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID_SECURE);

                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(Constants.MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);

                // sometimes exception is held,
                // so if it happens, retry it by other method
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    try {
                        Log.e(TAG, "retrying fallback...");

                        tmp = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);

                        Log.e(TAG, "Connected");
                    } catch (Exception e2) {
                        Log.e(TAG, "Couldn't establish Bluetooth connection!");

                        // it should be closed because it is impossilbe to connect
                        try {
                            tmp.close();
                        } catch (IOException e3) {
                            Log.e(TAG, "unable to close() " + mSocketType +
                                    " socket during connection failure", e3);
                        }
                        mmSocket = null;
                        connectionFailed();
                        return;
                    }
                }
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (ObdService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            Log.d(TAG, "ConnectThread // mmSocket.isConnected() :: " + mmSocket.isConnected());
            // 여기서.... obdGateway 에서 사용 가능하도록 복사....
            socket = mmSocket;
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        String s,msg;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    byte[] buffer = new byte[1];
                    int bytes = mmInStream.read(buffer, 0, buffer.length);
                    s = new String(buffer);

                    for(int i = 0; i < s.length(); i++){
                        char x = s.charAt(i);
                        msg = msg + x;
                        // TODO : generally speaking, deliminator will be '>', hyundai kia for CAN protocol received continuouslly same response with deliminator '/r'
                        if (x == 0x3e || x == 0x0d/* add */) {
                            //Log.d(TAG, "##################### : "+msg);
                            mResultHandler.obtainMessage(Constants.MESSAGE_READ, buffer.length, -1, msg).sendToTarget();
                            msg="";
                            break;
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    try {
                        if (mmInStream != null) mmInStream.close();
                        if (mmOutStream != null) mmOutStream.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "close inputstream and outputstream", e2);
                    }
                    // if connectoin is lost
                    connectionLost(mUserDisconnect);
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                mResultHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * Handle the response from OBD Terminal
     */
    private class ResultHandler extends Handler {

        /**
         * handle message
         * @param msg - message
         */
        public  void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    // Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case Constants.STATE_CONNECTED:
                            break;
                        case Constants.STATE_CONNECTING:
                            break;
                        case Constants.STATE_LISTEN:
                        case Constants.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_LOST:
                    Log.d(TAG, "=== 블루투스가 꺼졌습니다???????");
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    // Log.d(TAG, "========== 신규 :: MESSAGE_WRITE :: Command  ::  " + writeMessage.trim());
                    break;
                // 받을때
                case Constants.MESSAGE_READ:
                    // Log.i(TAG, "MESSAGE_READ: " + msg.arg1);
                    // Log.d(TAG, "========== 신규 :: MESSAGE_READ  :: Command  ::  " + msg.obj.toString());
                    compileMessage(msg.obj.toString());
                    break;
                case Constants.MESSAGE_OTHER:
                    // Log.d(TAG, "=== 이리로 오는게 맞는감?");
                    // handling trouble code
                    break;
                default:
                    break;
            }
        }

        /**
         * Compile the response
         *
         * @param msg - response message
         */
        private void compileMessage(String msg) {
            // Log.d(TAG, "compileMessage :: " + msg.toString());

            msg = msg.replace("null","");
            //msg = msg.substring(0,msg.length()-2); // why?
            msg = msg.replaceAll("\n", "");
            msg = msg.replaceAll("\r", "");
            msg = msg.replaceAll(" ", "");

            if(!Constants.INITIALIZED) {
                Log.d(TAG, "========== 신규 :: start initialize");

                if(msg.contains("ELM327")) {
                    msg = msg.replaceAll("ATZ", "");
                    msg = msg.replaceAll("ATI", "");
                }

                String send = mInitializeCommands[mWhichCommand];

                sendMessage(send);

                // when command is quickly sent, somethimes command is not sent.
                // so below code is added
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(mWhichCommand == mInitializeCommands.length-1) {
                    // Log.d(TAG, "========== 신규 :: initialized");

                    mWhichCommand = 0;
                    Constants.INITIALIZED = true;

                    // get simulation default value (번역 : 시뮬레이션 기본 값 가져오기) 여기서 기본값이 아니라 모든값을 가져오도록 설정하는 테스트 필요
                    sendMessage("0000");

                    Handler hd2 = new Handler(Looper.getMainLooper());
                    hd2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            // if send it quickly, obdii doesn't send message to the CAN bus
                            sendMessage("0000");
                        }
                    }, 2000);
                } else {
                    mWhichCommand++;
                }
            } else {

                if (msg.length() == 14) {

                    if(!Constants.OBD_INITIALIZED){
                        Constants.OBD_INITIALIZED = true;

                        mCallBackMsg.sendMessage(Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_OTHER, Constants.OBD_INITIALIZED));
                    }

                    if (mPrevMsg.equals(msg)) {
                        // if it has same message before, do nothing
                    } else {
                        String command = msg.substring(0, 4);       // 기존 (2byte 사용)
                        String commandv2 = msg.substring(0, 2);     // 추가된 정보 (1byte 사용)

                        if (command.equals("FFFF")) { // header : "5F4",  "FFFF" : response (변경) ==> header : "53B",  "FFFF" : response
                            // Log.d(TAG, "=======================> result response : " +  msg);


                            // Constants.COMMAND_MODE != "RESET" 왜 이렇게 비교했는지 확인 필요
                            // same response
                            if(!mPrevRespS1Msg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevRespS1Msg.equals(msg)) return;

                            // 테스트 20181129
                            // if(mPrevParamMsg.substring(4).equals(Constants.SEND_MSG_00) && Constants.COMMAND_MODE.equals("PARAM") ) return;

                            if (!mPrevParamMsg.equals("")){
                                Log.d(TAG, "========== **** : " + mPrevParamMsg.substring(4));
                                Log.d(TAG, "========== ???? : " + Constants.SEND_MSG_01);
                                if(mPrevParamMsg.substring(4).equalsIgnoreCase(Constants.SEND_MSG_01) && Constants.COMMAND_MODE.equals("SEND") ){
                                    Constants.SEND_MSG_01 = "";
                                    return;
                                }
                            }

                            // first default value
                            if(mRespResponse.equals("")|| mRespDecel.equals("") || mRespAcc.equals("") || mRespMPower.equals("") || mRespELevel.equals("")) {
                                mPref.setFD(true);
                            } else {
                                mPref.setFD(false);
                            }

                            mRespELevel = msg.substring(4, 6);      // 8bits
                            mRespMPower = msg.substring(6, 8);      // 8bits
                            mRespAcc = msg.substring(8, 10);        // 8bits
                            mRespDecel = msg.substring(10, 12);     // 8bits
                            mRespResponse = msg.substring(12, 14);  // 8bits

                            // send the response to main activity
                            responseHandler();

                            mPrevRespS1Msg = msg;

                        } else if(command.equals("F0F0")) { // header : "5F3",  "F0F0" : parameters (변경) ==> header : "53A",  "F0F0" : parameters
                            // Log.d(TAG, "=======================> result param : " +  msg);

                            // same parameters
                            if(!mPrevParamMsg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevParamMsg.equals(msg)) return;

                            mParamTorque = msg.substring(4, 6);     // 8bits
                            mParamDecel = msg.substring(6, 7);      // 4bits
                            mParamAcc = msg.substring(7, 8);        // 4bits
                            mParamEnergy = msg.substring(8, 9);    // 4bits
                            mParamBrake = msg.substring(9, 10);      // 4bits
                            mParamSpeed = msg.substring(10, 12);    // 8bits
                            mParamResponse = msg.substring(12, 14);  // 8bits

                            // send the parameters to main activity
                            paramHandler();

                            mPrevParamMsg = msg;

                        } else if (commandv2.equals("FA")) { // header : "53C",  "FA" : response
/*

                            // same response
                            if(!mPrevRespS2Msg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevRespS2Msg.equals(msg)) return;

                            // first default value
                            if(mRespVSpeed.equals("")|| mRespLStatus.equals("") || mRespEVReady.equals("") || mRespPaddle.equals("") || mRespDMode.equals("") || mRespPGauge.equals("") || mRespBtrSOC.equals("")) {
                                mPref.setFD(true);
                            } else {
                                mPref.setFD(false);
                            }
*/

                            // [VCUPs2] 해당 비트는 전체 비트길이에서 다 쓰지 않는데 해당 비트에 맞게들어오는 확인이 필요함
                            mRespVSpeed = msg.substring(2, 4);      // 8bits
                            mRespLStatus = msg.substring(6, 7);     // 4bits
                            mRespEVReady = msg.substring(4, 5);     // 4bits
                            mRespPaddle = msg.substring(7, 8);      // 4bits
                            mRespDMode = msg.substring(6, 7);       // 4bits
                            mRespPGauge = msg.substring(10, 12);     // 8bits
                            mRespBtrSOC = msg.substring(12, 14);    // 8bits

                            // send the response to main activity
                            // responseHandlerVCUPs2();

                            mPrevRespS2Msg = msg;

                            parseData_53C(msg.substring(2));

                        } else if (commandv2.equals("FB")) { // header : "53D",  "FB" : response

                            // same response
                            if(!mPrevRespS3Msg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevRespS3Msg.equals(msg)) return;

                            mRespADstn = msg.substring(2, 6);      // 12bits
                            mRespADstnBASE = msg.substring(6, 10);  // 12bits
                            mRespADstnMax = msg.substring(6, 10);  // 12bits
                            mRespADstnMin = msg.substring(6, 10);  // 12bits

                            // send the response to main activity
                            // responseHandlerVCUPs3();

                            mPrevRespS3Msg = msg;

                            parseData_53D(msg.substring(2));

                        } else if (commandv2.equals("FC")) { // header : "53E",  "FC" : response
                            // Log.d(TAG, "=======================> result response : " +  msg);

                            // same response
                            if(!mPrevRespS4Msg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevRespS4Msg.equals(msg)) return;

                            // [VCUPs4]
                            mRespDPtrnDTEMax = msg.substring(2, 4);     // 8bits
                            mRespDPtrnDTEMin = msg.substring(4, 6);     // 8bits
                            mRespHarmoDTEMax = msg.substring(6, 7);     // 8bits
                            mRespHarmoDTEMin = msg.substring(7, 8);    // 8bits
                            mRespHarmoDTE = msg.substring(8, 10);       // 8bits
                            mRespVSpeedDTEMax = msg.substring(10, 12);  // 8bits
                            mRespVSpeedDTEMin = msg.substring(12, 14);  // 8bits

                            // send the response to main activity
                            // responseHandlerVCUPs4();

                            mPrevRespS4Msg = msg;

                            parseData_53E(msg.substring(2));

                        } else if (commandv2.equals("FD")) { // header : "53F",  "FD" : response

                            // same response
                            //if(!mPrevRespS5Msg.equals("") && Constants.COMMAND_MODE != "RESET" && mPrevRespS5Msg.equals(msg)) return;

                            // [VCUPs5]
                            mRespInstFlefc = msg.substring(2, 4);         // 8bits
                            mRespAvgFlefc = msg.substring(4, 6);          // 8bits
                            mRespVSpeedDTE = msg.substring(6, 8);         // 8bits
                            mRespDFeelingDTE = msg.substring(8, 10);      // 8bits
                            mRespDFeelingDTEMax = msg.substring(10, 11);  // 4bits
                            mRespDFeelingDTEMin = msg.substring(11, 12);  // 4bits
                            mRespDPtrnDTE = msg.substring(12, 14);  // 8bits

                            // send the response to main activity
                            // responseHandlerVCUPs5();

                            parseData_53F(msg.substring(2));

                            mPrevRespS5Msg = msg;
/*
                            Handler hd2 = new Handler(Looper.getMainLooper());
                            hd2.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // if send it quickly, obdii doesn't send message to the CAN bus
                                    sendMessage("ATSH530");
                                }
                            }, 500);
                            */
                        }


                        // to check same message
                        mPrevMsg = msg;
                    }

                // message가 중단되는 경우 다시 재요청 한다(실시간 정보가 필요로 하여)
                } else if(msg.equals("STOPPED") || msg.equals("BUFFERFULL")){

                    if(Constants.OBD_INITIALIZED){

                        Constants.OBD_INITIALIZED = false;

                        mCallBackMsg.sendMessage(Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_OTHER, Constants.OBD_INITIALIZED));
                    }

                    Handler hd2 = new Handler(Looper.getMainLooper());

                    // 많은 송수신은 부담되기 시간을 3초로 간격을 준다(필요시 수정)
                    hd2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            // if send it quickly, obdii doesn't send message to the CAN bus
                            sendMessage("0000");
                        }
                    }, 500);
                } else {

                    if(Constants.OBD_INITIALIZED){

                        Constants.OBD_INITIALIZED = false;

                        mCallBackMsg.sendMessage(Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_OTHER, Constants.OBD_INITIALIZED));

                    }

                }
            }
        }

        /**
         * Sends a message.
         *
         * @param message - the string of text to send.
         */
        public void sendMessage(String message) {

            // Log.d(TAG, "=======================> sendMessage : " + message);

            // Check that we're actually connected before trying anything
            if (getState() != Constants.STATE_CONNECTED) {
                Toast.makeText(mContext, "You are not connected to a device", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check that there's actually something to send
            if (message.length() > 0) {
                message = message + "\r";
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                write(send);
            }
        }
    }

    /**
     * Send response to main activity
     */
    public void responseHandler() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Float> rValue = new HashMap<String, Float>();
            rValue.put(Constants.RESP_ECO_LEVEL, (float)(Long.parseLong(mRespELevel, 16)*0.125));
            rValue.put(Constants.RESP_MAX_POWER, (float)(Long.parseLong(mRespMPower, 16)*0.125));
            rValue.put(Constants.RESP_ACCELERATION, (float)(Long.parseLong(mRespAcc, 16)*0.125));
            rValue.put(Constants.RESP_DECELERATION, (float)(Long.parseLong(mRespDecel, 16)*0.125));
            rValue.put(Constants.RESP_RESPONSE, (float)(Long.parseLong(mRespResponse, 16)*0.125));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs1, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }

    /**
     * Send parameters to main activity
     */
    public void paramHandler() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Integer> rValue = new HashMap<String, Integer>();
            rValue.put(Constants.PARAM_TORQUE, (int)(Long.parseLong(mParamTorque, 16)));
            rValue.put(Constants.PARAM_ACCELERATION, (int)(Long.parseLong(mParamAcc, 16)));
            rValue.put(Constants.PARAM_DECELERATION, (int)(Long.parseLong(mParamDecel, 16)));
            rValue.put(Constants.PARAM_BRAKE, (int)(Long.parseLong(mParamBrake, 16)));
            rValue.put(Constants.PARAM_ENERGY, (int)(Long.parseLong(mParamEnergy, 16)));
            rValue.put(Constants.PARAM_SPEED, (int)(Long.parseLong(mParamSpeed, 16)));
            rValue.put(Constants.PARAM_RESPONSE, (int)(Long.parseLong(mParamResponse, 16)));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_PARAMETER, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }

    public void responseHandlerVCUPs2() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Integer> rValue = new HashMap<String, Integer>();

            rValue.put(Constants.RESP_VEHICLE_SPEED, (int)(Long.parseLong(mRespVSpeed , 16)));
            rValue.put(Constants.RESP_LEVER_STATUS, (int)(Long.parseLong(mRespLStatus , 16)));
            rValue.put(Constants.RESP_EV_READY, (int)(Long.parseLong(mRespEVReady , 16)));
            rValue.put(Constants.RESP_PADDLE_SHIFT, (int)(Long.parseLong(mRespPaddle , 16)));
            rValue.put(Constants.RESP_DRIVER_MODE, (int)(Long.parseLong(mRespDMode , 16)));
            rValue.put(Constants.RESP_POWER_GAUGE, (int)(Long.parseLong(mRespPGauge , 16)));
            rValue.put(Constants.RESP_BATTERY_SOC, (int)(Long.parseLong(mRespBtrSOC , 16)));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs2, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }

    public void responseHandlerVCUPs3() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Integer> rValue = new HashMap<String, Integer>();

            rValue.put(Constants.RESP_AVAILABLE_DISTANCE, (int)(Long.parseLong(mRespADstn , 16)));
            rValue.put(Constants.RESP_AVAILABLE_DISTANCE_BASE, (int)(Long.parseLong(mRespADstnBASE , 16)));
            rValue.put(Constants.RESP_AVAILABLE_DISTANCE_MAX, (int)(Long.parseLong(mRespADstnMax , 16)));
            rValue.put(Constants.RESP_AVAILABLE_DISTANCE_MIN, (int)(Long.parseLong(mRespADstnMin , 16)));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs3, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }

    public void responseHandlerVCUPs4() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Integer> rValue = new HashMap<String, Integer>();

            rValue.put(Constants.RESP_DRIVER_PATTERN_DTE_MAX, (int)(Long.parseLong(mRespDPtrnDTEMax , 16)));
            rValue.put(Constants.RESP_DRIVER_PATTERN_DTE_MIN, (int)(Long.parseLong(mRespDPtrnDTEMin , 16)));
            rValue.put(Constants.RESP_HARMONIZATION_DTE_MAX, (int)(Long.parseLong(mRespHarmoDTEMax , 16)));
            rValue.put(Constants.RESP_HARMONIZATION_DTE_MIN, (int)(Long.parseLong(mRespHarmoDTEMin , 16)));
            rValue.put(Constants.RESP_HARMONIZATION_DTE, (int)(Long.parseLong(mRespHarmoDTE , 16)));
            rValue.put(Constants.RESP_VEHICLE_SPEED_DTE_MAX, (int)(Long.parseLong(mRespVSpeedDTEMax , 16)));
            rValue.put(Constants.RESP_VEHICLE_SPEED_DTE_MIN, (int)(Long.parseLong(mRespVSpeedDTEMin , 16)));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs4, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }

    public void responseHandlerVCUPs5() {

        // only in case of main acivity, send callback message
        if(Constants.DISPLAY_MODE.equals("MAIN")) {
            HashMap<String, Integer> rValue = new HashMap<String, Integer>();

            rValue.put(Constants.RESP_INSTANT_FUEL_EFFICIENCY, (int)(Long.parseLong(mRespInstFlefc , 16)));
            rValue.put(Constants.RESP_AVERAGE_FUEL_EFFICIENCY, (int)(Long.parseLong(mRespAvgFlefc , 16)));
            rValue.put(Constants.RESP_VEHICLE_SPEED_DTE, (int)(Long.parseLong(mRespVSpeedDTE , 16)));
            rValue.put(Constants.RESP_DRIVING_FEELING_DTE, (int)(Long.parseLong(mRespDFeelingDTE , 16)));
            rValue.put(Constants.RESP_DRIVING_FEELING_DTE_MAX, (int)(Long.parseLong(mRespDFeelingDTEMax , 16)));
            rValue.put(Constants.RESP_DRIVING_FEELING_DTE_MIN, (int)(Long.parseLong(mRespDFeelingDTEMin , 16)));
            rValue.put(Constants.RESP_DRIVER_PATTERN_DTE, (int)(Long.parseLong(mRespDPtrnDTE , 16)));

            Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs5, rValue);
            mCallBackMsg.sendMessage(msg);
        }
    }




    private static HashMap parseData_53C(String val) {

        int _53c_1_1 = Integer.decode("0x"+val.substring(0, 2));
        int _53c_1_2 = Integer.decode("0x"+val.substring(2, 4));
        int _53c_1_3 = Integer.decode("0x"+val.substring(4, 6));
        int _53c_1_4 = Integer.decode("0x"+val.substring(6, 8));
        int _53c_1_5 = Integer.decode("0x"+val.substring(8, 10));
        int _53c_1_6 = Integer.decode("0x"+val.substring(10, 12));

        String s_53c_1_1 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_1)));
        String s_53c_1_2 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_2)));
        String s_53c_1_3 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_3)));
        String s_53c_1_4 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_4)));
        String s_53c_1_5 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_5)));
        String s_53c_1_6 = setBinary(String.valueOf(Integer.toBinaryString(_53c_1_6)));

        String bit9_53c_1_val = s_53c_1_1;
        String bit9_53c_2_val = s_53c_1_2.substring(4);
        String bit9_53c_3_val = s_53c_1_2.substring(3,4);
        String bit9_53c_4_val = s_53c_1_3.substring(4);
        String bit9_53c_5_val = s_53c_1_3.substring(1,4);
        String bit9_53c_6_val = s_53c_1_4;
        String bit9_53c_7_val = s_53c_1_5;
        String bit9_53c_8_val = s_53c_1_6;

        int num_53c_1_val = Integer.parseInt(bit9_53c_1_val,2);
        int num_53c_2_val = Integer.parseInt(bit9_53c_2_val,2);
        int num_53c_3_val = Integer.parseInt(bit9_53c_3_val,2);
        int num_53c_4_val = Integer.parseInt(bit9_53c_4_val,2);
        int num_53c_5_val = Integer.parseInt(bit9_53c_5_val,2);
        int num_53c_6_val = Integer.parseInt(bit9_53c_6_val,2);
        int num_53c_7_val = Integer.parseInt(bit9_53c_7_val,2);
        int num_53c_8_val = Integer.parseInt(bit9_53c_8_val,2);


        HashMap hm = new HashMap();

        num_53c_6_val = num_53c_6_val > 127 ? -(256-num_53c_6_val) : num_53c_6_val; // 공조DTEMax
        num_53c_7_val = num_53c_7_val > 127 ? -(256-num_53c_7_val) : num_53c_7_val; // 파워게이지

        hm.put(Constants.RESP_VEHICLE_SPEED, num_53c_1_val);
        hm.put(Constants.RESP_LEVER_STATUS, num_53c_2_val);
        hm.put(Constants.RESP_EV_READY, num_53c_3_val);
        hm.put(Constants.RESP_PADDLE_SHIFT, num_53c_4_val);
        hm.put(Constants.RESP_DRIVER_MODE, num_53c_5_val);
        hm.put(Constants.RESP_HARMONIZATION_DTE_MAX, num_53c_6_val);
        hm.put(Constants.RESP_POWER_GAUGE, num_53c_7_val);
        hm.put(Constants.RESP_BATTERY_SOC, num_53c_8_val);

        Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs2, hm);
        mCallBackMsg.sendMessage(msg);
        return hm;
    }

    private static HashMap parseData_53D(String val) {

        int _53d_1_1 = Integer.decode("0x"+val.substring(0, 2));
        int _53d_1_2 = Integer.decode("0x"+val.substring(2, 4));
        int _53d_1_3 = Integer.decode("0x"+val.substring(4, 6));
        int _53d_1_4 = Integer.decode("0x"+val.substring(6, 8));
        int _53d_1_5 = Integer.decode("0x"+val.substring(8, 10));
        int _53d_1_6 = Integer.decode("0x"+val.substring(10, 12));

        String s_53d_1_1 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_1)));
        String s_53d_1_2 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_2)));
        String s_53d_1_3 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_3)));
        String s_53d_1_4 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_4)));
        String s_53d_1_5 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_5)));
        String s_53d_1_6 = setBinary(String.valueOf(Integer.toBinaryString(_53d_1_6)));

        /**
         * ################################# 기존 패턴
         *
         *  1. 9bit = 1byte + 1bit    // 9, 9, 0, 8, 8
         *

        String bit9_53d_1_val = s_53d_1_2.substring(7) + s_53d_1_1; // 2byte에 앞에서 1bit + 1byte 전체
        String bit9_53d_2_val = s_53d_1_3.substring(3) + s_53d_1_2.substring(0,s_53d_1_2.length()-4); // 3byte에 앞에서 5bit + 2byte에 뒤에서 4bit
        String bit9_53d_3_val = s_53d_1_4; // 공백
        String bit9_53d_4_val = s_53d_1_5;
        String bit9_53d_5_val = s_53d_1_6;

        int num_53d_1_val = Integer.parseInt(bit9_53d_1_val,2);
        int num_53d_2_val = Integer.parseInt(bit9_53d_2_val,2);
        int num_53d_3_val = Integer.parseInt(bit9_53d_3_val,2);
        int num_53d_4_val = Integer.parseInt(bit9_53d_4_val,2);
        int num_53d_5_val = Integer.parseInt(bit9_53d_5_val,2);
*/
        /**
         *
         * ################################# 변경 패턴
         *
         *  2. 9bit = 1byte + 1bit    // 9, 9, 9, 9
         */

         String bit9_53d_1_val = s_53d_1_2.substring(7) + s_53d_1_1; // 2byte에 앞에서 1bit + 1byte 전체
         String bit9_53d_2_val = s_53d_1_3.substring(3) + s_53d_1_2.substring(0,s_53d_1_2.length()-4); // 3byte에 앞에서 5bit + 2byte에 뒤에서 4bit
         String bit9_53d_3_val = s_53d_1_5.substring(7) + s_53d_1_4; // 5byte에 앞에서 1bit + 4byte 전체
         String bit9_53d_4_val = s_53d_1_6.substring(3) + s_53d_1_5.substring(0,s_53d_1_2.length()-4); // 6byte에 앞에서 5bit + 5byte에 뒤에서 4bit

         int num_53d_1_val = Integer.parseInt(bit9_53d_1_val,2);
         int num_53d_2_val = Integer.parseInt(bit9_53d_2_val,2);
         int num_53d_3_val = Integer.parseInt(bit9_53d_3_val,2);
         int num_53d_4_val = Integer.parseInt(bit9_53d_4_val,2);
         /* **/


        HashMap hm = new HashMap();
        hm.put(Constants.RESP_AVAILABLE_DISTANCE, num_53d_1_val);
        hm.put(Constants.RESP_AVAILABLE_DISTANCE_BASE, num_53d_2_val);
        hm.put(Constants.RESP_AVAILABLE_DISTANCE_MAX, num_53d_3_val);
        hm.put(Constants.RESP_AVAILABLE_DISTANCE_MIN, num_53d_4_val);


        Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs3, hm);
        mCallBackMsg.sendMessage(msg);

        return hm;
    }

    private static HashMap parseData_53E(String val) {

        int _53e_1_1 = Integer.decode("0x"+val.substring(0, 2));
        int _53e_1_2 = Integer.decode("0x"+val.substring(2, 4));
        int _53e_1_3 = Integer.decode("0x"+val.substring(4, 6));
        int _53e_1_4 = Integer.decode("0x"+val.substring(6, 8));
        int _53e_1_5 = Integer.decode("0x"+val.substring(8, 10));
        int _53e_1_6 = Integer.decode("0x"+val.substring(10, 12));

        String s_53e_1_1 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_1)));
        String s_53e_1_2 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_2)));
        String s_53e_1_3 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_3)));
        String s_53e_1_4 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_4)));
        String s_53e_1_5 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_5)));
        String s_53e_1_6 = setBinary(String.valueOf(Integer.toBinaryString(_53e_1_6)));

        String bit9_53e_1_val = s_53e_1_1;
        String bit9_53e_2_val = s_53e_1_2;
        String bit9_53e_3_val = s_53e_1_3;
        String bit9_53e_4_val = s_53e_1_4;
        String bit9_53e_5_val = s_53e_1_5;
        String bit9_53e_6_val = s_53e_1_6;

        int num_53e_1_val = Integer.parseInt(bit9_53e_1_val,2);
        int num_53e_2_val = Integer.parseInt(bit9_53e_2_val,2);
        int num_53e_3_val = Integer.parseInt(bit9_53e_3_val,2);
        int num_53e_4_val = Integer.parseInt(bit9_53e_4_val,2);
        int num_53e_5_val = Integer.parseInt(bit9_53e_5_val,2);
        int num_53e_6_val = Integer.parseInt(bit9_53e_6_val,2);

        num_53e_1_val = num_53e_1_val > 127 ? -(256-num_53e_1_val) : num_53e_1_val; // 운전패턴DTEMax
        num_53e_2_val = num_53e_2_val > 127 ? -(256-num_53e_2_val) : num_53e_2_val; // 운전패턴DTEMin

        // num_53e_3_val = num_53e_5_val <= 7 ?  num_53e_4_val + (-7) : -((16-num_53e_4_val) + 7); // 공조DTEMin
        num_53e_3_val = num_53e_3_val > 127 ? -(256-num_53e_3_val) : num_53e_3_val; // 공조DTEMin
        num_53e_4_val = num_53e_4_val > 127 ? -(256-num_53e_4_val) : num_53e_4_val; // 공조DTE

        HashMap hm = new HashMap();
        hm.put(Constants.RESP_DRIVER_PATTERN_DTE_MAX, num_53e_1_val);
        hm.put(Constants.RESP_DRIVER_PATTERN_DTE_MIN, num_53e_2_val);
        hm.put(Constants.RESP_HARMONIZATION_DTE_MIN, num_53e_3_val);
        hm.put(Constants.RESP_HARMONIZATION_DTE, num_53e_4_val);
        hm.put(Constants.RESP_DRIVING_FEELING_DTE_MAX, num_53e_5_val);
        hm.put(Constants.RESP_DRIVING_FEELING_DTE_MIN, num_53e_6_val);
/*

        Log.d(TAG, "=======================> result response111111111 : " +  num_53e_1_val + " : " + num_53e_2_val + " : " +
                num_53e_3_val + " : " + num_53e_4_val + " : " + num_53e_5_val + " : " + num_53e_6_val  + " : " + num_53e_7_val);
*/

        Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs4, hm);
        mCallBackMsg.sendMessage(msg);

        return hm;
    }


    private static HashMap parseData_53F(String val) {

        int _53f_1_1 = Integer.decode("0x"+val.substring(0, 2));
        int _53f_1_2 = Integer.decode("0x"+val.substring(2, 4));
        int _53f_1_3 = Integer.decode("0x"+val.substring(4, 6));
        int _53f_1_4 = Integer.decode("0x"+val.substring(6, 8));
        int _53f_1_5 = Integer.decode("0x"+val.substring(8, 10));
        int _53f_1_6 = Integer.decode("0x"+val.substring(10, 12));

        String s_53f_1_1 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_1)));
        String s_53f_1_2 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_2)));
        String s_53f_1_3 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_3)));
        String s_53f_1_4 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_4)));
        String s_53f_1_5 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_5)));
        String s_53f_1_6 = setBinary(String.valueOf(Integer.toBinaryString(_53f_1_6)));

        String bit9_53f_1_val = s_53f_1_1;
        String bit9_53f_2_val = s_53f_1_2;
        String bit9_53f_3_val = s_53f_1_3;
        String bit9_53f_4_val = s_53f_1_4;
        String bit9_53f_5_val = s_53f_1_5.substring(4);
        String bit9_53f_6_val = s_53f_1_5.substring(0,4);
        String bit9_53f_7_val = s_53f_1_6;

        int num_53f_1_val = Integer.parseInt(bit9_53f_1_val,2);
        int num_53f_2_val = Integer.parseInt(bit9_53f_2_val,2);
        int num_53f_3_val = Integer.parseInt(bit9_53f_3_val,2);
        int num_53f_4_val = Integer.parseInt(bit9_53f_4_val,2);
        int num_53f_5_val = Integer.parseInt(bit9_53f_5_val,2);
        int num_53f_6_val = Integer.parseInt(bit9_53f_6_val,2);
        int num_53f_7_val = Integer.parseInt(bit9_53f_7_val,2);


        num_53f_6_val = num_53f_6_val <= 7 ?  num_53f_6_val + (-7) : -((16-num_53f_6_val) + 7); // 운전감DTEMin
        num_53f_7_val = num_53f_7_val > 127 ? -(256-num_53f_7_val) : num_53f_7_val; // 운전패턴DTE

        HashMap hm = new HashMap();
        hm.put(Constants.RESP_INSTANT_FUEL_EFFICIENCY, num_53f_1_val);
        hm.put(Constants.RESP_AVERAGE_FUEL_EFFICIENCY, num_53f_2_val);
        hm.put(Constants.RESP_VEHICLE_SPEED_DTE, num_53f_3_val);
        hm.put(Constants.RESP_DRIVING_FEELING_DTE, num_53f_4_val);
        hm.put(Constants.RESP_VEHICLE_SPEED_DTE_MAX, num_53f_5_val);
        hm.put(Constants.RESP_VEHICLE_SPEED_DTE_MIN, num_53f_6_val);
        hm.put(Constants.RESP_DRIVER_PATTERN_DTE, num_53f_7_val);
/*

        Log.d(TAG, "=======================> result response2222222 val: ["+val +"] / " +  num_53f_1_val + " : " + num_53f_2_val + " : " +
                num_53f_3_val + " : " + num_53f_4_val + " : " + num_53f_5_val + " : " + num_53f_6_val  + " : " + num_53f_7_val);
*/

        Message msg = Message.obtain(mCallBackMsg, Constants.HANDLE_OBD_RESPONSE_VCUPs5, hm);
        mCallBackMsg.sendMessage(msg);

        return hm;
    }

    private static String setBinary(String val) {
        if(null == val || val.length() == 0) {
            return "00000000";
        }

        int valLen = val.length();
        int addLen = 8-valLen;
        String addZero = "";
        for(int i = 0; i < addLen; i++) {
            addZero+="0";
        }

        return addZero+val;
    }




}
