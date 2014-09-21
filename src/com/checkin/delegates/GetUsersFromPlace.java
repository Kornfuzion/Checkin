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

import com.checkin.adapters.UserAdapter;
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;

public class GetUsersFromPlace extends AsyncTask<String,Void,Vector<User>>{
	LocalBroadcastManager mLocalBroadcastManager;
   private ListView lv;
   private Context context;

   public GetUsersFromPlace(Context context, ListView listview) {
      this.context = context;
      //list view instead of statusField
      this.lv = listview;
   }

   @Override
   protected void onPreExecute(){
	   super.onPreExecute(); 
   }
   
   @Override
   protected Vector<User> doInBackground(String... arg0) {
	   Log.d("g","getdata doinbackground");
	   try{
		   String place_ID = (String)arg0[0];
		   String data  = URLEncoder.encode("place_ID", "UTF-8") 	 
                    + "=" + URLEncoder.encode(place_ID, "UTF-8");
		   String phone_numbers="";
		   for(int i=0;i<SharedObjects.friends.size();i++){
			   if(SharedObjects.friends.get(i).getPhoneNumber()!=null)
			   phone_numbers+=SharedObjects.friends.get(i).getPhoneNumber()+" ";
		   }
		   
		   data  +="&"+ URLEncoder.encode("phone_numbers", "UTF-8") 
   	            + "=" + URLEncoder.encode(phone_numbers, "UTF-8");

		   //this URL determines which php code is executed at the server
		   String link= SharedObjects.DB + "getusersfromplace.php";

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
		   
		   if(result.length()<=1)
			   return null;
		   
		   String [] entries=result.split(delims1);
		   Log.d("entries length","entries length is "+entries.length);
     	   
		   Vector<User> friends = new Vector<User>();
		   for(int i=0;i<entries.length;i++){
			   Log.d("entries","entries is "+entries[i]);
			   String [] columns=entries[i].split(delims2);
			   User tmp = User.createUser(columns);
			   if (tmp != null){
				   friends.add(tmp);
			   }
		   }
        	 
		   return friends;
         }catch(Exception e){
        	 Log.e(SharedObjects.TAG, e.toString());
        	 return new Vector<User>();
         }
   }
   
   @Override
   protected void onPostExecute(Vector<User> list){
	   super.onPostExecute(list); 
	   if(list!=null){
	   UserAdapter pa = new UserAdapter(this.context, list);
	   lv.setAdapter(pa);
	   }
   }
  
   
}
