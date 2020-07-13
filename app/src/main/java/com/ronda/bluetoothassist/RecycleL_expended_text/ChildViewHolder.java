package com.ronda.bluetoothassist.RecycleL_expended_text;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ronda.bluetoothassist.R;

import androidx.recyclerview.widget.RecyclerView;

public class ChildViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private View view;
    private TextView childText;


    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final DataBean dataBean, final int pos){

        childText = (TextView) view.findViewById(R.id.content_child);
        childText.setText(dataBean.getChildTxt());


    }


}
