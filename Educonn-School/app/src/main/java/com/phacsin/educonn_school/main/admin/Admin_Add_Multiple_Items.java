package com.phacsin.educonn_school.main.admin;

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

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
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
import com.phacsin.educonn_school.main.Teacher.TakeAttendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 05-05-2017.
 */

public class Admin_Add_Multiple_Items extends AppCompatActivity {
    Toolbar toolbar;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private DatabaseReference mRef;
    SharedPreferences sharedPreferences;
    String institution_name;
    List<String> data_year = new ArrayList<>();
    List<String> data_standard = new ArrayList<>();
    List<String> data_division = new ArrayList<>();
    MaterialSpinner spinner_year,spinner_standard,spinner_division;
    ValueEventListener standard_listener,division_listener;
    String year_selected,standard_selected,division_selected;
    boolean valid_year = false,valid_standard = false,valid_division = false;
    FloatingActionButton btn_year_add,btn_year_delete,btn_standard_add,btn_standard_delete,btn_division_add,btn_division_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_batches);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        institution_name = sharedPreferences.getString("Institution Name","");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Academic Year");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        spinner_year = (MaterialSpinner) findViewById(R.id.spinner_year);
        spinner_standard = (MaterialSpinner) findViewById(R.id.spinner_standard);
        spinner_division = (MaterialSpinner) findViewById(R.id.spinner_division);

        btn_year_add = (FloatingActionButton) findViewById(R.id.btn_add_year);
        btn_year_delete = (FloatingActionButton) findViewById(R.id.btn_delete_year);
        btn_standard_add = (FloatingActionButton) findViewById(R.id.btn_add_standard);
        btn_standard_delete = (FloatingActionButton) findViewById(R.id.btn_delete_standard);
        btn_division_add = (FloatingActionButton) findViewById(R.id.btn_add_division);
        btn_division_delete = (FloatingActionButton) findViewById(R.id.btn_delete_division);

        mRef.child("School").child(institution_name).child("Academic Year").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data_year.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    data_year.add(postSnapshot.child("Name").getValue(String.class));
                }
                if(data_year.size()!=0)
                {
                    spinner_year.setError(null);
                    spinner_year.setItems(data_year);
                    year_selected = data_year.get(0);
                    valid_year = true;
                    mRef.child("School").child(institution_name).child("Standard").child(year_selected).orderByChild("Name").addListenerForSingleValueEvent(standard_listener);
                }
                else
                {
                    valid_year = false;
                    data_year.add("No Academic Years available");
                    spinner_year.setItems(data_year);
                    spinner_year.setError("No Academic Years available");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Called When Activity is initialized
        standard_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data_standard.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    data_standard.add(postSnapshot.child("Name").getValue(String.class));
                if(data_standard.size()!=0) {
                    spinner_standard.setError(null);
                    spinner_standard.setItems(data_standard);
                    valid_standard = true;
                    standard_selected = data_standard.get(0);
                    mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            data_division.clear();
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                data_division.add(postSnapshot.child("Name").getValue(String.class));
                            if(data_division.size()!=0) {
                                spinner_division.setError(null);
                                spinner_division.setItems(data_division);
                                division_selected = data_division.get(0);
                                valid_division = true;

                            }
                            else
                            {
                                data_division.add("No Divisions available.Add one");
                                spinner_division.setItems(data_division);
                                spinner_division.setError("No Divisions available for this standard");
                                valid_division = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    data_standard.add("No Standards available for this year");
                    spinner_standard.setItems(data_standard);
                    spinner_standard.setError("No Standards available.Add one");
                    valid_standard = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        spinner_year.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                year_selected = item;
                mRef.child("School").child(institution_name).child("Standard").child(year_selected).orderByChild("Name").addListenerForSingleValueEvent(standard_listener);
            }
        });

        spinner_standard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                standard_selected = item;
                mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        data_division.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            data_division.add(postSnapshot.child("Name").getValue(String.class));
                        if (data_division.size() != 0) {
                            spinner_division.setError(null);
                            spinner_division.setItems(data_division);
                            division_selected = data_division.get(0);
                            valid_division = true;

                        } else {
                            data_division.add("No Divisions available.Add one");
                            spinner_division.setItems(data_division);
                            spinner_division.setError("No Divisions available for this standard");
                            valid_division = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        spinner_division.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                division_selected = item;
            }
        });

        btn_year_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog mMaterialDialog = new Dialog(Admin_Add_Multiple_Items.this);
                mMaterialDialog.setContentView(R.layout.dialog_admin_add_year);
                final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_year_name);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                HelveticaButton add_staff =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add);
                HelveticaButton cancel_staff_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel);
                add_staff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = name_editText.getText().toString();
                        mRef.child("School").child(institution_name).child("Academic Year").push().child("Name").setValue(name);
                        if(!valid_year)
                        {
                            data_year.clear();
                            valid_year = true;
                            spinner_year.setError(null);
                        }
                        data_year.add(name);
                        Log.d("FirebaseError",data_year.toString());
                        spinner_year.setItems(data_year);
                        mMaterialDialog.dismiss();
                    }
                });
                cancel_staff_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });
            }
        });
        btn_standard_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_year) {
                    final Dialog mMaterialDialog = new Dialog(Admin_Add_Multiple_Items.this);
                    mMaterialDialog.setContentView(R.layout.dialog_admin_add_standard);
                    final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_standard_name);
                    mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                    mMaterialDialog.show();
                    HelveticaButton add_staff = (HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add);
                    HelveticaButton cancel_staff_add = (HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel);
                    add_staff.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String name = name_editText.getText().toString();
                            mRef.child("School").child(institution_name).child("Standard").child(year_selected).push().child("Name").setValue(name);
                            if (!valid_standard) {
                                data_standard.clear();
                                valid_standard = true;
                                spinner_standard.setError(null);
                            }
                            data_standard.add(name);
                            spinner_standard.setItems(data_standard);
                            mMaterialDialog.dismiss();
                        }
                    });
                    cancel_staff_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMaterialDialog.cancel();
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Year",Toast.LENGTH_LONG).show();
            }
        });

        btn_division_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_year) {
                    if(valid_standard) {
                        final Dialog mMaterialDialog = new Dialog(Admin_Add_Multiple_Items.this);
                        mMaterialDialog.setContentView(R.layout.dialog_admin_add_division);
                        final HelveticaEditText name_editText = (HelveticaEditText) mMaterialDialog.findViewById(R.id.admin_division_name);
                        mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                        mMaterialDialog.show();
                        HelveticaButton add_staff = (HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add);
                        HelveticaButton cancel_staff_add = (HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel);
                        add_staff.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String name = name_editText.getText().toString();
                                mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).push().child("Name").setValue(name);
                                if (!valid_division) {
                                    data_division.clear();
                                    valid_division = true;
                                    spinner_division.setError(null);
                                }
                                data_division.add(name);
                                spinner_division.setItems(data_division);
                                mMaterialDialog.dismiss();
                            }
                        });
                        cancel_staff_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMaterialDialog.cancel();
                            }
                        });
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Standard",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Year",Toast.LENGTH_LONG).show();
            }
        });

        btn_year_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_year) {
                    new SweetAlertDialog(Admin_Add_Multiple_Items.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Leave attendance register")
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
                                    mRef.child("School").child(institution_name).child("Academic Year").orderByChild("Name").equalTo(year_selected).addListenerForSingleValueEvent((new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                                postSnapshot.getRef().setValue(null);
                                            data_year.remove(year_selected);
                                            if(data_year.size()!=0) {
                                                valid_year = true;
                                                spinner_year.setItems(data_year);
                                                mRef.child("School").child(institution_name).child("Standard").child(data_year.get(0)).orderByChild("Name").addListenerForSingleValueEvent(standard_listener);
                                            }
                                            else
                                            {
                                                valid_year = false;
                                                data_year.add("No Academic Years available");
                                                spinner_year.setItems(data_year);
                                                spinner_year.setError("No Academic Years available");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }));
                                    back_to.dismissWithAnimation();
                                    finish();
                                }
                            })
                            .show();

                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Year",Toast.LENGTH_LONG).show();
            }
        });

        btn_standard_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_year) {
                    if (valid_standard) {
                        new SweetAlertDialog(Admin_Add_Multiple_Items.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Delete " + standard_selected + "?")
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
                                        mRef.child("School").child(institution_name).child("Standard").child(year_selected).orderByChild("Name").equalTo(standard_selected).addListenerForSingleValueEvent((new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                                    postSnapshot.getRef().setValue(null);
                                                data_standard.remove(standard_selected);
                                                if (data_standard.size() != 0) {
                                                    valid_standard = true;
                                                    spinner_standard.setItems(data_standard);
                                                    mRef.child("School").child(institution_name).child("Standard").child(year_selected).child(data_standard.get(0)).orderByChild("Name").addListenerForSingleValueEvent(division_listener);
                                                } else {
                                                    valid_standard = false;
                                                    data_standard.add("No Standards available");
                                                    spinner_year.setItems(data_standard);
                                                    spinner_year.setError("No Standards available");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }));
                                        back_to.dismissWithAnimation();
                                        finish();
                                    }
                                })
                                .show();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Standard",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Year",Toast.LENGTH_LONG).show();
            }
        });

        btn_division_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid_year) {
                    if (valid_standard) {
                        if(valid_division) {
                            new SweetAlertDialog(Admin_Add_Multiple_Items.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("Delete " + division_selected + "?")
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
                                            mRef.child("School").child(institution_name).child("Division").child(year_selected).child(standard_selected).orderByChild("Name").equalTo(division_selected).addListenerForSingleValueEvent((new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                                        postSnapshot.getRef().setValue(null);
                                                    data_division.remove(division_selected);
                                                    if (data_division.size() != 0) {
                                                        valid_division = true;
                                                        spinner_division.setItems(data_division);
                                                    } else {
                                                        valid_division = false;
                                                        data_division.add("No Divisions available");
                                                        spinner_division.setItems(data_division);
                                                        spinner_division.setError("No Divisions available");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            }));
                                            back_to.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                        else
                            Toast.makeText(getApplicationContext(),"Invalid Division",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Standard",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid Year",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}