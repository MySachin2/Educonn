package com.phacsin.student.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.R;

import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.recyclerview.DataModel;
import com.phacsin.student.recyclerviewAttendance.DataAttendance;
import com.phacsin.student.recyclerviewmarks.AdapterMarkDetails;
import com.phacsin.student.recyclerviewmarks.DataMarks;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bineesh P Babu on 26-11-2016.
 */

public class MarkFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataMarks> data = new ArrayList<>();
    private static ArrayList<Integer> removedItems;
    HelveticaButton btn_sbmt;
    private static AdapterMarkDetails adapter;
    String batch,reg_no;
    private DatabaseReference mref;
    private String semester;
    private Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mark, container, false);
        mref = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        btn_sbmt = (HelveticaButton) rootView.findViewById(R.id.btn_submit_month);
        final MaterialSpinner spinner = (MaterialSpinner) rootView.findViewById(R.id.spinner);
        spinner.setItems("Sessional 1", "Sessional 2", "Sessional 3");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        sharedPreferences = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AdapterMarkDetails(data);
        recyclerView.setAdapter(adapter);

        data.clear();
        adapter.notifyDataSetChanged();

        batch = sharedPreferences.getString("Batch","");
        reg_no = sharedPreferences.getString("Register Number","");
        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = sharedPreferences.getString("Semester","");
                String sessional = spinner.getItems().get(spinner.getSelectedIndex()).toString();
                mref.keepSynced(true);
                mref.child("College").child(institution_name).child("Mark").child(batch).child(semester).child(sessional).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        data.clear();
                        adapter.notifyDataSetChanged();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            DataMarks dataMarks = new DataMarks();
                            dataMarks.subject_name = postSnapshot.getKey();
                            String mark_str = postSnapshot.child(reg_no).child("Marks").getValue(String.class);
                            if(!mark_str.equals("Absent"))
                                dataMarks.marks = mark_str.substring(0,2) + "/"  + mark_str.substring(10,12);
                            else
                                dataMarks.marks = "Ab";
                            data.add(dataMarks);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        return rootView;
    }

}

