package com.phacsin.student.main.admin;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.phacsin.student.LoginActivity;
import com.phacsin.student.R;

import java.util.ArrayList;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student App");
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(this, gridViewStrings, gridViewImages));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        startActivity(new Intent(getApplicationContext(),Message_Admin.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),Admin_Batches.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),ListStaff.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),ListStudents.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(),Add_Subject.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(),ListSubject.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 6:
                        startActivity(new Intent(getApplicationContext(),Settings_Admin.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    case 7:
                        startActivity(new Intent(getApplicationContext(),Help_Desk_Admin.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;

                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
       /* card_profile =(CardView)findViewById(R.id.card_admin_profile);
        card_messages =(CardView)findViewById(R.id.card_admin_messages);
        card_staffs =(CardView)findViewById(R.id.card_admin_staffs);
        card_students =(CardView)findViewById(R.id.card_admin_students);
        card_subjects =(CardView)findViewById(R.id.card_admin_subjects);

      //  card_download =(CardView)findViewById(R.id.card_admin_download);

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Settings_Admin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Message_Admin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card_staffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ListStaff.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ListStudents.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card_subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ListSubject.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });*/
       /* card_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Download_Data.class);
                startActivity(i);
            }
        });*/

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}