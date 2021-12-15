package com.obigo.hkmotors.model;

public class FavoriteData {
    int id;
    String title;
    String date;
    String signal1;
    String signal2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignal1() {
        return signal1;
    }

    public void setSignal1(String signal1) {
        this.signal1 = signal1;
    }

    public String getSignal2() {
        return signal2;
    }

    public void setSignal2(String signal2) {
        this.signal2 = signal2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
