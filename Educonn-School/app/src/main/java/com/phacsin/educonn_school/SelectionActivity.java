package com.phacsin.educonn_school;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.customfonts.HelveticaButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 06-01-2017.
 */

public class SelectionActivity extends AppCompatActivity {
    HelveticaButton sbmt_sem_yr;
    ValueEventListener standard_listener;
    String standard_selected,year_selected,division_selected,institution_name;
    DatabaseReference mRef;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        final MaterialSpinner spinner_year = (MaterialSpinner) findViewById(R.id.spinner_year);
        final MaterialSpinner spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_standard);
        final MaterialSpinner spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division);
        final HelveticaButton submit = (HelveticaButton) findViewById(R.id.btn_submit);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        editor = sharedPreferences.edit();

        standard_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_standard = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    data_standard.add(postSnapshot.child("Name").getValue(String.class));
                Log.d("SS",data_standard.toString());

                if (data_standard.size() != 0) {
                    spinner_standard.setItems(data_standard);
                    spinner_standard.setError(null);

                    standard_selected = data_standard.get(0);
                    mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> data_division = new ArrayList<String>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                data_division.add(postSnapshot.child("Name").getValue(String.class));
                            if (data_division.size() != 0) {
                                spinner_division.setItems(data_division);
                                spinner_division.setError(null);
                            } else {
                                spinner_division.setItems(data_division);
                                spinner_division.setError("No Divisions available");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    spinner_standard.setItems(data_standard);
                    spinner_standard.setError("No Standards available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // First Time
        mRef.child("School").child(institution_name).child("Academic Year").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_year = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    data_year.add(postSnapshot.child("Name").getValue(String.class));
                if (data_year.size() != 0) {
                    spinner_year.setItems(data_year);
                    spinner_year.setError(null);
                    year_selected = data_year.get(0);
                    mRef.child("School").child(institution_name).child("Standard").child(year_selected).orderByChild("Name").addListenerForSingleValueEvent(standard_listener);
                } else {
                    spinner_year.setItems(data_year);
                    spinner_year.setError("No Years available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner_year.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                year_selected = item;
                mRef.child("School").child(institution_name).child("Academic Year").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data_year = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            data_year.add(postSnapshot.child("Name").getValue(String.class));
                        if (data_year.size() != 0) {
                            spinner_year.setItems(data_year);
                            spinner_year.setError(null);
                            mRef.child("School").child(institution_name).child("Standard").child(year_selected).orderByChild("Name").addListenerForSingleValueEvent(standard_listener);
                        } else {
                            spinner_year.setItems(data_year);
                            spinner_year.setError("No Years available");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        spinner_standard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                standard_selected = item;
                mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data_division = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            data_division.add(postSnapshot.child("Name").getValue(String.class));
                        if (data_division.size() != 0) {
                            division_selected = data_division.get(0);
                            spinner_division.setItems(data_division);
                            spinner_division.setError(null);
                        } else {
                            spinner_division.setItems(data_division);
                            spinner_division.setError("No Divisions available");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        spinner_division.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                division_selected = item;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                institution_name = institution_name.replaceAll("[^a-zA-Z0-9]","");
                if (sharedPreferences.contains("Academic Year") && sharedPreferences.contains("Standard") && sharedPreferences.contains("Division")) {
                    String year = sharedPreferences.getString("Academic Year", "").replaceAll("[^a-zA-Z0-9]", "");
                    String standard = sharedPreferences.getString("Standard", "").replaceAll("[^a-zA-Z0-9]", "");
                    String division = sharedPreferences.getString("Division", "").replaceAll("[^a-zA-Z0-9]", "");

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(institution_name + "_" + year);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(institution_name + "_" + year + "_" + standard);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(institution_name + "_" + year + "_" + standard + "_" + division);

                }
                year_selected = spinner_year.getItems().get(spinner_year.getSelectedIndex()).toString();
                standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();

                editor.putString("Academic Year", year_selected);
                editor.putString("Standard", standard_selected);
                editor.putString("Division", division_selected);
                editor.commit();
                year_selected = year_selected.replaceAll("[^a-zA-Z0-9]", "");
                standard_selected = standard_selected.replaceAll("[^a-zA-Z0-9]", "");
                division_selected = division_selected.replaceAll("[^a-zA-Z0-9]", "");
                FirebaseMessaging.getInstance().subscribeToTopic(institution_name + "_" + year_selected);
                FirebaseMessaging.getInstance().subscribeToTopic(institution_name + "_" + year_selected + "_" + standard_selected);
                FirebaseMessaging.getInstance().subscribeToTopic(institution_name + "_" + year_selected + "_" + standard_selected + "_" + division_selected);
                startActivity(new Intent(SelectionActivity.this,MainActivity.class));
            }
        });
    }
}