package com.phacsin.student.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.R;
import com.phacsin.student.RandomPasswordGenerator;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;
import com.phacsin.student.customfonts.NiceFont;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class ListStudents extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont batch_selected_dialog,semester_selected_dialog;
    String semester_selected,batch_selected;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataStudent> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    com.github.clans.fab.FloatingActionButton fab_add_student;
    MaterialSpinner spinner_batch,spinner_sem;
    private DatabaseReference mRef;
    private Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;
    private FirebaseAuth mAuth,mAuth_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Students");
        setSupportActionBar(toolbar);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://student-2c02a.firebaseio.com/")
                .setApiKey("AIzaSyCimNyM-hi4ET1boCA7138C02lqkRuQ6f0")
                .setApplicationId("student-2c02a").build();
        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions , RandomPasswordGenerator.generate(8));

            mAuth_temp = FirebaseAuth.getInstance(myApp);
        }catch (IllegalStateException e)
        {
            Log.e("FireBase",e.toString());
        }

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        spinner_batch = (MaterialSpinner) findViewById(R.id.spinner_batch);
        submit = (Button) findViewById(R.id.btn_submit_month);

        spinner_batch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                Snackbar.make(view, "Selected batch " + item, Snackbar.LENGTH_LONG).show();
                batch_selected=item;
            }
        });

        mRef.child("College").child(institution_name).child("Subject").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> batch_list = new ArrayList<String>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    batch_list.add(postSnapshot.getKey());
                }
                if(batch_list.size()!=0)
                    spinner_batch.setItems(batch_list);
                batch_selected=batch_list.get(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.clear();
                mRef.child("College").child(institution_name).child("Students").child(batch_selected).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            DataStudent dataSubject = new DataStudent();
                            dataSubject.name = postSnapshot.child("Name").getValue(String.class);
                            dataSubject.reg_no = postSnapshot.child("Registration Number").getValue(String.class);
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
        adapter = new AdapterAdminStudent(data);
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
                final Dialog mMaterialDialog = new Dialog(ListStudents.this);
                mMaterialDialog.setContentView(R.layout.activity_admin_student_enter);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();

                final HelveticaEditText editText_name = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_name);
                final HelveticaEditText editText_reg_no = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_reg);
                final HelveticaEditText editText_contact_no = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_phone);
                final HelveticaEditText editText_email = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_email);


                batch_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.batch_selected_student);
                batch_selected_dialog.setText(batch_selected);

                HelveticaButton add_student =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_student);
                HelveticaButton cancel_student =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_student_adding);

                add_student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = editText_name.getText().toString();
                        final String reg = editText_reg_no.getText().toString();
                        final String phone = editText_contact_no.getText().toString();
                        final String email = editText_email.getText().toString();

                        if(name.equals("")||reg.equals("")||phone.equals(""))
                            Toast.makeText(getApplicationContext(),"One or more fields are empty",Toast.LENGTH_LONG).show();
                        else
                        {
                            final Map<String,String> map = new HashMap<String, String>();
                            map.put("Name",name);
                            map.put("Registration Number",reg);
                            map.put("Email",email);
                            map.put("Contact Number",phone);
                            String randomPass = RandomPasswordGenerator.generate(16);
                            mAuth_temp.createUserWithEmailAndPassword(email,randomPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Map<String,String> user = new HashMap<String, String>();
                                        user.put("Name",name);
                                        user.put("Registration Number",reg);
                                        user.put("Email",email);
                                        user.put("Contact Number",phone);
                                        user.put("Institution Name",institution_name);
                                        user.put("Institution Type","College");
                                        user.put("Type","Student");
                                        mRef.child("Users").child(task.getResult().getUser().getUid()).setValue(user);
                                        mAuth_temp.signOut();
                                        Toast.makeText(getApplicationContext(),"Successfully Created",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                            mRef.child("College").child(institution_name).child("Students").child(batch_selected).push().setValue(map);
                            DataStudent dataStudent = new DataStudent();
                            dataStudent.name = name;
                            dataStudent.reg_no = reg;
                            data.add(dataStudent);
                            adapter.notifyDataSetChanged();
                            mMaterialDialog.cancel();
                        }
                    }
                });
                cancel_student.setOnClickListener(new View.OnClickListener() {
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