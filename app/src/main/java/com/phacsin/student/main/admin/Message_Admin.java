package com.phacsin.student.main.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.phacsin.student.DBHandler;
import com.phacsin.student.R;
import com.phacsin.student.recyclerviewnotifications.AdapterNotifications;
import com.phacsin.student.recyclerviewnotifications.DataModelNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 20-05-2017.
 */

public class Message_Admin extends AppCompatActivity {
    FloatingActionButton fab_sent_msg;
    Toolbar toolbar;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<DataModelNotification> data = new ArrayList<>();
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_messages);
        fab_sent_msg = (FloatingActionButton)findViewById(R.id.send_message_from_admin);
        fab_sent_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),Messages_Send_Admin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inbox");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Log.d("SIZE",dbHandler.getAllNotifications().size()+"");
        data = dbHandler.getAllNotifications();
        adapter = new AdapterNotifications(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

                        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(data!=null)
        {
            DBHandler dbHandler = new DBHandler(getApplicationContext());
            data = dbHandler.getAllNotifications();
            adapter = new AdapterNotifications(data);
            adapter.notifyDataSetChanged();
        }
    }
}