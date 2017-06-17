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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.MonthIndex;
import com.phacsin.student.R;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.recyclerviewAttendance.AdapterAttendanceDetails;
import com.phacsin.student.recyclerviewAttendance.DataAttendance;
import com.phacsin.student.recyclerviewmarks.DataMarks;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Bineesh P Babu on 26-11-2016.
 */

public class AttendanceFragment extends Fragment  {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<DataAttendance> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    HelveticaButton btn_sbmt;
    private DatabaseReference mref;
    SharedPreferences.Editor editor;
    String batch,semester,reg_no;
    List<String> month_list = new ArrayList<String>();
    int attended,total;
    SharedPreferences sharedPreferences;
    String institution_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        btn_sbmt = (HelveticaButton)rootView.findViewById(R.id.btn_submit_month);
        final MaterialSpinner spinner = (MaterialSpinner) rootView.findViewById(R.id.spinner);
        mref = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        //editor = sharedPreferences.edit();
        batch = sharedPreferences.getString("Batch","");
        reg_no = sharedPreferences.getString("Register Number","");
        semester = sharedPreferences.getString("Semester","");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterAttendanceDetails(data);
        recyclerView.setAdapter(adapter);


        mref.child("College").child(institution_name).child("Working Days").child(batch).child(semester).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> added_months = new ArrayList<String>();
                month_list.clear();
                data.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    String month_str = postSnapshot.getKey().substring(3,5);
                    Log.d("month",postSnapshot.getKey());
                    if(!added_months.contains(month_str))
                    {
                        added_months.add(MonthIndex.myMap.get(month_str));
                        month_list.add(MonthIndex.myMap.get(month_str));
                    }
                }
                Log.d("MONTh",month_list.toString());
                if(month_list.size()!=0)
                 spinner.setItems(month_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String month_selected = spinner.getItems().get(spinner.getSelectedIndex()).toString();
                mref.child("College").child(institution_name).child("Attendance").child(batch).child(semester).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        data.clear();
                        adapter.notifyDataSetChanged();
                        total = attended =0;
                        for(DataSnapshot subject : dataSnapshot.getChildren())
                        {
                            DataAttendance dataAttendance = new DataAttendance();
                            dataAttendance.subject_name = subject.getKey();
                            for(DataSnapshot date : subject.getChildren())
                            {
                                if(MonthIndex.myMap.get(date.getKey().substring(3,5)).equals(month_selected))
                                {
                                    if(date.hasChild(reg_no))
                                        attended++;
                                    total++;
                                }
                            }
                            dataAttendance.percentage = String.valueOf(Math.round((attended*100)/total)) + "%";
                            data.add(dataAttendance);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
/*
        myOnClickListener = new MyOnClickListener(this);
*/

        return rootView;
    }

}

