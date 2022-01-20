package com.obigo.hkmotors.model;

public class TPCANTimestamp {

    long micros;
    long millis;
    long millisOverflow;

    public TPCANTimestamp(long micros, long millis, long millisOverflow) {
        this.micros = micros;
        this.millis = millis;
        this.millisOverflow = millisOverflow;
    }
}
