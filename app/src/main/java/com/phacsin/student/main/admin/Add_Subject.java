package com.phacsin.student.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.R;
import com.phacsin.student.customfonts.NiceFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 18-06-2017.
 */

public class Add_Subject extends AppCompatActivity {
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
    SharedPreferences sharedPreferences;
    String institution_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subject_add);
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
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterAdminSubject(data);
        recyclerView.setAdapter(adapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_student_add, menu);
        return true;
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_staff:
                final Dialog mMaterialDialog = new Dialog(Add_Subject.this);
                mMaterialDialog.setContentView(R.layout.activity_admin_subject_add_dialog);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
        }
        return true;
    }
}