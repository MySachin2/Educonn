package com.phacsin.educonn_school.main.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.DBHandler;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Bineesh P Babu on 02-05-2017.
 */

public class Messages_Send_Admin extends AppCompatActivity {
    Toolbar toolbar;
    private DatabaseReference mRef;
    String standard_selected,division_selected;
    EditText message_edittext,title_edittext;
    private Button send_msg,cancel_msg;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String institution_name,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_messages);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Send Message");
        setSupportActionBar(toolbar);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        institution_name = sharedPreferences.getString("Institution Name","");
        year = sharedPreferences.getString("Academic Year","");

        message_edittext = (EditText) findViewById(R.id.admin_message_content);
        title_edittext = (EditText) findViewById(R.id.admin_subject);
        send_msg = (Button) findViewById(R.id.btn_send_msg);
        cancel_msg = (Button) findViewById(R.id.btn_cancel_msg);

        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(Messages_Send_Admin.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Cancel Message")
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
                                back_to.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
        final MaterialSpinner spinner_standard= (MaterialSpinner)findViewById(R.id.spinner_standard);
        final MaterialSpinner spinner_division = (MaterialSpinner)findViewById(R.id.spinner_division);

        mRef.child("School").child(institution_name).child("Standard").child(year).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> standard_list = new ArrayList<String>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    standard_list.add(postSnapshot.child("Name").getValue(String.class));
                }
                standard_list.add("All Standards");
                if(standard_list.size()!=0) {
                    spinner_standard.setItems(standard_list);
                    standard_selected = standard_list.get(0);
                    mRef.child("School").child(institution_name).child("Division").child(year).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> data_division = new ArrayList<String>();
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                data_division.add(postSnapshot.child("Name").getValue(String.class));
                            data_division.add("All Divisions");
                            if(data_division.size()!=0) {
                                spinner_division.setItems(data_division);
                                spinner_division.setError(null);
                            }
                            else
                            {

                                data_division.add("No Divisions available for this year");
                                spinner_division.setItems(data_division);
                                spinner_division.setError("No Divisions available");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    standard_list.add("No Standards available for this year");
                    spinner_standard.setItems(standard_list);
                    spinner_standard.setError("No Standards available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner_standard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                standard_selected = item;
                mRef.child("School").child(institution_name).child("Division").child(year).child(standard_selected).orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data_division = new ArrayList<String>();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            data_division.add(postSnapshot.child("Name").getValue(String.class));
                        if(data_division.size()!=0) {
                            data_division.add("All Divisions");
                            spinner_division.setItems(data_division);
                            spinner_division.setError(null);
                        }
                        else
                        {
                            if(!standard_selected.equals("All Standards")) {
                                data_division.add("No Divisions available for this year");
                                spinner_division.setError("No Divisions available");
                            }
                            else
                                data_division.add("");
                            spinner_division.setItems(data_division);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        spinner_division.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                division_selected = item;
            }
        });
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate_()) {
                    String standard = spinner_standard.getItems().get(spinner_standard.getSelectedIndex()).toString().replace(" ", "");
                    String division = spinner_division.getItems().get(spinner_division.getSelectedIndex()).toString().replace(" ", "");
                    String title = title_edittext.getText().toString();
                    String message = message_edittext.getText().toString();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("title", title);
                    map.put("message", message);
                    if (standard.equals("All Standards"))
                        map.put("topic_name", institution_name.replaceAll("[^a-zA-Z0-9]", "") + "_" + year.replaceAll("[^a-zA-Z0-9]", ""));
                    else
                        map.put("topic_name", institution_name.replaceAll("[^a-zA-Z0-9]", "") + "_" + year.replaceAll("[^a-zA-Z0-9]", "") + standard.replaceAll("[^a-zA-Z0-9]", ""));

                    if (division.equals("All Divisions"))
                        map.put("topic_name", institution_name.replaceAll("[^a-zA-Z0-9]", "") + "_" + year.replaceAll("[^a-zA-Z0-9]", "") + standard.replaceAll("[^a-zA-Z0-9]", ""));
                    else
                        map.put("topic_name", institution_name.replaceAll("[^a-zA-Z0-9]", "") + "_" + year.replaceAll("[^a-zA-Z0-9]", "") + standard.replaceAll("[^a-zA-Z0-9]", "") + division.replaceAll("[^a-zA-Z0-9]", ""));

                    mRef.child("Notification Requests").child("Group").push().setValue(map);
                    DBHandler dbHandler = new DBHandler(getApplicationContext());
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    dbHandler.insertNotification(title, message, formattedDate);
                    Toast.makeText(getApplicationContext(), "Message Sent Successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    new SweetAlertDialog(Messages_Send_Admin.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText("Some Fields are empty")
                            .show();
                }
            }
        });

        cancel_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(Messages_Send_Admin.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Cancel Message")
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
                            public void onClick(SweetAlertDialog back_to_profile) {
                                back_to_profile.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
            private boolean validate_(){
                String title = title_edittext.getText().toString();
                String message = message_edittext.getText().toString();
                if(title.isEmpty()&&message.isEmpty()){
                    return false;
                }
                else return  true;
            }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(Messages_Send_Admin.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Cancel Message")
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
                    public void onClick(SweetAlertDialog back_to_profile) {
                        back_to_profile.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }


}