package com.phacsin.student.main.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.DBHandler;
import com.phacsin.student.R;

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
    String batch_selected;
    EditText message_edittext,title_edittext;
    private Button send_msg,cancel_msg;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String institution_name;

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
        final MaterialSpinner spinner_batch = (MaterialSpinner)findViewById(R.id.spinner);
        mRef.child("College").child(institution_name).child("Subject").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> batch_list = new ArrayList<String>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    batch_list.add(postSnapshot.getKey());
                }
                if(batch_list.size()!=0)
                    spinner_batch.setItems(batch_list);
                batch_selected=batch_list.get(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner_batch.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String batch = spinner_batch.getItems().get(spinner_batch.getSelectedIndex()).toString().replace(" ","");
                String title = title_edittext.getText().toString();
                String message = message_edittext.getText().toString();
                Map<String,String> map = new HashMap<String, String>();
                map.put("title",title);
                map.put("message",message);
                map.put("topic_name",institution_name.replaceAll("[^a-zA-Z0-9]","")+ "_" + batch.replaceAll("[^a-zA-Z0-9]",""));
                mRef.child("Notification Requests").child("Group").push().setValue(map);
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());
                dbHandler.insertNotification(title,message,formattedDate);
                Toast.makeText(getApplicationContext(),"Message Sent Successfully",Toast.LENGTH_LONG).show();
                finish();
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