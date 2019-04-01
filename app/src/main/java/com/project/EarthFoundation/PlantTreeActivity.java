package com.project.EarthFoundation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Criteria;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Typeface;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class PlantTreeActivity extends Fragment implements LocationListener {

    private static final String ARG_COLOR = "color";
    private int color;

    UserSessionManager session;
    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    TextView tvLatitude, tvLongitude, tvTime,type_countName;
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
    EditText lblTreeName,lblTreeAddress,lblInMemoryOf,lblRelation,lblLandmark,type_count;
    Button lblSelectImage,lblSubmit,lblCancel;
    Bitmap FixBitmap;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetInMemoryOfValue,GetRelationValue,GetDateValue,GetAddressValue,GetLandmarkValue,GetCount;
    String email,db_date="";
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;
    String noTrees = "1",plantTypeDefaultValue="Individual";
    String selectedItemText,selectedItemTextValue,guardValue;
    RadioGroup radioGroup;
    CoordinatorLayout layoutPlant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_plant_tree, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TNR.ttf");

        session = new UserSessionManager(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // get email
        email = user.get(UserSessionManager.KEY_EMAIL);

        date = (EditText) rootView.findViewById(R.id.date);
        tvLatitude = (TextView) rootView.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) rootView.findViewById(R.id.tvLongitude);
        lblInMemoryOf = (EditText) rootView.findViewById(R.id.InMemory);
        lblRelation = (EditText) rootView.findViewById(R.id.relation);
        lblTreeImage=(ImageView) rootView.findViewById(R.id.tree_image);
        lblTreeName=(EditText) rootView.findViewById(R.id.tree_name);
        lblTreeAddress=(EditText) rootView.findViewById(R.id.tree_address);
        lblLandmark=(EditText) rootView.findViewById(R.id.tree_landmark);
        lblSubmit=(Button) rootView.findViewById(R.id.btnSubmit);
        lblSelectImage=(Button) rootView.findViewById(R.id.select_image);
        type_countName = (TextView) rootView.findViewById(R.id.type_countName);
        type_count = (EditText) rootView.findViewById(R.id.type_count);
        layoutPlant=rootView.findViewById(R.id.plantTreeLayout);

        lblTreeName.setTypeface(font);
        lblTreeAddress.setTypeface(font);
        lblInMemoryOf.setTypeface(font);
        lblRelation.setTypeface(font);
        lblLandmark.setTypeface(font);
        type_count.setTypeface(font);
        lblSelectImage.setTypeface(font);
        lblSubmit.setTypeface(font);
        date.setTypeface(font);

        layoutPlant.setBackgroundColor(getLighterColor(color));

        locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
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
                    if(selectedItemText.equals("SELECT TYPE")){
                        selectedItemTextValue=plantTypeDefaultValue;
                    }
                    selectedItemTextValue=selectedItemText;
                    //lblTreeImage.setImageBitmap(FixBitmap);
                    GetCount = type_count.getText().toString();
                    if (!GetCount.isEmpty()) {
                        noTrees = GetCount;
                    }
                    UploadTreeData();
                }

            });
        }
        datePicker();


        String[] plantType = new String[]{
                "SELECT TYPE",
                "Individual",
                "Group"
        };

        final List<String> genderList = new ArrayList<>(Arrays.asList(plantType));
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.plantation_type);
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, genderList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    //tv.setText(gender[position]);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);


                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0 && position <= 1) {
                    // Notify the selected item text
                    type_countName.setVisibility(View.GONE);
                    type_count.setVisibility(View.GONE);
                    //  selectedItemTextValue=plantTypeDefaultValue;
//                    Toast.makeText
//                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                            .show();
                } else if (position > 1) {
                    type_countName.setVisibility(View.VISIBLE);
                    type_count.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    guardValue=rb.getText().toString();
                }

            }
        });



        return rootView;
    }


    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }



    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                //try {
                //FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
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



        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    if(isConnectedToInternet.equals(true)) {
                        String type="TreeDataUpload";
                        int Unique_tree_Number=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        BackgroundWorker backgroundWorker=new BackgroundWorker(getContext(),type);
                        String imageName=GetTreeNameFromEditText;
                        imageName=imageName.replace(" ","")+String.valueOf(Unique_tree_Number);
                        if(db_date.isEmpty()){
                            getdate();
                        }
                        backgroundWorker.execute(GetTreeNameFromEditText,GetLatitudeValue,GetLongitudeValue,GetInMemoryOfValue,GetRelationValue,db_date,GetAddressValue,GetLandmarkValue,ConvertImage,imageName,email,selectedItemTextValue,noTrees,guardValue);
                    }
                    else{
                        buildDialog1(getContext()).show();
                    }
                });
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
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText( dayOfMonth+ "/"
                                        + (monthOfYear + 1) + "/" + year);
                                db_date=year+"/"+(monthOfYear+1)+"/"+dayOfMonth;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
    public void getdate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        db_date = mYear+"/"+(mMonth+1)+"/"+mDay;
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
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
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

                Toast.makeText(getContext(), "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
        new AlertDialog.Builder(getContext())
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
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void onSubmitFailed() {
        Toast.makeText(getContext(), "Unable to submit, Please check all the fields", Toast.LENGTH_LONG).show();

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
        if(GetInMemoryOfValue.isEmpty()) {
            lblInMemoryOf.setError("Enter valid value");
            valid = false;
        }
        else if (containsDigit(GetInMemoryOfValue)) {
            lblInMemoryOf.setError("In memory of cannot be a number");
            valid = false;
        } else {
            lblInMemoryOf.setError(null);
        }
        if(GetRelationValue.isEmpty()) {
            lblRelation.setError("Enter valid relation");
            valid = false;
        }
        else if (containsDigit(GetRelationValue)) {
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
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_LONG).show();
            valid = false;
        }else{
            lblTreeImage.setImageDrawable(null);
        }
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            Toast.makeText(getContext(), "Please select a value for Tree Guard", Toast.LENGTH_LONG).show();
            valid = false;

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
public android.app.AlertDialog.Builder buildDialog(Context c) {

    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
    builder.setTitle("No Internet Connection");
    builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            getActivity().finish();
        }
    });

    return builder;
}
    public android.app.AlertDialog.Builder buildDialog1(Context c) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setTitle("Oops!");
        builder.setMessage("No internet. Check your connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        return builder;
    }

    private int getLighterColor(int color) {
        return Color.argb(30,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }


}
// output=output.replace("[","");
//         output=output.replace("]","");
//         // JSONObject userDetail = userArray.getJSONObject(i);
//         // fetch email and name and store it in arraylist
//         JSONObject reader = new JSONObject(output);
//         urls.add(reader.getString("tree_image_url"));