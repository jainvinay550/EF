package com.project.EarthFoundation;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUs extends AppCompatActivity {

    @BindView(R.id.twitter) TextView _twitterClick;
    @BindView(R.id.facebook) TextView _facebookClick;
    @BindView(R.id.whatsApp) TextView _whatsAppClick;
    @BindView(R.id.youtube) TextView _youtubeClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        ButterKnife.bind(this);

//        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
//        _twitterClick.setTypeface(myCustomFont);
//        _facebookClick.setTypeface(myCustomFont);
//        _whatsAppClick.setTypeface(myCustomFont);
//        _youtubeClick.setTypeface(myCustomFont);

        _twitterClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/EarthFoundatio1?s=08"));
                startActivity(browserIntent);
            }
        });
        _facebookClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/earthfoundation.ngo.1"));
                startActivity(browserIntent);
            }
        });
        _whatsAppClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://chat.whatsapp.com/JLGSJWhcBa4G9p6nsOHTSo"));
                startActivity(browserIntent);
            }
        });
        _youtubeClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://youtu.be/addme/RTTc2flwJb8Ty0eLq8_kKQqfIcXLwA"));
                startActivity(browserIntent);
            }
        });
    }
}

