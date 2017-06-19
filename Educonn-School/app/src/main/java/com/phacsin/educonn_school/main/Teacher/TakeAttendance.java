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
    String date,semester,subject,batch;
    private DatabaseReference mref;
    SharedPreferences sharedPreferences;
    String institution_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
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
        batch = getIntent().getStringExtra("batch");
        date = getIntent().getStringExtra("date");
        semester = getIntent().getStringExtra("semester");
        subject = getIntent().getStringExtra("subject");
        mref.child("College").child(institution_name).child("Students").child(batch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data.add(postSnapshot.child("Registration Number").getValue(String.class));
                }
                adapter = new AttendanceTakeAdapter(data,TakeAttendance.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                for(String reg_no:adapter.lstChk) {
                    Log.d("SMD",reg_no);
                    mref.child("College").child(institution_name).child("Attendance").child(batch).child(semester).child(subject).child(date).child(reg_no).setValue(true);
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("title","Attendance Notification : " + date);
                    map.put("message","The student with Register Number " + reg_no + " has attended the class of " + subject + " on "  + date);
                    map.put("reg_no",reg_no);
                    map.put("institution_name",institution_name);
                    map.put("institution_type","College");
                    map.put("batch",batch);
                    mref.child("Notification Requests").child("Single").push().setValue(map);
                }
                Log.d("LST",adapter.lstChk.toString());
                Log.d("DATA",data.toString());
                data.removeAll(adapter.lstChk);
                for(String reg_no:data) {
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("title","Attendance Notification : " + date);
                    map.put("message","The student with Register Number " + reg_no + " has not attended the class of " + subject + " on "  + date);
                    map.put("reg_no",reg_no);
                    map.put("institution_name",institution_name);
                    map.put("institution_type","College");
                    map.put("batch",batch);
                    mref.child("Notification Requests").child("Single").push().setValue(map);
                }
                 mref.child("College").child(institution_name).child("Working Days").child(batch).child(semester).child(date).setValue(true);
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