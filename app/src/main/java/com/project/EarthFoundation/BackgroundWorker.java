package com.project.EarthFoundation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

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
    private String type;
    Context context;
    boolean check = true;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    int RC ;
    ProgressBar homeBar;

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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                StringBuilder result = new StringBuilder();
                while((line = bufferedReader.readLine())!= null) {
                    result.append(line+"\n");
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
        }else if(type.equals("register")) {
            try {
                String fname = params[0];
                String lname = params[1];
                String password = params[3];
                String email = params[2];

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
                        + URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";

                while(( line= bufferedReader.readLine())!= null) {
                    result += line;
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";

                while(( line= bufferedReader.readLine())!= null) {
                    result += line;
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                if (RC == HttpURLConnection.HTTP_OK) {
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
                if (RC == HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                StringBuilder result = new StringBuilder();
                while((line = bufferedReader.readLine())!= null) {
                    result.append(line+"\n");
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";

                while(( line= bufferedReader.readLine())!= null) {
                    result += line;
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                StringBuilder result = new StringBuilder();
                while((line = bufferedReader.readLine())!= null) {
                    result.append(line+"\n");
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                StringBuilder result = new StringBuilder();
                while((line = bufferedReader.readLine())!= null) {
                    result.append(line+"\n");
                }
                int responseCode = httpURLConnection.getResponseCode();

                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                StringBuilder result = new StringBuilder();
                while((line = bufferedReader.readLine())!= null) {
                    result.append(line+"\n");
                }
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 202 || responseCode == 200 || responseCode ==HttpURLConnection.HTTP_OK) {
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
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Login Status");
        progressDialog = new ProgressDialog(context,
                R.style.Theme_AppCompat_Light_Dialog_Alert);

        progressDialog.setIndeterminate(true);
        if(type.equals("GetIntValues")) {
            progressDialog.setMessage("Loading");
            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.show();
        }else if(type.equals("getProfile")) {
//            progressDialog.setTitle("Please wait till we get things ready for you");
            progressDialog.setMessage("Loading");
            progressDialog.show();
        } else if(type.equals("TreeDataUpload")) {
            progressDialog.setMessage("Planting..");
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        if(type.equals("login")){
            if(result.equals("NotConnected")){
                alertDialog.show();
            }else if(!result.isEmpty())
                delegate.processFinish(result);
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
                    String tokensEarned = Integer.toString(Integer.valueOf(contribution) * 5);
                    TextView lblTreesPlanted = (TextView) ((Activity)context).findViewById(R.id.trees_planted);
                    lblTreesPlanted.setText(treesPlanted);
                    TextView lblContribution = (TextView) ((Activity)context).findViewById(R.id.plantation_contribution);
                    lblContribution.setText(contribution);
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
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }else if(type.equals("ImageUpload")){
            if(!result.isEmpty())
                Toast.makeText(context,
                           "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
        }else if(type.equals("TreeDataUpload")){
            if(!result.isEmpty())
                progressDialog.dismiss();
                Toast.makeText(context,
                        "Tree Planted",
                        Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);

        }
//        alertDialog.setMessage(result);
//        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
