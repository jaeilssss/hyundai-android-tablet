package com.obigo.hkmotors.model;

import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.net.Socket;

public class ConnectThread  extends Thread{

    String TAG = "ConnectThread";
    String hostName;
    Socket socket;
    View view;
    int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public void run() {



    }
}
