package com.phacsin.educonn_school.main.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.LoginActivity;
import com.phacsin.educonn_school.PaymentReceiver;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.RandomPasswordGenerator;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Bineesh P Babu on 25-01-2017.
 */

public class AdminMainActivity extends AppCompatActivity {
    CardView card_profile,card_messages,card_staffs,card_students,card_subjects,card_download;
    Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    TextView text;
    GridView gridView;
    Context context;
    ArrayList arrayList;

    public static String[] gridViewStrings = {
            "Message",
            "Add Batches",
            "Staffs",
            "Students",
            "Add Subjects",
            "Assign Subject",
            "Settings",
            "Help Desk",

    };
    public static int[] gridViewImages = {
            R.mipmap.msg_admin,
            R.mipmap.batches,
            R.mipmap.admin_profile_icon,
            R.mipmap.students_admin,
            R.mipmap.asgn_subject,
            R.mipmap.subject_admin,
            R.mipmap.settings,
            R.mipmap.helpdesk_admin,
    };
    private DatabaseReference mRef;
    private String institution_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student App");
        setSupportActionBar(toolbar);
        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);

        institution_name = sharedPreferences.getString("Institution Name","");

        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(this, gridViewStrings, gridViewImages));
        String status = sharedPreferences.getString("Payment Status","Active");
        if(status.equals("Expired"))
            Toast.makeText(getApplicationContext(),"Expired. Please pay for additional services",Toast.LENGTH_LONG).show();
        else {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            if (sharedPreferences.contains("Academic Year")) {
                                startActivity(new Intent(getApplicationContext(), Message_Admin.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else
                                Toast.makeText(getApplicationContext(), "Select an Academic Year from the Menu", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            startActivity(new Intent(getApplicationContext(), Admin_Add_Multiple_Items.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case 2:
                            if (sharedPreferences.contains("Academic Year")) {

                                startActivity(new Intent(getApplicationContext(), ListStaff.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else
                                Toast.makeText(getApplicationContext(), "Select an Academic Year from the Menu", Toast.LENGTH_LONG).show();

                            break;
                        case 3:
                            if (sharedPreferences.contains("Academic Year")) {
                                startActivity(new Intent(getApplicationContext(), ListStudents.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else
                                Toast.makeText(getApplicationContext(), "Select an Academic Year from the Menu", Toast.LENGTH_LONG).show();
                            break;
                        case 4:
                            if (sharedPreferences.contains("Academic Year")) {

                                startActivity(new Intent(getApplicationContext(), Add_Subject.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else
                                Toast.makeText(getApplicationContext(), "Select an Academic Year from the Menu", Toast.LENGTH_LONG).show();
                            break;
                        case 5:
                            if (sharedPreferences.contains("Academic Year")) {
                                startActivity(new Intent(getApplicationContext(), ListSubject.class));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            } else
                                Toast.makeText(getApplicationContext(), "Select an Academic Year from the Menu", Toast.LENGTH_LONG).show();
                            break;
                        case 6:
                            startActivity(new Intent(getApplicationContext(), Settings_Admin.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        case 7:
                            startActivity(new Intent(getApplicationContext(), Help_Desk_Admin.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;

                    }

                }
            });
        }
        mAuth = FirebaseAuth.getInstance();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.remove("Type");
                editor.commit();
                mAuth.signOut();

                startActivity(new Intent(AdminMainActivity.this,LoginActivity.class));
                finish();
                return true;
            case R.id.choose_year:
                final Dialog mMaterialDialog = new Dialog(AdminMainActivity.this);
                mMaterialDialog.setContentView(R.layout.dialog_admin_select_year);
                final MaterialSpinner spinner_year = (MaterialSpinner) mMaterialDialog.findViewById(R.id.spinner_year);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                final HelveticaButton add_staff =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_add);
                final HelveticaButton cancel_staff_add =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel);
                mRef.child("School").child(institution_name).child("Academic Year").orderByChild("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> data = new ArrayList<String>();
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                            data.add(postSnapshot.child("Name").getValue(String.class));
                        if(data.size()!=0) {
                            spinner_year.setItems(data);
                            spinner_year.setError(null);
                            add_staff.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String year = spinner_year.getItems().get(spinner_year.getSelectedIndex()).toString();
                                    sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("Academic Year",year);
                                    editor.commit();
                                    mRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Academic Year").setValue(year);
                                    mMaterialDialog.dismiss();
                                }
                            });
                        }
                        else
                        {
                            spinner_year.setItems(data);
                            spinner_year.setError("No Admin");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                cancel_staff_add.setOnClickListener(new View.OnClickListener() {
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
}