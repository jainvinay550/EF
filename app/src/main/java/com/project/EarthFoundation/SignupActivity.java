package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity implements BackgroundWorkerResponce{
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_fname) EditText _fnameText;
    @BindView(R.id.input_lname) EditText _lnameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.titletextview) EditText _titletextview;
    @BindView(R.id.signup_layout) CoordinatorLayout coordinatorLayout;

    String msg="",titleText="";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        _emailText.setTypeface(myCustomFont);
        _passwordText.setTypeface(myCustomFont);
        _fnameText.setTypeface(myCustomFont);
        _lnameText.setTypeface(myCustomFont);
        _reEnterPasswordText.setTypeface(myCustomFont);
        _signupButton.setTypeface(myCustomFont);
        _titletextview.setTypeface(myCustomFont);


        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        connectivity -> {
                            if(connectivity.state()== NetworkInfo.State.CONNECTED) {

//                                ButterKnife.bind(this);

                                // Initializing a String Array
                                String[] gender = new String[]{
                                        "Title",
                                        "Mr.",
                                        "Mrs.",
                                        "Ms."
                                };

                                final List<String> genderList = new ArrayList<>(Arrays.asList(gender));
                                final Spinner spinner = (Spinner) findViewById(R.id.simpleSpinner);
                                // Initializing an ArrayAdapter
                                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                        this,R.layout.spinner_item,genderList){
                                    @Override
                                    public boolean isEnabled(int position){
                                        if(position == 0)
                                        {
                                            // Disable the first item from Spinner
                                            // First item will be use for hint
                                            return false;
                                        }
                                        else
                                        {
                                            return true;
                                        }
                                    }
                                    @Override
                                    public View getDropDownView(int position, View convertView,
                                                                ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        if(position == 0){
                                            // Set the hint text color gray
                                            tv.setTextColor(Color.GRAY);
                                            //tv.setText(gender[position]);
                                        }
                                        else {
                                            tv.setTextColor(Color.WHITE);
                                        }
                                        return view;
                                    }
                                };
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                                spinner.setAdapter(spinnerArrayAdapter);

                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedItemText = (String) parent.getItemAtPosition(position);


                                        // If user change the default selection
                                        // First item is disable and it is used for hint
                                        if(position > 0){
                                            // Notify the selected item text
                                            spinner.setVisibility(View.GONE);
                                            _titletextview.setVisibility(View.VISIBLE);
                                            _titletextview.setInputType(InputType.TYPE_NULL);
                                            titleText=selectedItemText;
                                            _titletextview.setText(titleText);

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                                _signupButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                        single
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(isConnectedToInternet -> {
                                                    if(isConnectedToInternet.equals(true)) {
                                                        signup();
                                                    }
                                                    else{
                                                        Snackbar snackbar = Snackbar
                                                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                                        snackbar.show();
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

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        String type = "register";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
                        backgroundWorker.delegate=this;
                        backgroundWorker.execute(fname,lname,email,password,titleText);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onSignupSuccess or onSignupFailed
                                        // depending on success
                                        if(msg.isEmpty()) {
                                            Toast.makeText(getBaseContext(), "Failed to SignUp. Please try again", Toast.LENGTH_LONG).show();
                                            _signupButton.setEnabled(true);
                                        } else if(msg.equals("Not Match")){
                                            _emailText.setError("Email already taken. Please try using different email address.");
                                            _signupButton.setEnabled(true);
                                        }else {
                                            onSignupSuccess();
                                        }

                                        // onSignupFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                        snackbar.show();

                        //buildDialog1(HomeActivity.this).show();
                    }
                });




    }
    @Override
    public void processFinish(String output){
            msg = output;
            //Toast.makeText(getBaseContext(),received_email+" "+email+","+received_password+" "+password, Toast.LENGTH_LONG).show();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        // finish activity
       finish();
    }

    public void onSignupFailed() {
        if (titleText.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please select title", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
        }

        _signupButton.setEnabled(true);
    }

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

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (fname.isEmpty() || fname.length() < 3 || fname.length() > 20) {
            _fnameText.setError("Min 3 - Max 20 characters");
            valid = false;
        } else if(containsDigit(fname)) {
            _fnameText.setError("Name cannot be a number");
            valid = false;
        }else {
            _fnameText.setError(null);
        }
        if (lname.isEmpty() || lname.length() < 3 || lname.length() > 20) {
            _lnameText.setError("Min 3 - Max 20 characters");
            valid = false;
        } else if(containsDigit(lname)) {
            _lnameText.setError("Name cannot be a number");
            valid = false;
        }else {
            _lnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _passwordText.setError("Password must be between 4 to 15 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (titleText.isEmpty()) {
            valid = false;
        }


        return valid;
    }
}