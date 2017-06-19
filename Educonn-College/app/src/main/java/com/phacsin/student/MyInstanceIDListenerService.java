package com.phacsin.student;

import android.app.Service;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GD on 6/10/2017.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(mAuth.getCurrentUser()!=null) {
            Log.d("Reg TOken", mAuth.getCurrentUser().getUid() + "\n" + refreshedToken);
            sharedPreferences = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("Registration Token",refreshedToken);
            editor.commit();
            mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Registration Token").setValue(refreshedToken);
        }
        else {
            sharedPreferences = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("Registration Token",refreshedToken);
            editor.commit();
            //mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Registration Token").setValue(refreshedToken);
        }
    }

}