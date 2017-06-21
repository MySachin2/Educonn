package com.phacsin.educonn_school.fragments;

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
import com.phacsin.educonn_school.MonthIndex;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.recyclerviewAttendance.AdapterAttendanceDetails;
import com.phacsin.educonn_school.recyclerviewAttendance.DataAttendance;

import java.util.ArrayList;
import java.util.Collections;
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
    String year,standard,division,name;
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
        year = sharedPreferences.getString("Academic Year","");
        name = sharedPreferences.getString("Name","");
        standard = sharedPreferences.getString("Standard","");
        division = sharedPreferences.getString("Division","");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterAttendanceDetails(data);
        recyclerView.setAdapter(adapter);


        mref.child("School").child(institution_name).child("Working Days").child(year).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> added_months = new ArrayList<String>();
                month_list.clear();
                data.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Log.d("month",postSnapshot.getKey());
                    String month_str = postSnapshot.getKey().substring(3,5);
                    if(!added_months.contains(MonthIndex.myMap.get(month_str)))
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


        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String month_selected = spinner.getItems().get(spinner.getSelectedIndex()).toString();
                mref.child("School").child(institution_name).child("Attendance").child(year).child(standard).child(division).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        data.clear();
                        adapter.notifyDataSetChanged();
                        for(DataSnapshot date : dataSnapshot.getChildren())
                        {
                            if(MonthIndex.myMap.get(date.getKey().substring(3,5)).equals(month_selected))
                            {
                                DataAttendance dataAttendance = new DataAttendance();
                                dataAttendance.day = date.getKey();
                                if(date.hasChild(name))
                                {
                                    dataAttendance.status = "Pr";
                                }
                                else
                                    dataAttendance.status = "Ab";
                                data.add(dataAttendance);
                            }
                        }
                        Collections.reverse(data);
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

