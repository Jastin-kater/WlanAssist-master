package com.ronda.bluetoothassist;


public class Jingbao_data {
private String name;
private String num;
private int leftimageid;
private int rightimageid;

    public String getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setLeftimageid(int leftimageid) {
        this.leftimageid = leftimageid;
    }

    public void setRightimageid(int rightimageid) {
        this.rightimageid = rightimageid;
    }

    public Jingbao_data(String name, String num, int leftimageid, int rightimageid){

        this.leftimageid = leftimageid;
        this.rightimageid = rightimageid;
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public int getLeftimageid() {
        return leftimageid;
    }

    public int getRightimageid() {
        return rightimageid;
    }
}
