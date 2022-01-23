package com.obigo.hkmotors.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.Constants;
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


    Button connection;
    EditText ipEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_connection);

        init();

    }

    public void init(){

        connection = findViewById(R.id.connection_btn);
        ipEdit = findViewById(R.id.ip_edit);

        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ipEdit.getText().length()!=0){
                    connectedSocket(ipEdit.getText().toString());
                }
            }
        });
    }


    public void connectedSocket(final String ip){

        new Thread(new Runnable() {
            @Override
            public void run() {

                String data ="";
                Network.getInstance(ip);
                if(Network.getInstance(ip).isConnected()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"차량과 연결이 완료되었습니다",Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent();
                    intent.putExtra("isConnected",true);
                    setResult(Constants.REQUEST_CONNECT_WIFI,intent);
                    finish();
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"차량과 연결이 실패했습니다",Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        }).start();

    }
}