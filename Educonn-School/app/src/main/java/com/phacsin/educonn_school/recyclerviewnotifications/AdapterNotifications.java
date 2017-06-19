package com.phacsin.educonn_school.recyclerviewnotifications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phacsin.educonn_school.R;

import java.util.List;

/**
 * Created by Bineesh P Babu on 08-01-2017.
 */

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyViewHolder> {

    private List<DataModelNotification> dataSet;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        TextView date;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.subject);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.date = (TextView) itemView.findViewById(R.id.data_time);

        }

    }

    public AdapterNotifications(List<DataModelNotification> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification_fragment, parent, false);

/*
        view.setOnClickListener(NotificationFragment.myOnClickListener);
*/
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        TextView date = holder.date;


        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getBody());
        date.setText(dataSet.get(listPosition).getDate());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}