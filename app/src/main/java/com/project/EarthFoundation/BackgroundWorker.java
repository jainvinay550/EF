package com.project.EarthFoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ProgrammingKnowledge on 1/5/2016.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {

    public BackgroundWorkerResponce delegate=null;
    private String type,imageUrl;
    Context context;
    boolean check = true;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    int RC ;
    ProgressBar homeBar;
    UserSessionManager session;

    BackgroundWorker (Context ctx,String received_type) {
        context = ctx;
        type=received_type;
    }
    @Override
    protected String doInBackground(String... params) {

//        String type = params[0];
//        received_type=type;

        String login_url = "http://www.earthfoundation.in/EF/login.php";
        String signup_url = "http://www.earthfoundation.in/EF/register.php";
        String imageUpload_url="http://www.earthfoundation.in/EF/imageupload.php";
        String treeData_url="http://www.earthfoundation.in/EF/treedata.php";
        String forgotPassword_url="http://www.earthfoundation.in/EF/forgotpassword.php";
        String changePassword_url="http://www.earthfoundation.in/EF/changepassword.php";
        String gettreedata_url="http://www.earthfoundation.in/EF/gettreedata.php";
        String getIntValues_url="http://www.earthfoundation.in/EF/getintvalues.php";
        String treeUpdate_url="http://www.earthfoundation.in/EF/treeupdate.php";
        String fetchimage_url="http://www.earthfoundation.in/EF/fetchtreeimages.php";
        String fetchadmindata_url="http://www.earthfoundation.in/EF/fetchadmindata.php";
        if(type.equals("login")) {
            try {
                String user_name = params[0];
                String password = params[1];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result.toString().trim();
                }else{
                    return "NotConnected";

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("register")) {
            try {
                String fname = params[0];
                String lname = params[1];
                String password = params[3];
                String email = params[2];
                String title = params[4];

                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("fname","UTF-8")+"="+URLEncoder.encode(fname,"UTF-8")+"&"
                        + URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8") +"&"
                        + URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
                    // response code is OK
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";

                    while(( line= bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("updateProfile")) {
            try {
                String phoneNo = params[0];
                String country = params[1];
                String state = params[2];
                String city = params[3];
                String pincode = params[4];
                String aadhar = params[5];
                String email = params[6];

                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phoneNo","UTF-8")+"="+URLEncoder.encode(phoneNo,"UTF-8")+"&"
                        + URLEncoder.encode("country","UTF-8")+"="+URLEncoder.encode(country,"UTF-8")+"&"
                        + URLEncoder.encode("state","UTF-8")+"="+URLEncoder.encode(state,"UTF-8")+"&"
                        + URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"
                        + URLEncoder.encode("pincode","UTF-8")+"="+URLEncoder.encode(pincode,"UTF-8")+"&"
                        + URLEncoder.encode("aadhar","UTF-8")+"="+URLEncoder.encode(aadhar,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
                    // response code is OK
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";

                    while(( line= bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("ImageUpload")){
            try{
                String ImageTag=params[0];
                String GetImageNameFromEditText=params[1];
                String ImageName=params[2];
                String ConvertImage=params[3];
                String email=params[4];
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put(ImageTag, GetImageNameFromEditText);
                HashMapParams.put(ImageName, ConvertImage);
                HashMapParams.put("Email_Id", email);
                imageUrl=ImageName;
                URL url = new URL(imageUpload_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String,String> KEY : HashMapParams.entrySet()) {
                    if(check)
                        check = false;
                    else
                        stringBuilder.append("&");

                    stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
                bufferedWriter.write(stringBuilder.toString());

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                StringBuilder result = new StringBuilder();
                String line;
                RC = httpURLConnection.getResponseCode();
                if (RC == 202 || RC == 200 ||  RC == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("TreeDataUpload")){
            try{
                String TreeName=params[0];
                String Latitude=params[1];
                String Longitude=params[2];
                String InMemoryOf=params[3];
                String Relation=params[4];
                String PlantDate=params[5];
                String TreeAddress=params[6];
                String Landmark=params[7];
                String ImageData=params[8];
                String ImageName=params[9];
                String email=params[10];
                String plantationType=params[11];
                String treeCount=params[12];
                String guardValue=params[13];
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("TreeName", TreeName);
                HashMapParams.put("Latitude", Latitude);
                HashMapParams.put("Longitude", Longitude);
                HashMapParams.put("InMemoryOf", InMemoryOf);
                HashMapParams.put("Relation", Relation);
                HashMapParams.put("PlantDate", PlantDate);
                HashMapParams.put("TreeAddress", TreeAddress);
                HashMapParams.put("Landmark", Landmark);
                HashMapParams.put("ImageData", ImageData);
                HashMapParams.put("ImageName", ImageName);
                HashMapParams.put("email", email);
                HashMapParams.put("plantationType", plantationType);
                HashMapParams.put("treeCount", treeCount);
                HashMapParams.put("guardValue",guardValue);

                URL url = new URL(treeData_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String,String> KEY : HashMapParams.entrySet()) {
                    if(check)
                        check = false;
                    else
                        stringBuilder.append("&");

                    stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
                bufferedWriter.write(stringBuilder.toString());
                // response code is OK

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                StringBuilder result = new StringBuilder();
                String line;
                RC = httpURLConnection.getResponseCode();
                if (RC == 202 || RC == 200 ||  RC == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("ForgotPassword")){
            try {
                String email = params[0];
                URL url = new URL(forgotPassword_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("ChangePassword")){
            try {
                String email = params[0];
                String password = params[1];

                URL url = new URL(changePassword_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";

                    while(( line= bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    // response code is OK
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("GetTreeData")){
            try {
                String email = params[0];
                URL url = new URL(gettreedata_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("GetIntValues")) {

            try {
                String email = params[0];
                URL url = new URL(getIntValues_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("fetchimageurl")) {

            try {
                String tree_id = params[0];
                URL url = new URL(fetchimage_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tree_id","UTF-8")+"="+URLEncoder.encode(tree_id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("getProfile")){
            try {
                String email = params[0];
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("TreeDataUpdate")){
            try {
                String tree_id = params[0];
                String update_date = params[1];
                String image_name = params[2];
                String image_data = params[3];
                String tree_status = params[4];
                String tree_guard = params[5];

                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("type", type);
                HashMapParams.put("tree_id", tree_id);
                HashMapParams.put("update_date", update_date);
                HashMapParams.put("image_name", image_name);
                HashMapParams.put("image_data", image_data);
                HashMapParams.put("tree_status", tree_status);
                HashMapParams.put("tree_guard", tree_guard);

                URL url = new URL(treeUpdate_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String,String> KEY : HashMapParams.entrySet()) {
                    if(check)
                        check = false;
                    else
                        stringBuilder.append("&");

                    stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
                bufferedWriter.write(stringBuilder.toString());
                // response code is OK

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                StringBuilder result = new StringBuilder();
                String line;
                RC = httpURLConnection.getResponseCode();
                if (RC == 202 || RC == 200 ||RC == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("TreeDataUpdateIfNotExpired")){
            try {
                String tree_id = params[0];
                String tree_status = params[1];

                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("type", type);
                HashMapParams.put("tree_id", tree_id);
                HashMapParams.put("tree_status", tree_status);

                URL url = new URL(treeUpdate_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String,String> KEY : HashMapParams.entrySet()) {
                    if(check)
                        check = false;
                    else
                        stringBuilder.append("&");

                    stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
                bufferedWriter.write(stringBuilder.toString());
                // response code is OK

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                StringBuilder result = new StringBuilder();
                String line;
                RC = httpURLConnection.getResponseCode();
                if (RC == 202 || RC == 200 || RC == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("AdminData")){
            try {
                String GetFromDateValue = params[0];
                String GetToDateValue = params[1];
                URL url = new URL(fetchadmindata_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("GetFromDateValue","UTF-8")+"="+URLEncoder.encode(GetFromDateValue,"UTF-8")+"&"
                        + URLEncoder.encode("GetToDateValue","UTF-8")+"="+URLEncoder.encode(GetToDateValue,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine())!= null) {
                        result.append(line+"\n");
                    }
                    // response code is OK
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result.toString().trim();
                }else{
                    return "NotConnected";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Oops!");
        alertDialog.setMessage("Check your internet connection");
        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Login Status");
        progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_Light_Dialog);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.Theme_AppCompat_Light_Dialog);


        progressDialog.setIndeterminate(true);
        if(type.equals("GetIntValues")) {
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }
        else if(type.equals("GetTreeData")) {
//            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.setMessage("Loading..");
            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.show();
        }
        else if(type.equals("login")) {
//            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.setMessage("Authenticating...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }else if(type.equals("getProfile")) {
//            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        } else if(type.equals("TreeDataUpload")) {
            progressDialog.setMessage("Planting..");
            progressDialog.show();
        } else if(type.equals("TreeDataUpdate")) {
            progressDialog.setMessage("Updating..");
            progressDialog.show();
        } else if(type.equals("TreeDataUpdateIfNotExpired")) {
            progressDialog.setMessage("Updating..");
            progressDialog.show();
        } else if(type.equals("ChangePassword")) {
            progressDialog.setMessage("Changing password");
            progressDialog.show();
        }
        else if(type.equals("updateProfile")) {
            progressDialog.setMessage("Updating..");
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        if(type.equals("login")) {
            if (result.equals("NotConnected")) {
                alertDialog.show();
            } else if (!result.isEmpty()){
                try {
                    EditText _emailText = ((Activity)context).findViewById(R.id.input_email);
                    EditText _passwordText = ((Activity)context).findViewById(R.id.input_password);
                    Button _loginButton=((Activity)context).findViewById(R.id.btn_login) ;
                    final String email = _emailText.getText().toString();
                    final String password = _passwordText.getText().toString();
                    session = new UserSessionManager(context);

                    result=result.replace("[","");
                    result=result.replace("]","");
                    // Toast.makeText(getBaseContext(),output, Toast.LENGTH_LONG).show();
                    JSONObject reader = new JSONObject(result);
                    String received_email=reader.getString("email");
                    String received_fname=reader.getString("fname");
                    String received_lname=reader.getString("lname");
                    String received_password=reader.getString("password");
                    String received_image_url=reader.getString("image_url");
                    String received_user_type=reader.getString("user_type");

                    if(email.equals(received_email) && password.equals(received_password)){
                        _loginButton.setEnabled(true);

                        session.createUserLoginSession(received_fname+" "+received_lname,email,received_image_url,received_user_type,received_password);

                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra("password",received_password);
                        ((Activity)context).startActivity(intent);
                        ((Activity)context).finish();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        builder.setTitle("Login Failed");
                        builder.setMessage("Incorrect email or password. Please enter correct credentials");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        _loginButton.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            progressDialog.dismiss();
            //delegate.processFinish(result);
        }
        else if(type.equals("register")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
        }
        else if(type.equals("ForgotPassword")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
        }
        else if(type.equals("GetTreeData")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
            progressDialog.dismiss();
        }else if(type.equals("fetchimageurl")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
        }
        else if(type.equals("GetIntValues")){
            if(result.equals("NotConnected")){
                progressDialog.dismiss();
                alertDialog.show();
            }else if(!result.isEmpty())
                try {
                    result = result.replace("[", "");
                    result = result.replace("]", "");
                    // Toast.makeText(getBaseContext(),output,   Toast.LENGTH_LONG).show();
                    JSONObject reader = new JSONObject(result);
                    String treesPlanted = reader.getString("treesPlanted");
                    String contribution = reader.getString("contribution");
                    String treelived = reader.getString("treelived");
                    String tokensEarned = Integer.toString(Integer.valueOf(contribution) * 5);
                    TextView lblTreesPlanted = (TextView) ((Activity)context).findViewById(R.id.trees_planted);
                    lblTreesPlanted.setText(treesPlanted);
                    TextView lblContribution = (TextView) ((Activity)context).findViewById(R.id.plantation_contribution);
                    lblContribution.setText(contribution);
                    TextView lbltreeslived = (TextView) ((Activity)context).findViewById(R.id.trees_lived);
                    lbltreeslived.setText(treelived);
                    TextView lblTokensEarned = (TextView) ((Activity)context).findViewById(R.id.tokens_earned);
                    lblTokensEarned.setText(tokensEarned);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }else if(type.equals("getProfile")) {
            if(result.equals("NotConnected")){
                progressDialog.dismiss();
                alertDialog.show();
            }else if(!result.isEmpty())
                try {
                    result = result.replace("[", "");
                    result = result.replace("]", "");
                    // Toast.makeText(getBaseContext(),output,   Toast.LENGTH_LONG).show();
                    JSONObject reader = new JSONObject(result);
                    String cntno = reader.getString("cntno");
                    String country = reader.getString("country");
                    String state = reader.getString("state");
                    String city = reader.getString("city");
                    String aadharno = reader.getString("aadharno");
                    String pincode = reader.getString("pincode");

                    EditText lblcntno = (EditText) ((Activity)context).findViewById(R.id.phoneNo);
                    lblcntno.setText(cntno);
                    EditText lblcountry = (EditText) ((Activity)context).findViewById(R.id.country);
                    lblcountry.setText(country);
                    EditText lblstate = (EditText) ((Activity)context).findViewById(R.id.state);
                    lblstate.setText(state);
                    EditText lblcity = (EditText) ((Activity)context).findViewById(R.id.city);
                    lblcity.setText(city);
                    EditText lblaadharno = (EditText) ((Activity)context).findViewById(R.id.aadhar);
                    lblaadharno.setText(aadharno);
                    EditText lblpincode = (EditText) ((Activity)context).findViewById(R.id.pincode);
                    lblpincode.setText(pincode);
                    if(cntno.equals("0")){
                        lblcntno.setText("");
                    }
                    if(aadharno.equals("null")){
                        lblaadharno.setText("");
                    }
                    if(pincode.equals("0")){
                        lblpincode.setText("");
                    }
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }else if(type.equals("ImageUpload")){
            if(!result.isEmpty()){
                Toast.makeText(context,
                        "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        }else if(type.equals("TreeDataUpload")){
            if(!result.isEmpty())
                progressDialog.dismiss();
            Toast.makeText(context,
                    "Tree Planted Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

        } else if(type.equals("TreeDataUpdate")){
            if(!result.isEmpty())
                progressDialog.dismiss();
            Toast.makeText(context,
                    "Tree Updated Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

        } else if(type.equals("TreeDataUpdateIfNotExpired")){
            if(!result.isEmpty())
                progressDialog.dismiss();
            Toast.makeText(context,
                    "Tree Updated Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();

        } else if(type.equals("ChangePassword")) {
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                progressDialog.dismiss();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
        else if(type.equals("updateProfile")) {
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                progressDialog.dismiss();
            Toast.makeText(context,
                    "Profile Updated Successfully",
                    Toast.LENGTH_SHORT).show();
        }else if(type.equals("AdminData")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
        }

//        alertDialog.setMessage(result);
//        alertDialog.show();
    }

    public void loadImage(String url, ProgressBar imageBar, ImageView ImgView){
        Glide.with(context)
                .load(url)
                //.placeholder()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))

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
                .into(ImgView);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
