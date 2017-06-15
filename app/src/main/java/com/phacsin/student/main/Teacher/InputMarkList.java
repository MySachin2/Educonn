package com.phacsin.student.main.Teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phacsin.student.R;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class InputMarkList extends AppCompatActivity {
    private static AdapterMarkAdding adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<String> data;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    TextView sessional_text,semester_text,subject_text;
    String batch,semester,subject,total,sessional;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mark_list);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        sessional_text = (TextView) findViewById(R.id.sessional_text);
        semester_text = (TextView) findViewById(R.id.semester_text);
        subject_text = (TextView) findViewById(R.id.subject_text);
        mref = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);

        batch = getIntent().getStringExtra("batch");
        semester = getIntent().getStringExtra("semester");
        subject = getIntent().getStringExtra("subject");
        total = getIntent().getStringExtra("total");
        sessional = getIntent().getStringExtra("sessional");

        semester_text.setText(semester.substring(9));
        subject_text.setText(subject);
        sessional_text.setText(sessional.substring(10));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(InputMarkList.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Leave Mark register")
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
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<String>();
        mref.child("Students").child(batch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data.add(postSnapshot.child("Registration Number").getValue(String.class));
                }
                adapter = new AdapterMarkAdding(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        removedItems = new ArrayList<Integer>();
        adapter = new AdapterMarkAdding(data);
        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_marks, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_msg:
                uploadToFireBase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadToFireBase() {
        for(MarkClass markClass:adapter.lstChk)
        {
            Log.d("list",markClass.marks);
            if(!markClass.marks.equals(""))
            {
                int mark_int = Integer.parseInt(markClass.marks);
                int total_int = Integer.parseInt(total);
                if(mark_int>total_int)
                {
                    Toast.makeText(InputMarkList.this,"Invalid Marks",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        mref.child("Mark").child(batch).child(semester).child(sessional).child(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(MarkClass markClass:adapter.lstChk) {
                    if(!markClass.marks.equals(""))
                        mref.child("Mark").child(batch).child(semester).child(sessional).child(subject).child(markClass.reg_no).child("Marks").setValue(markClass.marks + " out of " + total);
                    else
                        mref.child("Mark").child(batch).child(semester).child(sessional).child(subject).child(markClass.reg_no).child("Marks").setValue("Absent");
                }
                Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        new SweetAlertDialog(InputMarkList.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Leave Mark register")
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

}