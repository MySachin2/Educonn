package com.phacsin.educonn_school;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.phacsin.educonn_school.customfonts.Helvetica;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;
import com.phacsin.educonn_school.main.Teacher.TeacherMainActivity;
import com.phacsin.educonn_school.main.admin.AdminMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 04-01-2017.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @InjectView(R.id.input_email)
    HelveticaEditText _emailText;
    @InjectView(R.id.input_password) HelveticaEditText _passwordText;
    @InjectView(R.id.btn_login)
    HelveticaButton _loginButton;
    Helvetica forgot_pass;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgot_pass = (Helvetica)findViewById(R.id.forgot_password_text);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this,Forgot_Password.class);
                startActivity(i);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    editor = sharedPreferences.edit();
                    if(!sharedPreferences.contains("Type")) {
                        mDatabase.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String year = dataSnapshot.child("Academic Year").getValue(String.class);
                                String institution_name = dataSnapshot.child("Institution Name").getValue(String.class);
                                String type = dataSnapshot.child("Type").getValue(String.class);
                                String reg_no = dataSnapshot.child("Register Number").getValue(String.class);
                                Log.d("INST",institution_name);
                                editor.putString("Institution Name",institution_name);
                                editor.putString("Type",type);
                                editor.putString("Email",user.getEmail());

                                if (type.equals("Student")) {
                                    editor.putString("Academic Year", year);
                                    editor.putString("Register Number", reg_no);
                                    editor.commit();
                                    String reg_token = sharedPreferences.getString("Registration Token","");
                                    mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Registration Token").setValue(reg_token);
                                    //FirebaseMessaging.getInstance().subscribeToTopic(institution_name.replaceAll("[^a-zA-Z0-9]","") + "_" + year.replaceAll("[^a-zA-Z0-9]",""));

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                else if (type.equals("Teacher")) {
                                    editor.commit();
                                    startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
                                }
                                else if(type.equals("Admin"))
                                {
                                    Date now = Calendar.getInstance().getTime();
                                    String nowAsString = new SimpleDateFormat("dd-MM-yyyy").format(now);
                                    editor.putString("Last Login",nowAsString);
                                    editor.putString("Academic Year",year);
                                    editor.commit();
                                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        String value = sharedPreferences.getString("Type","Student");
                        if (value.equals("Student"))
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        else if (value.equals("Teacher"))
                            startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
                        else if(value.equals("Admin"))
                            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        ButterKnife.inject(this);
        mAuth = FirebaseAuth.getInstance();
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });



    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.green);
        pDialog.setTitleText("Logging in");
        pDialog.setCancelable(false);
        pDialog.show();


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        pDialog.cancel();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                           /* Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        /*Intent i=new Intent(this,SelectionActivity.class);
        startActivity(i);*/
   }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        /*if (email.isEmpty() || email.length()>10 || email.length()<10) {
            _emailText.setError("enter a valid register number");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
