package com.project.EarthFoundation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

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
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MyProfileActivity extends AppCompatActivity {

    // ArrayList for person names, email Id's and mobile numbers


    UserSessionManager session;
    String name,phoneNo, country,state,city,pincode,aadhar,email;
    String profile_picture;

    TextView _userName;
    ImageView _profilePic;
    @BindView(R.id.p_email) TextView _email;
    @BindView(R.id.phoneNo) EditText _phoneText;
    @BindView(R.id.country) EditText _countryText;
    @BindView(R.id.state) EditText _stateText;
    @BindView(R.id.city) EditText _cityText;
    @BindView(R.id.pincode) EditText _pinCodeText;
    @BindView(R.id.aadhar) EditText _aadharText;
    @BindView(R.id.btnSubmitProfile) Button _updatebtn;
    @BindView(R.id.profile_bar2) ProgressBar _imageBar;
    @BindView(R.id.EditProfile) ImageView _editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ButterKnife.bind(this);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");


        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        connectivity -> {
                            if(connectivity.state()==NetworkInfo.State.CONNECTED) {
                                setContentView(R.layout.activity_my_profile);
                                ButterKnife.bind(this);
                                _email.setTypeface(myCustomFont);
                                _phoneText.setTypeface(myCustomFont);
                                _cityText.setTypeface(myCustomFont);
                                _pinCodeText.setTypeface(myCustomFont);
                                _countryText.setTypeface(myCustomFont);
                                _stateText.setTypeface(myCustomFont);
                                _aadharText.setTypeface(myCustomFont);
                                _updatebtn.setTypeface(myCustomFont);

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
                                _email.setText(email);
                                if (!profile_picture.equals("")) {
                                    //new ImageLoadTask(profile_picture, _profilePic, _imageBar).execute();
                                    loadImage(profile_picture);
                                } else {
                                    _imageBar.setVisibility(View.GONE);
                                }
                                getProfile();

                                _editProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        enableEditText(_phoneText);
                                        enableEditText(_aadharText);
                                        enableEditText(_countryText);
                                        enableEditText(_stateText);
                                        enableEditText(_cityText);
                                        enableEditText(_pinCodeText);
                                        _updatebtn.setVisibility(View.VISIBLE);
                                    }
                                });

                                _updatebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                        single
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(isConnectedToInternet -> {
                                                    if(isConnectedToInternet.equals(true)) {
                                                        updateProfile();
                                                    }
                                                    else{
                                                        buildDialog1(MyProfileActivity.this).show();
                                                    }
                                                });

                                    }
                                });


                                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                                setSupportActionBar(toolbar);

                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                getSupportActionBar().setDisplayShowHomeEnabled(true);
                            }else{
                                    buildDialog(MyProfileActivity.this).show();
                                    // finish();
                                }
                            } /* handle connectivity here */,
                            throwable    ->{} /* handle error here */
                    );


    }
    public void loadImage(String url){
        Glide.with(getApplicationContext())
                .load(url)
                //.placeholder()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .override(200, 200))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        _imageBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        _imageBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                //.override(300, 200)
                .into(_profilePic);
    }

    public void disableEditText(EditText text){
        text.setEnabled(false);
        //text.setInputType(InputType.TYPE_NULL);
        //text.setFocusable(false);
    }
    public void enableEditText(EditText text){
        text.setEnabled(true);
        //text.setInputType(InputType.TYPE_CLASS_TEXT);
        //text.setFocusable(true);
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
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        backgroundWorker.execute(email);
                    }
                    else{
                        buildDialog1(MyProfileActivity.this).show();
                    }
                });

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
            //_updatebtn.setEnabled(false);
            return;
        }
        //_updatebtn.setEnabled(true);
            String type = "updateProfile";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this, type);
            //backgroundWorker.delegate=this;
            backgroundWorker.execute(phoneNo, country, state, city, pincode, aadhar, email);
            _updatebtn.setVisibility(View.GONE);
            disableEditText(_phoneText);
            disableEditText(_aadharText);
            disableEditText(_countryText);
            disableEditText(_stateText);
            disableEditText(_cityText);
            disableEditText(_pinCodeText);


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
        if ( ((pincode.length() > 0 && pincode.length() < 6) || pincode.length()>6)){

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
    public android.app.AlertDialog.Builder buildDialog(Context c) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
    public android.app.AlertDialog.Builder buildDialog1(Context c) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setTitle("Oops!");
        builder.setMessage("No internet. Check your connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        return builder;
    }

}

