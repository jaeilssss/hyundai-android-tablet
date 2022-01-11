package com.obigo.hkmotors.model;

public class Sound {


    private String isOn;
    private String driveType;
    private String volume;
    private String backVolume;
    private String backSensitive;

    boolean isChange = false;

    private String tempIsOn;
    private String tempDriveType;
    private String tempVolume ;
    private String tempBackVolume;
    private String tempBackSensitive;

    public String getTempIsOn() {
        return tempIsOn;
    }

    public void setTempIsOn(String tempIsOn) {
        this.tempIsOn = tempIsOn;
    }

    public String getTempDriveType() {
        return tempDriveType;
    }

    public void setTempDriveType(String tempDriveType) {
        this.tempDriveType = tempDriveType;
    }

    public String getTempVolume() {
        return tempVolume;
    }

    public void setTempVolume(String tempVolume) {
        this.tempVolume = tempVolume;
    }

    public String getTempBackVolume() {
        return tempBackVolume;
    }

    public void setTempBackVolume(String tempBackVolume) {
        this.tempBackVolume = tempBackVolume;
    }

    public String getTempBackSensitive() {
        return tempBackSensitive;
    }

    public void setTempBackSensitive(String tempBackSensitive) {
        this.tempBackSensitive = tempBackSensitive;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
        tempIsOn = isOn;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
        tempDriveType = driveType;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
        tempVolume = volume;
    }

    public String getBackVolume() {
        return backVolume;
    }

    public void setBackVolume(String backVolume) {
        this.backVolume = backVolume;
        tempBackVolume = backVolume;
    }

    public String getBackSensitive() {
        return backSensitive;
    }

    public void setBackSensitive(String backSensitive) {
        this.backSensitive = backSensitive;
        tempBackSensitive = backSensitive;
    }

    private static Sound sound;

    // Lazy Initailization
    public static synchronized Sound getInstance() {
        if(sound == null) {
            sound = new Sound();
        }
        return sound;
    }

    public void reset(){

        tempIsOn =  isOn;
        tempDriveType =  driveType;
        tempVolume =  volume;
        tempBackVolume =  backVolume;
        tempBackSensitive =  backSensitive;
    }

    public void update(){
        isOn  = tempIsOn;
        driveType = tempDriveType;
        volume = tempVolume;
        backVolume = tempBackVolume;
        backSensitive = tempBackSensitive;
    }
}
