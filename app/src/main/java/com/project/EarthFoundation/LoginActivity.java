package com.project.EarthFoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements BackgroundWorkerResponce {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String received_email;
    private String received_fname;
    private  String received_lname;
    private String received_password;
    private String received_image_url,received_user_type;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_forgotpassword) TextView _forgotpasswordLink;
    @BindView(R.id.login_layout) CoordinatorLayout coordinatorLayout;
    UserSessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        _emailText.setTypeface(myCustomFont);
        _passwordText.setTypeface(myCustomFont);
        _loginButton.setTypeface(myCustomFont);

        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        connectivity -> {
                            if(connectivity.state()==NetworkInfo.State.CONNECTED) {
                                    loadLocale();

//                                    ButterKnife.bind(this);

                                    // User Session Manager
                                    session = new UserSessionManager(getApplicationContext());

                                    _loginButton.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                            single
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(isConnectedToInternet -> {
                                                        if(isConnectedToInternet.equals(true)) {
                                                            login();
                                                        }
                                                        else{
                                                            Snackbar snackbar = Snackbar
                                                                    .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                                            snackbar.show();
                                                            _loginButton.setEnabled(true);

                                                            //buildDialog1(HomeActivity.this).show();
                                                        }
                                                    });
                                        }
                                    });


                                    _signupLink.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // Start the Signup activity
                                            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                            single
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(isConnectedToInternet -> {
                                                        if(isConnectedToInternet.equals(true)) {
                                                            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                                                            startActivityForResult(intent, REQUEST_SIGNUP);
                                                            //finish();
                                                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                                        }
                                                        else{
                                                            Snackbar snackbar = Snackbar
                                                                    .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                                            snackbar.show();
                                                            _loginButton.setEnabled(true);

                                                            //buildDialog1(HomeActivity.this).show();
                                                        }
                                                    });

                                        }
                                    });

                                    _forgotpasswordLink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                            single
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(isConnectedToInternet -> {
                                                        if(isConnectedToInternet.equals(true)) {
                                                            Intent intent = new Intent(getApplicationContext(), forgotpassword.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else{
                                                            Snackbar snackbar = Snackbar
                                                                    .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                                            snackbar.show();
                                                            _loginButton.setEnabled(true);

                                                            //buildDialog1(HomeActivity.this).show();
                                                        }
                                                    });

                                        }
                                    });
                                }
                            else{
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                snackbar.show();
                                // finish();
                            }
                        } /* handle connectivity here */,
                        throwable    ->{} /* handle error here */
                );
    }

    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        try {
            output=output.replace("[","");
            output=output.replace("]","");
           // Toast.makeText(getBaseContext(),output, Toast.LENGTH_LONG).show();
            JSONObject reader = new JSONObject(output);
            received_email=reader.getString("email");
            received_fname=reader.getString("fname");
            received_lname=reader.getString("lname");
            received_password=reader.getString("password");
            received_image_url=reader.getString("image_url");
            received_user_type=reader.getString("user_type");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.Theme_AppCompat_Light_Dialog);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        String type = "login";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
                        backgroundWorker.delegate=this;
                        backgroundWorker.execute(email, password);
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        _loginButton.setEnabled(true);

                        //buildDialog1(HomeActivity.this).show();
                    }
                });



        //String result=backgroundWorker.doInBackground(type,email,password);

       // Toast.makeText(getBaseContext(), received_email+" "+received_password, Toast.LENGTH_LONG).show();

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        if(email.equals(received_email) && password.equals(received_password)){
//                            onLoginSuccess(received_fname+" "+received_lname,received_email);
//                        }
//                        else{
//                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                            builder.setTitle("Login Failed");
//                            builder.setMessage("Incorrect email or password. Please enter correct credentials");
//
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            builder.show();
//                            onLoginFailed();
//                        }
//                        //progressDialog.dismiss();
//                    }
//                }, 2000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String name,String email) {
        _loginButton.setEnabled(true);

        session.createUserLoginSession(name,email,received_image_url,received_user_type,received_password);

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("password",received_password);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Please enter your password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;}
            else{ return false;}
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile data or Wi-Fi to access this. Press OK to Exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String language = pref.getString("My Lang","");
        setLocale(language);

    }
}
