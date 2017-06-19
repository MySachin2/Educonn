package com.phacsin.student.main.Teacher;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.phacsin.student.R;

import java.util.ArrayList;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class AdapterMarkAdding extends RecyclerView.Adapter<AdapterMarkAdding.MyViewHolder> {

    private ArrayList<String> dataSet;


    MarkClass[] lstChk;
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        EditText marks;
        MyCustomEditTextListener myCustomEditTextListener;

        public MyViewHolder(View itemView , MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            this.myCustomEditTextListener = myCustomEditTextListener;
            textViewName = (TextView) itemView.findViewById(R.id.subject);
            marks = (EditText) itemView.findViewById(R.id.input_marks);
            marks.addTextChangedListener(myCustomEditTextListener);
        }

    }

    public AdapterMarkAdding(ArrayList<String> data) {
        this.dataSet = data;
        lstChk = new MarkClass[dataSet.size()];
        for(int i=0;i<dataSet.size();i++)
        {
            MarkClass markClass = new MarkClass();
            markClass.reg_no = dataSet.get(i);
            markClass.marks="";
            lstChk[i]=markClass;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_input_mark_list_card, parent, false);
        return new MyViewHolder(view , new MyCustomEditTextListener());
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        textViewName.setText(dataSet.get(listPosition));
        MarkClass markClass = new MarkClass();
        markClass.marks = holder.marks.getText().toString();
        markClass.reg_no = holder.textViewName.getText().toString();
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

         void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            MarkClass markClass = new MarkClass();
            markClass.reg_no = dataSet.get(position);
            markClass.marks = charSequence.toString();
            lstChk[position] = markClass;
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
