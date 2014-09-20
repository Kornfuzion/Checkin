package com.example.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class InsertUsers  extends AsyncTask<String,Void,String>{

	   private TextView statusField,roleField;
	   private Context context;
	   private int byGetOrPost = 1; 
	   String longitude,latitude;
	   //flag 0 means get and 1 means post.(By default it is get.)
	   public InsertUsers(Context context,int flag) {
	      this.context = context;
	      this.statusField = statusField;
	      this.roleField = roleField;
	      byGetOrPost = flag;
	   }

	   protected void onPreExecute(){
		   //TODO
	   }
	   
	   @Override
	   protected String doInBackground(String... arg0) {
	      if(byGetOrPost == 0){ //means by Get Method
	         try{
	        	 String username = (String)arg0[0];
	        	 String link = "http://ec2-54-84-102-132.compute-1.amazonaws.com/insertusers.php?user="+username;
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
	      }else{
	         try{
	        	 String name = (String)arg0[0];
	        	 String phone_number= (String)arg0[1];
	        	 //this url determines which php code is executed
	        	 String link="http://ec2-54-84-102-132.compute-1.amazonaws.com/insertusers.php";
	        	 //encoding data into the URL to be sent over by POST method
	        	 String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
	            
	        	 data  +="&"+ URLEncoder.encode("phone_number", "UTF-8") 
	    	            + "=" + URLEncoder.encode(phone_number, "UTF-8");
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
	        	 while((line = reader.readLine()) != null){
	        		 sb.append(line);
	        		 break;
        		 }
	        	 return sb.toString();
        	 }catch(Exception e){
        		 return new String("Exception: " + e.getMessage());
	         }
	      }
	   }
	   @Override
	   protected void onPostExecute(String result){
	      Log.d("got result","RESULT IS "+result);
	   }
	}