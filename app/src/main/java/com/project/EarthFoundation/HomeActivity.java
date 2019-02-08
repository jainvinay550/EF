package com.project.EarthFoundation;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BackgroundWorkerResponce {
    UserSessionManager session;
    Button UploadImageOnServerButton;
    ImageView lblImage;
    TextView lblName, lblEmail, lblTreesPlanted, lblContribution, lblTokensEarned;
    ProgressBar imageBar,homeBar;
    String treesPlanted, contribution, tokensEarned, name, email, profile_picture,user_type;
    LinearLayout lblPlantTree,lblcontribution;
    Button btnLogout;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
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
    ArrayList<String> treeNamesList = new ArrayList<>();
    ArrayList<String> treeAddressList = new ArrayList<>();
    ArrayList<String> plantDateList = new ArrayList<>();
    ArrayList<String> updatedDateList = new ArrayList<>();
    ArrayList<String> updateStatusList = new ArrayList<>();
    ArrayList<String> treeImageList = new ArrayList<>();

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Theme_AppCompat_Light_Dialog_Alert
        if(!isConnected(HomeActivity.this)) {
            buildDialog(HomeActivity.this).show();}
        else {
            loadLocale();
            setContentView(R.layout.activity_home);
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

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            AppRate.with(this)
                    .setInstallDays(3) // default 10, 0 means install day.
                    .setLaunchTimes(3) // default 10
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
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef001.jpeg");
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef002.jpeg");
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef003.jpeg");
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef004.jpeg");
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef005.jpeg");
            urls.add("http://earthfoundation.in/EF/Uploads/TreePhotos/ef006.jpeg");
            init();
            getIntegerValues();
            loadTreeDetails();

            lblPlantTree = (LinearLayout) findViewById(R.id.plant_tree_layout);
            lblcontribution = (LinearLayout) findViewById(R.id.plantation_contribution_layout);
            lblPlantTree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //                        .setAction("Action", null).show();
                    Intent intent = new Intent(getApplicationContext(), PlantTreeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            lblcontribution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //                        .setAction("Action", null).show();
                    Intent intent = new Intent(getApplicationContext(), MyTreesActivity.class);
                    intent.putStringArrayListExtra("treeNamesList", treeNamesList);
                    intent.putStringArrayListExtra("treeAddressList", treeAddressList);
                    intent.putStringArrayListExtra("plantDateList", plantDateList);
                    intent.putStringArrayListExtra("updatedDateList", updatedDateList);
                    intent.putStringArrayListExtra("updateStatusList", updateStatusList);
                    intent.putStringArrayListExtra("treeImageList", treeImageList);
                    startActivity(intent);

                }
            });
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);
            /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            UploadImageOnServerButton = (Button) header.findViewById(R.id.upload_btn);
            lblImage = (ImageView) header.findViewById(R.id.profile_image);
            imageBar = (ProgressBar) header.findViewById(R.id.profile_bar);
            lblName = (TextView) header.findViewById(R.id.username);
            lblEmail = (TextView) header.findViewById(R.id.email);
            byteArrayOutputStream = new ByteArrayOutputStream();
            lblImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
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
            if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                            5);
                }
            }
            if(user_type.equals("Admin"))
                lblName.setText(name+"(Admin)");
            else
                lblName.setText(name);
            lblEmail.setText(email);
            // profile_picture=profile_picture.replace("\\","");

            if (!profile_picture.isEmpty()) {
                new ImageLoadTask(profile_picture, lblImage, imageBar).execute();
                //profile_picture=profile_picture.replace("\\","");
                // lblImage.setImageBitmap(getBitmapFromURL("http://www.earthfoundation.in/EF/Uploads/Nayan%20Dhawas.jpg"));
            } else{
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
                intent.putExtra("treeName",treeNamesList.get(i));
                intent.putExtra("treeAddress",treeAddressList.get(i));
                intent.putExtra("plantDate",plantDateList.get(i));
                intent.putExtra("updatedDate",updatedDateList.get(i));
                intent.putExtra("updateStatus",updateStatusList.get(i));
                intent.putExtra("treeImage",treeImageList.get(i));
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(Unique_Integer_Number, mBuilder.build());
            }
        }
    }


    //Method to initialize Total trees, Contribution and token values
    public void getIntegerValues() {

        lblTreesPlanted = (TextView) findViewById(R.id.trees_planted);
        lblContribution = (TextView) findViewById(R.id.plantation_contribution);
        lblTokensEarned = (TextView) findViewById(R.id.tokens_earned);

        String type = "GetIntValues";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(email);

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//                        lblTreesPlanted.setText(treesPlanted);
//                        lblContribution.setText(contribution);
//                        lblTokensEarned.setText(tokensEarned);
//                    }
//                }, 2000);
    }


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
                updateStatusList.add(userDetail.getString("updateStatus"));
                treeNamesList.add(userDetail.getString("treeName"));
                treeAddressList.add(userDetail.getString("treeAddress"));
                plantDateList.add(userDetail.getString("plantDate"));
                updatedDateList.add(userDetail.getString("updatedDate"));
                treeImageList.add(userDetail.getString("treeImage"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(getApplicationContext(), aboutus.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
            contribution=lblContribution.getText().toString();
            tokensEarned=lblTokensEarned.getText().toString();
            Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
            intent.putExtra("contribution",contribution);
            intent.putExtra("tokensEarned",tokensEarned);
            startActivity(intent);
            //finish();
//                        }
//                    });
//                }
//            });

            //thread.start();

        }else if(id == R.id.nav_trees){
            Intent intent = new Intent(getApplicationContext(), MyTreesActivity.class);
            intent.putStringArrayListExtra("treeNamesList", treeNamesList);
            intent.putStringArrayListExtra("treeAddressList", treeAddressList);
            intent.putStringArrayListExtra("plantDateList", plantDateList);
            intent.putStringArrayListExtra("updatedDateList", updatedDateList);
            intent.putStringArrayListExtra("updateStatusList", updateStatusList);
            intent.putStringArrayListExtra("treeImageList", treeImageList);
            startActivity(intent);
        }
//        else if (id == R.id.nav_contactus) {
//
//        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle(R.string.logoutnotification);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            session.logoutUser();
                            //finish();
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



        else if (id == R.id.nav_setting) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    Toast.makeText(HomeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    lblImage.setImageBitmap(FixBitmap);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            lblImage.setImageBitmap(FixBitmap);
            UploadImageOnServerButton.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public void UploadImageToServer() {
        String type = "ImageUpload";
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        String url_name = name.replace(" ", "_");
        backgroundWorker.execute(ImageTag, url_name, ImageName, ConvertImage, email);
        UploadImageOnServerButton.setVisibility(View.GONE);
        session.createUserLoginSession(name, email, "http://earthfoundation.in/EF/Uploads/UserProfilePictures/" + url_name + ".jpg",user_type);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            } else {

                Toast.makeText(HomeActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public android.app.AlertDialog.Builder buildDialog(Context c) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this, urls));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

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
        }, 1500, 1500);

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
        String type = "GetTreeData";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        backgroundWorker.delegate = this;
        backgroundWorker.execute(email);
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
