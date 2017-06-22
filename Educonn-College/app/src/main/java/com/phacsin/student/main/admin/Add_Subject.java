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
import android.widget.TextView;
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
import com.phacsin.student.recyclerview.RecyclerItemClickListener;

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
    NiceFont batch_selected_dialog,semester_selected_dialog;
    String semester_selected,batch_selected;
    private static AdapterAdminSubjectAdd adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<String> data_subject= new ArrayList<>();

    private DatabaseReference mRef;
    MaterialSpinner spinner_batch ,spinner_sem;
    Button submit;
    SharedPreferences sharedPreferences;
    String institution_name;

    List<String> multiselect_list = new ArrayList<>();
    ActionMode mActionMode;
    boolean isMultiSelect = false;

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
        spinner_batch = (MaterialSpinner) findViewById(R.id.spinner_year_subject);
        submit = (Button) findViewById(R.id.btn_submit_month);
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

        mRef.child("College").child(institution_name).child("Batch").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data_batch = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    data_batch.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(data_batch.size()!=0) {
                    spinner_batch.setError(null);
                    spinner_batch.setItems(data_batch);
                }
                else {
                    data_batch.add("No Subjects available");
                    spinner_batch.setItems(data_batch);
                    spinner_batch.setError("No Subjects available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                semester_selected = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();
                mRef.child("College").child(institution_name).child("Subject").child(batch_selected).child(semester_selected).orderByChild("Subject Code").addListenerForSingleValueEvent(new ValueEventListener() {
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
                TextView batch_selected_text = (TextView) mMaterialDialog.findViewById(R.id.batch_selected_subject);
                TextView semester_selected_text = (TextView) mMaterialDialog.findViewById(R.id.semester_selected_subject);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_subject_edit);
                final HelveticaEditText code_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_subject_code_edit);
                batch_selected = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString();
                semester_selected = spinner_sem.getItems().get(spinner_sem.getSelectedIndex()).toString();
                batch_selected_text.setText(batch_selected);
                semester_selected_text.setText(semester_selected);
                HelveticaButton add_batch =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_subject);
                HelveticaButton cancel_batch_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_subject_adding);
                add_batch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject_name = name_editText.getText().toString();
                        String subject_code = code_editText.getText().toString();

                        if(!subject_name.equals(""))
                        {
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("Name",subject_name);
                            map.put("Subject Code",subject_code);
                            mRef.child("College").child(institution_name).child("Subject").child(batch_selected).child(semester_selected).push().setValue(map);
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
                            .setContentText("Delete selected Subjects")
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
                                            data_subject.remove(multiselect_list.get(i));
                                            mRef.child("College").child(institution_name).child("Subject").child(batch_selected).child(semester_selected).orderByChild("Name").equalTo(multiselect_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
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