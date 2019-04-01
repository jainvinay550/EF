package com.project.EarthFoundation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.app.ProgressDialog;
import java.io.ByteArrayOutputStream;
import android.content.DialogInterface;

import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.bumptech.glide.Glide;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BackgroundWorkerResponce{
    UserSessionManager session;
    Button UploadImageOnServerButton;
    ImageView lblImage;
    TextView lblName, lblEmail,lblTokensEarned;
    ProgressBar imageBar,homeBar;
    String treesPlanted, contribution, tokensEarned, name, email, profile_picture,user_type,password;
    LinearLayout be_a_part,plant_function,about_earthF,about_santual;
    CoordinatorLayout coordinatorLayout;
    Button btnLogout;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    String dispDate;
   // ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> urls = new ArrayList<String>();


    // ArrayList for person names, email Id's and mobile numbers
    ArrayList<String> treeIdList = new ArrayList<>();
    ArrayList<String> treeNamesList = new ArrayList<>();
    ArrayList<String> treeAddressList = new ArrayList<>();
    ArrayList<String> plantDateList = new ArrayList<>();
    ArrayList<String> updatedDateList = new ArrayList<>();
    ArrayList<String> updateStatusList = new ArrayList<>();
    ArrayList<String> treeImageList = new ArrayList<>();
    ArrayList<String> treeRelationList = new ArrayList<>();
    ArrayList<String> treeCountList = new ArrayList<>();

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
//

        //Theme_AppCompat_Light_Dialog_Alert
//        if(!isConnected(HomeActivity.this)) {
//            buildDialog(HomeActivity.this).show();}
//        else {
            ReactiveNetwork
                    .observeNetworkConnectivity(getApplicationContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            connectivity -> {
                                if(connectivity.state()==NetworkInfo.State.CONNECTED) {
                                    // do something with isConnectedToInternet value
                                    loadLocale();
                                    //            final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this,
                                    //                    R.style.Theme_AppCompat_Light_Dialog_Alert);
                                    //            progressDialog.setTitle("Please wait till we get things ready for you");
                                    //            progressDialog.setMessage("Loading");
                                    //            progressDialog.setIndeterminate(true);
                                    //            progressDialog.show();
                                    //            new android.os.Handler().postDelayed(
                                    //                    new Runnable() {
                                    //                        public void run() {
                                    //                            progressDialog.dismiss();
                                    //                        }
                                    //                    }, 4000);
                                    session = new UserSessionManager(getApplicationContext());

                                    Toolbar toolbar = findViewById(R.id.toolbar);
                                    setSupportActionBar(toolbar);

                                    AppRate.with(this)
                                            .setInstallDays(1) // default 10, 0 means install day.
                                            .setLaunchTimes(2) // default 10
                                            .setRemindInterval(7) // default 1
                                            .setShowLaterButton(true) // default true
                                            .setDebug(false) // default false
                                            .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                                                @Override
                                                public void onClickButton(int which) {
                                                    Log.d(MainActivity.class.getName(), Integer.toString(which));
                                                }
                                            })
                                            .monitor();

                                    // Show a dialog if meets conditions
                                    AppRate.showRateDialogIfMeetsConditions(this);

                                    //        TextView lblName = (TextView) findViewById(R.id.username);
                                    //        TextView lblEmail = (TextView) findViewById(R.id.email);

                                    //            Toast.makeText(getApplicationContext(),
                                    //                    "User Login Status: " + session.isUserLoggedIn(),
                                    //                    Toast.LENGTH_LONG).show();


                                    // get user data from session
                                    HashMap<String, String> user = session.getUserDetails();
                                    // get name
                                    name = user.get(UserSessionManager.KEY_NAME);
                                    // get email
                                    email = user.get(UserSessionManager.KEY_EMAIL);
                                    user_type = user.get(UserSessionManager.KEY_USER_TYPE);
                                    profile_picture = user.get(UserSessionManager.KEY_IMAGE);
                                    password = user.get(UserSessionManager.KEY_PASSWORD);
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef001.jpeg");
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef002.jpeg");
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef003.jpeg");
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef004.jpeg");
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef005.jpeg");
                                    urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef006.jpeg");
                                    init();

                                    loadTreeDetails();

                                    //get password from login activity
                                    //Intent intent = getIntent();
                                    // password=intent.getStringExtra("password");

//                                    lblPlantTree = (LinearLayout) findViewById(R.id.plant_tree_layout);
                                    coordinatorLayout=findViewById(R.id.coordinatorLayout);
                                    be_a_part = findViewById(R.id.be_a_part_layout);
                                    plant_function = findViewById(R.id.plant_function_layout);
                                    about_earthF = findViewById(R.id.about_ef_layout);
                                    about_santual = findViewById(R.id.balance_project_layout);
                                    plant_function.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this,R.style.PauseDialog);
//                                            builder.setMessage(R.string.tree_importance)
//                                                    .setCancelable(false)
//                                                    .setPositiveButton(R.string.message, new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    });
//                                            AlertDialog dialog = builder.create();
//                                            dialog.show();
//                                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                                            positiveButton.setTextColor(Color.parseColor("#ffffff"));
                                            Intent intent = new Intent(getApplicationContext(),TreeImportanceActivity.class);
                                            startActivity(intent);
//
                                        }
                                    });
                                    about_earthF.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this,R.style.PauseDialog);
                                            builder.setMessage(R.string.about_EF)
                                                    .setCancelable(false)
                                                    .setPositiveButton(R.string.message, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                            positiveButton.setTextColor(Color.parseColor("#ffffff"));
//
                                        }
                                    });
                                    about_santual.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this,R.style.PauseDialog);
                                            builder.setMessage(R.string.about_SP)
                                                    .setCancelable(false)
                                                    .setPositiveButton(R.string.message, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                            positiveButton.setTextColor(Color.parseColor("#ffffff"));
//
                                        }
                                    });

