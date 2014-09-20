package com.checkin.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.checkin.delegates.GetPlaces;
import com.checkin.delegates.GetUsersFromPlace;
import com.checkin.utils.SharedObjects;
import com.example.checkin.R;

public class FriendsAtPlaceActivity extends ActionBarActivity {

	String placeID;
	
	@SuppressLint("NewApi")
	public void refreshPlace(View v){
		ListView lv = (ListView) findViewById(R.id.friends);
		GetUsersFromPlace getUserFromPlace = new GetUsersFromPlace(this, lv);
		getUserFromPlace.execute(placeID);
	}
	
	public void goBack(View v){
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_main);
		
	    Intent intent = getIntent();
	    
	    String placeName = intent.getStringExtra(SharedObjects.PLACE_NAME);
	    TextView tv = (TextView) findViewById(R.id.title);
	    tv.setText(placeName);
	    
	    placeID = intent.getStringExtra(SharedObjects.PLACE_ID);
	    

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshPlace(null);
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
