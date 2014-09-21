package com.checkin.delegates;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.checkin.utils.Place;
import com.checkin.utils.SharedObjects;

public class InsertPlace  extends AsyncTask<String,Void,String>{
	   private Context context;
	   private Place place;
	   
	   public InsertPlace(Context context, Place place) {
	      this.context = context;
	      this.place = place;
	   }

	   @Override
	   protected void onPreExecute(){
		   super.onPreExecute();
		   //TODO
	   }
	   
	   @Override
	   protected String doInBackground(String...arg0) {
	         try{
	        	 String phone_number= (String)arg0[1];
	        	 
	        	 //encoding data into the URL to be sent over by POST method
	        	 String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(place.getName(), "UTF-8");
	            
	        	 data  +="&"+ URLEncoder.encode("phone_number", "UTF-8") 
	    	            + "=" + URLEncoder.encode(phone_number, "UTF-8");
	        	 
	        	 String longStr = String.valueOf(place.getLocation().longitude);
	        	 String latStr = String.valueOf(place.getLocation().latitude);
	        	 String radiusStr = String.valueOf(place.getRadius());
	        	 String iconNumStr = String.valueOf(place.getIconNumber());
	        	 
	        	 data  +="&"+ URLEncoder.encode("longitude", "UTF-8") 
		    	            + "=" + URLEncoder.encode(longStr, "UTF-8");
	        	 data  +="&"+ URLEncoder.encode("latitude", "UTF-8") 
		    	            + "=" + URLEncoder.encode(latStr, "UTF-8");
	        	 data  +="&"+ URLEncoder.encode("radius", "UTF-8") 
		    	            + "=" + URLEncoder.encode(radiusStr, "UTF-8");
	        	 data  +="&"+ URLEncoder.encode("icon_number", "UTF-8") 
		    	            + "=" + URLEncoder.encode(iconNumStr, "UTF-8");
	        	 
	        	 //this url determines which php code is executed
	        	 String link= SharedObjects.DB + "insertPlace.php";
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
        		 Log.d(SharedObjects.TAG, e.toString());
        		 return null;
	         }
	   }
	   
	   
	   @Override
	   protected void onPostExecute(String result){
		   super.onPostExecute(result);
		   Log.d("got result","Result is: "+result);
		   if (result != null){
			   Toast.makeText(context,"Saved Your Place Successfully!", Toast.LENGTH_SHORT).show();
		   }else{
			   Toast.makeText(context,"Save Was Not Successful!", Toast.LENGTH_SHORT).show();
		   }
		   
	   }
	}