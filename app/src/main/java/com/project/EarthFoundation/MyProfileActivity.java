package com.project.EarthFoundation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;


public class MyProfileActivity extends AppCompatActivity {

    // ArrayList for person names, email Id's and mobile numbers


    UserSessionManager session;
    String name;
    String email;
    String profile_picture;

    TextView _userName;
    ImageView _profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Get user details using session
        session = new UserSessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // get name
        name = user.get(UserSessionManager.KEY_NAME);
        // get email
        email = user.get(UserSessionManager.KEY_EMAIL);
        profile_picture = user.get(UserSessionManager.KEY_IMAGE);

        _userName = (TextView) findViewById(R.id.name);
        _profilePic = (ImageView) findViewById(R.id.profile);
        _userName.setText(name);
        if (!profile_picture.equals(""))
            new ImageLoadTask(profile_picture, _profilePic).execute();


//        String type = "GetTreeData";
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.delegate=this;
//        backgroundWorker.execute(type,email);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        Thread adapter_thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

                        //finish();
//                    }
//                });
//            }
//        });
//        adapter_thread.start();
    }


//    @Override
//    public void processFinish(String output){
//        //Here you will receive the result fired from async class
//        //of onPostExecute(result) method.
//        try {
//            // get JSONObject from JSON file
//           // JSONObject obj = new JSONObject();
//            // fetch JSONArray named users
//            JSONArray userArray = new JSONArray(output);
//            // implement for loop for getting users list data
//            for (int i = 0; i < userArray.length(); i++) {
//                // create a JSONObject for fetching single user data
//                JSONObject userDetail = userArray.getJSONObject(i);
//                // fetch email and name and store it in arraylist
//                updateStatusList.add(userDetail.getString("updateStatus"));
//                treeNamesList.add(userDetail.getString("treeName"));
//                treeAddressList.add(userDetail.getString("treeAddress"));
//                plantDateList.add(userDetail.getString("plantDate"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
