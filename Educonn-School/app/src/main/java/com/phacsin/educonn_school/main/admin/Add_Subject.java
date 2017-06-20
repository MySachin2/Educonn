package com.phacsin.educonn_school.main.admin;

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
import android.widget.TextView;
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
import com.phacsin.educonn_school.customfonts.NiceFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bineesh P Babu on 18-06-2017.
 */

public class Add_Subject extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont batch_selected_dialog,semester_selected_dialog;
    String division_selected,class_selected;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<String> data_subject= new ArrayList<>();

    private DatabaseReference mRef;
    MaterialSpinner spinner_class ,spinner_division;
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

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        spinner_class = (MaterialSpinner) findViewById(R.id.spinner_class_subject);
        submit = (Button) findViewById(R.id.btn_submit_month);
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division_subject);
        spinner_division.setItems("Division 1", "Division 2", "Division 3", "Division 4","Division 5","Division 6");
        spinner_division.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                Snackbar.make(view,"Selected Division " + item, Snackbar.LENGTH_LONG).show();
                division_selected =item;

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRef.child("College").child(institution_name).child("Batch").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_batch = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_batch.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(data_batch.size()!=0) {
                    spinner_class.setError(null);
                    spinner_class.setItems(data_batch);
                }
                else {
                    data_batch.add("No Subjects available");
                    spinner_class.setItems(data_batch);
                    spinner_class.setError("No Subjects available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class_selected = spinner_class.getItems().get(spinner_class.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                mRef.child("College").child(institution_name).child("Subject").child(class_selected).child(division_selected).orderByChild("Subject Code").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        data_subject.clear();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            data_subject.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        adapter = new AdapterAdminSubjectAdd(data_subject);
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
                mMaterialDialog.setContentView(R.layout.dialog_admin_subject_add);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                TextView class_selected_text = (TextView) mMaterialDialog.findViewById(R.id.class_selected_subject);
                TextView division_selected_text = (TextView) mMaterialDialog.findViewById(R.id.division_selected_subject);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_subject_edit);
                class_selected = spinner_class.getItems().get(spinner_class.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                class_selected_text.setText(class_selected);
                division_selected_text.setText(division_selected);
                HelveticaButton add_batch =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_subject);
                HelveticaButton cancel_batch_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_subject_adding);
                add_batch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject_name = name_editText.getText().toString();

                        if(!subject_name.equals(""))
                        {
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("Name",subject_name);
                            mRef.child("College").child(institution_name).child("Subject").child(class_selected).child(division_selected).push().setValue(map);
                            Toast.makeText(getApplicationContext(),"Subject Added Successfully",Toast.LENGTH_LONG).show();
                            data_subject.add(subject_name);
                            adapter.notifyDataSetChanged();
                            mMaterialDialog.dismiss();
                        }
                    }
                });
                cancel_batch_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });
        }
        return true;
    }
}