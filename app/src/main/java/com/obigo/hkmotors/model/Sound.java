package com.obigo.hkmotors.model;

public class Sound {


    private boolean isOn;
    private int driveType;
    private int volume;
    private int backVolume;
    private int backSensitive;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getDriveType() {
        return driveType;
    }

    public void setDriveType(int driveType) {
        this.driveType = driveType;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getBackVolume() {
        return backVolume;
    }

    public void setBackVolume(int backVolume) {
        this.backVolume = backVolume;
    }

    public int getBackSensitive() {
        return backSensitive;
    }

    public void setBackSensitive(int backSensitive) {
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
