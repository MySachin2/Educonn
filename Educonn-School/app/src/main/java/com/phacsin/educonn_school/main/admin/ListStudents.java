package com.phacsin.educonn_school.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.RandomPasswordGenerator;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;
import com.phacsin.educonn_school.customfonts.NiceFont;
import com.phacsin.educonn_school.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class ListStudents extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont standard_selected_dialog,division_selected_dialog;
    String division_selected,standard_selected;
    private static AdapterAdminStudent adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<String> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    com.github.clans.fab.FloatingActionButton fab_add_student;
    MaterialSpinner spinner_standard,spinner_division;
    private DatabaseReference mRef;
    private Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;
    private FirebaseAuth mAuth,mAuth_temp;
    List<String> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    private String year;
    boolean valid_division;

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
        year = sharedPreferences.getString("Academic Year","");
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_standard);
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division);

        submit = (Button) findViewById(R.id.btn_submit);


        mRef.child("School").child(institution_name).child("Standard").child(year).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_std = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_std.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(data_std.size()!=0) {
                    spinner_standard.setError(null);
                    spinner_standard.setItems(data_std);
                    standard_selected = data_std.get(0);
                    mRef.child("School").child(institution_name).child("Division").child(year).child(data_std.get(0)).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> data_div = new ArrayList<>();
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            {
                                data_div.add(postSnapshot.child("Name").getValue(String.class));
                            }
                            if(data_div.size()!=0) {
                                valid_division = true;
                                spinner_division.setError(null);
                                spinner_division.setItems(data_div);
                            }
                            else
                            {
                                data_div.add("No Divisions available");
                                spinner_standard.setItems(data_div);
                                spinner_standard.setError("No Divisions available");
                                valid_division = false;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    data_std.add("No Standards available");
                    spinner_standard.setItems(data_std);
                    spinner_standard.setError("No Standards available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner_standard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mRef.child("School").child(institution_name).child("Division").child(year).child(item).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data_div = new ArrayList<>();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            data_div.add(postSnapshot.child("Name").getValue(String.class));
                        }
                        if(data_div.size()!=0) {
                            valid_division = true;
                            spinner_division.setError(null);
                            spinner_division.setItems(data_div);
                        }
                        else
                        {
                            valid_division = false;
                            data_div.add("No Divisions available");
                            spinner_standard.setItems(data_div);
                            spinner_standard.setError("No Divisions available");
                        }
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_division) {
                    data.clear();
                    standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                    division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();

                    mRef.child("School").child(institution_name).child("Students").child(year).child(standard_selected).child(division_selected).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                data.add(postSnapshot.child("Name").getValue(String.class));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Division",Toast.LENGTH_LONG).show();

            }
        });
        adapter = new AdapterAdminStudent(data,getApplicationContext(),multiselect_list);
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
                final HelveticaEditText editText_contact_no = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_phone);
                final HelveticaEditText editText_email = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_name_student_email);

                standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();

                standard_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.standard_selected_student);
                division_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.division_selected_student);
                standard_selected_dialog.setText(standard_selected);
                division_selected_dialog.setText(division_selected);

                HelveticaButton add_student =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_student);
                HelveticaButton cancel_student =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_student_adding);

                add_student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = editText_name.getText().toString();
                        final String phone = editText_contact_no.getText().toString();
                        final String email = editText_email.getText().toString();

                        if(name.equals("")||phone.equals(""))
                            Toast.makeText(getApplicationContext(),"One or more fields are empty",Toast.LENGTH_LONG).show();
                        else
                        {
                            final Map<String,String> map = new HashMap<String, String>();
                            map.put("Name",name);
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
                                        user.put("Email",email);
                                        user.put("Contact Number",phone);
                                        user.put("Institution Name",institution_name);
                                        user.put("Institution Type","School");
                                        user.put("Type","Student");
                                        map.put("UID",task.getResult().getUser().getUid());
                                        mRef.child("Users").child(task.getResult().getUser().getUid()).setValue(user);
                                        mAuth_temp.signOut();
                                        Toast.makeText(getApplicationContext(),"Successfully Created",Toast.LENGTH_LONG).show();
                                        mRef.child("School").child(institution_name).child("Students").child(year).child(standard_selected).child(division_selected).push().setValue(map);
                                        data.add(name);
                                        adapter.notifyDataSetChanged();

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Email already exists",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

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
                    new SweetAlertDialog(ListStudents.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete all selections")
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
                                            mRef.child("School").child(institution_name).child("Students").child(year).child(standard_selected).child(division_selected).orderByChild("Name").equalTo(multiselect_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                                        postSnapshot.getRef().setValue(null);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        adapter.notifyDataSetChanged();

                                        if (mActionMode != null) {
                                            mActionMode.finish();
                                        }
                                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
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