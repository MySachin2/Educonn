package com.phacsin.student.main.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.phacsin.student.R;

/**
 * Created by Bineesh P Babu on 02-05-2017.
 */

public class Profile_Admin extends AppCompatActivity {
    CardView card_payment_details,card_admin_batches;
    ImageView edit_profile_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
       // card_payment_details = (CardView)findViewById(R.id.card_admin_profile_payment_details);
       // card_admin_batches=(CardView)findViewById(R.id.card_admin_profile_batches);
        edit_profile_button = (ImageView)findViewById(R.id.edit_admin_profile);
       /* card_payment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Payment_Admin_Details.class);
                startActivity(i);
            }
        });*/
        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Profile_Admin_Edit.class);
                startActivity(i);
            }
        });
       /* card_admin_batches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Admin_Batches.class);
                startActivity(i);
            }
        });*/
    }
}