//
                                    be_a_part.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                                            single
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(isConnectedToInternet -> {
                                                        if(isConnectedToInternet.equals(true)) {
                                                            //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                            //                        .setAction("Action", null).show();
                                                            Intent intent = new Intent(getApplicationContext(), SantulanPrakalpActivity.class);
                                                            intent.putStringArrayListExtra("treeIdList", treeIdList);
                                                            intent.putStringArrayListExtra("treeNamesList", treeNamesList);
                                                            intent.putStringArrayListExtra("treeAddressList", treeAddressList);
                                                            intent.putStringArrayListExtra("plantDateList", plantDateList);
                                                            intent.putStringArrayListExtra("updatedDateList", updatedDateList);
                                                            intent.putStringArrayListExtra("updateStatusList", updateStatusList);
                                                            intent.putStringArrayListExtra("treeImageList", treeImageList);
                                                            intent.putStringArrayListExtra("treeRelationList", treeRelationList);
                                                            intent.putStringArrayListExtra("treeCountList", treeCountList);
                                                             // Intent intent = new Intent(getApplicationContext(), SantulanPrakalpActivity.class);
                                                              startActivity(intent);
                                                            } else{
                                                                Snackbar snackbar = Snackbar
                                                                        .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                                                snackbar.show();

                                                            //buildDialog1(HomeActivity.this).show();
                                                        }
                                                    });
