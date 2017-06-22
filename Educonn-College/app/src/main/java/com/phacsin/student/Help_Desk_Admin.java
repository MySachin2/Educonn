package com.phacsin.student;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.phacsin.student.customfonts.HelveticaButton;
import com.phacsin.student.customfonts.HelveticaEditText;

/**
 * Created by Bineesh P Babu on 18-06-2017.
 */

public class Help_Desk_Admin extends AppCompatActivity {
    CardView phone, mail, website;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_help_desk);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help Desk");
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        phone = (CardView) findViewById(R.id.contact_phone);
        mail = (CardView) findViewById(R.id.contact_mail);
        website = (CardView) findViewById(R.id.contact_website);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "8590332334"));
                if (ActivityCompat.checkSelfPermission(Help_Desk_Admin.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog mMaterialDialog = new Dialog(Help_Desk_Admin.this);
                mMaterialDialog.setContentView(R.layout.activity_helpdesk_feedback);
                mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                mMaterialDialog.show();
                final HelveticaEditText feedback = (HelveticaEditText) mMaterialDialog.findViewById(R.id.typed_feedback);
                HelveticaButton send_msg =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_send);
                HelveticaButton cancel_msg =(HelveticaButton) mMaterialDialog.findViewById(R.id.btn_cancel);
                send_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String feed= feedback.getText().toString();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"phacsindevs.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                        i.putExtra(Intent.EXTRA_TEXT   , feed);
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Help_Desk_Admin.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancel_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMaterialDialog.cancel();
                    }
                });

            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.phacsin.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }
}