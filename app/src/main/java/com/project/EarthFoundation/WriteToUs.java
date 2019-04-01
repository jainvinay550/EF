package com.project.EarthFoundation;


import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class WriteToUs extends AppCompatActivity{
    UserSessionManager session;
    String email;

    TextView to,from;
    EditText subject,message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_us);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_darker)));

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(UserSessionManager.KEY_EMAIL);

        from = findViewById(R.id.txtFrom);
        to = findViewById(R.id.txtTo);
        subject = findViewById(R.id.txtSubject);
        message = findViewById(R.id.txtMessage);

        from.setText(email);
        to.setText("contact@earthfoundation.in");

        Button okButton = findViewById(R.id.btnOK);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        okButton.setTypeface(myCustomFont);
        subject.setTypeface(myCustomFont);
        message.setTypeface(myCustomFont);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fm = from.getText().toString();
                String too = to.getText().toString();
                String sub = subject.getText().toString();
                String mess = message.getText().toString();
                Intent mail = new Intent(Intent.ACTION_SEND);

                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{too});
                mail.putExtra(Intent.EXTRA_SUBJECT, sub);
                mail.putExtra(Intent.EXTRA_TEXT, mess);
                mail.setType("message/rfc822");
                startActivity(Intent.createChooser(mail, "Send email via:"));
                finish();
            }
        });
    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//    @Override
//    public void onBackPressed() {
//        // Disable going back to the previous activity
//        finish();
//    }
}