package com.phacsin.educonn_school;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;

/**
 * Created by Bineesh P Babu on 17-06-2017.
 */

public class Forgot_Password extends AppCompatActivity {

    Toolbar toolbar;
    HelveticaEditText email_edit;
    Button button;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        email_edit = (HelveticaEditText) findViewById(R.id.input_email);
        button = (Button) findViewById(R.id.btn_submit);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_edit.getText().toString();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Verification Email Sent",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                {
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    if(e!=null)
                                    {
                                        Log.e("FireBase",e.toString());
                                    }
                                    Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}
