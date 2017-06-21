package com.phacsin.educonn_school.main.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bineesh P Babu on 24-01-2017.
 */

public class EditActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialCalendarView calenderview;
    HelveticaButton btn_submit_attendance;
    private DatabaseReference mref;
    MaterialSpinner spinner_division,spinner_standard;
    ValueEventListener subject_change_listener;
    SharedPreferences sharedPreferences;
    String institution_name;
    String standard_selected,division_selected,subject_selected;
    private boolean valid_division = false;
    private String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select date and subject");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        mref = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        year = sharedPreferences.getString("Academic Year","");

        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division_take_attendance);
        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_standard_take_attendance);

        mref.child("School").child(institution_name).child("Standard").child(year).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_std = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_std.add(postSnapshot.child("Name").getValue(String.class));
                }
                Log.d("DS",dataSnapshot.toString());
                if(data_std.size()!=0) {
                    standard_selected = data_std.get(0);
                    spinner_standard.setError(null);
                    spinner_standard.setItems(data_std);
                    mref.child("School").child(institution_name).child("Division").child(year).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> data_div = new ArrayList<>();
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {
                                data_div.add(postSnapshot.child("Name").getValue(String.class));
                            }
                            if(data_div.size()!=0) {
                                spinner_division.setError(null);
                                spinner_division.setItems(data_div);
                                division_selected = data_div.get(0);
                                valid_division = true;
                            }
                            else
                            {
                                data_div.add("No Divisions available");
                                spinner_division.setItems(data_div);
                                spinner_division.setError("No Divisions available");
                                valid_division = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    data_std.add("No Standards available");
                    spinner_standard.setItems(data_std);
                    spinner_standard.setError("No Standards available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner_standard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                standard_selected = item;
                mref.child("School").child(institution_name).child("Division").child(year).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data_div = new ArrayList<>();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            data_div.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        if(data_div.size()!=0) {
                            spinner_division.setError(null);
                            spinner_division.setItems(data_div);
                            division_selected = data_div.get(0);
                            valid_division = true;
                        }
                        else
                        {
                            data_div.add("No Divisions available");
                            spinner_division.setItems(data_div);
                            spinner_division.setError("No Divisions available");
                            valid_division = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        btn_submit_attendance=(HelveticaButton)findViewById(R.id.btn_submit_take_attendance);
        calenderview = (MaterialCalendarView) findViewById(R.id.calendarView);


        btn_submit_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calenderview.getSelectedDate()==null)
                    Toast.makeText(getApplicationContext(),"No Date Selected",Toast.LENGTH_LONG).show();
                else {
                    if(valid_division) {
                        Intent i = new Intent(getApplicationContext(), TakeAttendance.class);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = calenderview.getSelectedDate().getDate();
                        i.putExtra("date", df.format(date));
                        i.putExtra("standard", spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString());
                        i.putExtra("division", spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString());
                        startActivity(i);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Division Invalid",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}