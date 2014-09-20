package com.checkin.delegates;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;

import com.checkin.utils.Place;
import com.checkin.utils.SharedObjects;
import com.checkin.adapters.PlaceAdapter;

public class GetPlaces extends AsyncTask<String,Void,Vector<Place>>{
	LocalBroadcastManager mLocalBroadcastManager;
   private ListView lv;
   private Context context;

   public GetPlaces(Context context, ListView listview) {
      this.context = context;
      //list view instead of statusField
      this.lv = listview;
   }

   @Override
   protected void onPreExecute(){
	   super.onPreExecute(); 
   }
   
   @Override
   protected Vector<Place> doInBackground(String... arg0) {
	   Log.d("g","getdata doinbackground");
	   try{
		   String phone_number = (String)arg0[0];
		   String data  = URLEncoder.encode("phone_number", "UTF-8") 	 
                    + "=" + URLEncoder.encode(phone_number, "UTF-8");
		   //this URL determines which php code is executed at the server
		   String link = SharedObjects.DB + "getyourplaces.php";
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
		   
		   String result=sb.toString();
		   String delims1="::";
		   String delims2="==";

		   Log.d("result","result is: "+result);
		   String [] entries=result.split(delims1);
		   Log.d("entries length","entries length is "+entries.length);
     	   
		   Vector<Place> places = new Vector<Place>();
		   for(int i=0;i<entries.length;i++){
			   Log.d("entries","entries is "+entries[i]);
			   String [] columns=entries[i].split(delims2);
			   Place tmp = Place.createPlace(columns);
			   if (tmp != null){
				   places.add(tmp);
			   }
		   }
		   
		   return places;
         }catch(Exception e){
        	 Log.e(SharedObjects.TAG, e.toString());
        	 Log.d(SharedObjects.TAG, e.toString());
        	 return new Vector<Place>();
         }
   }
   
   @Override
   protected void onPostExecute(Vector<Place> list){
	   super.onPostExecute(list); 
	   PlaceAdapter pa = new PlaceAdapter(this.context, list);
	   lv.setAdapter(pa);
	   SharedObjects.places = list;
   }
  
   
}
