package com.phacsin.student.main.admin;

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
import com.phacsin.student.recyclerview.RecyclerItemClickListener;

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
    NiceFont batch_selected_dialog,semester_selected_dialog;
    String semester_selected,batch_selected;
    private static AdapterAdminSubject adapter;
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
    List<DataSubject> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;

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

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");

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

        mRef.child("College").child(institution_name).child("Batch").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BigSnapshot = dataSnapshot;
                List<String> batch_list = new ArrayList<String>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    batch_list.add(postSnapshot.child("Name").getValue(String.class));
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
                mRef.child("College").child(institution_name).child("Subject_Taken").child(batch).child(semester).addListenerForSingleValueEvent(new ValueEventListener() {
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
                batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                semester_selected = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();

                batch_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.batch_selected_subject);
                semester_selected_dialog=(NiceFont)mMaterialDialog.findViewById(R.id.semester_selected_subject);
                batch_selected_dialog.setText(batch_selected);
                semester_selected_dialog.setText(semester_selected);

                final MaterialSpinner spinner_staff = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_staff);
                final MaterialSpinner spinner_subject = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_subject);

                mRef.child("College").child(institution_name).child("Staff").orderByChild("Name").addValueEventListener(new ValueEventListener() {
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

                mRef.child("College").child(institution_name).child("Subject").child(batch_selected).child(semester_selected).orderByChild("Subject Code").addValueEventListener(new ValueEventListener() {
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
                        mRef.child("College").child(institution_name).child("Subject_Taken").child(batch_selected).child(semester_selected).push().setValue(map);
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
                                        batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                                        semester_selected = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();
                                        for(int i=0;i<multiselect_list.size();i++) {
                                            data.remove(multiselect_list.get(i));
                                            mRef.child("College").child(institution_name).child("Subject_Taken").child(batch_selected).child(semester_selected).orderByChild("Staff").equalTo(multiselect_list.get(i).teacher).addListenerForSingleValueEvent(new ValueEventListener() {
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