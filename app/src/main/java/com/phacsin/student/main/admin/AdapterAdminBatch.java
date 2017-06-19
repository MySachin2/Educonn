package com.phacsin.student.main.admin;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.student.R;

import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterAdminBatch extends RecyclerView.Adapter<AdapterAdminBatch.MyViewHolder> {

    private List<String> dataSet;
    public CardView cardview2;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBatch;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewBatch = (TextView) itemView.findViewById(R.id.batch);
        }

    }

    public AdapterAdminBatch(List<String> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_batch_card, parent, false);
        cardview2 = (CardView) view.findViewById(R.id.card_view_admin);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewSubject = holder.textViewBatch;
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i= new Intent(view.getContext(),AttendanceCalender.class);
                view.getContext().startActivity(i);*/
                Snackbar.make(view, "Details updated soon ", Snackbar.LENGTH_LONG).show();
            }
        });
        Log.d("SIZE",dataSet.size()+"");
        textViewSubject.setText(dataSet.get(listPosition));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
