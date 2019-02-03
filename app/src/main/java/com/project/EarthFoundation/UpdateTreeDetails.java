package com.project.EarthFoundation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.DatePickerDialog;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class UpdateTreeDetails extends AppCompatActivity {

    String treeName;
    String treeAddress;
    String plantDate;
    String updatedDate;
    String updateStatus;
    String treeImage;

    EditText date;
    DatePickerDialog datePickerDialog;

    static int tree_counter=0;

    ImageView lblTreeImage;
    TextView lblTreeName,lblTreeLocation,lblTreePlantDate,lblTreeUpdatedDate;
    Button lblSelectImage,lblSubmit,lblShowUpdate;
    LinearLayout lblDateLayout,lblImageLayout,lblButtonLayout;
    Bitmap FixBitmap;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetTimeValue,GetDateValue,GetAddressValue;
    String email;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private String[] urls = new String[] {"https://demonuts.com/Demonuts/SampleImages/W-03.JPG"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tree_details);



        Intent intent= getIntent();
        treeName = intent.getStringExtra("treeName");
        treeAddress = intent.getStringExtra("treeAddress");
        plantDate = intent.getStringExtra("plantDate");
        updatedDate = intent.getStringExtra("updatedDate");
        updateStatus = intent.getStringExtra("updateStatus");
        treeImage = intent.getStringExtra("treeImage");

        init();
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

        if(updateStatus.equals("yes")){
            lblShowUpdate.setVisibility(View.VISIBLE);
        }

        lblShowUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lblDateLayout.setVisibility(View.VISIBLE);
                lblImageLayout.setVisibility(View.VISIBLE);
                lblButtonLayout.setVisibility(View.VISIBLE);
            }
        });

        lblSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetDateValue=date.getText().toString();
                UploadTreeData();
            }
        });

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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    Toast.makeText(UpdateTreeDetails.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    lblTreeImage.setImageBitmap(FixBitmap);
                    lblTreeImage.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateTreeDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            lblTreeImage.setImageBitmap(FixBitmap);
            lblTreeImage.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public void UploadTreeData() {
        String type="TreeDataUpload";
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        String imageName=GetTreeNameFromEditText+String.valueOf(tree_counter);
        tree_counter++;
        backgroundWorker.execute(type,GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetTimeValue,GetDateValue,GetAddressValue,ConvertImage,imageName,email);
    }

    public void datePicker(){
        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);
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

        NUM_PAGES = urls.length;

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


}
