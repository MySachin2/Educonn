package com.phacsin.educonn_school.main.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phacsin.educonn_school.Forgot_Password;
import com.phacsin.educonn_school.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bineesh P Babu on 02-05-2017.
 */

public class Settings_Admin extends AppCompatActivity {
    CardView card_admin_change_pass;
    Toolbar toolbar;
    TextView last_login,payment_status,activation_date,next_payment,days_left;
    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        mRef = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        last_login = (TextView) findViewById(R.id.admin_profile_last_login_id);
        payment_status = (TextView) findViewById(R.id.admin_profile_payment_status);
        activation_date = (TextView) findViewById(R.id.admin_profile_payment_activation_date);
        next_payment = (TextView) findViewById(R.id.admin_profile_payment_next_payment);
        days_left = (TextView) findViewById(R.id.admin_profile_payment_days_left);
        last_login.setText(sharedPreferences.getString("Last Login","Unknown"));

        mRef.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str_act = dataSnapshot.child("Activation Date").getValue(String.class);
                String str_next = dataSnapshot.child("Next Payment").getValue(String.class);
                activation_date.setText(str_act);
                next_payment.setText(str_next);
                if(str_act!=null && str_next!=null) {
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    try {
                        Date date_act = new Date();
                        Date date_next = format.parse(str_next);
                        int days = daysBetween(date_act, date_next);
                        if (days > 0) {
                            days_left.setText(String.valueOf(days));
                            payment_status.setText("Active");
                            payment_status.setTextColor(getResources().getColor(R.color.green));
                            editor.putString("Payment Status", "Active");
                            editor.commit();

                        } else {
                            days_left.setText("");
                            payment_status.setText("Expired");
                            payment_status.setTextColor(getResources().getColor(R.color.red));
                            editor.putString("Payment Status", "Expired");
                            editor.commit();
                        }
                    } catch (ParseException e) {
                        Log.e("Date Error", e.toString());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        card_admin_change_pass=(CardView)findViewById(R.id.card_admin_profile_change_pass);
        card_admin_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Forgot_Password.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_payment_pay, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.admin_pay:
                //  uploadToFireBase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}