package com.obigo.hkmotors.model;

public class CarData {

    private static CarData carData;

    // 차량 데이터 값
    private float comfortable;    //안락감
    private float leading;        //주도성
    private float dynamic;        //역동성
    private float efficiency;     //효율성
    private float performance;    // 동력성능


       float tempComfortable;
     float tempLeading;
     float tempDynamic;
     float tempEfficiency;
     float tempPerformance;


     public void setTempEVMode(){
        tempComfortable = 7;
        tempLeading = 2;
        tempDynamic = 4;
        tempEfficiency = 8;
        tempPerformance = 8;
     }
     public void setEVMode(){
         comfortable = 7;
         leading = 2;
         dynamic = 4;
         efficiency = 8;
         performance = 8;
     }
    public float getTempComfortable() {
        return tempComfortable;
    }

    public float getTempLeading() {
        return tempLeading;
    }

    public float getTempDynamic() {
        return tempDynamic;
    }

    public float getTempEfficiency() {
        return tempEfficiency;
    }

    public float getTempPerformance() {
        return tempPerformance;
    }

    public float getComfortable() {
        return comfortable;
    }

    public float getLeading() {
        return leading;
    }

    public float getDynamic() {
        return dynamic;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getPerformance() {
        return performance;
    }

    public void setComfortable(){
        int size = 0;
        int sum=0;
                // 가상 음향 효과
        if(Sound.getInstance().getIsOn().equals("1")){
            size+=4;
            if(Sound.getInstance().getDriveType().equals("0")){
                sum+=6;
                
            }else{
                sum+=2;
                
            }

            if(Sound.getInstance().getVolume().equals("00")){
                sum+=6;
                
            }else if(Sound.getInstance().getVolume().equals("01")){
                sum+=4;
                
            }else if(Sound.getInstance().getVolume().equals("10")){
                sum+=1;
                
            }

            if(Sound.getInstance().getBackSensitive().equals("0")){
                sum+=5;
                
            }else{
                sum+=1;
                
            }

            if(Sound.getInstance().getBackVolume().equals("00")){
                sum+=10;
            }else if(Sound.getInstance().getBackVolume().equals("01")){
                sum+=6;
            }else if(Sound.getInstance().getBackVolume().equals("10")){
                sum+=3;
            }else{
                sum+=1;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getIsOn().equals("1")){
            size+=5;
            if(Transmission.getInstance().getType().equals("00")){
                sum+=5;
                
            }else if(Transmission.getInstance().getType().equals("01")){
                sum+=3;
            }else {
                sum+=1;
            }

            if(Transmission.getInstance().getGearRate().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getGearRate().equals("01")){
                sum+=6;
            }else{
                sum+=7;
            }

            if(Transmission.getInstance().getTransmissionSpeed().equals("00")){
                sum+=8;
            }else if(Transmission.getInstance().getTransmissionSpeed().equals("01")){
                sum+=6;
            }else{
                sum+=4;
            }

            if(Transmission.getInstance().getTransmissionPower().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTransmissionPower().equals("01")){
                sum+=3;
            }else{
                sum+=0;
            }

            if(Transmission.getInstance().getTransmissionMap().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getTransmissionMap().equals("01")){
                sum+=4;
            }else{
                sum+=2;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getReducer().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getReducer().equals("01")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Drive.getInstance().getStiffness().equals("00")){
                sum+=10;
            }else if(Drive.getInstance().getStiffness().equals("01")){
                sum+=6;
            }else{
                sum+=2;
            }
        }

        comfortable = ((float)sum)/size;
    }

    public void setLeading(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getIsOn().equals("1")){
            size+=2;


            if(Sound.getInstance().getBackSensitive().equals("0")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getBackVolume().equals("00")){
                sum+=0;
            }else if(Sound.getInstance().getBackVolume().equals("01")){
                sum+=4;
            }else if(Sound.getInstance().getBackVolume().equals("10")){
                sum+=7;
            }else{
                sum+=10;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getIsOn().equals("1")){
            size+=5;
            if(Transmission.getInstance().getType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getType().equals("01")){
                sum+=7;
            }else {
                sum+=8;
            }

            if(Transmission.getInstance().getGear().equals("000")){
                sum+=4;
            }else if(Transmission.getInstance().getGear().equals("001")){
                sum+=5;
            }else if(Transmission.getInstance().getGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getGear().equals("011")){
                sum+=7;
            }else{
                sum+=8;
            }



            if(Transmission.getInstance().getTransmissionSpeed().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getTransmissionSpeed().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }

            if(Transmission.getInstance().getTransmissionPower().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTransmissionPower().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTransmissionMap().equals("00")){
                sum+=3;
            }else if(Transmission.getInstance().getTransmissionMap().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getReducer().equals("00")){
                sum+=7;
            }else if(Drive.getInstance().getReducer().equals("01")){
                sum+=6;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getStiffness().equals("00")){
                sum+=2;
            }else if(Drive.getInstance().getStiffness().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }
        }

        leading = ((float)sum)/size;
    }


    public void setDynamic(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getIsOn().equals("1")){
            size+=4;

            if(Sound.getInstance().getDriveType().equals("0")){
                sum+=4;
            }else{
                sum+=7;
            }

            if(Sound.getInstance().getBackSensitive().equals("0")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getBackVolume().equals("00")){
                sum+=0;
            }else if(Sound.getInstance().getBackVolume().equals("01")){
                sum+=4;
            }else if(Sound.getInstance().getBackVolume().equals("10")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getVolume().equals("00")){
                sum+=5;
            }else if(Sound.getInstance().getVolume().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getIsOn().equals("1")){
            size+=6;
            if(Transmission.getInstance().getType().equals("00")){
                sum+=5;

            }else if(Transmission.getInstance().getType().equals("01")){
                sum+=7;
            }else {
                sum+=10;
            }

            if(Transmission.getInstance().getGear().equals("000")){
                sum+=4;
            }else if(Transmission.getInstance().getGear().equals("001")){
                sum+=5;
            }else if(Transmission.getInstance().getGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getGear().equals("011")){
                sum+=7;
            }else{
                sum+=8;
            }

            if(Transmission.getInstance().getGearRate().equals("00")){
                sum+=9;
            }else if(Transmission.getInstance().getGearRate().equals("01")){
                sum+=7;
            }else{
                sum+=5;
            }



            if(Transmission.getInstance().getTransmissionSpeed().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTransmissionSpeed().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTransmissionPower().equals("00")){
                sum+=2;
            }else if(Transmission.getInstance().getTransmissionPower().equals("01")){
                sum+=6;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTransmissionMap().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTransmissionMap().equals("01")){
                sum+=8;
            }else{
                sum+=9;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getReducer().equals("00")){
                sum+=10;
            }else if(Drive.getInstance().getReducer().equals("01")){
                sum+=5;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getStiffness().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getStiffness().equals("01")){
                sum+=5;
            }else{
                sum+=10;
            }
        }

        dynamic =((float)sum)/size;
    }

    public void setEfficiency(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getIsOn().equals("1")){
            size+=3;



            if(Sound.getInstance().getBackSensitive().equals("0")){
                sum+=9;
            }else{
                sum+=8;
            }

            if(Sound.getInstance().getBackVolume().equals("00")){
                sum+=8;
            }else if(Sound.getInstance().getBackVolume().equals("01")){
                sum+=7;
            }else if(Sound.getInstance().getBackVolume().equals("10")){
                sum+=6;
            }else{
                sum+=5;
            }

            if(Sound.getInstance().getVolume().equals("00")){
                sum+=8;
            }else if(Sound.getInstance().getVolume().equals("01")){
                sum+=7;
            }else{
                sum+=6;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getIsOn().equals("1")){
            size+=4;
            if(Transmission.getInstance().getType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getType().equals("01")){
                sum+=4;
            }else {
                sum+=2;
            }

            if(Transmission.getInstance().getGear().equals("000")){
                sum+=8;
            }else if(Transmission.getInstance().getGear().equals("001")){
                sum+=7;
            }else if(Transmission.getInstance().getGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getGear().equals("011")){
                sum+=5;
            }else{
                sum+=4;
            }





            if(Transmission.getInstance().getTransmissionSpeed().equals("00")){
                sum+=9;
            }else if(Transmission.getInstance().getTransmissionSpeed().equals("01")){
                sum+=8;
            }else{
                sum+=7;
            }

            if(Transmission.getInstance().getTransmissionPower().equals("00")){
                sum+=8;
            }else if(Transmission.getInstance().getTransmissionPower().equals("01")){
                sum+=7;
            }else{
                sum+=6;
            }

        }

        //가상 구동축 효과

        if(Drive.getInstance().getIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getReducer().equals("00")){
                sum+=0;
            }else if(Drive.getInstance().getReducer().equals("01")){
                sum+=6;
            }else{
                sum+=9;
            }

            if(Drive.getInstance().getStiffness().equals("00")){
                sum+=9;
            }else if(Drive.getInstance().getStiffness().equals("01")){
                sum+=7;
            }else{
                sum+=5;
            }
        }

        efficiency = ((float)sum)/size;
    }

    public void setPerformance(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과

        // 가상 변속 효과
        if(Transmission.getInstance().getIsOn().equals("1")){
            size+=4;
            if(Transmission.getInstance().getType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getType().equals("01")){
                sum+=5;
            }else {
                sum+=1;
            }

            if(Transmission.getInstance().getGear().equals("000")){
                sum+=8;
            }else if(Transmission.getInstance().getGear().equals("001")){
                sum+=7;
            }else if(Transmission.getInstance().getGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getGear().equals("011")){
                sum+=5;
            }else{
                sum+=4;
            }
            if(Transmission.getInstance().getGearRate().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getGearRate().equals("01")){
                sum+=8;
            }else{
                sum+=6;
            }


            if(Transmission.getInstance().getTransmissionPower().equals("00")){
                sum+=7;
            }else if(Transmission.getInstance().getTransmissionPower().equals("01")){
                sum+=6;
            }else{
                sum+=5;
            }

        }

        //가상 구동축 효과

        if(Drive.getInstance().getIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getReducer().equals("00")){
                sum+=7;
            }else if(Drive.getInstance().getReducer().equals("01")){
                sum+=9;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getStiffness().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getStiffness().equals("01")){
                sum+=6;
            }else{
                sum+=9;
            }
        }

        performance =((float)sum)/size;
    }

