package com.phacsin.student.recyclerviewmarks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.student.R;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;

/**
 * Created by Bineesh P Babu on 09-01-2017.
 */

public class AdapterMarkDetails extends RecyclerView.Adapter<AdapterMarkDetails.MyViewHolder> {

    private ArrayList<DataMarks> dataSet;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMarks;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.subject);
            this.textViewMarks = (TextView) itemView.findViewById(R.id.textViewMark);
        }

    }

    public AdapterMarkDetails(ArrayList<DataMarks> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_marks_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewMarks;

        textViewName.setText(dataSet.get(listPosition).subject_name);
        textViewVersion.setText(dataSet.get(listPosition).marks);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


