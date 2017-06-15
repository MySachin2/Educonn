package com.phacsin.student.main.Teacher;

import android.app.Dialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.phacsin.student.LoginActivity;
import com.phacsin.student.R;
import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;
import com.phacsin.student.main.admin.Message_Admin;

/**
 * Created by Bineesh P Babu on 26-01-2017.
 */

public class TeacherMainActivity extends AppCompatActivity {
    CardView card1,card2,card3;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student App");
        setSupportActionBar(toolbar);
        card1=(CardView)findViewById(R.id.card_add_attendance);
        card2=(CardView)findViewById(R.id.card_add_mark);
        card3=(CardView)findViewById(R.id.card_view_message);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit=new Intent(getApplicationContext(),EditActivity.class);
                startActivity(edit);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit=new Intent(getApplicationContext(),InputMark.class);
                startActivity(edit);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit=new Intent(getApplicationContext(),Message_Admin.class);
                startActivity(edit);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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