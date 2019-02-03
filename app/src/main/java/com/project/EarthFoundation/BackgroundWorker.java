package com.project.EarthFoundation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
    private String received_type="";
    Context context;
    boolean check = true;
    AlertDialog alertDialog;
    int RC ;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        received_type=type;
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
                String user_name = params[1];
                String password = params[2];
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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("register")) {
            try {
                String fname = params[1];
                String lname = params[2];
                String password = params[9];
                String mobile = params[8];
                String email = params[7];
                String address = params[3];
                String city = params[4];
                String aadhar = params[6];
                String pin = params[5];

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
                        + URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                        + URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"
                        + URLEncoder.encode("aadhar","UTF-8")+"="+URLEncoder.encode(aadhar,"UTF-8")+"&"
                        + URLEncoder.encode("pin","UTF-8")+"="+URLEncoder.encode(pin,"UTF-8");
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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("ImageUpload")){
            try{
                String ImageTag=params[1];
                String GetImageNameFromEditText=params[2];
                String ImageName=params[3];
                String ConvertImage=params[4];
                String email=params[5];
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
                String TreeName=params[1];
                String Latitude=params[2];
                String Longitude=params[3];
                String PlantTime=params[4];
                String PlantDate=params[5];
                String TreeAddress=params[6];
                String ImageData=params[7];
                String ImageName=params[8];
                String email=params[9];
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("TreeName", TreeName);
                HashMapParams.put("Latitude", Latitude);
                HashMapParams.put("Longitude", Longitude);
                HashMapParams.put("PlantTime", PlantTime);
                HashMapParams.put("PlantDate", PlantDate);
                HashMapParams.put("TreeAddress", TreeAddress);
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
                String email = params[1];
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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("ChangePassword")){
            try {
                String email = params[1];
                String password = params[2];

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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("GetTreeData")){
            try {
                String email = params[1];
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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("GetIntValues")) {
            try {
                String email = params[1];
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
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString().trim();
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
//        if(received_type.equals("TreeDataUpload")) {
//            final ProgressDialog progressDialog = new ProgressDialog(context,
//                    R.style.Theme_AppCompat_Light_Dialog);
//            progressDialog.setMessage("Authenticating...");
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
//        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        if(received_type.equals("login")){
            if(!result.isEmpty())
                delegate.processFinish(received_type,result);
        }
        else if(received_type.equals("register")){
            if(!result.isEmpty())
                delegate.processFinish(received_type,result);
        }
        else if(received_type.equals("ForgotPassword")){
            if(!result.isEmpty())
                delegate.processFinish(received_type,result);
        }
        else if(received_type.equals("GetTreeData")){
            if(!result.isEmpty())
                delegate.processFinish(received_type,result);
        }
        else if(received_type.equals("GetIntValues")){
            if(!result.isEmpty())
                delegate.processFinish(received_type,result);
        }else if(received_type.equals("TreeDataUpload")) {
        }
//        alertDialog.setMessage(result);
//        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
