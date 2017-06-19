package com.phacsin.student.main.Teacher;

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
import com.phacsin.student.LoginActivity;
import com.phacsin.student.R;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;
import com.phacsin.student.main.admin.CustomAndroidGridViewAdapter;
import com.phacsin.student.main.admin.Message_Admin;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        mAuth = FirebaseAuth.getInstance();
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
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_msg:
                final Dialog mMaterialDialog = new Dialog(TeacherMainActivity.this);
                mMaterialDialog.setContentView(R.layout.activity_send_message_card);
                HelveticaEditText editText = (HelveticaEditText) findViewById(R.id.typed_message);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                HelveticaButton msg_send =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_send_msg);
                HelveticaButton msg_cancel =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel_msg);
                msg_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });
                msg_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });
                return true;
            case R.id.logout:
                sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.remove("Type");
                editor.commit();
                mAuth.signOut();

                startActivity(new Intent(TeacherMainActivity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}