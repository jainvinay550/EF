package com.project.EarthFoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity{

    final String[] listitems = {"English","मराठी","हिंदी"};
    UserSessionManager session;
    String email,password,newPassword,received_password;

    @BindView(R.id.old_password) EditText _oldPasswordText;
    @BindView(R.id.et_code) EditText _newPasswordText;
    @BindView(R.id.re_password) EditText _reEnterPasswordText;
    @BindView(R.id.btn_changepassword) Button _changePassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // get email
        email = user.get(UserSessionManager.KEY_EMAIL);
        received_password=user.get(UserSessionManager.KEY_PASSWORD);
//        Intent intent=getIntent();
//        received_password=intent.getStringExtra("password");

        _changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=_oldPasswordText.getText().toString();
                changepassword();
            }
        });

        Button changeLang = findViewById(R.id.btn_changelanguage);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

    }

    private void changepassword() {
        if (!validate()) {
            onChangeFailed();
            return;
        }
//        _changePassButton.setEnabled(false);

        if(password.equals(received_password)){
            onSuccess();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("Failed to change password");
            builder.setMessage("Incorrect old password. Please enter correct old password");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }

            });
            builder.show();
            //onLoginFailed();
        }
//        String type = "FetchPassword";
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
//        backgroundWorker.delegate=this;
//        backgroundWorker.execute(email);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        if(password.equals(received_password)){
//                            onSuccess(email,newPassword);
//                        }
//                        else{
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                            builder.setTitle("Change Password Failed");
//                            builder.setMessage("Incorrect old password. Please enter correct old password");
//
//                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            builder.show();
//                            onLoginFailed();
//                        }
//                    }
//                }, 2000);
    }

    private void onSuccess() {
        _changePassButton.setEnabled(true);
        _oldPasswordText.setVisibility(View.GONE);
        _oldPasswordText.setHintTextColor(0);
        _newPasswordText.setVisibility(View.VISIBLE);
        _reEnterPasswordText.setVisibility(View.VISIBLE);
        _changePassButton.setText("Change Password");
        _changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword=_newPasswordText.getText().toString();
                confirmchangepass();
            }
        });
    }
    public void confirmchangepass() {
        if (!validate1()) {
            onChangeFailed();
            return;
        }
        String type = "ChangePassword";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this, type);

        backgroundWorker.execute(email, newPassword);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        //
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    }
//                }, 5000);

    }
    public void onChangeFailed() {
        Toast.makeText(getBaseContext(), "Failed to change password", Toast.LENGTH_LONG).show();
        //_changePassButton.setEnabled(true);
    }
//    public void onLoginFailed() {
//
//        _changePassButton.setEnabled(true);
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
    private void showChangeLanguageDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
        mBuilder.setTitle("Choose Your Language");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    setLocale("en");
                    recreate();
                }
                else if(i==1){
                    setLocale("mr");
                    recreate();
                }
                else if(i==2){
                    setLocale("hi");
                    recreate();
                }
                dialogInterface.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
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

    public boolean validate() {
        boolean valid = true;

        newPassword = _newPasswordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (password.isEmpty()) {
            _oldPasswordText.setError("Enter valid password ");
            valid = false;
        } else {
            _oldPasswordText.setError(null);
        }
//        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 15) {
//            _newPasswordText.setError("Password must be between 4 to 15 alphanumeric characters");
//            valid = false;
//        } else {
//            _newPasswordText.setError(null);
//        }
//        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(newPassword))) {
//            _reEnterPasswordText.setError("Password do not match");
//            valid = false;
//        } else {
//            _reEnterPasswordText.setError(null);
//        }

        return valid;
    }
    public boolean validate1() {
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
}
