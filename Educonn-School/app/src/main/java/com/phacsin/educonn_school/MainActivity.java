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
import com.phacsin.educonn_school.fragments.AttendanceFragment;
import com.phacsin.educonn_school.fragments.MarkFragment;
import com.phacsin.educonn_school.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

import static com.phacsin.educonn_school.R.id.tabs;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    private ImageView img_send,img_logout,img_edit;
    private int[] tabicons={R.drawable.atte_white,R.drawable.mark_white,R.drawable.not_white,R.drawable.atte_grey,R.drawable.mark_grey,R.drawable.not_grey};
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ViewPagerAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setSupportActionBar(toolbar);
       // img_send =(ImageView) findViewById(R.id.send_message);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        /*
        Map notification = new HashMap<>();
        notification.put("deviceToken", refreshedToken);
        notification.put("message", "Hello");
        mDatabase.child("notificationRequests").push().setValue(notification);
        */

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(tabs);

        if(!sharedPreferences.contains("Semester"))
        {
            mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("Semester")) {
                        String semester = dataSnapshot.child("Semester").getValue(String.class);
                        editor.putString("Semester", semester);
                        editor.commit();
                    }
                    else {
                        String semester = "Semester 1";
                        editor.putString("Semester", semester);
                        editor.commit();
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                        setupTabIcons();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        }

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
            case R.id.semester:
                mDatabase = FirebaseDatabase.getInstance().getReference();
                //mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Semester").setValue("Yoo");
                mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Semester").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Dialog mMaterialDialog = new Dialog(MainActivity.this);
                        mMaterialDialog.setContentView(R.layout.dialog_semester_select);
                        mMaterialDialog.getWindow().setBackgroundDrawableResource(R.color.white);
                        mMaterialDialog.show();
                        RadioGroup radioGroup = (RadioGroup) mMaterialDialog.findViewById(R.id.radio_group);
                        Log.d("value",dataSnapshot.getValue(String.class));
                        for(int i=0;i<radioGroup.getChildCount();i++)
                        {
                            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                            if(rb.getText().equals(dataSnapshot.getValue(String.class)))
                                rb.setChecked(true);
                        }
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                                RadioButton rb_new = (RadioButton) mMaterialDialog.findViewById(i);
                                mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Semester").setValue(rb_new.getText().toString());
                                editor.putString("Semester",rb_new.getText().toString());
                                editor.commit();
                                final Handler handler  = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                                        finish();
                                    }
                                };
                                handler.postDelayed(runnable,300);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.logout:
                String institution = sharedPreferences.getString("Institution Name","").replaceAll("[^a-zA-Z0-9]","");
                String batch = sharedPreferences.getString("Batch","").replaceAll("[^a-zA-Z0-9]","");
                FirebaseMessaging.getInstance().unsubscribeFromTopic(institution + "_" + batch);
                editor.remove("Type");
                editor.remove("Batch");
                editor.remove("Institution Name");
                editor.commit();
                mAuth.signOut();

                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
