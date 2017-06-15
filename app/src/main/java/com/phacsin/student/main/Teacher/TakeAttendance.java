package com.phacsin.student.main.Teacher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phacsin.student.R;
import com.phacsin.student.main.admin.Profile_Admin_Edit;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
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
        mref.child("Students").child(batch).addValueEventListener(new ValueEventListener() {
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
                for(String reg_no:adapter.lstChk)
                    mref.child("Attendance").child(batch).child(semester).child(subject).child(date).child(reg_no).setValue(true);
                 mref.child("Working Days").child(batch).child(semester).child(date).setValue(true);
        Toast.makeText(getApplicationContext(),"Uploaded Attendance",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {

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

    private void checkBoxFunction() {
        if(select_all.isChecked())
            adapter.selectAll();
        else {
            adapter.isSelectedAll = false;
            adapter.notifyDataSetChanged();
        }
    }
}