    public void setTempComfortable(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getTempIsOn().equals("1")){
            size+=4;
            if(Sound.getInstance().getTempDriveType().equals("0")){
                sum+=6;

            }else{
                sum+=2;

            }

            if(Sound.getInstance().getTempVolume().equals("00")){
                sum+=6;

            }else if(Sound.getInstance().getTempVolume().equals("01")){
                sum+=4;

            }else if(Sound.getInstance().getTempVolume().equals("10")){
                sum+=1;

            }

            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                sum+=5;

            }else{
                sum+=1;

            }

            if(Sound.getInstance().getTempBackVolume().equals("00")){
                sum+=10;
            }else if(Sound.getInstance().getTempBackVolume().equals("01")){
                sum+=6;
            }else if(Sound.getInstance().getTempBackVolume().equals("10")){
                sum+=3;
            }else{
                sum+=1;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getTempIsOn().equals("1")){
            size+=5;
            if(Transmission.getInstance().getTempType().equals("00")){
                sum+=5;

            }else if(Transmission.getInstance().getTempType().equals("01")){
                sum+=3;
            }else {
                sum+=1;
            }

            if(Transmission.getInstance().getTempGearRate().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getTempGearRate().equals("01")){
                sum+=6;
            }else{
                sum+=7;
            }

            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                sum+=8;
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")){
                sum+=6;
            }else{
                sum+=4;
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                sum+=3;
            }else{
                sum+=0;
            }

