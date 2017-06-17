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
import com.phacsin.student.customfonts.HelveticaEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class InputMark  extends AppCompatActivity {
    HelveticaButton btn_input_mark;
    HelveticaEditText edittext_mark;
    private DatabaseReference mref;
    MaterialSpinner spinner_batch, spinner_semester, spinner_subject, spinner_sessional;
    private ValueEventListener subject_change_listener;
    SharedPreferences sharedPreferences;
    String institution_name;
    boolean valid_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_mark_add);
        mref = FirebaseDatabase.getInstance().getReference();
        edittext_mark =(HelveticaEditText)findViewById(R.id.out_of_mark);
        edittext_mark.setFocusable(false);
        edittext_mark.setFocusableInTouchMode(true);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        btn_input_mark=(HelveticaButton)findViewById(R.id.btn_submit_input_mark);
        btn_input_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_subject) {
                    Intent i = new Intent(getApplicationContext(), InputMarkList.class);
                    i.putExtra("batch", spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString());
                    i.putExtra("semester", spinner_semester.getItems().get(spinner_semester.getSelectedIndex()).toString());
                    i.putExtra("subject", spinner_subject.getItems().get(spinner_subject.getSelectedIndex()).toString());
                    i.putExtra("sessional", spinner_sessional.getItems().get(spinner_sessional.getSelectedIndex()).toString());
                    i.putExtra("total", edittext_mark.getText().toString());
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                else
                    Toast.makeText(getApplicationContext(),"Subject is Invalid",Toast.LENGTH_LONG).show();

            }
        });
        spinner_batch = (MaterialSpinner) findViewById(R.id.spinner_mark_add_year);
        spinner_semester = (MaterialSpinner) findViewById(R.id.spinner_mark_add_sem);

        spinner_subject = (MaterialSpinner) findViewById(R.id.spinner_mark_add_subject);
        spinner_sessional = (MaterialSpinner) findViewById(R.id.spinner_mark_add_sessional);
        spinner_sessional.setItems("Sessional 1","Sessional 2","Sessional 3");

        mref.child("College").child(institution_name).child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                }
                spinner_batch.setItems(list);
                //Defaullt Subject
                mref.child("College").child(institution_name).child("Subject").child(list.get(0)).child(spinner_semester.getItems().get(0).toString()).addValueEventListener(new ValueEventListener() {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error",databaseError.toString());
            }
        });

        spinner_batch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
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

                        }
                    });

            }
        });

        /*
        mref.child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("value",dataSnapshot.toString());
                List<String> list = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                }
                spinner_semester.setItems(list);
                spinner_semester.setSelectedIndex(0);
                mref.child("Subject").child(list.get(0)).orderByKey().addListenerForSingleValueEvent(subject_change_listener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error",databaseError.toString());
            }
        });*/
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}