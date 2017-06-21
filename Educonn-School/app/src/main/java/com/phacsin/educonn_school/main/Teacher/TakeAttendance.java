package com.phacsin.educonn_school.main.Teacher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.phacsin.educonn_school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class TakeAttendance extends AppCompatActivity {
    Toolbar toolbar;
    private static AttendanceTakeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<String> data;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private FloatingActionButton fab_submit;
    private CheckBox select_all;
    String date,division,standard;
    private DatabaseReference mref;
    SharedPreferences sharedPreferences;
    String institution_name,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        year = sharedPreferences.getString("Academic Year","");

        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(TakeAttendance.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Leave attendnce register")
                        .setConfirmText("Ok")
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog stay_here) {
                                stay_here.dismissWithAnimation();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog back_to) {
                                back_to.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        fab_submit = (FloatingActionButton) findViewById(R.id.submit_attendance_btn);
        select_all = (CheckBox) findViewById(R.id.select_all_checkbox);
        mref = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<String>();
        standard = getIntent().getStringExtra("standard");
        date = getIntent().getStringExtra("date");
        division = getIntent().getStringExtra("division");
        mref.child("School").child(institution_name).child("Students").child(year).child(standard).child(division).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data.add(postSnapshot.child("Name").getValue(String.class));
                }
                adapter = new AttendanceTakeAdapter(data,TakeAttendance.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("Reg_token",FirebaseInstanceId.getInstance().getToken());

        removedItems = new ArrayList<Integer>();

        fab_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFireBase();
            }
        });

        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxFunction();
            }
        });

    }

    private void uploadToFireBase() {
        for(String name:adapter.lstChk) {
                    Log.d("SMD",name);
                    mref.child("School").child(institution_name).child("Attendance").child(year).child(standard).child(division).child(date).child(name).setValue(true);
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("title","Attendance Notification : " + date);
                    map.put("message","The student " + name + " has attended the class on "  + date);
                    map.put("name",name);
                    map.put("institution_name",institution_name);
                    map.put("institution_type","School");
                    map.put("year",year);
                    map.put("standard",standard);
                    map.put("division",division);
                    mref.child("Notification Requests").child("Single").push().setValue(map);
                }
                Log.d("LST",adapter.lstChk.toString());
                Log.d("DATA",data.toString());
                data.removeAll(adapter.lstChk);
                for(String name:data) {
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("title","Attendance Notification : " + date);
                    map.put("message","The student " + name + " has not attended the class  on "  + date);
                    map.put("name",name);
                    map.put("institution_name",institution_name);
                    map.put("institution_type","School");
                    map.put("year",year);
                    map.put("standard",standard);
                    map.put("division",division);
                    mref.child("Notification Requests").child("Single").push().setValue(map);
                }
                 mref.child("School").child(institution_name).child("Working Days").child(year).child(date).setValue(true);
        Toast.makeText(getApplicationContext(),"Uploaded Attendance",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(TakeAttendance.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Leave attendance register")
                .setConfirmText("Ok")
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog stay_here) {
                        stay_here.dismissWithAnimation();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog back_to) {
                        back_to.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }

    private void checkBoxFunction() {
        if(select_all.isChecked())
            adapter.selectAll();
        else {
            adapter.isSelectedAll = false;
            adapter.notifyDataSetChanged();
        }
    }
}