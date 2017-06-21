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

import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterAdminSubject extends RecyclerView.Adapter<AdapterAdminSubject.MyViewHolder> {

    public List<DataSubject> dataSet;
    public List<DataSubject> selectedList;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject;
        TextView textViewStaff;
        public CardView cardview2;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewSubject = (TextView) itemView.findViewById(R.id.subject);
            this.textViewStaff = (TextView) itemView.findViewById(R.id.staff);
            cardview2 = (CardView) itemView.findViewById(R.id.card_view_admin);

        }

    }

    public AdapterAdminSubject(List<DataSubject> data, Context context, List<DataSubject> selectedList) {
        this.dataSet = data;
        this.mContext=context;
        this.selectedList=selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_subject_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewSubject = holder.textViewSubject;
        TextView textViewStaff = holder.textViewStaff;
        if(selectedList.contains(dataSet.get(listPosition)))
            holder.cardview2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.cardview2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

        textViewSubject.setText(dataSet.get(listPosition).name);
        textViewStaff.setText(dataSet.get(listPosition).teacher);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
