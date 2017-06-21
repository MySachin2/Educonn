package com.phacsin.educonn_school;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phacsin.educonn_school.customfonts.HelveticaButton;
import com.phacsin.educonn_school.fragments.AttendanceFragment;
import com.phacsin.educonn_school.fragments.MarkFragment;
import com.phacsin.educonn_school.fragments.NotificationFragment;
import com.phacsin.educonn_school.main.admin.AdminMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phacsin.educonn_school.R.id.tabs;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    private ImageView img_send,img_logout,img_edit;
    private int[] tabicons={R.drawable.atte_white,R.drawable.mark_white,R.drawable.not_white,R.drawable.atte_grey,R.drawable.mark_grey,R.drawable.not_grey};
    private FirebaseAuth mAuth;
    ViewPagerAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student");
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setSupportActionBar(toolbar);
       // img_send =(ImageView) findViewById(R.id.send_message);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        mRef = FirebaseDatabase.getInstance().getReference();
        /*
        Map notification = new HashMap<>();
        notification.put("deviceToken", refreshedToken);
        notification.put("message", "Hello");
        mDatabase.child("notificationRequests").push().setValue(notification);
        */

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        tabLayout = (TabLayout) findViewById(tabs);
        Log.d("HEllo",sharedPreferences.getString("Academic Year",""));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabicons[0]);
        tabLayout.getTabAt(1).setIcon(tabicons[4]);
        tabLayout.getTabAt(2).setIcon(tabicons[5]);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getSelectedTabPosition();
                if(tabLayout.getSelectedTabPosition()==0){
                    tabLayout.getTabAt(0).setIcon(tabicons[0]);
                    tabLayout.getTabAt(1).setIcon(tabicons[4]);
                    tabLayout.getTabAt(2).setIcon(tabicons[5]);
                }
                if(tabLayout.getSelectedTabPosition()==1){
                    tabLayout.getTabAt(0).setIcon(tabicons[3]);
                    tabLayout.getTabAt(1).setIcon(tabicons[1]);
                    tabLayout.getTabAt(2).setIcon(tabicons[5]);

                }
                else if(tabLayout.getSelectedTabPosition()==2){
                    tabLayout.getTabAt(0).setIcon(tabicons[3]);
                    tabLayout.getTabAt(1).setIcon(tabicons[4]);
                    tabLayout.getTabAt(2).setIcon(tabicons[2]);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AttendanceFragment(), "Attendance");
        adapter.addFragment(new MarkFragment(), "Exam");
        adapter.addFragment(new NotificationFragment(), "Notifications");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_student, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        switch (item.getItemId()) {
           /* case R.id.send_msg:
                final Dialog mMaterialDialog = new Dialog(MainActivity.this);
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
                return true;*/
            case R.id.details:
                startActivity(new Intent(this,SelectionActivity.class));
                finish();

                return true;
            case R.id.logout:
                String institution = sharedPreferences.getString("Institution Name","").replaceAll("[^a-zA-Z0-9]","");
                String year = sharedPreferences.getString("Academic Year","").replaceAll("[^a-zA-Z0-9]","");
                String standard = sharedPreferences.getString("Standard","").replaceAll("[^a-zA-Z0-9]","");
                String division = sharedPreferences.getString("Division","").replaceAll("[^a-zA-Z0-9]","");

                FirebaseMessaging.getInstance().unsubscribeFromTopic(institution + "_" + year);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(institution + "_" + year + "_" + standard);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(institution + "_" + year + "_" + standard + "_" + division);

                editor.remove("Type");
                editor.remove("Batch");
                editor.remove("Academic Year");
                editor.remove("Standard");
                editor.remove("Division");
                editor.remove("Institution Name");
                editor.commit();
                mAuth.signOut();

                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
