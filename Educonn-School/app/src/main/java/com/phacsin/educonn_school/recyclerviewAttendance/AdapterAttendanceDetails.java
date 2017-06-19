package com.phacsin.educonn_school.recyclerviewAttendance;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.educonn_school.R;

import java.util.List;

/**
 * Created by Bineesh P Babu on 07-01-2017.
 */

public class AdapterAttendanceDetails extends RecyclerView.Adapter<AdapterAttendanceDetails.MyViewHolder> {

    private List<DataAttendance> dataSet;
    public CardView cardview1;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.subject);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
        }

    }

    public AdapterAttendanceDetails(List<DataAttendance> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_attendance_fragment, parent, false);
        cardview1 =(CardView) view.findViewById(R.id.card_view1);
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

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i= new Intent(view.getContext(),AttendanceCalender.class);
                view.getContext().startActivity(i);*/
                Snackbar.make(view, "Details updated soon ", Snackbar.LENGTH_LONG).show();
            }
        });
        textViewName.setText(dataSet.get(listPosition).subject_name);
        textViewVersion.setText(dataSet.get(listPosition).percentage);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}