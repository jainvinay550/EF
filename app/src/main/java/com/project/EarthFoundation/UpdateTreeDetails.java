package com.project.EarthFoundation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.DatePickerDialog;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.security.AccessController.getContext;

public class UpdateTreeDetails extends AppCompatActivity implements BackgroundWorkerResponce{

    String treeName;
    String treeAddress;
    String plantDate;
    String updatedDate;
    String updateStatus;
    String treeImage;
    String treeId;
    String relation;
    String noOfTrees;

    EditText date;
    DatePickerDialog datePickerDialog;

    static int tree_counter=0;

    ImageView lblTreeImage;
    TextView lblTreeName,lblTreeLocation,lblTreePlantDate,lblTreeUpdatedDate,inMemoryOf,NoOfTrees;
    Button lblSelectImage,lblSubmit,lblShowUpdate;
    RadioGroup PlantStatusRadioGroup,TreeGuardRadioGroup;
    RadioButton radioYes,radioNo;
    LinearLayout lblDateLayout,lblImageLayout,lblButtonLayout,lbltreeStatusLayout,haveGuardLayout,NoOfTreesLayout;
    Bitmap FixBitmap;
    CoordinatorLayout coordinatorLayout;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage,treeStatusValue="",guardValue="",imageName="",GetDateValue="";
    String email;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> urls = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tree_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent= getIntent();
        treeName = intent.getStringExtra("treeName");
        treeAddress = intent.getStringExtra("treeAddress");
        plantDate = intent.getStringExtra("plantDate");
        updatedDate = intent.getStringExtra("updatedDate");
        updateStatus = intent.getStringExtra("updateStatus");
        treeImage = intent.getStringExtra("treeImage");
        treeId = intent.getStringExtra("treeId");
        relation = intent.getStringExtra("treeRelation");
        noOfTrees = intent.getStringExtra("treeCount");

        getTreeImages();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        init();
                    }
                }, 4000);
        datePicker();

        lblTreeImage=(ImageView) findViewById(R.id.tree_image);
        lblTreeName=(TextView) findViewById(R.id.tree_name);
        lblTreeLocation = (TextView) findViewById(R.id.location);
        lblTreePlantDate = (TextView) findViewById(R.id.planted_date);
        lblTreeUpdatedDate = (TextView) findViewById(R.id.updated_date);
        lblShowUpdate= (Button) findViewById(R.id.show_upload);
        lblSubmit=(Button) findViewById(R.id.btnSubmit);
        lblSelectImage=(Button) findViewById(R.id.select_image);
        lblDateLayout=(LinearLayout) findViewById(R.id.date_layout);
        lblImageLayout=(LinearLayout) findViewById(R.id.image_layout);
        lblButtonLayout=(LinearLayout) findViewById(R.id.button_layout);
        inMemoryOf = (TextView) findViewById(R.id.inMemoryOf);
        NoOfTrees = (TextView) findViewById(R.id.NoOfTrees);
        lbltreeStatusLayout=findViewById(R.id.treeStatusLayout);
        haveGuardLayout=findViewById(R.id.haveGuardLayout);
        NoOfTreesLayout=findViewById(R.id.NoOfTreesLayout);
        radioYes=findViewById(R.id.radioYes);
        radioNo=findViewById(R.id.radioNo);
        coordinatorLayout=findViewById(R.id.updateTree_Layout);

        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/TNR.ttf");
        date.setTypeface(myCustomFont);
        lblSelectImage.setTypeface(myCustomFont);
        lblSubmit.setTypeface(myCustomFont);
        lblShowUpdate.setTypeface(myCustomFont);
        radioYes.setTypeface(myCustomFont);
        radioNo.setTypeface(myCustomFont);

        byteArrayOutputStream = new ByteArrayOutputStream();
        lblSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        lblTreeName.setText(treeName);
        lblTreeLocation.setText(treeAddress);
        lblTreePlantDate.setText(plantDate);
        lblTreeUpdatedDate.setText(updatedDate);
        inMemoryOf.setText(relation);
        if(Integer.parseInt(noOfTrees)>1){
            NoOfTreesLayout.setVisibility(View.VISIBLE);
            NoOfTrees.setText(noOfTrees);
        }


