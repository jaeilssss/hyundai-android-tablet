package com.obigo.hkmotors.model;

public class Sound {


    private String isOn;
    private String driveType;
    private String volume;
    private String backVolume;
    private String backSensitive;

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getBackVolume() {
        return backVolume;
    }

    public void setBackVolume(String backVolume) {
        this.backVolume = backVolume;
    }

    public String getBackSensitive() {
        return backSensitive;
    }

    public void setBackSensitive(String backSensitive) {
        this.backSensitive = backSensitive;
    }

    private static Sound sound;

    // Lazy Initailization
    public static synchronized Sound getInstance() {
        if(sound == null) {
            sound = new Sound();
        }
        return sound;
    }

}
