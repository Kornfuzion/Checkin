package com.checkin.activities;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.checkin.R;
import com.checkin.delegates.GetPlaces;
import com.checkin.delegates.GetUser;
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXAccountManager.MXAccountLinkListener;
import com.moxtra.sdk.MXSDKConfig.MXProfileInfo;
import com.moxtra.sdk.MXSDKConfig.MXUserIdentityType;
import com.moxtra.sdk.MXSDKConfig.MXUserInfo;
import com.moxtra.sdk.MXSDKException.InvalidParameter;

public class MainActivity extends ActionBarActivity {
	
	MXAccountManager mAcctMgr = null;
	
	
	@SuppressLint("NewApi")
	public void refreshPlaces(View v){
		ListView lv = (ListView) findViewById(R.id.places);
		ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
		ImageView rIcon = (ImageView) findViewById(R.id.refresh);
		GetPlaces getPlaces = new GetPlaces(this, lv, p, rIcon);
		getPlaces.execute(SharedObjects.phoneNumber);
		
	}
	
	@SuppressLint("NewApi")
	public void startMapActivity(View v){
		Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		Typeface font1 = Typeface.createFromAsset(getAssets(), "raleway_thin.otf"); 
		TextView t=(TextView)findViewById(R.id.logo); t.setTypeface(font1); 
		t=(TextView)findViewById(R.id.username); t.setTypeface(font1); 
		t=(TextView)findViewById(R.id.aboutme); t.setTypeface(font1);
		TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		
//		mPhoneNumber = "4168411532"; //FIXME Remove later
		SharedObjects.phoneNumber = parsePhoneNumber(mPhoneNumber);
		
		fetchContacts();
			
		ImageView imgView = (ImageView) findViewById(R.id.profilepic);
		TextView usernameView = (TextView) findViewById(R.id.username);
		TextView aboutMeView = (TextView) findViewById(R.id.aboutme);
		GetUser getUser = new GetUser(this, imgView, usernameView, null, aboutMeView);
		getUser.execute(SharedObjects.phoneNumber);
		
		try {
		    mAcctMgr = MXAccountManager.createInstance(this, SharedObjects.MOXTRA_CLIENT_ID, SharedObjects.MOXTRA_CLIENT_SECRET);
		} catch (InvalidParameter invalidParameter) {
		    invalidParameter.printStackTrace();
		    return;
		} 
		
		//should really occur at registration
		MXUserInfo userInfo = new MXUserInfo(SharedObjects.phoneNumber,MXUserIdentityType.IdentityUniqueId);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), User.GenerateProfileId(SharedObjects.phoneNumber));
		
		MXProfileInfo profile = new MXProfileInfo(SharedObjects.name,"",bm);
		mAcctMgr.setupUser(userInfo, profile ,SharedObjects.MOXTRA_CLIENT_SECRET, new MXAccountLinkListener(){
		        @Override
		        public void onLinkAccountDone(boolean bSuccess){
		        	//Callback if you want to do anything...s
		        }
		});
				
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshPlaces(null);
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		MXAccountManager.releaseInstance();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	//helper functions
	private String parsePhoneNumber(String phoneNumber){
		phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
		if (phoneNumber.charAt(0) == '1'){
			phoneNumber = phoneNumber.substring(1);
		}
		return phoneNumber;
	}
	
	private void fetchContacts() {
		
		String phoneNumber = null;
		
		try{
			Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
			String _ID = ContactsContract.Contacts._ID;
			String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
			String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
			
			Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
			String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
			
			ContentResolver contentResolver = getContentResolver();
			
			Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);	
			
			Vector<User> friends = new Vector<User>();
			// Loop for every contact in the phone
			if (cursor.getCount() > 0) {
				
				
				while (cursor.moveToNext()) {
					User friend = new User();
					String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
					String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
	
					int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
					
					if (hasPhoneNumber > 0) {
						
						friend.setRealName(name);
						
						// Query and loop for every phone number of the contact
						Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
						
						while (phoneCursor.moveToNext()) {
							phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
							
							friend.setPhoneNumber(parsePhoneNumber(phoneNumber));
							Log.d(SharedObjects.TAG, friend.getPhoneNumber());
						}
						phoneCursor.close();
					}
					if (friend.getPhoneNumber() != null && !friend.getPhoneNumber().isEmpty()){
						friends.add(friend);
					}
				}
			}
			SharedObjects.friends = friends;
		}catch (Exception e){
			Log.d(SharedObjects.TAG, "Fetch Contacts Error!");
		}
		
	}
	
}
