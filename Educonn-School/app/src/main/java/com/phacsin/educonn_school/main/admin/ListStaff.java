package com.phacsin.educonn_school.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.RandomPasswordGenerator;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;
import com.phacsin.educonn_school.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class ListStaff extends AppCompatActivity {
    Toolbar toolbar;
    private static AdapterAdminStaff adapter;
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
    List<String> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;

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
        mRef.child("School").child(institution_name).child("Staff").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Teacher teacher = postSnapshot.getValue(Teacher.class);
                    data.add(teacher.Name);
                    Log.e("FireBase",postSnapshot.toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new AdapterAdminStaff(data,getApplicationContext(),multiselect_list);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<String>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));
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
                        final Map notification = new HashMap<>();
                        notification.put("Name", name);
                        notification.put("Email", email);
                        data.add(name_editText.getText().toString());
                        adapter.notifyDataSetChanged();
                        String randomPass = RandomPasswordGenerator.generate(16);
                        mAuth_temp.createUserWithEmailAndPassword(email,randomPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    mRef.child("School").child(institution_name).child("Staff").push().setValue(notification);
                                    Map<String,String> user = new HashMap<String, String>();
                                    user.put("Name",name);
                                    user.put("Email",email);
                                    user.put("Institution Name",institution_name);
                                    user.put("Institution Type","School");
                                    user.put("Type","Teacher");
                                    user.put("UID",task.getResult().getUser().getUid());
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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    new SweetAlertDialog(ListStaff.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete selected staffs")
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
                                    if(multiselect_list.size()>0)
                                    {
                                        for(int i=0;i<multiselect_list.size();i++) {
                                            data.remove(multiselect_list.get(i));
                                            mRef.child("School").child(institution_name).child("Staff").orderByChild("Name").equalTo(multiselect_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                                        postSnapshot.getRef().setValue(null);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            //mRef.child("Users").child()
                                        }

                                        adapter.notifyDataSetChanged();

                                        if (mActionMode != null) {
                                            mActionMode.finish();
                                        }
                                        Toast.makeText(getApplicationContext(), "Deleted Staffs", Toast.LENGTH_LONG).show();
                                    }
                                    back_to.dismissWithAnimation();
                                }
                            })
                            .show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<String>();
            refreshAdapter();
        }
    };
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(data.get(position)))
                multiselect_list.remove(data.get(position));
            else
                multiselect_list.add(data.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public void refreshAdapter()
    {
        adapter.selectedList=multiselect_list;
        adapter.dataSet=data;
        adapter.notifyDataSetChanged();
    }

}