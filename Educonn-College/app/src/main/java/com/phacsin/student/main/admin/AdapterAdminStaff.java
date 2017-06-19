package com.phacsin.student.main.admin;

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
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class AdapterAdminStaff extends RecyclerView.Adapter<AdapterAdminStaff.MyViewHolder> {

    private List<String> dataSet;
    public CardView cardview1;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.subject);
        }

    }

    public AdapterAdminStaff(List<String> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_staff_card, parent, false);
        cardview1 = (CardView) view1.findViewById(R.id.card_view_admin);
        MyViewHolder myViewHolder = new MyViewHolder(view1);
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

        TextView textViewName = holder.textViewName;
        cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i= new Intent(view.getContext(),AttendanceCalender.class);
                view.getContext().startActivity(i);*/
               // Snackbar.make(view, "Details updated soon ", Snackbar.LENGTH_LONG).show();
            }
        });
        textViewName.setText(dataSet.get(listPosition));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


