package com.obigo.hkmotors.model;

import java.util.ArrayList;

public class TPCANMsg {


    String [] data;
    long id;
    long len;
    long msgType;

    public TPCANMsg(String [] data, long id, long len, long msgType) {
        this.data = data;
        this.id = id;
        this.len = len;
        this.msgType = msgType;
    }


}
