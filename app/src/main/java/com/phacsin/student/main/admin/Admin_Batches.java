package com.phacsin.student.main.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phacsin.student.R;

/**
 * Created by Bineesh P Babu on 05-05-2017.
 */

public class Admin_Batches extends AppCompatActivity {
    String[] mobileArray = {"2017B1","2017B2","2018B1","2017B2",
            "2019B1","2019B2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_batches);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_admin_batches_list, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

    }
}