package com.obigo.hkmotors.common;

import java.util.UUID;


/**
 * Constants to save
 */
public class Constants {

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE          = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE        = 2;
    public static final int REQUEST_ENABLE_BT                      = 3;
    public static final int REQUEST_FAVORITE                       = 4;
    public static final int REQUEST_GEAR_SETTING                   = 5;
    public static final int REQUEST_SPEAKER_SETTING                   = 6;
    public static final int REQUEST_DRIVING_SETTING                   = 7;
    public static final int REQUEST_EDIT_CUSTOM_RESULT              =8;
    public static boolean INIT_FLAG                                = true; // 앱 최초 실행시에만 쓰이는 프래그 - true :: 최초실행시 메인프레그먼트에서 OBD 연결창을 띄운다
    public static boolean CONNECTION_STATUS                               = false; // OBD 연결상태 - true :: 연결 / false :: 미연결


    public static final int REQUEST_CONNECT_WIFI                   =9;
    public static int MODE_STATUS                                  = 0;

    public static int DTE_MODE_STATUS                              = 0;

    // Activity Status tag
    public static String DISPLAY_MODE                              = "NONE"; // 화면 상태

    // Command mode like SEND, RESET, and SAVE
    public static String COMMAND_MODE                              = "NONE"; // command mode

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE                   = 1;
    public static final int MESSAGE_READ                           = 2;
    public static final int MESSAGE_WRITE                          = 3;
    public static final int MESSAGE_DEVICE_NAME                    = 4;
    public static final int MESSAGE_TOAST                          = 5;
    public static final int MESSAGE_LOST                           = 6; // Bluetooth 연결 끊어짐
    public static final int MESSAGE_OTHER                          = 999;

    // Constants that indicate the current connection state
    public static final int STATE_NONE                             = 0;    // we're doing nothing
    public static final int STATE_LISTEN                           = 1;    // now listening for incoming connections
    public static final int STATE_CONNECTING                       = 2;    // now initiating an outgoing connection
    public static final int STATE_CONNECTED                        = 3;    // now connected to a remote device

    // obd handler
    public static final int HANDLE_OBD_RESPONSE_VCUPs1             = 0;
    public static final int HANDLE_OBD_PARAMETER                   = 1;
    public static final int HANDLE_OBD_CONN_LOST                   = 2;
    public static final int HANDLE_OBD_USER_DISCONN                = 3;
    public static final int HANDLE_OBD_RESPONSE_VCUPs2             = 4;
    public static final int HANDLE_OBD_RESPONSE_VCUPs3             = 5;
    public static final int HANDLE_OBD_RESPONSE_VCUPs4             = 6;
    public static final int HANDLE_OBD_RESPONSE_VCUPs5             = 7;
    public static final int HANDLE_OBD_OTHER                       = 99;


    // Name for the SDP record when creating server socket
    public static final String NAME_SECURE                         = "BluetoothChatSecure";
    public static final String NAME_INSECURE                       = "BluetoothChatInsecure";

    // Unique UUID for this application
    public static final UUID MY_UUID                                = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   // UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final UUID MY_UUID_SECURE                        = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    // UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final UUID MY_UUID_INSECURE                      = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    // UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // default pwd
    public static final String D_PWD                               = "0000";

    // mode selection such as general and expert
    public static final String MODE_RCDATION                       = "r";
    public static final String MODE_EXPERT                         = "e";

    // OBD 초기화용
    public static boolean INITIALIZED                              = false;

    // 데이터 통신 진행 여부
    public static boolean OBD_INITIALIZED                          = false;

    // Response
    public static String RESP_ECO_LEVEL                             = "RespEcoLevel";
    public static String RESP_MAX_POWER                             = "RespMaxPower";
    public static String RESP_ACCELERATION                          = "RespAcceleration";
    public static String RESP_DECELERATION                          = "RespDeceleration";
    public static String RESP_RESPONSE                              = "RespResponse";

    // parameters
    public static String PARAM_TORQUE                               = "ParamTorque";
    public static String PARAM_ACCELERATION                         = "ParamAcceleration";
    public static String PARAM_DECELERATION                         = "ParamDeceleration";
    public static String PARAM_BRAKE                                = "ParamBrake";
    public static String PARAM_ENERGY                               = "ParamEnergy";
    public static String PARAM_SPEED                                = "ParamSpeed";
    public static String PARAM_RESPONSE                             = "ParamResponse";

    // ResponseVCUPs2
    public static String RESP_VEHICLE_SPEED                         = "RespVehicleSpeed";
    public static String RESP_LEVER_STATUS                          = "RespLeverStatus";
    public static String RESP_EV_READY                              = "RespEvReady";
    public static String RESP_PADDLE_SHIFT                          = "RespPaddleShift";
    public static String RESP_DRIVER_MODE                           = "RespDriverMode";
    public static String RESP_POWER_GAUGE                           = "RespPowerGauge";
    public static String RESP_BATTERY_SOC                           = "RespBatterySOC";

    // ResponseVCUPs3
    public static String RESP_AVAILABLE_DISTANCE                    = "RespAvailableDistance";
    public static String RESP_AVAILABLE_DISTANCE_BASE               = "RespAvailableDistanceBASE";
    public static String RESP_AVAILABLE_DISTANCE_MAX                = "RespAvailableDistanceMax";
    public static String RESP_AVAILABLE_DISTANCE_MIN                = "RespAvailableDistanceMin";

    // ResponseVCUPs4
    public static String RESP_DRIVER_PATTERN_DTE_MAX                = "RespDriverPatternDTEMax";
    public static String RESP_DRIVER_PATTERN_DTE_MIN                = "RespDriverPatternDTEMin";
    public static String RESP_HARMONIZATION_DTE_MAX                 = "RespHarmonizationDTEMax";
    public static String RESP_HARMONIZATION_DTE_MIN                 = "RespHarmonizationDTEMin";
    public static String RESP_HARMONIZATION_DTE                     = "RespHarmonizationDTE";
    public static String RESP_VEHICLE_SPEED_DTE_MAX                 = "RespVehicleSpeedDTEMax";
    public static String RESP_VEHICLE_SPEED_DTE_MIN                 = "RespVehicleSpeedDTEMin";

    // ResponseVCUPs5
    public static String RESP_INSTANT_FUEL_EFFICIENCY               = "RespInstantFuelEfficiency";
    public static String RESP_AVERAGE_FUEL_EFFICIENCY               = "RespAverageFuelEfficiency";
    public static String RESP_VEHICLE_SPEED_DTE                     = "RespVehicleSpeedDTE";
    public static String RESP_DRIVING_FEELING_DTE                   = "RespDrivingFeelingDTE";
    public static String RESP_DRIVING_FEELING_DTE_MAX               = "RespDrivingFeelingDTEMax";
    public static String RESP_DRIVING_FEELING_DTE_MIN               = "RespDrivingFeelingDTEMin";
    public static String RESP_DRIVER_PATTERN_DTE                    = "RespDriverPatternDTE";

    public static String SEND_MSG_00                                = "";
    public static String SEND_MSG_01                                = "";



}
