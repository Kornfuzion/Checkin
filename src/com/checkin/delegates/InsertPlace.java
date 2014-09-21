package com.checkin.delegates;
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
import android.os.AsyncTask;
import android.util.Log;

import com.checkin.activities.GeofenceUtils;
import com.checkin.activities.MapActivity;
import com.checkin.activities.SimpleGeofence;
import com.checkin.utils.SharedObjects;
import com.google.android.gms.location.Geofence;

public class InsertPlace  extends AsyncTask<String,Void,String>{

	   private Context context;
	   private int byGetOrPost = 1; 
	   String longitude,latitude;
	   //flag 0 means get and 1 means post.(By default it is get.)
	   public InsertPlace(Context context,int flag) {
	      this.context = context;
	      byGetOrPost = flag;
	   }

	   protected void onPreExecute(){
	   super.onPreExecute();
	   }

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
	        	 String name= (String)arg0[0];

	        	 longitude= (String)arg0[1];

	        	 latitude= (String)arg0[2];
	        	 //this url determines which php code is executed
	        	 String link= SharedObjects.DB + "insertyourplaces.php";
	        	 //encoding data into the URL to be sent over by POST method
	        	 String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
	            
	        	 data  +="&"+ URLEncoder.encode("longitude", "UTF-8") 
	    	            + "=" + URLEncoder.encode(longitude, "UTF-8");
	        	 

	        	 data  +="&"+ URLEncoder.encode("latitude", "UTF-8") 
	    	            + "=" + URLEncoder.encode(latitude, "UTF-8");
	        	 

	        	 data  +="&"+ URLEncoder.encode("phone_number", "UTF-8") 
	    	            + "=" + URLEncoder.encode(SharedObjects.phoneNumber+"", "UTF-8");

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
	      MapActivity.mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;
	        
	        MapActivity.mUIGeofence1 = new SimpleGeofence(
	                "1",
	                // Get latitude, longitude, and radius from the UI
	                 Double.parseDouble(latitude),
	                 Double.parseDouble(longitude),
	                50,
	                // Set the expiration time
	                2147483646,
	                // Only detect entry transitions
	                Geofence.GEOFENCE_TRANSITION_ENTER |Geofence.GEOFENCE_TRANSITION_EXIT);
	        
	        
	        MapActivity.mCurrentGeofences.add(MapActivity.mUIGeofence1.toGeofence());
	        
	        try {
	            // Try to add geofences
	            MapActivity.mGeofenceRequester.addGeofences(MapActivity.mCurrentGeofences);
	        } catch (UnsupportedOperationException e) {
	            // Notify user that previous request hasn't finished.
	           
	        }

	         
	   }
	   
	   
	}