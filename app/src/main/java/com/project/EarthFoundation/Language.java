package com.project.EarthFoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Language extends AppCompatActivity {

    TextView tv;
    final String[] listitems = {"English","मराठी","हिंदी"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_language);

        tv = (TextView) findViewById(R.id.selected_language);

        Button changeLang = findViewById(R.id.btn_changelanguage);
        Button next = findViewById(R.id.btn_next);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        changeLang.setTypeface(myCustomFont);
        next.setTypeface(myCustomFont);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
    }

    private void showChangeLanguageDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Language.this);
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
}
