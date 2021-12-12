package com.obigo.hkmotors.model;

public class TempTransmission {
    private static TempTransmission transmission;

    private String isOn;
    private String type;
    private String gear;
    private String gearRate;
    private String transmissionSpeed;
    private String transmissionPower;
    private String transmissionMap;

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getGearRate() {
        return gearRate;
    }

    public void setGearRate(String gearRate) {
        this.gearRate = gearRate;
    }

    public String getTransmissionSpeed() {
        return transmissionSpeed;
    }

    public void setTransmissionSpeed(String transmissionSpeed) {
        this.transmissionSpeed = transmissionSpeed;
    }

    public String getTransmissionPower() {
        return transmissionPower;
    }

    public void setTransmissionPower(String transmissionPower) {
        this.transmissionPower = transmissionPower;
    }

    public String getTransmissionMap() {
        return transmissionMap;
    }

    public void setTransmissionMap(String transmissionMap) {
        this.transmissionMap = transmissionMap;
    }


    public static synchronized TempTransmission getInstance() {
        if(transmission == null) {
            transmission = new TempTransmission();
        }
        return transmission;
    }
}

