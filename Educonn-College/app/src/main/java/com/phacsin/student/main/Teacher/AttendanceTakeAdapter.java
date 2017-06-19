package com.phacsin.student.main.Teacher;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.phacsin.student.R;

import java.util.ArrayList;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AttendanceTakeAdapter extends RecyclerView.Adapter<AttendanceTakeAdapter.MyViewHolder> {

    private final Activity activity;
    private ArrayList<String> dataSet;
    public CardView cardview4;
    boolean isSelectedAll = false;
    ArrayList<String> lstChk= new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        CheckBox select_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.subject);
            select_item = (CheckBox) itemView.findViewById(R.id.checkbox_take_attendance);
            select_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
/*
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
*/
        }

    }
    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }


    public AttendanceTakeAdapter(ArrayList<String> data, Activity activity) {
        this.dataSet = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_take_attendance_card, parent, false);
        cardview4 = (CardView) view.findViewById(R.id.card_view_admin);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /*public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view1);
        }
    }
*/


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        holder.textViewName.setText(dataSet.get(listPosition));
        if (!isSelectedAll) holder.select_item.setChecked(false);
        else holder.select_item.setChecked(true);

        if(lstChk.contains(dataSet.get(listPosition)))
        {
            holder.select_item.setChecked(true);
        }

        holder.select_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    compoundButton.setChecked(true);
                    lstChk.add(dataSet.get(listPosition));
                }
                else
                {
                    compoundButton.setChecked(false);
                    lstChk.remove(dataSet.get(listPosition));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
