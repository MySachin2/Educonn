package com.phacsin.educonn_school.main.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.phacsin.educonn_school.R;
import com.phacsin.educonn_school.customfonts.HelveticaButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Bineesh P Babu on 02-05-2017.
 */

public class Profile_Admin_Edit extends AppCompatActivity {
    Toolbar toolbar;
    HelveticaButton save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_edit);
        save_btn=(HelveticaButton)findViewById(R.id.btn_save_admin_edit_profile);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog pDialog = new SweetAlertDialog(Profile_Admin_Edit.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#32b475"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(true);
                pDialog.show();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(Profile_Admin_Edit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Your Changes Will be Discarded")
                        .setConfirmText("Ok")
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog stay_here) {
                                stay_here.dismissWithAnimation();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog back_to_profile) {
                                back_to_profile.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(Profile_Admin_Edit.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Your Changes Will be Discarded")
                .setConfirmText("Ok")
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog stay_here) {
                        stay_here.dismissWithAnimation();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog back_to_profile) {
                        back_to_profile.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }
}

