package com.phacsin.educonn_school.main.admin;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.educonn_school.R;

import java.util.ArrayList;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterAdminStudent extends RecyclerView.Adapter<AdapterAdminStudent.MyViewHolder> {

    private ArrayList<DataStudent> dataSet;
    public CardView cardview3;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewReg;
/*
        TextView textViewVersion;
*/


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.name);
            this.textViewReg = (TextView) itemView.findViewById(R.id.registration);

/*
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
*/
        }

    }

    public AdapterAdminStudent(ArrayList<DataStudent> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_student_card, parent, false);
        cardview3 = (CardView) view.findViewById(R.id.card_view_admin);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewReg = holder.textViewReg;

        /*TextView textViewVersion = holder.textViewVersion;*/
        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i= new Intent(view.getContext(),AttendanceCalender.class);
                view.getContext().startActivity(i);*/
                Snackbar.make(view, "Details updated soon ", Snackbar.LENGTH_LONG).show();
            }
        });

        textViewName.setText(dataSet.get(listPosition).name);
        textViewReg.setText(dataSet.get(listPosition).reg_no);

      /*  textViewVersion.setText(dataSet.get(listPosition).getVersion()); */   }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

