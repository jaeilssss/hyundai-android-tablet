package com.obigo.hkmotors.model;

public class Drive {

    private String isOn;

    private String stiffness;

    private String reducer;

    private String tempIsOn;
    private String tempStiffness;
    private String tempReducer;


    boolean isChange = false;

    public String getTempIsOn() {
        return tempIsOn;
    }

    public void setTempIsOn(String tempIsOn) {
        this.tempIsOn = tempIsOn;
    }

    public String getTempStiffness() {
        return tempStiffness;
    }

    public void setTempStiffness(String tempStiffness) {
        this.tempStiffness = tempStiffness;
    }

    public String getTempReducer() {
        return tempReducer;
    }

    public void setTempReducer(String tempReducer) {
        this.tempReducer = tempReducer;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
        tempIsOn = isOn;
    }

    public String getStiffness() {
        return stiffness;
    }

    public void setStiffness(String stiffness) {
        this.stiffness = stiffness;
        tempStiffness  = stiffness;
    }

    public String getReducer() {
        return reducer;
    }

    public void setReducer(String reducer) {
        this.reducer = reducer;
        tempReducer = reducer;
    }

    private static Drive drive;

    public static  synchronized Drive getInstance(){
        if(drive==null){
            drive = new Drive();
        }
        return drive;
    }

    public void reset(){
        tempIsOn = isOn;
        tempReducer = reducer;
        tempStiffness  = stiffness;
    }

    public void update(){
        isOn = tempIsOn;
        reducer = tempReducer;
        stiffness = tempStiffness;
    }
}
