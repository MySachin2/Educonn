package com.phacsin.student.main.admin;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phacsin.student.R;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;
import com.phacsin.student.main.Teacher.TakeAttendance;
import com.phacsin.student.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 05-05-2017.
 */

public class Admin_Batches extends AppCompatActivity {
    Toolbar toolbar;
    private static AdapterAdminBatch adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private DatabaseReference mRef;
    SharedPreferences sharedPreferences;
    String institution_name;
    private static List<String> data = new ArrayList<>();
    List<String> multiselect_list = new ArrayList<>();
    private Menu context_menu;
    ActionMode mActionMode;
    boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_batches);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Batches");
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
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mRef.child("College").child(institution_name).child("Batch").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("FireBase",postSnapshot.toString());
                    data.add(postSnapshot.child("Name").getValue(String.class));
                    Log.d("FireBase",data.toString());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new AdapterAdminBatch(data,getApplicationContext(),multiselect_list);
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
        inflater.inflate(R.menu.menu_admin_student_add,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_staff:
                final Dialog mMaterialDialog = new Dialog(Admin_Batches.this);
                mMaterialDialog.setContentView(R.layout.dialog_admin_add_batches);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_batch_name);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                HelveticaButton add_batch =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add_batch);
                HelveticaButton cancel_batch_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_batch_adding);
                final String name = name_editText.getText().toString();
                add_batch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            final String name = name_editText.getText().toString();
                            mRef.child("College").child(institution_name).child("Batch").push().child("Name").setValue(name);
                            data.add(name);
                            adapter.notifyDataSetChanged();
                            mMaterialDialog.dismiss();
                        }

                });
                cancel_batch_add.setOnClickListener(new View.OnClickListener() {
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
    /*private boolean validate(String edittext){
        String title = edittext;
        if(title.isEmpty()){
            return true;
        }
        else return  false;
    }*/
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
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
                    new SweetAlertDialog(Admin_Batches.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete selected batches")
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
                                            mRef.child("College").child(institution_name).child("Batch").orderByChild("Name").equalTo(multiselect_list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                                                        postSnapshot.getRef().child("Name").setValue(null);
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