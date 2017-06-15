package com.phacsin.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.phacsin.student.main.Teacher.TeacherMainActivity;
import com.phacsin.student.main.admin.AdminMainActivity;

/**
 * Created by Bineesh P Babu on 27-04-2017.
 */

public class Demologin extends AppCompatActivity {

    CardView card1,card2,card3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_login);
        card1=(CardView)findViewById(R.id.student_);
        card2=(CardView)findViewById(R.id.teacher);
        card3=(CardView)findViewById(R.id.admin);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stu=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(stu);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tea=new Intent(getApplicationContext(),TeacherMainActivity.class);
                startActivity(tea);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adm=new Intent(getApplicationContext(),AdminMainActivity.class);
                startActivity(adm);
            }
        });
    }
}