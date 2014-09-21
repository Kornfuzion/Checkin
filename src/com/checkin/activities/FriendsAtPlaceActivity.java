package com.checkin.activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkin.R;
import com.checkin.adapters.UserAdapter;
import com.checkin.delegates.GetUsersFromPlace;
import com.checkin.utils.SharedObjects;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException.AccountManagerIsNotValid;

public class FriendsAtPlaceActivity extends ActionBarActivity {

	String placeID;
	Context context;
	
	   
	public static List<String> uniqueIds;
	
	@SuppressLint("NewApi")
	public void refreshPlace(View v){
		ListView lv = (ListView) findViewById(R.id.friends);
		GetUsersFromPlace getUserFromPlace = new GetUsersFromPlace(this, lv);
		getUserFromPlace.execute(placeID);
	}
	
	@SuppressLint("NewApi")
	public void startMapActivity(View v){
		Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
	}
	
	public void goBack(View v){
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.friend_main);
		
	    Intent intent = getIntent();
	    
	    String placeName = intent.getStringExtra(SharedObjects.PLACE_NAME);
	    TextView tv = (TextView) findViewById(R.id.title);
	    tv.setText(placeName);
	    Typeface font1 = Typeface.createFromAsset(getAssets(), "raleway_thin.otf"); 
       
        tv.setTypeface(font1);
	    placeID = intent.getStringExtra(SharedObjects.PLACE_ID);
	    context = this;
	    
	    TextView groupChatBtn = (TextView) findViewById(R.id.groupChatBtn);
	    groupChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            	try {
            	    MXChatManager.getInstance().createChat(new MXChatManager.OnCreateChatListener() {
            	        @Override
            	        public void onCreateChatSuccess(String binderId) {
            	            Log.d(SharedObjects.TAG, "onCreateChatSuccess(), binderId = " + binderId);
            	            MXChatManager.OnInviteListener callback = new MXChatManager.OnInviteListener() {
            	                @Override
            	                public void onInviteSuccess() {
            	                    Toast.makeText(context, "Invite Successfully", Toast.LENGTH_SHORT).show();
            	                }
            	                @Override
            	                public void onInviteFailed(int errorCode, String message) {
            	                    Toast.makeText(context, "Invite Failed. Error: " + message, Toast.LENGTH_LONG).show();
            	                }
            	            };
            	    
            	            MXChatManager.getInstance().inviteByUniqueIds(binderId, uniqueIds, callback);
            	        }
            	        @Override
            	        public void onCreateChatFailed(int errorCode, String message) {
            	            Log.d(SharedObjects.TAG, "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
            	        }
            	    });
            	} catch (AccountManagerIsNotValid e) {
            	        e.printStackTrace();
            	}
            }
        }); 
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
