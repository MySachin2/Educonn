package com.phacsin.student.main.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.phacsin.student.customfonts.NiceFont;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class ListSubject extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont batch_selected_dialog,semester_selected_dialog;
    String semester_selected,batch_selected;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<DataSubject> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    com.github.clans.fab.FloatingActionButton fab_add_sub;
    private DatabaseReference mRef;
    MaterialSpinner spinner_batch ,spinner_sem;
    DataSnapshot BigSnapshot;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subject_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Subjects");
        mRef = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        spinner_batch = (MaterialSpinner) findViewById(R.id.spinner_year_subject);
        submit = (Button) findViewById(R.id.btn_submit_month);
        spinner_batch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            }
        });
        spinner_sem = (MaterialSpinner) findViewById(R.id.spinner_sem_subject);
        spinner_sem.setItems("Semester 1", "Semester 2", "Semester 3", "Semester 4","Semester 5","Semester 6","Semester 7","Semester 8");
        spinner_sem.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                Snackbar.make(view,"Selected Semester " + item, Snackbar.LENGTH_LONG).show();
                semester_selected =item;

            }
        });

        mRef.child("Subject").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BigSnapshot = dataSnapshot;
                List<String> batch_list = new ArrayList<String>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    batch_list.add(postSnapshot.getKey());
                }
                if(batch_list.size()!=0)
                 spinner_batch.setItems(batch_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String batch = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                String semester = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();
                data.clear();
                mRef.child("Subject_Taken").child(batch).child(semester).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            DataSubject dataSubject = new DataSubject();
                            dataSubject.name = postSnapshot.child("Subject").getValue(String.class);
                            dataSubject.teacher = postSnapshot.child("Staff").getValue(String.class);
                            data.add(dataSubject);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterAdminSubject(data);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_student_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_staff:
                final Dialog mMaterialDialog = new Dialog(ListSubject.this);
                mMaterialDialog.setContentView(R.layout.activity_admin_subject_enter);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                final List<String> staff_list = new ArrayList<>();
                final List<String> subject_list = new ArrayList<>();
                batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                semester_selected = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();

                batch_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.batch_selected_subject);
                semester_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.semester_selected_subject);
                batch_selected_dialog.setText(batch_selected);
                semester_selected_dialog.setText(semester_selected);

                final MaterialSpinner spinner_staff = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_staff);
                final MaterialSpinner spinner_subject = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_subject);

                mRef.child("Staff").orderByChild("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {
                                staff_list.add(postSnapshot.child("Name").getValue(String.class));
                            }
                            if(staff_list.size()!=0)
                                spinner_staff.setItems(staff_list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mRef.child("Subject").child(batch_selected).child(semester_selected).orderByChild("Subject Code").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            subject_list.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        if(subject_list.size()!=0)
                            spinner_subject.setItems(subject_list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                HelveticaButton add_subject =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_subject);
                HelveticaButton cancel_subject =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_subject_adding);
                add_subject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject_name = spinner_subject.getItems().get(spinner_subject.getSelectedIndex()).toString();
                        String staff_name = spinner_staff.getItems().get(spinner_staff.getSelectedIndex()).toString();
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("Staff",staff_name);
                        map.put("Subject",subject_name);
                        mRef.child("Subject_Taken").child(batch_selected).child(semester_selected).push().setValue(map);
                        DataSubject dataSubject = new DataSubject();
                        dataSubject.name = subject_name;
                        dataSubject.teacher = staff_name;
                        data.add(dataSubject);
                        adapter.notifyDataSetChanged();
                        mMaterialDialog.cancel();
                    }
                });
                cancel_subject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}