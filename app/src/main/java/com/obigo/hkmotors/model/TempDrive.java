package com.obigo.hkmotors.model;

public class TempDrive {
    private String isOn;

    private String stiffness;

    private String reducer;

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }

    public String getStiffness() {
        return stiffness;
    }

    public void setStiffness(String stiffness) {
        this.stiffness = stiffness;
    }

    public String getReducer() {
        return reducer;
    }

    public void setReducer(String reducer) {
        this.reducer = reducer;
    }

    private static TempDrive drive;

    public static  synchronized TempDrive getInstance(){
        if(drive==null){
            drive = new TempDrive();
        }
        return drive;
    }
}

