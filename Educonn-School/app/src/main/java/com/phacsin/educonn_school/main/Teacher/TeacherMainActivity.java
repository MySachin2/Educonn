package com.phacsin.educonn_school.main.Teacher;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.Help_Desk_Admin;
import com.phacsin.educonn_school.LoginActivity;
import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.customfonts.HelveticaEditText;
import com.phacsin.educonn_school.main.admin.AdminMainActivity;
import com.phacsin.educonn_school.main.admin.CustomAndroidGridViewAdapter;
import com.phacsin.educonn_school.main.admin.Message_Admin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class TeacherMainActivity extends AppCompatActivity {
    CardView card1,card2,card3;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    GridView gridView;
    Context context;
    ArrayList arrayList;

    public static String[] gridViewStrings = {
            "Attendance",
            "Mark",
            "Message",
            "Help Desk",

    };
    public static int[] gridViewImages = {
            R.drawable.atte,
            R.drawable.mark,
            R.mipmap.msg_admin,
            R.mipmap.helpdesk_admin
    };
    private DatabaseReference mRef;
    private String institution_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student App");
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.grid);

        mRef = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);

        institution_name = sharedPreferences.getString("Institution Name","");
        gridView.setAdapter(new CustomAndroidGridViewAdapter(this, gridViewStrings, gridViewImages));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        startActivity(new Intent(getApplicationContext(),EditActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),InputMark.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),Message_Admin.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),Help_Desk_Admin.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                }
            }
        });
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

                startActivity(new Intent(TeacherMainActivity.this,LoginActivity.class));
                finish();
                return true;
            case R.id.choose_year:
                final Dialog mMaterialDialog = new Dialog(TeacherMainActivity.this);
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
                            spinner_year.setError("No Years Available");
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