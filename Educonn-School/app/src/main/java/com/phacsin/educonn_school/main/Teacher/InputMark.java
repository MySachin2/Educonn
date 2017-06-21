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
import com.phacsin.educonn_school.customfonts.HelveticaEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class InputMark  extends AppCompatActivity {
    HelveticaButton btn_input_mark;
    HelveticaEditText edittext_mark,out_of_mark;
    private DatabaseReference mref;
    MaterialSpinner spinner_standard, spinner_division, spinner_subject;
    private ValueEventListener subject_change_listener;
    SharedPreferences sharedPreferences;
    String institution_name,year;
    boolean valid_subject;
    String standard_selected,division_selected;

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
        year = sharedPreferences.getString("Academic Year","");

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
                if (verify_out_of_mark()){
                    if (valid_subject) {
                        final String standard = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                        final String division = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                        mref.child("School").child(institution_name).child("Mark").child(year).child(standard).child(division).orderByKey().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Intent i = new Intent(getApplicationContext(), InputMarkList.class);
                                i.putExtra("standard",standard );
                                i.putExtra("division", division);
                                i.putExtra("test_name", "Test " + (dataSnapshot.getChildrenCount()+1));
                                i.putExtra("subject", spinner_subject.getItems().get(spinner_subject.getSelectedIndex()).toString());
                                i.putExtra("total", edittext_mark.getText().toString());
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    else
                        Toast.makeText(getApplicationContext(), "Subject is Invalid", Toast.LENGTH_LONG).show();

                }
                else {
                    out_of_mark = (HelveticaEditText) findViewById(R.id.out_of_mark);
                    out_of_mark.setError("Enter Out Of Mark");
                }
            }
        });
        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_mark_add_class);
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_mark_add_division);

        spinner_subject = (MaterialSpinner) findViewById(R.id.spinner_mark_add_subject);

        mref.child("School").child(institution_name).child("Standard").child(year).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_std = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_std.add(postSnapshot.child("Name").getValue(String.class));
                }
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
                                mref.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        List<String> data_div = new ArrayList<>();
                                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                        {
                                            data_div.add(postSnapshot.child("Name").getValue(String.class));
                                        }
                                        if(data_div.size()!=0) {
                                            spinner_subject.setError(null);
                                            spinner_subject.setItems(data_div);
                                            valid_subject =true;
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
                            }
                            else
                            {
                                data_div.add("No Divisions available");
                                spinner_division.setItems(data_div);
                                spinner_division.setError("No Divisions available");
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
                            mref.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
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
                        }
                        else
                        {
                            data_div.add("No Divisions available");
                            spinner_division.setItems(data_div);
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
                mref.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
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
            }
        });

    }

    private boolean verify_out_of_mark(){
        out_of_mark=(HelveticaEditText)findViewById(R.id.out_of_mark);
        if(out_of_mark.getText().toString().isEmpty()){
            return false;
        }
        else return  true;
    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}