package com.project.EarthFoundation;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlantTreeActivity extends AppCompatActivity implements LocationListener {

    UserSessionManager session;
    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    TextView tvLatitude, tvLongitude, tvTime;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    EditText date;
    DatePickerDialog datePickerDialog;

    //insertion fields
    ImageView lblTreeImage;
    EditText lblTreeName,lblTreeAddress,lblInMemoryOf,lblRelation,lblLandmark;
    Button lblSelectImage,lblSubmit,lblCancel;
    Bitmap FixBitmap;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetInMemoryOfValue,GetRelationValue,GetDateValue,GetAddressValue,GetLandmarkValue;
    String email;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_tree);

        session = new UserSessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // get email
        email = user.get(UserSessionManager.KEY_EMAIL);

        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        lblInMemoryOf = (EditText) findViewById(R.id.InMemory);
        lblRelation = (EditText) findViewById(R.id.relation);
        lblTreeImage=(ImageView) findViewById(R.id.tree_image);
        lblTreeName=(EditText) findViewById(R.id.tree_name);
        lblTreeAddress=(EditText) findViewById(R.id.tree_address);
        lblLandmark=(EditText) findViewById(R.id.tree_landmark);
        lblSubmit=(Button) findViewById(R.id.btnSubmit);
        lblSelectImage=(Button) findViewById(R.id.select_image);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();

            byteArrayOutputStream = new ByteArrayOutputStream();
            lblSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPictureDialog();
                }
            });

            lblSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetTreeNameFromEditText=lblTreeName.getText().toString();
                    GetLatitudeValue=tvLatitude.getText().toString();
                    GetLongitudeValue=tvLongitude.getText().toString();
                    GetInMemoryOfValue=lblInMemoryOf.getText().toString();
                    GetRelationValue=lblRelation.getText().toString();
                    GetDateValue=date.getText().toString();
                    GetAddressValue=lblTreeAddress.getText().toString();
                    GetLandmarkValue=lblLandmark.getText().toString();
                    UploadTreeData();
                }
            });
        }
        datePicker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new OnClickListener() {
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
                    Toast.makeText(PlantTreeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    lblTreeImage.setImageBitmap(FixBitmap);
                    lblTreeImage.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PlantTreeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
//            lblTreeImage.setImageBitmap(
//                    decodeSampledBitmapFromResource(getResources(), R.id.tree_image, 100, 100));
            lblTreeImage.setImageBitmap(FixBitmap);
            lblTreeImage.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public void UploadTreeData() {

        if (!validate()) {
            onSubmitFailed();
            return;
        }

//        final ProgressDialog progressDialog = new ProgressDialog(PlantTreeActivity.this,
//                R.style.Theme_AppCompat_Light_Dialog);
//        progressDialog.setMessage("Planting");
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();


        String type="TreeDataUpload";
        int Unique_tree_Number=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if(FixBitmap.getByteCount()<=10000000){
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        }else if(FixBitmap.getByteCount()>10000000 && FixBitmap.getByteCount()<=50000000){
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 9, byteArrayOutputStream);
        }
        else if(FixBitmap.getByteCount()>50000000 && FixBitmap.getByteCount()<=100000000){
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 6, byteArrayOutputStream);
        }
        else{
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 3, byteArrayOutputStream);
        }

        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        BackgroundWorker backgroundWorker=new BackgroundWorker(this,type);
        String imageName=GetTreeNameFromEditText;
        imageName=imageName.replace(" ","")+String.valueOf(Unique_tree_Number);;
        backgroundWorker.execute(GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetInMemoryOfValue,GetRelationValue,GetDateValue,GetAddressValue,GetLandmarkValue,ConvertImage,imageName,email);
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                        progressDialog.dismiss();
//////                        Toast.makeText(getApplicationContext(),
//////                    "Tree Planted",
////                    Toast.LENGTH_LONG).show();
//
//                    }
//                }, 2000);
    }

    public void datePicker(){
        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);

        // perform click event on edit text
        date.setInputType(InputType.TYPE_NULL);
                date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(PlantTreeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText( dayOfMonth+ "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {

                Toast.makeText(PlantTreeActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            Object application;
//                            showMessageOKCancel("These permissions are mandatory for the
//                                    application. Please allow access.",
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(
                                                new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            };
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, OnClickListener okListener) {
        new AlertDialog.Builder(PlantTreeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        tvLatitude.setText(Double.toString(loc.getLatitude()));
        tvLongitude.setText(Double.toString(loc.getLongitude()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Unable to submit, Please check all the fields", Toast.LENGTH_LONG).show();

//        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String GetTreeNameFromEditText = lblTreeName.getText().toString();
        String GetAddressValue = lblTreeAddress.getText().toString();
        //String GetDateValue = date.getText().toString();

        if (GetTreeNameFromEditText.isEmpty()) {
            lblTreeName.setError("Please enter a valid tree name");
            valid = false;
        } else if (containsDigit(GetTreeNameFromEditText)) {
            lblTreeName.setError("Tree name cannot be a number");
            valid = false;
        } else {
            lblTreeName.setError(null);
        }
        if (GetAddressValue.isEmpty()) {
            lblTreeAddress.setError("Enter valid address");
            valid = false;
        } else {
            lblTreeAddress.setError(null);
        }
        if(GetLandmarkValue.isEmpty()) {
            lblLandmark.setError("Enter valid landmark");
            valid = false;
        } else {
            lblLandmark.setError(null);
        }
        if (containsDigit(GetInMemoryOfValue)) {
            lblInMemoryOf.setError("In memory of cannot be a number");
            valid = false;
        } else {
            lblInMemoryOf.setError(null);
        }
        if (containsDigit(GetRelationValue)) {
            lblRelation.setError("Relation cannot be a number");
            valid = false;
        } else {
            lblRelation.setError(null);
        }

//        if(GetDateValue.isEmpty()){
//            date.setError("Select valid date");
//            valid = false;
//        } else {
//            date.setError(null);
//        }
        if(lblTreeImage.getDrawable()== null){
            Toast.makeText(getBaseContext(), "Please select an image", Toast.LENGTH_LONG).show();
            valid = false;
        }else{
            lblTreeImage.setImageDrawable(null);
        }

        return valid;
    }
    public final boolean containsDigit(String s){
        boolean containsDigit = false;
        if(s != null && !s.isEmpty()){
            for (char c : s.toCharArray()){
                if (containsDigit = Character.isDigit(c)){
                    break;
                }
            }
        }
        return containsDigit;
    }

//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight
//                    && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }

}
