package com.ronda.bluetoothassist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JingbaoAdapter extends RecyclerView.Adapter <JingbaoAdapter.ViewHolder>{

    private List<Jingbao_data> dataJingbao;
    private Context context;
    public JingbaoAdapter( List<Jingbao_data> datalist){
        dataJingbao = datalist;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView JingbaoImageL;
        ImageView JingbaoImageR;
        TextView textView_context;
        TextView textView_num;
        LinearLayout ll ;
        public ViewHolder(View view)
        {
            super(view);
            JingbaoImageL = view.findViewById(R.id.jingbao_picleft);
            JingbaoImageR = view.findViewById(R.id.jingbao_picright);
            textView_context = view.findViewById(R.id.jingbao_text);
            textView_num = view.findViewById(R.id.jingbao_num);
            ll = view.findViewById(R.id.lay_jingbao);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jingbao,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Jingbao_data j_data = dataJingbao.get(position);
        holder.JingbaoImageL.setImageResource(j_data.getLeftimageid());
        holder.JingbaoImageR.setImageResource(j_data.getRightimageid());
        holder.textView_context.setText(j_data.getName());
        holder.textView_num.setText(j_data.getNum());

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ContentActivity.class);
                intent.putExtra("type",""+position);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataJingbao.size();
    }



}
