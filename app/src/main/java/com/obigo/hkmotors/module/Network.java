package com.obigo.hkmotors.module;

import android.database.Observable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observer;

public class Network {

    private static Network instance = null;
    public PrintWriter sendWriter;
    Socket socket;
    Observable<Observer> observable;
   public BufferedReader input;

    private Network(){

    }
    private Network(String ip) throws IOException {
        // 생성자는 외부에서 호출못하게 private 으로 지정해야 한다.


        try {
            InetSocketAddress isa = new InetSocketAddress(ip, 12345);
            socket= new Socket();
            String data ="";
            socket.setReuseAddress(true);
            socket.connect(isa);
            int count = 0;
            sendWriter = new PrintWriter(socket.getOutputStream());
             input =   new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (SocketException e) {
            e.printStackTrace();
        }



    }

    public boolean isConnected(){

        return socket.isConnected();
    }

    public void sendData(){

    }
    public static Network getInstance(String ip) {

        if(instance==null){
            try{
                instance = new Network(ip);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return instance;
    }

    public void say() {
        System.out.println("hi, there");
    }
}
