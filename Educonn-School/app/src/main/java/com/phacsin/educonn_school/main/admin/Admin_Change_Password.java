package com.phacsin.educonn_school.main.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.phacsin.educonn_school.R;

/**
 * Created by Bineesh P Babu on 17-06-2017.
 */

public class Admin_Change_Password extends AppCompatActivity {
    Toolbar toolbar;
    private EditText oldpass_edit,newpass_edit;
    Button submit;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
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
        oldpass_edit = (EditText) findViewById(R.id.old_password);
        newpass_edit = (EditText) findViewById(R.id.new_password);
        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpass = oldpass_edit.getText().toString();
                final String newpass = newpass_edit.getText().toString();
                AuthCredential authcred = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),oldpass);
                mAuth.getCurrentUser().reauthenticate(authcred).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mAuth.getCurrentUser().updatePassword(newpass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("FIreBase", "User password updated.");
                                                Toast.makeText(getApplicationContext(),"User password updated",Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Password is incorrect",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}