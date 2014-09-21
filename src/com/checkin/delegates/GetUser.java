package com.checkin.delegates;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkin.R;
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;

public class GetUser extends AsyncTask<String,Void, User>{
	LocalBroadcastManager mLocalBroadcastManager;
	private Context context;
	private ImageView profilePic;
	private TextView username;
	private TextView phoneNumber;
	private TextView aboutMe;

	public GetUser(Context context, ImageView profilePic, TextView username,
			TextView phoneNumber, TextView aboutMe) {
		this.context = context;
		this.profilePic = profilePic;
		this.username=username;
		this.phoneNumber=phoneNumber;
		this.aboutMe=aboutMe;
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute(); 
	}
   
	@Override
	protected User doInBackground(String... arg0) {
		Log.d(SharedObjects.TAG,"Getting User Data");
		try{
			String phone_number = (String)arg0[0];
			String data  = URLEncoder.encode("phone_number", "UTF-8") 	 
                    + "=" + URLEncoder.encode(phone_number, "UTF-8");
			//this URL determines which php code is executed at the server
			String link = SharedObjects.DB + "getuser.php";
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
			String[] entries = result.split(delims1);
			Log.d("entries length","entries length is "+entries.length);
		   
			if (entries[0] != null){
				String [] columns=entries[0].split(delims2);
				User user = User.createUser(columns);
				return user;
			}else{
				String [] columns=result.split(delims2);
				User user = User.createUser(columns);
				return user;
			}
		}catch(Exception e){
			Log.d(SharedObjects.TAG, e.toString());
			return null;
		}
	}
   
	private static int[] profileResIds = {R.drawable.profileblue,
										R.drawable.profilegreen,
										R.drawable.profilegrey,
										R.drawable.profileorange,
										R.drawable.profilepink,
										R.drawable.profilepurple,
										R.drawable.profilered,
										R.drawable.profileyellow};
	
	@Override
	protected void onPostExecute(User user){
		super.onPostExecute(user);
		if (user != null){   
			if (this.username != null){
				this.username.setText(user.getUsername());
			}
			
			if (this.phoneNumber != null){
				this.phoneNumber.setText(user.getPhoneNumber());
			}
			
			if (this.aboutMe != null){
				this.aboutMe.setText(user.getRealName() + '\n' + user.getAboutMe());
			}
			
			if (this.profilePic != null){
				int index;
				if (user.getPhoneNumber() == null){
					Random rand = new Random(System.currentTimeMillis());
				    index = rand.nextInt(8);
				}
				else{
					BigInteger phoneNum = new BigInteger(user.getPhoneNumber());
					BigInteger indexBig = phoneNum.mod(new BigInteger("8"));
					index = indexBig.intValue();
				}
				this.profilePic.setImageResource(profileResIds[index]);;
			}
		}
	}
}
