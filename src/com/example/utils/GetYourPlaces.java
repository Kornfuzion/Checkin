package com.example.utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

public class GetYourPlaces  extends AsyncTask<String,Void,String>{
	LocalBroadcastManager mLocalBroadcastManager;
   private TextView statusField,roleField;
   private Context context;
   private int byGetOrPost = 1; 
   private boolean broadcast;
   private boolean refreshed;
   SharedPreferences preferences;
   Editor editor;
   //flag 0 means get and 1 means post.(By default it is get.)
   public GetYourPlaces(Context context,int flag) {
      this.context = context;
      this.statusField = statusField;
      this.roleField = roleField;
      byGetOrPost = flag;
   }

   protected void onPreExecute(){
	   super.onPreExecute(); 
   
   }
   @Override
   protected String doInBackground(String... arg0) {
	   Log.d("g","getdata doinbackground");
      if(byGetOrPost == 0){ //means by Get Method
         try{
			String username = (String)arg0[0];
			String link = "http://ec2-54-84-102-132.compute-1.amazonaws.com/retrievesong.php?user="
			    +username;
		    URL url = new URL(link);
		    HttpClient client = new DefaultHttpClient();
		    HttpGet request = new HttpGet();
		    request.setURI(new URI(link));
		    HttpResponse response = client.execute(request);
		    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
		    StringBuffer sb = new StringBuffer("");
		    String line="";
		    while ((line = in.readLine()) != null) {
		    	sb.append(line);
				break;
			}
		    in.close();
			    
		    return sb.toString();
            
         }catch(Exception e){
        	 return new String("Exception: " + e.getMessage());
    	 }
      }
      else{
         try{
        	 String phone_number = (String)arg0[0];
        	 String data  = URLEncoder.encode("phone_number", "UTF-8") 	 
                    + "=" + URLEncoder.encode(phone_number, "UTF-8");
        	 //this URL determines which php code is executed at the server
        	 String link="http://ec2-54-84-102-132.compute-1.amazonaws.com/getyourplaces.php";
        	 URL url = new URL(link);
        	 URLConnection conn = url.openConnection();
        	 conn.setDoOutput(true); 
        	 OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
        	 wr.write( data ); 
        	 wr.flush(); 
        	 BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        	 StringBuilder sb = new StringBuilder();
        	 String line = null;
        	 // Read Server Response
        	 while((line = reader.readLine()) != null)
        	 {
        		 sb.append(line);
        		 break;
    		 }
        	 String result=sb.toString();
        	 String delims1="::";
        	 String delims2="==";

        	 Log.d("result","result is: "+result);
        	 String [] entries=result.split(delims1);
        	 Log.d("entries length","entries length is "+entries.length);
     	   
        	 for(int i=0;i<entries.length-1;i++){
        		 Log.d("entries","entries is "+entries[i]);
        		 String [] columns=entries[i].split(delims2);
    		 }
         }catch(Exception e){
        	 return new String("Exception: " + e.getMessage());
         }
      }
	return null;
   }
   @Override
   protected void onPostExecute(String result){
	   //TODO 
	 
		
   }
  
   
}