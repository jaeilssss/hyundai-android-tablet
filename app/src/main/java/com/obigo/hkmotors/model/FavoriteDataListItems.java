package com.obigo.hkmotors.model;


/**
 * Class for the group data of my favorite
 */
public class FavoriteDataListItems {

    private String id = "";             // database _id
    private boolean isChecked = false;  // checkbox state
    private String title = "";          // title
    private String date = "";           // saved date
    private String param = "";          //e:100:5:0:4:2:160:32:50 ( torque:acc:decel:brake:energy:speed:current:eco-sprot )
    private String resp = "";           //2.5:2.5:2.5:2.5:2.5 ( 5개의 결과 값 정보 )

    public FavoriteDataListItems() {
    } // end of constructor

    public FavoriteDataListItems(String id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.param = param;
        this.resp = resp;
    } // end of constructor

    //////////////////////////////////////////////////////////////////////////////////
    // SET METHOD
    ////////////////////////////////////////////////////////////////////////////////////
    public void setId(String id) {
        this.id = id;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // GET METHOD
    ////////////////////////////////////////////////////////////////////////////////////
    public String getId() {
        return id;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getParam() {
        return param;
    }

    public String getResp() {
        return resp;
    }
}
