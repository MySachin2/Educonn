package com.phacsin.educonn_school;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PaymentReceiver extends BroadcastReceiver {
    private DatabaseReference mRef;
    private SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    private SharedPreferences.Editor editor;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //You can do the processing here.
        Bundle extras = intent.getExtras();
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE);

        editor = sharedPreferences.edit();
        Log.d("Payement_Receiver","Yoo");
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mRef.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                String str_next = dataSnapshot.child("Next Payment").getValue(String.class);
                if(str_next!=null) {
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    try {
                        Date date_act = new Date();
                        Date date_next = format.parse(str_next);
                        int days = daysBetween(date_act, date_next);
                        if (days > 0) {
                            editor.putString("Payment Status", "Active");
                            editor.commit();

                        } else {
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
    }

    public void setAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PaymentReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HALF_DAY , pi);
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
