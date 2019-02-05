package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class forgotchangepassword extends AppCompatActivity {

    forgotpassword fp;
    String email,newPassword;

    @BindView(R.id.et_code) EditText _newPasswordText;
    @BindView(R.id.btn_reset) Button _forgotPassButton;
    @BindView(R.id.re_password) EditText _reEnterPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotchangepassword);

        fp = new forgotpassword();

        // get email
        email = fp.email;
        ButterKnife.bind(this);


        _forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepassword();
            }
        });

    }

    public void changepassword() {

        if (!validate()) {
            onChangeFailed();
            return;
        }

        _forgotPassButton.setEnabled(false);

        newPassword = _newPasswordText.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(forgotchangepassword.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();



        // TODO: Implement your own forgotpassword logic here.

        String type = "ChangePassword";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);

        backgroundWorker.execute(email,newPassword);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                    }
                }, 2000);

    }
    public void onChangeFailed() {
        Toast.makeText(getBaseContext(), "Password Changed Failed", Toast.LENGTH_LONG).show();

        _forgotPassButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        newPassword = _newPasswordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            _newPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _newPasswordText.setError(null);
        }
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(newPassword))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}