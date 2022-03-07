package com.obigo.hkmotors.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class connectionActivity extends BaseActivity implements View.OnClickListener {
    PrintWriter sendWriter;
    static BufferedReader input;
    String read;


    Button connection;
    Button notConnection;
    EditText ipEdit;
    TextView ipTxt;

    TextView btn0;
    TextView btn1;
    TextView btn2;
    TextView btn3;
    TextView btn4;
    TextView btn5;
    TextView btn6;
    TextView btn7;
    TextView btn8;
    TextView btn9;
    TextView btnPoint;
    TextView btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_connection);

        init();

    }

    public void init(){

        connection = findViewById(R.id.connection_btn);
        ipTxt = findViewById(R.id.connection_ip);

        btn0 = findViewById(R.id.connection_0);
        btn1 = findViewById(R.id.connection_1);
        btn2 = findViewById(R.id.connection_2);
        btn3 = findViewById(R.id.connection_3);
        btn4 = findViewById(R.id.connection_4);
        btn5 = findViewById(R.id.connection_5);
        btn6 = findViewById(R.id.connection_6);
        btn7 = findViewById(R.id.connection_7);
        btn8 = findViewById(R.id.connection_8);
        btn9 = findViewById(R.id.connection_9);
        btnPoint = findViewById(R.id.connection_point);
        btnDel = findViewById(R.id.connection_del);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        notConnection = findViewById(R.id.not_connection_btn);
        notConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CONNECTION_STATUS = false;
                Intent intent = new Intent();
                intent.putExtra("isConnected",false);
                setResult(Constants.REQUEST_CONNECT_WIFI,intent);
                finish();
            }
        });
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ipTxt.getText().length()!=0){
                    connectedSocket(ipTxt.getText().toString());
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
                input = Network.getInstance().input;
                if(Network.getInstance(ip).isConnected()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"차량과 연결이 완료되었습니다",Toast.LENGTH_SHORT).show();
                        }
                    });


                    Constants.CONNECTION_STATUS = true;
                    Intent intent = new Intent();
                    intent.putExtra("isConnected",true);
                    setResult(Constants.REQUEST_CONNECT_WIFI,intent);
                    finish();
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"차량과 연결이 실패했습니다",Toast.LENGTH_SHORT).show();
                            Constants.CONNECTION_STATUS = false;
                        }
                    });
                }

                while (true){
                    try {
                        String read = input.readLine();
                        if(read!=null){
                            System.out.println(read);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }
        }).start();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.connection_1:


                ipTxt.setText(ipTxt.getText() + "1");

                break;

            case R.id.connection_2:


                    ipTxt.setText(ipTxt.getText() + "2");


                break;

            case R.id.connection_3:


                ipTxt.setText(ipTxt.getText() + "3");

                break;

            case R.id.connection_4:


                ipTxt.setText(ipTxt.getText() + "4");

                break;

            case R.id.connection_5:


                ipTxt.setText(ipTxt.getText() + "5");

                break;

            case R.id.connection_6:


                ipTxt.setText(ipTxt.getText() + "6");


                break;

            case R.id.connection_7:


                ipTxt.setText(ipTxt.getText() + "7");

                break;

            case R.id.connection_8:


                ipTxt.setText(ipTxt.getText() + "8");

                break;

            case R.id.connection_9:


                ipTxt.setText(ipTxt.getText() + "9");

                break;

            case R.id.connection_0:


                ipTxt.setText(ipTxt.getText() + "0");

                break;

            case R.id.connection_del:



                    String txt = ipTxt.getText().toString();
                ipTxt.setText(txt.substring(0, txt.length()-1));

                break;

            case R.id.connection_point :
                ipTxt.setText(ipTxt.getText() + ".");
                break;

            default:
                // nothing to this
                break;
        }
    }

}