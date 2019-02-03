package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements BackgroundWorkerResponce{
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_fname) EditText _fnameText;
    @BindView(R.id.input_lname) EditText _lnameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_city) EditText _cityText;
    @BindView(R.id.input_pin) EditText _pinText;
    @BindView(R.id.input_aadhar) EditText _aadharText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;

    String msg;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

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
        String address = _addressText.getText().toString();
        String city = _cityText.getText().toString();
        String pin = _pinText.getText().toString();
        String aadhar = _aadharText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate=this;
        backgroundWorker.execute(type, fname,lname,address,city,pin,aadhar,email,mobile,password);



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if(msg.equals("Not Match")){
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
    @Override
    public void processFinish(String type, String output){
            msg = output;
            //Toast.makeText(getBaseContext(),received_email+" "+email+","+received_password+" "+password, Toast.LENGTH_LONG).show();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // finish activity
       finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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
        String address = _addressText.getText().toString();
        String pin = _pinText.getText().toString();
        String city = _cityText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String aadhar = _aadharText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (fname.isEmpty() || fname.length() < 3 || fname.length() > 20) {
            _fnameText.setError("Min 3 - Max 20 Characters");
            valid = false;
        } else if(containsDigit(fname)) {
            _fnameText.setError("Name cannot be a number");
            valid = false;
        }else {
            _fnameText.setError(null);
        }
        if (lname.isEmpty() || lname.length() < 3 || lname.length() > 20) {
            _lnameText.setError("Min 3 - Max 20 Characters");
            valid = false;
        } else if(containsDigit(lname)) {
            _lnameText.setError("Name cannot be a number");
            valid = false;
        }else {
            _lnameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (containsDigit(city)) {
            _cityText.setError("Enter Valid City");
            valid = false;
        } else if (city.isEmpty()) {
            _cityText.setError("Enter Valid City");
            valid = false;
        } else {
            _cityText.setError(null);
        }

        if (pin.isEmpty() || !(pin.length()==6)) {
            _pinText.setError("Enter Valid 6 Digits Pincode");
            valid = false;
        } else if(containsText(pin)) {
            _pinText.setError("Pin cannot be a text");
            valid = false;
        }else {
            _pinText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (aadhar.isEmpty() || !(aadhar.length() == 12)) {
            _aadharText.setError("Enter 12 Digits Aadhar Number ");
            valid = false;
        } else if(containsText(aadhar)) {
            _aadharText.setError("Aadhar cannot be a text");
            valid = false;
        }else {
            _aadharText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}