package com.ronda.bluetoothassist.RecycleL_expended_text;

public interface ItemClickListener {

    /**
     * 展开子Item
     * @param bean
     */
    void onExpandChildren(DataBean bean);

    /**
     * 隐藏子Item
     * @param bean
     */
    void onHideChildren(DataBean bean);


}
