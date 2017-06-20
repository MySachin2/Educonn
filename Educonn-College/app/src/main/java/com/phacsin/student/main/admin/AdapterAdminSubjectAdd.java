package com.phacsin.student.main.admin;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

public class AdapterAdminSubjectAdd extends RecyclerView.Adapter<AdapterAdminSubjectAdd.MyViewHolder> {

    public List<String> dataSet;
    public List<String> selectedList;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject;
        public CardView cardview2;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewSubject = (TextView) itemView.findViewById(R.id.subject);
            cardview2 = (CardView) itemView.findViewById(R.id.card_view_admin);
        }

    }

    public AdapterAdminSubjectAdd(List<String> data, Context context, List<String> selectedList) {
        this.dataSet = data;
        this.mContext=context;
        this.selectedList=selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin_subject_add_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewSubject = holder.textViewSubject;
        Log.d("SIZE",dataSet.size()+"");
        textViewSubject.setText(dataSet.get(listPosition));
        if(selectedList.contains(dataSet.get(listPosition)))
            holder.cardview2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.cardview2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
