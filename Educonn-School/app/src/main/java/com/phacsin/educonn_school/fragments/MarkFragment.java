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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.recyclerviewmarks.AdapterMarkDetails;
import com.phacsin.educonn_school.recyclerviewmarks.DataMarks;

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
    private DatabaseReference mref;
    private String year,standard,division,name;
    private Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;
    MaterialSpinner spinner_subject;
    private boolean valid_subject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mark, container, false);
        mref = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        btn_sbmt = (HelveticaButton) rootView.findViewById(R.id.btn_submit_month);
        spinner_subject = (MaterialSpinner) rootView.findViewById(R.id.spinner_subject);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        sharedPreferences = getActivity().getSharedPreferences("prefs",MODE_PRIVATE);

        year = sharedPreferences.getString("Academic Year","");
        name = sharedPreferences.getString("Name","");
        standard = sharedPreferences.getString("Standard","");
        division = sharedPreferences.getString("Division","");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AdapterMarkDetails(data);
        recyclerView.setAdapter(adapter);

        data.clear();
        adapter.notifyDataSetChanged();
        mref.child("School").child(institution_name).child("Subject").child(year).child(standard).child(division).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_div = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_div.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(data_div.size()!=0) {
                    valid_subject =true;
                    spinner_subject.setError(null);
                    spinner_subject.setItems(data_div);
                }
                else
                {
                    valid_subject =false;
                    data_div.add("No Subjects available");
                    spinner_subject.setItems(data_div);
                    spinner_subject.setError("No Subjects available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_subject) {
                    String subject = spinner_subject.getItems().get(spinner_subject.getSelectedIndex()).toString();
                    mref.child("School").child(institution_name).child("Mark").child(year).child(standard).child(division).child(subject).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            data.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                DataMarks dataMarks = new DataMarks();
                                dataMarks.test_name = postSnapshot.getKey();
                                String marks = postSnapshot.child(name).child("Marks").getValue(String.class);
                                String total = postSnapshot.child("Total").getValue(String.class);
                                Log.d("dSA",name);
                                Log.d("total",total);
                                if(marks!=null) {
                                    if (!marks.equals("Absent"))
                                        dataMarks.marks = marks + "/" + total;
                                    else
                                        dataMarks.marks = "Ab";
                                    data.add(dataMarks);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(getActivity(), "Subject is Invalid", Toast.LENGTH_LONG).show();

            }
        });


        return rootView;
    }

}

