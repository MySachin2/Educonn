package com.phacsin.student.main.admin;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.student.R;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterAdminSubject extends RecyclerView.Adapter<AdapterAdminSubject.MyViewHolder> {

    private List<DataSubject> dataSet;
    public CardView cardview2;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject;
        TextView textViewStaff;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewSubject = (TextView) itemView.findViewById(R.id.subject);
            this.textViewStaff = (TextView) itemView.findViewById(R.id.staff);
        }

    }

    public AdapterAdminSubject(List<DataSubject> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_subject_card, parent, false);
        cardview2 = (CardView) view.findViewById(R.id.card_view_admin);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewSubject = holder.textViewSubject;
        TextView textViewStaff = holder.textViewStaff;
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i= new Intent(view.getContext(),AttendanceCalender.class);
                view.getContext().startActivity(i);*/
                Snackbar.make(view, "Details updated soon ", Snackbar.LENGTH_LONG).show();
            }
        });

        textViewSubject.setText(dataSet.get(listPosition).name);
        textViewStaff.setText(dataSet.get(listPosition).teacher);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
