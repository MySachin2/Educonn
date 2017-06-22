package com.phacsin.educonn_school.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import com.phacsin.educonn_school.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 18-06-2017.
 */

public class Add_Subject extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont standard_selected_dialog,division_selected_dialog;
    String division_selected,standard_selected;
    private static AdapterAdminSubjectAdd adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<String> data_subject= new ArrayList<>();

    private DatabaseReference mRef;
    MaterialSpinner spinner_standard ,spinner_division;
    Button submit;
    SharedPreferences sharedPreferences;
    String institution_name,year;

    List<String> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    boolean valid_division;

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
        year = sharedPreferences.getString("Academic Year","");

        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_class_subject);
        submit = (Button) findViewById(R.id.btn_submit);
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division_subject);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                                spinner_division.setError(null);
                                spinner_division.setItems(data_div);
                                valid_division = true;
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
                            spinner_division.setError(null);
                            spinner_division.setItems(data_div);
                            valid_division = true;

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
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_division) {
                    standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                    division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                    mRef.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            data_subject.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                data_subject.add(postSnapshot.child("Name").getValue(String.class));
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

        adapter = new AdapterAdminSubjectAdd(data_subject,getApplicationContext(),multiselect_list);
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
                TextView standard_selected_text = (TextView) mMaterialDialog.findViewById(R.id.standard_selected_subject);
                TextView division_selected_text = (TextView) mMaterialDialog.findViewById(R.id.division_selected_subject);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_subject_edit);
                standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                standard_selected_text.setText(standard_selected);
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
                            mRef.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).push().setValue(map);
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
                    new SweetAlertDialog(Add_Subject.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete Selected Subjects")
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
                                        standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                                        division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                                        for(int i=0;i<multiselect_list.size();i++) {
                                            data_subject.remove(multiselect_list.get(i));
                                            mRef.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Name").equalTo(multiselect_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
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
            if (multiselect_list.contains(data_subject.get(position)))
                multiselect_list.remove(data_subject.get(position));
            else
                multiselect_list.add(data_subject.get(position));

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
        adapter.dataSet=data_subject;
        adapter.notifyDataSetChanged();
    }
}