//        if(updateStatus.equals("yes")){
//            lblShowUpdate.setVisibility(View.VISIBLE);
//        }


        lblShowUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbltreeStatusLayout.setVisibility(View.VISIBLE);
                lblButtonLayout.setVisibility(View.VISIBLE);


            }
        });

        treeStatusRadioFunction();

        radioYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateStatus.equals("yes")){
                    lblDateLayout.setVisibility(View.VISIBLE);
                    lblImageLayout.setVisibility(View.VISIBLE);
                    haveGuardLayout.setVisibility(View.VISIBLE);
                    treeGuardRadioFunction();
                }
            }
        });

        radioNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    lblDateLayout.setVisibility(View.GONE);
                    lblImageLayout.setVisibility(View.GONE);
                    haveGuardLayout.setVisibility(View.GONE);
                }
        });

        //radioValue();
//        if(treeStatusValue.equals("Yes"))
//        {
//
//        }


        lblSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //GetDateValue=date.getText().toString();
                UpdateTreeData();
            }
        });

        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                urls.add(userDetail.getString("tree_image_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTreeImages(){
        String type = "fetchimageurl";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,type);
        backgroundWorker.delegate = this;
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        backgroundWorker.execute(treeId);
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                });
    }

    public void treeStatusRadioFunction(){
        PlantStatusRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        PlantStatusRadioGroup.clearCheck();

        PlantStatusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    treeStatusValue=rb.getText().toString();
                }

            }
        });
    }
    public void treeGuardRadioFunction(){
        TreeGuardRadioGroup = (RadioGroup) findViewById(R.id.radioGroupGuard);
        TreeGuardRadioGroup.clearCheck();

        TreeGuardRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    guardValue=rb.getText().toString();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
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
                Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                FixBitmap = scaleDownAndRotatePic(filePath);
                lblTreeImage.setImageBitmap(FixBitmap);
                if(FixBitmap.getByteCount()<=10000000){
                    FixBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                }else if(FixBitmap.getByteCount()>10000000 && FixBitmap.getByteCount()<=50000000){
                    FixBitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                }
                else if(FixBitmap.getByteCount()>50000000 && FixBitmap.getByteCount()<=100000000){
                    FixBitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
                }
                else{
                    FixBitmap.compress(Bitmap.CompressFormat.JPEG, 6, byteArrayOutputStream);
                }

                byteArray = byteArrayOutputStream.toByteArray();
                lblTreeImage.setVisibility(View.VISIBLE);

//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PlantTreeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//
//                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
//            lblTreeImage.setImageBitmap(
//                    decodeSampledBitmapFromResource(getResources(), R.id.tree_image, 100, 100));
            lblTreeImage.setImageBitmap(FixBitmap);
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            lblTreeImage.setVisibility(View.VISIBLE);
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
            final int REQUIRED_WIDTH = 900;
            final int REQUIRED_HEIGHT = 700;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            Log.e("ExifInteface .........","width : "+Integer.toString(width_tmp)+" Height : "+Integer.toString(height_tmp));
            while (true) {
                if (width_tmp / 2 < REQUIRED_WIDTH
                        || height_tmp / 2 < REQUIRED_HEIGHT)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            Log.e("ExifInteface .........","scale : "+Integer.toString(scale));
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            //Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
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

    public void UpdateTreeData() {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {

                        if(updateStatus.equals("yes")){
                            String type="TreeDataUpdate";
                            int Unique_tree_Number=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            BackgroundWorker backgroundWorker=new BackgroundWorker(this,type);
                            imageName=treeName;
                            imageName=imageName.replace(" ","")+String.valueOf(Unique_tree_Number);
                            if(GetDateValue.isEmpty()){
                                getdate();
                            }
                            backgroundWorker.execute(treeId,GetDateValue,imageName,ConvertImage,treeStatusValue,guardValue);
                        }
                        else{
                            String type="TreeDataUpdateIfNotExpired";
                            BackgroundWorker backgroundWorker=new BackgroundWorker(this,type);
                            backgroundWorker.execute(treeId,treeStatusValue);
                        }


                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No Internet...Please check your internet connection", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }
                });
    }

    public void getdate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        GetDateValue = mYear+"/"+(mMonth+1)+"/"+mDay;
    }

    public void datePicker(){
        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(UpdateTreeDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                GetDateValue=year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(UpdateTreeDetails.this,urls));

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
        }, 3500, 3500);

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


}
