package com.phacsin.educonn_school.main.Teacher;

import android.content.SharedPreferences;
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
import com.phacsin.educonn_school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    TextView standard_text,division_text,subject_text;
    String standard,division,subject,total,test;
    private DatabaseReference mref;
    SharedPreferences sharedPreferences;
    String institution_name,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mark_list);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        standard_text = (TextView) findViewById(R.id.standard_text);
        division_text = (TextView) findViewById(R.id.division_text);
        subject_text = (TextView) findViewById(R.id.subject_text);
        mref = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        year = sharedPreferences.getString("Academic Year","");

        standard = getIntent().getStringExtra("standard");
        division = getIntent().getStringExtra("division");
        subject = getIntent().getStringExtra("subject");
        total = getIntent().getStringExtra("total");
        test = getIntent().getStringExtra("test_name");

/*
        sessional = getIntent().getStringExtra("sessional");
*/

        standard_text.setText(standard);
        subject_text.setText(subject);
        division_text.setText(division);
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
        mref.child("School").child(institution_name).child("Students").child(year).child(standard).child(division).orderByChild("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data.add(postSnapshot.child("Name").getValue(String.class));
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
        for(MarkClass markClass:adapter.lstChk) {
            if (!markClass.marks.equals(""))
                mref.child("School").child(institution_name).child("Mark").child(standard).child(division).child(subject).child(markClass.name).child("Marks").setValue(markClass.marks + " out of " + total);
            else
                mref.child("School").child(institution_name).child("Mark").child(standard).child(division).child(subject).child(markClass.name).child("Marks").setValue("Absent");
          }
            Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();
            Map<String,String> map = new HashMap<String, String>();
            map.put("title", " Uploaded");
            map.put("message", test  + " : Marks uploaded of " + standard + "," + division);
            map.put("topic_name",institution_name.replaceAll("[^a-zA-Z0-9]","")+ "_" + standard.replaceAll("[^a-zA-Z0-9]","") + division.replaceAll("[^a-zA-Z0-9]",""));
            mref.child("Notification Requests").child("Group").push().setValue(map);
            finish();
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