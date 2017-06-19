package com.phacsin.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.student.customfonts.HelveticaButton;

/**
 * Created by Bineesh P Babu on 06-01-2017.
 */

public class SemesterActivity extends AppCompatActivity {
    HelveticaButton sbmt_sem_yr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_selection);
        MaterialSpinner spinner_year = (MaterialSpinner) findViewById(R.id.spinner_year);
        spinner_year.setItems("2013", "2014", "2015", "2016", "2017","2018");
        spinner_year.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Year " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        MaterialSpinner spinner_sem = (MaterialSpinner) findViewById(R.id.spinner_semester);
        spinner_sem.setItems("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5","Semester 6","Semester 7","Semester 8");
        spinner_sem.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Semester " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        sbmt_sem_yr =(HelveticaButton)findViewById(R.id.btn_submit_year_sem);
        sbmt_sem_yr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main);
            }
        });
    }
}