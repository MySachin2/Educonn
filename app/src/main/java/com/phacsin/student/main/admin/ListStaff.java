package com.phacsin.student.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phacsin.student.R;
import com.phacsin.student.RandomPasswordGenerator;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;
import com.phacsin.student.recyclerview.DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class ListStaff extends AppCompatActivity {
    Toolbar toolbar;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<String> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    com.github.clans.fab.FloatingActionButton fab_add;
    private DatabaseReference mRef;
    SharedPreferences sharedPreferences;
    String institution_name;
    private FirebaseAuth mAuth,mAuth_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_list);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Staffs");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        //mAuth = FirebaseAuth.getInstance();

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

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mRef.child("College").child(institution_name).child("Staff").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Teacher teacher = postSnapshot.getValue(Teacher.class);
                    data.add(teacher.Name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new AdapterAdminStaff(data);
        recyclerView.setAdapter(adapter);
        removedItems = new ArrayList<Integer>();
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_staff_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_staff:
                final Dialog mMaterialDialog = new Dialog(ListStaff.this);
                mMaterialDialog.setContentView(R.layout.activity_admin_staff_enter);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_staff);
                final HelveticaEditText email_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_email_id_staff);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                HelveticaButton add_staff =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_staff);
                HelveticaButton cancel_staff_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_staff_adding);
                add_staff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = name_editText.getText().toString();
                        final String email = email_editText.getText().toString();
                        Map notification = new HashMap<>();
                        notification.put("Name", name);
                        notification.put("Email", email);
                        mRef.child("College").child(institution_name).child("Staff").push().setValue(notification);
                        data.add(name_editText.getText().toString());
                        adapter.notifyDataSetChanged();

                        String randomPass = RandomPasswordGenerator.generate(16);
                        mAuth_temp.createUserWithEmailAndPassword(email,randomPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Map<String,String> user = new HashMap<String, String>();
                                    user.put("Name",name);
                                    user.put("Email",email);
                                    user.put("Institution Name",institution_name);
                                    user.put("Institution Type","College");
                                    user.put("Type","Teacher");
                                    mRef.child("Users").child(task.getResult().getUser().getUid()).setValue(user);
                                    mAuth_temp.signOut();
                                    Toast.makeText(getApplicationContext(),"Successfully Created",Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                    if(e!=null) {
                                        Log.e("FireBase", e.toString());
                                    }
                                    Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        mMaterialDialog.dismiss();
                        /*Toast.makeText(getApplicationContext(),"A verification mail sent to staff",Toast.LENGTH_SHORT).show();
                        mMaterialDialog.cancel();*/
                    }
                });
                cancel_staff_add.setOnClickListener(new View.OnClickListener() {
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

    public static class Teacher {

        public String Email;
        public String Name;
        public Teacher()
        {

        }

        public Teacher(String email, String name) {
            this.Email = email;
            this.Name = name;
        }

    }
}