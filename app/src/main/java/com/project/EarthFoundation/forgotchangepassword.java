package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class forgotchangepassword extends AppCompatActivity {

    forgotpassword fp;
    String email,newPassword;

    @BindView(R.id.et_code) EditText _newPasswordText;
    @BindView(R.id.btn_reset) Button _forgotPassButton;
    @BindView(R.id.re_password) EditText _reEnterPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        _newPasswordText.setTypeface(myCustomFont);
        _forgotPassButton.setTypeface(myCustomFont);
        _reEnterPasswordText.setTypeface(myCustomFont);


        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        connectivity -> {
                            if(connectivity.state()==NetworkInfo.State.CONNECTED) {
                                setContentView(R.layout.activity_forgotchangepassword);
                                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                                setSupportActionBar(toolbar);

                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                // getSupportActionBar().setDisplayShowHomeEnabled(true);
                                fp = new forgotpassword();

                                // get email
                                email = fp.email;
//                                ButterKnife.bind(this);


                                _forgotPassButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changepassword();
                                    }
                                });
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
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        backgroundWorker.execute(email,newPassword);                    }
                    else{
                        buildDialog(forgotchangepassword.this).show();
                    }
                });


    }
    public void onChangeFailed() {
        Toast.makeText(getBaseContext(), "Password Changed Failed", Toast.LENGTH_LONG).show();

        _forgotPassButton.setEnabled(true);
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

    public boolean validate() {
        boolean valid = true;

        newPassword = _newPasswordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 15) {
            _newPasswordText.setError("Password must be between 4 to 15 alphanumeric characters");
            valid = false;
        } else {
            _newPasswordText.setError(null);
        }
        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(newPassword))) {
            _reEnterPasswordText.setError("Password do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
    public android.app.AlertDialog.Builder buildDialog(Context c) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setTitle("Oops!");
        builder.setMessage("No internet. Check your connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                 finish();
            }
        });

        return builder;
    }

}