//
                                        }
                                    });
                                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                                            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                                    drawer.addDrawerListener(toggle);
                                    toggle.syncState();

                                    NavigationView navigationView = findViewById(R.id.nav_view);
                                    navigationView.setNavigationItemSelectedListener(this);

                                    View header = navigationView.getHeaderView(0);
                                    /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
                                    UploadImageOnServerButton = header.findViewById(R.id.upload_btn);
                                    UploadImageOnServerButton.setTypeface(myCustomFont);
                                    lblImage = header.findViewById(R.id.profile_image);
                                    imageBar = header.findViewById(R.id.profile_bar);
                                    lblName = header.findViewById(R.id.username);
                                    lblEmail = header.findViewById(R.id.email);
                                    byteArrayOutputStream = new ByteArrayOutputStream();
                                    lblImage.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                            if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                                            5);
                                                }
                                            }
                                            showPictureDialog();
                                            //UploadImageToServer();
                                            return false;
                                        }
                                    });
                                    UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //GetImageNameFromEditText = imageName.getText().toString();
                                            UploadImageToServer();
                                        }
                                    });
                                    //

                                    if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    3);
                                        }
                                    }
                                    //isReadStoragePermissionGranted();
                                    if (user_type.equals("Admin"))
                                        lblName.setText(name + "(Admin)");
                                    else
                                        lblName.setText(name);
                                    lblEmail.setText(email);
                                    // profile_picture=profile_picture.replace("\\","");

                                    if (!profile_picture.isEmpty()) {
                                        //new ImageLoadTask(profile_picture, lblImage, imageBar).execute();
                                        loadImage(profile_picture);

                                    } else {
                                        imageBar.setVisibility(View.GONE);
                                    }


                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                                                    notificationShow();
                                                }
                                            }, 5000);
                                }
                                else{
//                                    buildDialog(HomeActivity.this).show();
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                                    snackbar.show();
                                   // finish();
                                }
                                } /* handle connectivity here */,
                            throwable    ->{} /* handle error here */
                    );

        //}
    }

    public void loadImage(String url){
        Glide.with(getApplicationContext())
                .load(url)
                //.placeholder()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .override(170, 170))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                //.override(300, 200)
                .into(lblImage);
    }

    public void notificationShow(){

        String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";
        for(int i=0;i<updateStatusList.size();i++){
            int Unique_Integer_Number=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            if(updateStatusList.get(i).equals("yes")){
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("default",
                            "YOUR_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                    mNotificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                        .setSmallIcon(R.drawable.elflogo) // notification icon
                        .setContentTitle(treeNamesList.get(i)) // title for notification
                        .setContentText("Please Update Tree information\r\n"+"Last updated on "+updatedDateList.get(i))// message for notification
                        .setAutoCancel(true); // clear notification after click

                Intent intent = new Intent(getApplicationContext(), UpdateTreeDetails.class);
                intent.putStringArrayListExtra("treeIdList", treeIdList);
                intent.putStringArrayListExtra("treeNamesList", treeNamesList);
                intent.putStringArrayListExtra("treeAddressList", treeAddressList);
                intent.putStringArrayListExtra("plantDateList", plantDateList);
                intent.putStringArrayListExtra("updatedDateList", updatedDateList);
                intent.putStringArrayListExtra("updateStatusList", updateStatusList);
                intent.putStringArrayListExtra("treeImageList", treeImageList);
                intent.putStringArrayListExtra("treeRelationList", treeRelationList);
                intent.putStringArrayListExtra("treeCountList", treeCountList);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(Unique_Integer_Number, mBuilder.build());
            }
        }
    }


    //Method to initialize Total trees, Contribution and token values



    @Override
    public void processFinish(String output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        try {
            JSONArray userArray = new JSONArray(output);
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                treeIdList.add(userDetail.getString("treeId"));
                updateStatusList.add(userDetail.getString("updateStatus"));
                treeNamesList.add(userDetail.getString("treeName"));
                treeAddressList.add(userDetail.getString("treeAddress"));
                stringToDate(userDetail.getString("plantDate"));
                plantDateList.add(dispDate);
                stringToDate(userDetail.getString("updatedDate"));
                updatedDateList.add(dispDate);
                treeImageList.add(userDetail.getString("treeImage"));
                treeRelationList.add(userDetail.getString("relation"));
                treeCountList.add(userDetail.getString("treeCount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void stringToDate(String received_date) {

        try {
            Calendar cal = Calendar.getInstance();
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(received_date);
            cal.setTime(date1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            dispDate = Integer.toString(day) + "/" + Integer.toString(month+1) + "/" + Integer.toString(year);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final ProgressDialog progressDialog1 = new ProgressDialog(HomeActivity.this,
                    R.style.Theme_AppCompat_Light_Dialog_Alert);
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle(R.string.exitnotification);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progressDialog1.onBackPressed();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.share){
//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            String shareBodyText = "Let's sign-up and become part of Earth Foundation.\n" +
//                    "Download the App using - https://play.google.com/store/apps/details?id=com.project.EarthFoundation.\n"+
//                    "Download the Earth Foundation App NOW!";
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//            startActivity(Intent.createChooser(sharingIntent, "Choose sharing method"));
            Intent shareIntent;
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.sharelogo);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"sharelogo.png";
            OutputStream out = null;
            File file=new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path=file.getPath();
            Uri bmpUri = Uri.parse("file://"+path);
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Let's sign-up and become part of Earth Foundation.\n" +
                    "Download the App using - https://play.google.com/store/apps/details?id=com.project.EarthFoundation.\n"+
                    "Download the Earth Foundation App NOW!");
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent,"Choose sharing method"));
        }
        else if (id == R.id.nav_dashboard) {
            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
            single
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isConnectedToInternet -> {
                        if(isConnectedToInternet.equals(true)) {
                            Intent intent = new Intent(getApplicationContext(), SantulanPrakalpActivity.class);
                            intent.putStringArrayListExtra("treeIdList", treeIdList);
                            intent.putStringArrayListExtra("treeNamesList", treeNamesList);
                            intent.putStringArrayListExtra("treeAddressList", treeAddressList);
                            intent.putStringArrayListExtra("plantDateList", plantDateList);
                            intent.putStringArrayListExtra("updatedDateList", updatedDateList);
                            intent.putStringArrayListExtra("updateStatusList", updateStatusList);
                            intent.putStringArrayListExtra("treeImageList", treeImageList);
                            intent.putStringArrayListExtra("treeRelationList", treeRelationList);
                            intent.putStringArrayListExtra("treeCountList", treeCountList);
                            startActivity(intent);                  }
                        else{
                            //buildDialog1(HomeActivity.this).show();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                    });

        }
        else if(id==R.id.nav_admin){
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_aboutus){
            Intent intent = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(getApplicationContext(), ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//            contribution=lblContribution.getText().toString();
//            tokensEarned=lblTokensEarned.getText().toString();
            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
            single
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isConnectedToInternet -> {
                        if(isConnectedToInternet.equals(true)) {
                            Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                            startActivity(intent); }
                        else{
                           // buildDialog1(HomeActivity.this).show();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                    });

            //finish();
//                        }
//                    });
//                }
//            });

            //thread.start();

        }
        else if(id == R.id.nav_writeus){
            Intent intent = new Intent(getApplicationContext(), WriteToUs.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle(R.string.logoutnotification);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            session.logoutUser();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_setting) {
            finish();
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            //intent.putExtra("password",password);
            startActivity(intent);
       }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                //try {
                    //FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                   //int rotateImage = getCameraPhotoOrientation(HomeActivity.this, contentURI, filePath);
                    // String path = saveImage(bitmap);
                    Toast.makeText(HomeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    FixBitmap = scaleDownAndRotatePic(filePath);
                    lblImage.setImageBitmap(FixBitmap);
                    if(FixBitmap.getByteCount()<=10000000){
                        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    }else if(FixBitmap.getByteCount()>10000000 && FixBitmap.getByteCount()<=50000000){
                        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                    }
                    else if(FixBitmap.getByteCount()>50000000 && FixBitmap.getByteCount()<=100000000){
                        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 5, byteArrayOutputStream);
                    }
                    else{
                        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 2, byteArrayOutputStream);
                    }
                    //FixBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                   // byteArray = byteArrayOutputStream.toByteArray();
//                    Glide
//                            .with( getApplicationContext() )
//                            .load(FixBitmap)
//                            .apply(new RequestOptions()
//                                    .placeholder(R.drawable.profile_image)
//                                    .skipMemoryCache(true)
//                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                    .override(120, 120)
//                                .transform( new RotateTransformation( getApplicationContext(), rotateImage)))
//                            .into( lblImage );
                   UploadImageOnServerButton.setVisibility(View.VISIBLE);

//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(HomeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            lblImage.setImageBitmap(FixBitmap);
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
//            if(FixBitmap.getByteCount()<=10000000){
//                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
//            }else if(FixBitmap.getByteCount()>10000000 && FixBitmap.getByteCount()<=50000000){
//                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
//            }
//            else if(FixBitmap.getByteCount()>50000000 && FixBitmap.getByteCount()<=100000000){
//                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
//            }
//            else{
//                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//            }
            //FixBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
//            byteArray = byteArrayOutputStream.toByteArray();
            UploadImageOnServerButton.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public static   Bitmap scaleDownAndRotatePic(String path) {//you can provide file path here
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 400;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            Log.e("ExifInteface .........","width : "+Integer.toString(width_tmp)+" Height : "+Integer.toString(height_tmp));
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            Log.e("ExifInteface .........","scale : "+Integer.toString(scale));
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
//            if(o.inBitmap.getByteCount()<=10000000){
//                o2.inSampleSize = 1;
//            }else if(o.inBitmap.getByteCount()>10000000 && o.inBitmap.getByteCount()<=50000000){
//                o2.inSampleSize = 2;
//            }
//            else if(o.inBitmap.getByteCount()>50000000 && o.inBitmap.getByteCount()<=100000000){
//                o2.inSampleSize = 3;
//            }
//            else{
//                o2.inSampleSize = 4;
//            }

            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            //Log.e("ExifInteface .........", "rotation =" + orientation);

            //exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            //Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);
                return bitmap;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                //Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

//    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
//        int rotate = 0;
//        try {
//            context.getContentResolver().notifyChange(imageUri, null);
//            File imageFile = new File(imagePath);
//
//            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }
//
//            Log.i("RotateImage", "Exif orientation: " + orientation);
//            Log.i("RotateImage", "Rotate value: " + rotate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rotate;
//    }

    public void UploadImageToServer() {
        String type = "ImageUpload";
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        String url_name = name.replace(" ", "_");
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        backgroundWorker.execute(ImageTag, url_name, ImageName, ConvertImage, email);
                        UploadImageOnServerButton.setVisibility(View.GONE);
                        session.createUserLoginSession(name, email, "http://earthfoundation.in/EF/Uploads/UserProfilePictures/" + url_name + ".jpg", user_type, password);
                        //loadImage("http://earthfoundation.in/EF/Uploads/UserProfilePictures/" + url_name + ".jpg");
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 2:
                //Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //downloadPdfFile();
                }else{
                    Toast.makeText(HomeActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

                }
                break;

            case 3:
                //Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                   // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //SharePdfFile();
                }else{
                    Toast.makeText(HomeActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
                }
                break;
            case 5:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Now user should be able to use camera

                } else {

                    Toast.makeText(HomeActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

                }

        }
    }


//    public android.app.AlertDialog.Builder buildDialog(Context c) {
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
//        builder.setTitle("No Internet Connection");
//        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
//
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//               finish();
//            }
//        });
//
//        return builder;
//    }
//

    private void init() {
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this, urls));

        CirclePageIndicator indicator = findViewById(R.id.indicator);

        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = urls.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });

    }

    public void loadTreeDetails() {

        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        String type = "GetTreeData";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
                        backgroundWorker.delegate = this;
                        backgroundWorker.execute(email);
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                        snackbar.show();

                        //buildDialog1(HomeActivity.this).show();
                    }
                });

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        new MyProfileActivity(HomeActivity.this,treeNamesList, treeAddressList, plantDateList, updateStatusList);
//                    }
//                }, 15000);


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
