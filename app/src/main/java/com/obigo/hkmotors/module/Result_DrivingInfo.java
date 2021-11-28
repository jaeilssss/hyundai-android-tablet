package com.obigo.hkmotors.module;

import com.google.gson.annotations.SerializedName;

public class Result_DrivingInfo {
    @SerializedName("areaCodeInfo")
    areaCodeInfo areaCodeInfo;

    public class areaCodeInfo{
        @SerializedName("totalCnt") String totalCnt;
        @SerializedName("listCnt") String listCnt;
        @SerializedName("contFlag") String contFlag;
        @SerializedName("poiAreaCodes") poiAreaCodes poiAreaCodes;

        public String getTotalCnt(){return totalCnt;}

        public String getListCnt(){return listCnt;}

        public String getContFlag(){return contFlag;}

        public class poiAreaCodes{
            @SerializedName("areaDepth") String areaDepth;
            @SerializedName("largeCd") String largeCd;
            @SerializedName("middleCd") String middleCd;
            @SerializedName("smallCd") String smallCd;
            @SerializedName("districtName") String districtName;

            public String getAreaDepth(){return areaDepth;}

            public String getLargeCd(){return largeCd;}

            public String getMiddleCd(){return middleCd;}

            public String getSmallCd(){return smallCd;}

            public String getDistrictName(){return districtName;}
        }

        public poiAreaCodes getPoiAreaCodes(){return poiAreaCodes;}

    }

    public areaCodeInfo getAreaCodeInf(){return areaCodeInfo;}



}
