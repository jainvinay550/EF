package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class forgotpassword extends AppCompatActivity implements BackgroundWorkerResponce {

    private static final String TAG = "ForgotPassword";
    private String received_email,received_otp;
    private String otp;
    public String message;
    public static String email;
    AlertDialog alertDialog;

    @BindView(R.id.et_email) EditText _emailText;
    @BindView(R.id.btn_reset) Button _forgotPassButton;
    @BindView(R.id.et_code) EditText _otp;
    @BindView(R.id.emailNotification) TextView _emailNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        _forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });

    }

    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        try {
            output=output.replace("[","");
            output=output.replace("]","");
            JSONObject reader = new JSONObject(output);
            received_email=reader.getString("email");
            received_otp=reader.getString("temp_password");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgotpassword() {
        Log.d(TAG, "FPassword");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _forgotPassButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(forgotpassword.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


         email = _emailText.getText().toString();


        // TODO: Implement your own forgotpassword logic here.

        String type = "ForgotPassword";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        backgroundWorker.delegate=this;
        backgroundWorker.execute(email);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if(email.equals(received_email)){
                            onForgotSuccess();
                        } else{
                            _emailText.setError("Email does not Exist. Please try using registered email address.");
                            _forgotPassButton.setEnabled(true);
                        }
                        progressDialog.dismiss();
                    }
                }, 10000);
    }


    public void onForgotSuccess() {
        _forgotPassButton.setEnabled(true);
        _emailText.setVisibility(View.GONE);
        _emailText.setHint("");
        _otp.setVisibility(View.VISIBLE);
        _emailNotification.setVisibility(View.VISIBLE);
        _forgotPassButton.setText("Verify OTP");
        _forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateOtp();
            }
        });
    }

    public void validateOtp(){
        otp=_otp.getText().toString();
        if(otp.equals(received_otp)){

            Intent intent = new Intent(getApplicationContext(), forgotchangepassword.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(), "Code do not match", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), forgotpassword.class);
            startActivity(intent);
            finish();
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _forgotPassButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
