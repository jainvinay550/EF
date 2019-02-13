package com.project.EarthFoundation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyProfileActivity extends AppCompatActivity {

    // ArrayList for person names, email Id's and mobile numbers


    UserSessionManager session;
    String name,phoneNo, country,state,city,pincode,aadhar,email;
    String profile_picture;

    TextView _userName;
    ImageView _profilePic;
    @BindView(R.id.textTree) TextView _treeText;
    @BindView(R.id.p_email) TextView _email;
    @BindView(R.id.textToken) TextView _tokenText;
    @BindView(R.id.phoneNo) EditText _phoneText;
    @BindView(R.id.country) EditText _countryText;
    @BindView(R.id.state) EditText _stateText;
    @BindView(R.id.city) EditText _cityText;
    @BindView(R.id.pincode) EditText _pinCodeText;
    @BindView(R.id.aadhar) EditText _aadharText;
    @BindView(R.id.btnSubmitProfile) Button _updatebtn;
    @BindView(R.id.profile_bar2) ProgressBar _imageBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        //Get user details using session
        session = new UserSessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // get name
        name = user.get(UserSessionManager.KEY_NAME);
        // get email
        email = user.get(UserSessionManager.KEY_EMAIL);
        profile_picture = user.get(UserSessionManager.KEY_IMAGE);

        Intent intent = getIntent();
        String tree = intent.getStringExtra("contribution");
        String token = intent.getStringExtra("tokensEarned");

        _userName = (TextView) findViewById(R.id.name);
        _profilePic = (ImageView) findViewById(R.id.profile);
        _userName.setText(name);
        _email.setText(email);
        _treeText.setText(tree);
        _tokenText.setText(token);
        if (!profile_picture.equals("")){
            new ImageLoadTask(profile_picture, _profilePic,_imageBar).execute();
        } else{
            _imageBar.setVisibility(View.GONE);
        }
        getProfile();
        _updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
    }
    public void getProfile(){
        String type = "getProfile";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        //backgroundWorker.delegate=this;
        backgroundWorker.execute(email);
    }

    public void updateProfile(){

        phoneNo = _phoneText.getText().toString();
        country = _countryText.getText().toString();
        state = _stateText.getText().toString();
        city = _cityText.getText().toString();
        pincode = _pinCodeText.getText().toString();
        aadhar = _aadharText.getText().toString();

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Failed to edit profile. Please enter valid details.", Toast.LENGTH_LONG).show();
           // _updatebtn.setEnabled(false);
            return;
        }
        //_updatebtn.setEnabled(true);
            String type = "updateProfile";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this, type);
            //backgroundWorker.delegate=this;
            backgroundWorker.execute(phoneNo, country, state, city, pincode, aadhar, email);


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

    public final boolean containsDigit(String s){
        boolean containsDigit = false;
        if(s != null && !s.isEmpty()){
            for (char c : s.toCharArray()){
                if (containsDigit = Character.isDigit(c)){
                    break;
                }
            }
        }
        return containsDigit;
    }

    public final boolean containsText(String s){
        boolean containsDigit = false;
        if(s != null && !s.isEmpty()){
            for (char c : s.toCharArray()){
                if (containsDigit = Character.isLetter(c)){
                    break;
                }
            }
        }
        return containsDigit;
    }

    public boolean validate() {
        boolean valid = true;

        if ( containsDigit(phoneNo)){
            if((phoneNo.length() > 0 && phoneNo.length() < 10) || phoneNo.length()>10)
            {
                _phoneText.setError("Please enter 10 digits phone number");
                valid = false;
            }}
        else if(containsText(phoneNo)) {
            _phoneText.setError("Phone number can't be text");
            valid = false;
        }
        else {
            _phoneText.setError(null);
        }
        if ( pincode.isEmpty() || ((pincode.length() > 0 && pincode.length() < 6) || pincode.length()>6)){

            _pinCodeText.setError("Please enter 6 digits of pin-code");
            valid = false;
        } else if(containsText(pincode)) {
            _pinCodeText.setError("Pin-code can't be text");
            valid = false;
        }else {
            _pinCodeText.setError(null);
        }
        if ( containsDigit(aadhar)){
            if((aadhar.length() > 0 && aadhar.length() < 12) || aadhar.length()>12)
            {
            _aadharText.setError("Please enter 12 digits of aadhar number");
            valid = false;}
        } else if(containsText(aadhar)) {
            _aadharText.setError("Aadhar number can't be text");
            valid = false;
        }else {
            _aadharText.setError(null);
        }
        if(containsDigit(country)) {
            _countryText.setError("Country cannot be a number");
            valid = false;
        }else {
            _countryText.setError(null);
        }
        if(containsDigit(state)) {
            _stateText.setError("State cannot be a number");
            valid = false;
        }else {
            _stateText.setError(null);
        }
        if(containsDigit(city)) {
            _cityText.setError("City cannot be a number");
            valid = false;
        }else {
            _cityText.setError(null);
        }

        return valid;
    }
}

