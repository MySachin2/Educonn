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
import android.util.Log;
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
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.NiceFont;
import com.phacsin.educonn_school.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class ListSubject extends AppCompatActivity {
    Toolbar toolbar;
    NiceFont standard_selected_dialog,division_selected_dialog;
    String division_selected,standard_selected;
    private static AdapterAdminSubject adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<DataSubject> data = new ArrayList<>();
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    com.github.clans.fab.FloatingActionButton fab_add_sub;
    private DatabaseReference mRef;
    MaterialSpinner spinner_standard ,spinner_division;
    DataSnapshot BigSnapshot;
    Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;
    List<DataSubject> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    private String year;
    boolean valid_division;

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
        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_standard);

        submit = (Button) findViewById(R.id.btn_submit);

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        year = sharedPreferences.getString("Academic Year","");
        institution_name = sharedPreferences.getString("Institution Name","");
        
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division);

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
                            division_selected = data_div.get(0);
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
                standard_selected = item;
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
                    String standard = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                    String division = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();
                    data.clear();
                    mRef.child("School").child(institution_name).child("Subject_Taken").child(year).child(standard).child(division).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("DS", dataSnapshot.toString());
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
                else
                    Toast.makeText(getApplicationContext(),"Invalid Division",Toast.LENGTH_SHORT).show();

            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterAdminSubject(data,getApplicationContext(),multiselect_list);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<DataSubject>();
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
                final Dialog mMaterialDialog = new Dialog(ListSubject.this);
                mMaterialDialog.setContentView(R.layout.activity_admin_subject_enter);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                final List<String> staff_list = new ArrayList<>();
                final List<String> subject_list = new ArrayList<>();
                standard_selected = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString();
                division_selected = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString();

                standard_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.standard_selected_subject);
                division_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.division_selected_subject);
                standard_selected_dialog.setText(standard_selected);
                division_selected_dialog.setText(division_selected);

                final MaterialSpinner spinner_staff = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_staff);
                final MaterialSpinner spinner_subject = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_subject);

                mRef.child("School").child(institution_name).child("Staff").orderByChild("Name").addValueEventListener(new ValueEventListener() {
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

                mRef.child("School").child(institution_name).child("Subject").child(year).child(standard_selected).child(division_selected).orderByChild("Subject Code").addValueEventListener(new ValueEventListener() {
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
                        mRef.child("School").child(institution_name).child("Subject_Taken").child(year).child(standard_selected).child(division_selected).push().setValue(map);
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
                    new SweetAlertDialog(ListSubject.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete selected subjects")
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
                                            data.remove(multiselect_list.get(i));
                                            mRef.child("School").child(institution_name).child("Subject_Taken").child(year).child(standard_selected).child(division_selected).orderByChild("Staff").equalTo(multiselect_list.get(i).teacher).addListenerForSingleValueEvent(new ValueEventListener() {
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
            multiselect_list = new ArrayList<DataSubject>();
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