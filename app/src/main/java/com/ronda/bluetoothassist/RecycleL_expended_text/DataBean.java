package com.ronda.bluetoothassist.RecycleL_expended_text;

public class DataBean {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开
    private DataBean childBean;

    private String ID;
    private String parentTxt;
    private String parentRightTxt;
    private String childTxt;
    private String childRightTxt;
    private int parentImage;

    public void setParentImage(int parentImage) {
        this.parentImage = parentImage;
    }

    public int getParentImage() {
        return parentImage;
    }
    public String getParentTxt() {
        return parentTxt;

    }

    public void setParentTxt(String parentTxt) {

        this.parentTxt = parentTxt;
    }

    public String getChildTxt() {
        return childTxt;
    }

    public void setChildTxt(String childTxt) {
        this.childTxt = childTxt;
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public DataBean getChildBean() {
        return childBean;
    }

    public void setChildBean(DataBean childBean) {
        this.childBean = childBean;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }




}
