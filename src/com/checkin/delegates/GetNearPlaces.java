package com.checkin.delegates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.checkin.activities.MainActivity;
import com.checkin.activities.MapActivity;
import com.checkin.adapters.PlaceAdapter;
import com.checkin.utils.Place;
import com.checkin.utils.SharedObjects;
import com.checkin.R;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetNearPlaces extends AsyncTask<String,Void,Vector<Place>>{
	LocalBroadcastManager mLocalBroadcastManager;
	private ListView lv;
	private ProgressBar p;
	private ImageView refreshicon;
	private Context context;

   public GetNearPlaces(Context context, ListView listview, ProgressBar pb, ImageView imgView) {
      this.context = context;
      this.lv = listview;
      this.p = pb;
      this.refreshicon = imgView;
   }

   @Override
   protected void onPreExecute(){
	   super.onPreExecute(); 
   }
   
   @Override
   protected Vector<Place> doInBackground(String... arg0) {
	   Log.d(SharedObjects.TAG,"Getting Place Data");
	   try{
		   String NElat = (String)arg0[0];
           String NElon = (String)arg0[1];
           String SWlat = (String)arg0[2];
           String SWlon = (String)arg0[3];

           String data  = URLEncoder.encode("user", "UTF-8") 
                   + "=" + URLEncoder.encode("ohai", "UTF-8");

           	 data  +="&"+ URLEncoder.encode("NElat", "UTF-8") 
        	            + "=" + URLEncoder.encode(NElat, "UTF-8");

                data  +="&"+ URLEncoder.encode("NElon", "UTF-8") 
        	            + "=" + URLEncoder.encode(NElon, "UTF-8");

                data  +="&"+ URLEncoder.encode("SWlat", "UTF-8") 
        	            + "=" + URLEncoder.encode(SWlat, "UTF-8");

                data  +="&"+ URLEncoder.encode("SWlon", "UTF-8") 
        	            + "=" + URLEncoder.encode(SWlon, "UTF-8");
                
                Log.d("data is","data is "+data);
		   String link = SharedObjects.DB + "getnearplaces.php";
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
		   final String delims2="==";

		   Log.d("result","result is: "+result);
		   
		   if(result.length()<=1)
			   return null;
		   final String [] entries=result.split(delims1);
		   Log.d("entries length","entries length is "+entries.length);
     	   
		   Handler handler = new Handler(Looper.getMainLooper());
		   handler.post(new Runnable(){ 
			   
			   
			@Override
			public void run() {
				for(int i=0;i<entries.length;i++){
					   Log.d("entries","entries is "+entries[i]);
					   String [] columns=entries[i].split(delims2);
					   MapActivity.gMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(columns[2]), Double.parseDouble(columns[1])))
					   .title(columns[0]).snippet("Tap to Subscribe"));
					    CircleOptions circleOptions = new CircleOptions()
						.center(new LatLng(Double.parseDouble(columns[2]), Double.parseDouble(columns[1]))).radius(30)
						.fillColor(0x5500ff00)	//55 represents transparency , 00ff00 specifies the fill colour
						.strokeWidth(2);
						MapActivity.gMap.addCircle(circleOptions);
				
			}
		   }});
		   Vector<Place> places = new Vector<Place>();
		   
		   
		   return places;
         }catch(IOException e){
        	 Log.d(SharedObjects.TAG, e.toString());
        	 return new Vector<Place>();
         }
   }
   
   @Override
   protected void onPostExecute(Vector<Place> list){
	   super.onPostExecute(list); 
   }
  
   
}
