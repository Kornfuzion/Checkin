package com.checkin.activities;

import java.util.Vector;
import com.checkin.delegates.GetPlaces;
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;
import com.example.checkin.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Build;
import android.provider.ContactsContract;

public class MainActivity extends ActionBarActivity {

	public static ProgressBar p;
	public static ImageView refreshicon;
	
	@SuppressLint("NewApi")
	public void refreshPlaces(View v){
		ListView lv = (ListView) findViewById(R.id.places);
		p.setVisibility(View.VISIBLE);
		refreshicon.setImageResource(R.drawable.gone);
		GetPlaces getPlaces = new GetPlaces(this, lv);
		getPlaces.execute(SharedObjects.phoneNumber);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		refreshicon=(ImageView)findViewById(R.id.refresh);

		p=(ProgressBar)findViewById(R.id.progressBar);
		p.setVisibility(View.INVISIBLE);
		SharedObjects.phoneNumber = "4168411532";
		fetchContacts();
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ListView lv = (ListView) findViewById(R.id.places);
		GetPlaces getPlaces = new GetPlaces(this, lv);
		getPlaces.execute(SharedObjects.phoneNumber);
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

public void fetchContacts() {
		
		String phoneNumber = null;
		String email = null;
		
		try{
			Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
			String _ID = ContactsContract.Contacts._ID;
			String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
			String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
			
			Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
			String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
			
			Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
			String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
			String DATA = ContactsContract.CommonDataKinds.Email.DATA;
			
			ContentResolver contentResolver = getContentResolver();
			
			Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);	
			
			Vector<User> friends = new Vector<User>();
			// Loop for every contact in the phone
			if (cursor.getCount() > 0) {
				
				User friend = new User();
				while (cursor.moveToNext()) {
					
					String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
					String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
	
					int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
					
					if (hasPhoneNumber > 0) {
						
						friend.setName(name);
						
						// Query and loop for every phone number of the contact
						Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
						
						while (phoneCursor.moveToNext()) {
							phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
							friend.setPhoneNumber(phoneNumber);
						
						}
						phoneCursor.close();
					}
				}
	
				friends.add(friend);
			}
			
			SharedObjects.friends = friends;
		}catch (Exception e){
			Log.d(SharedObjects.TAG, "Fetch Contacts Error!");
		}
		
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

	
}
