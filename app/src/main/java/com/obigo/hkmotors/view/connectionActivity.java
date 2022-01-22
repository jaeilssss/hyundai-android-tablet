package com.obigo.hkmotors.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.module.BaseActivity;
import com.obigo.hkmotors.module.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class connectionActivity extends BaseActivity {
    PrintWriter sendWriter;
    static BufferedReader input;
    String read;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_connection);



        connectedSocket();

    }


    public void connectedSocket(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    String data ="";
                    input = Network.getInstance("192.168.0.3").input;
                    int count=0;
                    while (true) {

                        read = input.readLine();
                        if (read != null) {
                            if(count!=12){
                                data+=read;

                            }else{
                                data += read;

                            }

                            count++;
                            if(count==13){
                                System.out.println(data);
                                JSONObject jsonObject = new JSONObject(data);

                                JSONObject tpcanMsgJson = jsonObject.getJSONObject("TPCANMsg");
                                JSONObject tpcanTimestampJson = jsonObject.getJSONObject("TPCANTimestamp");

                                JSONArray array =  tpcanMsgJson.getJSONArray("DATA");


                                count=0;
                                data="";
                            }

                        }
                    }

                }catch (IOException | JSONException e){
                    e.printStackTrace();
                }



            }
        }).start();

//        loadingDialog  = new LoadingDialog(MainActivity.this,1);
//        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        loadingDialog.show();
//        loadingDialog.setNotTouch();

        //로딩창을 투명하게
    }
}