package com.obigo.hkmotors.model;

public class Transmission {

    private static Transmission transmission;

    private String isOn;
    private String type;
    private String gear;
    private String gearRate;
    private String transmissionSpeed;
    private String transmissionPower;
    private String transmissionMap;

    boolean isChange = false;

    private String tempIsOn;
    private String tempType;
    private String tempGear;
    private String tempGearRate;
    private String tempTransmissionSpeed;
    private String tempTransmissionPower;
    private String tempTransmissionMap;

    public String getTempIsOn() {
        return tempIsOn;
    }

    public void setTempIsOn(String tempIsOn) {
        this.tempIsOn = tempIsOn;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getTempGear() {
        return tempGear;
    }

    public void setTempGear(String tempGear) {
        this.tempGear = tempGear;
    }

    public String getTempGearRate() {
        return tempGearRate;
    }

    public void setTempGearRate(String tempGearRate) {
        this.tempGearRate = tempGearRate;
    }

    public String getTempTransmissionSpeed() {
        return tempTransmissionSpeed;
    }

    public void setTempTransmissionSpeed(String tempTransmissionSpeed) {
        this.tempTransmissionSpeed = tempTransmissionSpeed;
    }

    public String getTempTransmissionPower() {
        return tempTransmissionPower;
    }

    public void setTempTransmissionPower(String tempTransmissionPower) {
        this.tempTransmissionPower = tempTransmissionPower;
    }

    public String getTempTransmissionMap() {
        return tempTransmissionMap;
    }

    public void setTempTransmissionMap(String tempTransmissionMap) {
        this.tempTransmissionMap = tempTransmissionMap;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
        tempIsOn = isOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        tempType = type;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
        tempGear = gear;
    }

    public String getGearRate() {
        return gearRate;
    }

    public void setGearRate(String gearRate) {
        this.gearRate = gearRate;
        tempGearRate = gearRate;
    }

    public String getTransmissionSpeed() {
        return transmissionSpeed;
    }

    public void setTransmissionSpeed(String transmissionSpeed) {
        this.transmissionSpeed = transmissionSpeed;
        tempTransmissionSpeed = transmissionSpeed;
    }

    public String getTransmissionPower() {
        return transmissionPower;
    }

    public void setTransmissionPower(String transmissionPower) {
        this.transmissionPower = transmissionPower;
        tempTransmissionPower = transmissionPower;
    }

    public String getTransmissionMap() {
        return transmissionMap;
    }

    public void setTransmissionMap(String transmissionMap) {
        this.transmissionMap = transmissionMap;
        tempTransmissionMap = transmissionMap;
    }

    public static synchronized Transmission getInstance() {
        if(transmission == null) {
            transmission = new Transmission();
        }
        return transmission;
    }
}
