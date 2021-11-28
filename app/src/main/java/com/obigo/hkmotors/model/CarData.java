package com.obigo.hkmotors.model;

public class CarData {

    private static CarData carData;

    // 차량 데이터 값
    private int comfortable;    //안락감
    private int leading;        //주도성
    private int dynamic;        //역동성
    private int Efficiency;     //효율성
    private int performance;    // 동력성능


    public static CarData getCarData() {
        return carData;
    }

    public static void setCarData(CarData carData) {
        CarData.carData = carData;
    }

    public int getComfortable() {
        return comfortable;
    }

    public void setComfortable(int comfortable) {
        this.comfortable = comfortable;
    }

    public int getLeading() {
        return leading;
    }

    public void setLeading(int leading) {
        this.leading = leading;
    }

    public int getDynamic() {
        return dynamic;
    }

    public void setDynamic(int dynamic) {
        this.dynamic = dynamic;
    }

    public int getEfficiency() {
        return Efficiency;
    }

    public void setEfficiency(int efficiency) {
        Efficiency = efficiency;
    }

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    private CarData() {}

    // Lazy Initailization
    public static synchronized CarData getInstance() {
        if(carData == null) {
            carData = new CarData();
        }
        return carData;
    }
}
