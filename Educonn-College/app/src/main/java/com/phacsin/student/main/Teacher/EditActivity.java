package com.phacsin.student.main.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.phacsin.student.R;
import com.phacsin.student.customfonts.HelveticaButton;
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
    MaterialSpinner spinner_semester, spinner_subject,spinner_batch;
    ValueEventListener subject_change_listener;
    SharedPreferences sharedPreferences;
    String institution_name;
    private boolean valid_subject;

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

        spinner_semester = (MaterialSpinner) findViewById(R.id.spinner_seme_take_attendance);
        spinner_subject = (MaterialSpinner) findViewById(R.id.spinner_subject_take_attendance);
        spinner_batch = (MaterialSpinner) findViewById(R.id.spinner_batch_take_attendance);

        mref.child("College").child(institution_name).child("Batch").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> spinner_list = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    spinner_list.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(spinner_list.size()!=0) {
                    spinner_batch.setItems(spinner_list);
                    mref.child("College").child(institution_name).child("Subject").child(spinner_list.get(0)).child(spinner_semester.getItems().get(0).toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> list = new ArrayList<String>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                list.add(postSnapshot.child("Name").getValue(String.class));
                            }
                            if(list.size()!=0) {
                                spinner_subject.setError(null);
                                spinner_subject.setItems(list);
                                valid_subject = true;
                            }
                            else {
                                list.add("No Subjects available");
                                spinner_subject.setItems(list);
                                spinner_subject.setError("No Subjects available");
                                valid_subject = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("FirebaseError",databaseError.toString());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseError",databaseError.toString());

            }
        });

        spinner_batch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                String semester_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                mref.child("College").child(institution_name).child("Subject").child(item).child(semester_selected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> list = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            list.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        if(list.size()!=0) {
                            spinner_subject.setError(null);
                            spinner_subject.setItems(list);
                            valid_subject = true;
                        }
                        else {
                            list.add("No Subjects available");
                            spinner_subject.setItems(list);
                            spinner_subject.setError("No Subjects available");
                            valid_subject = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("FirebaseError",databaseError.toString());
                    }
                });
            }
        });

        btn_submit_attendance=(HelveticaButton)findViewById(R.id.btn_submit_take_attendance);
        calenderview = (MaterialCalendarView) findViewById(R.id.calendarView);
        spinner_semester.setItems("Semester 1", "Semester 2", "Semester 3", "Semester 4","Semester 5","Semester 6","Semester 7","Semester 8");
        spinner_semester.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                String batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                mref.child("College").child(institution_name).child("Subject").child(batch_selected).child(item).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> list = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            list.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        if(list.size()!=0) {
                            spinner_subject.setError(null);
                            spinner_subject.setItems(list);
                            valid_subject = true;
                        }
                        else {
                            list.add("No Subjects available");
                            spinner_subject.setItems(list);
                            spinner_subject.setError("No Subjects available");
                            valid_subject = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("FirebaseError",databaseError.toString());
                    }
                });

            }
        });


        btn_submit_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calenderview.getSelectedDate()==null)
                    Toast.makeText(getApplicationContext(),"No Date Selected",Toast.LENGTH_LONG).show();
                else {
                    if(valid_subject) {
                        Intent i = new Intent(getApplicationContext(), TakeAttendance.class);
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = calenderview.getSelectedDate().getDate();
                        i.putExtra("date", df.format(date));
                        i.putExtra("batch", spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString());
                        i.putExtra("semester", spinner_semester.getItems().get(spinner_semester.getSelectedIndex()).toString());
                        i.putExtra("subject", spinner_subject.getItems().get(spinner_subject.getSelectedIndex()).toString());
                        startActivity(i);
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Subject Invalid",Toast.LENGTH_LONG).show();
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