            if(Transmission.getInstance().getTempTransmissionMap().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getTempTransmissionMap().equals("01")){
                sum+=4;
            }else{
                sum+=2;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getTempIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getTempReducer().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Drive.getInstance().getTempStiffness().equals("00")){
                sum+=10;
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                sum+=6;
            }else{
                sum+=2;
            }
        }

        tempComfortable = ((float)sum)/size;
    }

    public void setTempLeading(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getTempIsOn().equals("1")){
            size+=2;


            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getTempBackVolume().equals("00")){
                sum+=0;
            }else if(Sound.getInstance().getTempBackVolume().equals("01")){
                sum+=4;
            }else if(Sound.getInstance().getTempBackVolume().equals("10")){
                sum+=7;
            }else{
                sum+=10;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getTempIsOn().equals("1")){
            size+=5;
            if(Transmission.getInstance().getTempType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getTempType().equals("01")){
                sum+=7;
            }else {
                sum+=8;
            }

            if(Transmission.getInstance().getTempGear().equals("000")){
                sum+=4;
            }else if(Transmission.getInstance().getTempGear().equals("001")){
                sum+=5;
            }else if(Transmission.getInstance().getTempGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                sum+=7;
            }else{
                sum+=8;
            }



            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                sum+=5;
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTempTransmissionMap().equals("00")){
                sum+=3;
            }else if(Transmission.getInstance().getTempTransmissionMap().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getTempIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getTempReducer().equals("00")){
                sum+=7;
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                sum+=6;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getTempStiffness().equals("00")){
                sum+=2;
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }
        }

        tempLeading = ((float)sum)/size;
    }


    public void setTempDynamic(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getTempIsOn().equals("1")){
            size+=4;

            if(Sound.getInstance().getTempDriveType().equals("0")){
                sum+=4;
            }else{
                sum+=7;
            }

            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getTempBackVolume().equals("00")){
                sum+=0;
            }else if(Sound.getInstance().getTempBackVolume().equals("01")){
                sum+=4;
            }else if(Sound.getInstance().getTempBackVolume().equals("10")){
                sum+=7;
            }else{
                sum+=10;
            }

            if(Sound.getInstance().getTempVolume().equals("00")){
                sum+=5;
            }else if(Sound.getInstance().getTempVolume().equals("01")){
                sum+=7;
            }else{
                sum+=9;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getTempIsOn().equals("1")){
            size+=6;
            if(Transmission.getInstance().getTempType().equals("00")){
                sum+=5;

            }else if(Transmission.getInstance().getTempType().equals("01")){
                sum+=7;
            }else {
                sum+=10;
            }

            if(Transmission.getInstance().getTempGear().equals("000")){
                sum+=4;
            }else if(Transmission.getInstance().getTempGear().equals("001")){
                sum+=5;
            }else if(Transmission.getInstance().getTempGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                sum+=7;
            }else{
                sum+=8;
            }

            if(Transmission.getInstance().getTempGearRate().equals("00")){
                sum+=9;
            }else if(Transmission.getInstance().getTempGearRate().equals("01")){
                sum+=7;
            }else{
                sum+=5;
            }



            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")){
                sum+=8;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                sum+=2;
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                sum+=6;
            }else{
                sum+=10;
            }

            if(Transmission.getInstance().getTempTransmissionMap().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTempTransmissionMap().equals("01")){
                sum+=8;
            }else{
                sum+=9;
            }
        }

        //가상 구동축 효과

        if(Drive.getInstance().getTempIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getTempReducer().equals("00")){
                sum+=10;
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                sum+=5;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getTempStiffness().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                sum+=5;
            }else{
                sum+=10;
            }
        }

        tempDynamic =((float)sum)/size;
    }

    public void setTempEfficiency(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과
        if(Sound.getInstance().getTempIsOn().equals("1")){
            size+=3;



            if(Sound.getInstance().getTempBackSensitive().equals("0")){
                sum+=9;
            }else{
                sum+=8;
            }

            if(Sound.getInstance().getTempBackVolume().equals("00")){
                sum+=8;
            }else if(Sound.getInstance().getTempBackVolume().equals("01")){
                sum+=7;
            }else if(Sound.getInstance().getTempBackVolume().equals("10")){
                sum+=6;
            }else{
                sum+=5;
            }

            if(Sound.getInstance().getTempVolume().equals("00")){
                sum+=8;
            }else if(Sound.getInstance().getTempVolume().equals("01")){
                sum+=7;
            }else{
                sum+=6;
            }
        }
        // 가상 변속 효과
        if(Transmission.getInstance().getTempIsOn().equals("1")){
            size+=4;
            if(Transmission.getInstance().getTempType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getTempType().equals("01")){
                sum+=4;
            }else {
                sum+=2;
            }

            if(Transmission.getInstance().getTempGear().equals("000")){
                sum+=8;
            }else if(Transmission.getInstance().getTempGear().equals("001")){
                sum+=7;
            }else if(Transmission.getInstance().getTempGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                sum+=5;
            }else{
                sum+=4;
            }





            if(Transmission.getInstance().getTempTransmissionSpeed().equals("00")){
                sum+=9;
            }else if(Transmission.getInstance().getTempTransmissionSpeed().equals("01")){
                sum+=8;
            }else{
                sum+=7;
            }

            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                sum+=8;
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                sum+=7;
            }else{
                sum+=6;
            }

        }

        //가상 구동축 효과

        if(Drive.getInstance().getTempIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getTempReducer().equals("00")){
                sum+=0;
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                sum+=6;
            }else{
                sum+=9;
            }

            if(Drive.getInstance().getTempStiffness().equals("00")){
                sum+=9;
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                sum+=7;
            }else{
                sum+=5;
            }
        }

        tempEfficiency = ((float)sum)/size;
    }

    public void setTempPerformance(){
        int size = 0;
        int sum=0;
        // 가상 음향 효과

        // 가상 변속 효과
        if(Transmission.getInstance().getTempIsOn().equals("1")){
            size+=4;
            if(Transmission.getInstance().getTempType().equals("00")){
                sum+=6;

            }else if(Transmission.getInstance().getTempType().equals("01")){
                sum+=5;
            }else {
                sum+=1;
            }

            if(Transmission.getInstance().getTempGear().equals("000")){
                sum+=8;
            }else if(Transmission.getInstance().getTempGear().equals("001")){
                sum+=7;
            }else if(Transmission.getInstance().getTempGear().equals("010")){
                sum+=6;
            }else if(Transmission.getInstance().getTempGear().equals("011")){
                sum+=5;
            }else{
                sum+=4;
            }
            if(Transmission.getInstance().getTempGearRate().equals("00")){
                sum+=6;
            }else if(Transmission.getInstance().getTempGearRate().equals("01")){
                sum+=8;
            }else{
                sum+=6;
            }


            if(Transmission.getInstance().getTempTransmissionPower().equals("00")){
                sum+=7;
            }else if(Transmission.getInstance().getTempTransmissionPower().equals("01")){
                sum+=6;
            }else{
                sum+=5;
            }

        }

        //가상 구동축 효과

        if(Drive.getInstance().getTempIsOn().equals("1")){
            size+=2;
            if(Drive.getInstance().getTempReducer().equals("00")){
                sum+=7;
            }else if(Drive.getInstance().getTempReducer().equals("01")){
                sum+=9;
            }else{
                sum+=1;
            }

            if(Drive.getInstance().getTempStiffness().equals("00")){
                sum+=1;
            }else if(Drive.getInstance().getTempStiffness().equals("01")){
                sum+=6;
            }else{
                sum+=9;
            }
        }

        tempPerformance =((float)sum)/size;
    }
    // Lazy Initailization
    public static synchronized CarData getInstance() {
        if(carData == null) {
            carData = new CarData();
        }
        return carData;
    }


}
