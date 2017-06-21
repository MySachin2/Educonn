package com.phacsin.educonn_school.main.admin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.phacsin.educonn_school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterAdminStudent extends RecyclerView.Adapter<AdapterAdminStudent.MyViewHolder> {

    public ArrayList<String> dataSet;
    public List<String> selectedList;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        CardView cardview3;

/*
        TextView textViewVersion;
*/


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.name);
            cardview3 = (CardView) itemView.findViewById(R.id.card_view_admin);

/*
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
*/
        }

    }

    public AdapterAdminStudent(ArrayList<String> data, Context context, List<String> selectedList) {
        this.dataSet = data;
        this.mContext=context;
        this.selectedList=selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_student_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;


        textViewName.setText(dataSet.get(listPosition));
        if(selectedList.contains(dataSet.get(listPosition)))
            holder.cardview3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.cardview3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

      /*  textViewVersion.setText(dataSet.get(listPosition).getVersion()); */   }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

