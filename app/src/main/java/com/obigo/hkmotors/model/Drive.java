package com.obigo.hkmotors.model;

public class Drive {

    private boolean isOn;

    private int stiffness;

    private int reducer;

    private static Drive drive;

    public  synchronized Drive getInstance(){
        if(drive==null){
            drive = new Drive();
        }
        return drive;
    }
}
