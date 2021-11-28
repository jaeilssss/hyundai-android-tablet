package com.obigo.hkmotors.model;

public class Transmission {

    private static Transmission transmission;

    private boolean isOn;
    private int type;
    private int gear;
    private int gearRate;
    private int transmissionSpeed;
    private int transmissionPower;
    private int transmissionMap;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public int getGearRate() {
        return gearRate;
    }

    public void setGearRate(int gearRate) {
        this.gearRate = gearRate;
    }

    public int getTransmissionSpeed() {
        return transmissionSpeed;
    }

    public void setTransmissionSpeed(int transmissionSpeed) {
        this.transmissionSpeed = transmissionSpeed;
    }

    public int getTransmissionPower() {
        return transmissionPower;
    }

    public void setTransmissionPower(int transmissionPower) {
        this.transmissionPower = transmissionPower;
    }

    public int getTransmissionMap() {
        return transmissionMap;
    }

    public void setTransmissionMap(int transmissionMap) {
        this.transmissionMap = transmissionMap;
    }

    // Lazy Initailization
    public static synchronized Transmission getInstance() {
        if(transmission == null) {
            transmission = new Transmission();
        }
        return transmission;
    }
}
