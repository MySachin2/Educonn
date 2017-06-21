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
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class AdapterAdminStaff extends RecyclerView.Adapter<AdapterAdminStaff.MyViewHolder> {

    public List<String> dataSet;

    public List<String> selectedList;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        public CardView cardview1;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.subject);
            cardview1 = (CardView) itemView.findViewById(R.id.card_view_admin);
        }

    }

    public AdapterAdminStaff(List<String> data, Context context, List<String> selectedList) {
        this.dataSet = data;
        this.mContext=context;
        this.selectedList=selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_staff_card, parent, false);
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
        if(selectedList.contains(dataSet.get(listPosition)))
            holder.cardview1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.cardview1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
        textViewName.setText(dataSet.get(listPosition));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}


