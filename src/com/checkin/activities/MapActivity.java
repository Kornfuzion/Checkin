package com.checkin.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.checkin.activities.GeofenceUtils.REQUEST_TYPE;
import com.checkin.delegates.GetNearPlaces;
import com.checkin.delegates.InsertPlace;
import com.checkin.delegates.SubscribePlace;
import com.checkin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checkin.R;
import com.checkin.activities.GeofenceUtils.REQUEST_TYPE;
import com.checkin.delegates.InsertPlace;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity {

	 	private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
	    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
	            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;

	    // Store the current request
	    public static REQUEST_TYPE mRequestType;

	    // Persistent storage for geofences
	    private SimpleGeofenceStore mPrefs;

	    // Store a list of geofences to add
	    public static List<Geofence> mCurrentGeofences;

	    // Add geofences handler
	    public static GeofenceRequester mGeofenceRequester;
	    // Remove geofences handler
	    private GeofenceRemover mGeofenceRemover;
	    // Handle to geofence 1 latitude in the UI
	    private EditText mLatitude1;

	    // Handle to geofence 1 longitude in the UI
	    private EditText mLongitude1;

	    // Handle to geofence 1 radius in the UI
	    private EditText mRadius1;

	    // Handle to geofence 2 latitude in the UI
	    private EditText mLatitude2;

	    // Handle to geofence 2 longitude in the UI
	    private EditText mLongitude2;

	    // Handle to geofence 2 radius in the UI
	    private EditText mRadius2;

	    /*
	     * Internal lightweight geofence objects for geofence 1 and 2
	     */
	    public static SimpleGeofence mUIGeofence1;
	    private SimpleGeofence mUIGeofence2;

	    // decimal formats for latitude, longitude, and radius
	    private DecimalFormat mLatLngFormat;
	    private DecimalFormat mRadiusFormat;

	    /*
	     * An instance of an inner class that receives broadcasts from listeners and from the
	     * IntentService that receives geofence transition events
	     */
	    private GeofenceSampleReceiver mBroadcastReceiver;

	    // An intent filter for the broadcast receiver
	    private IntentFilter mIntentFilter;

	    // Store the list of geofences to remove
	    private List<String> mGeofenceIdsToRemove;
	    
	    public static GoogleMap gMap;
	    
	    private View infoWindow;    

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        // Set the pattern for the latitude and longitude format
	        String latLngPattern = getString(R.string.lat_lng_pattern);

	        // Set the format for latitude and longitude
	        mLatLngFormat = new DecimalFormat(latLngPattern);

	        // Localize the format
	        mLatLngFormat.applyLocalizedPattern(mLatLngFormat.toLocalizedPattern());

	        // Set the pattern for the radius format
	        String radiusPattern = getString(R.string.radius_pattern);

	        // Set the format for the radius
	        mRadiusFormat = new DecimalFormat(radiusPattern);

	        // Localize the pattern
	        mRadiusFormat.applyLocalizedPattern(mRadiusFormat.toLocalizedPattern());

	        // Create a new broadcast receiver to receive updates from the listeners and service
	        mBroadcastReceiver = new GeofenceSampleReceiver();

	        // Create an intent filter for the broadcast receiver
	        mIntentFilter = new IntentFilter();

	        // Action for broadcast Intents that report successful addition of geofences
	        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

	        // Action for broadcast Intents that report successful removal of geofences
	        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

	        // Action for broadcast Intents containing various types of geofencing errors
	        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

	        // All Location Services sample apps use this category
	        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

	        // Instantiate a new geofence storage area
	        mPrefs = new SimpleGeofenceStore(this);

	        // Instantiate the current List of geofences
	        mCurrentGeofences = new ArrayList<Geofence>();

	        // Instantiate a Geofence requester
	        mGeofenceRequester = new GeofenceRequester(this);

	        // Instantiate a Geofence remover
	        mGeofenceRemover = new GeofenceRemover(this);

	        // Attach to the main UI
	        setContentView(R.layout.pin_map_main);
	        Typeface font1 = Typeface.createFromAsset(getAssets(), "raleway_thin.otf"); 
	        TextView t=(TextView)findViewById(R.id.logo);
	        t.setTypeface(font1);
	        FragmentManager myFragmentManager = getSupportFragmentManager();
	        SupportMapFragment mySupportMapFragment 
	         = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.mapView);
	        
	        gMap = mySupportMapFragment.getMap();
	        
	        LocationManager locationManager = (LocationManager) 
	 	            getSystemService(LOCATION_SERVICE);
	 	    
	 	    Location location =  locationManager
	                 .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	 	    double lat;
			double lon;
			try {
	 	        lat = location.getLatitude();
	 	        lon = location.getLongitude();
	 	    } catch (NullPointerException e) {
	 	        lat = -1.0;
	 	        lon = -1.0;
	 	        location =  locationManager
	 	                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	 	        try{

	 		        lat = location.getLatitude();
	 		        lon = location.getLongitude();
	 	        }
	 	        catch(NullPointerException d){
	 		        lat = -1.0;
	 		        lon = -1.0;
	 	        }
	 	    }
	        
	        
	        
	        gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
	    	gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
	    	
			    // Zoom in the Google Map
	        gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	        
	        LatLngBounds bounds = gMap.getProjection().getVisibleRegion().latLngBounds;
		    double offset=0.1;
	        new GetNearPlaces(this,null,null,null).execute((lat+offset)+"",(lon+offset)+"",(lat-offset)+"",(lon-offset)+"");   
	        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
	        String placeName = null;
	        
			@Override
			public void onMapClick(final LatLng arg0) {
				
				
				LayoutInflater li = LayoutInflater.from(MapActivity.this);
				View promptsView = li.inflate(R.layout.prompts, null);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MapActivity.this);
 
				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
 				
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);
				
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
					    if(userInput.getText().toString().isEmpty()) {
					    	placeName = "Place Name";
					    }else {
					    	placeName = (userInput.getText().toString());
					    }
					    gMap.addMarker(new MarkerOptions().position(arg0).title(placeName).snippet("Tap to edit"));
					    CircleOptions circleOptions = new CircleOptions()
						.center(arg0).radius(30)
						.fillColor(0x5500ff00)	//55 represents transparency , 00ff00 specifies the fill colour
						.strokeWidth(2);
						gMap.addCircle(circleOptions);
						new InsertPlace(MapActivity.this,1).execute(placeName,arg0.longitude+"",arg0.latitude+"");
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();

			}
		});
	        
	    gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	    	
			@Override
			public void onInfoWindowClick(final Marker arg0) {
				
				LayoutInflater li = LayoutInflater.from(MapActivity.this);
				View promptsView = li.inflate(R.layout.prompts, null);
 /*
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MapActivity.this);
 
				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
 				
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);
				
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
					    if(userInput.getText().toString().isEmpty()) {
					    	arg0.setTitle("Place Name");
					    }else {
					    	arg0.setTitle(userInput.getText().toString());
					    }
						
						arg0.hideInfoWindow();
						arg0.showInfoWindow();
					    	
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
				*/
				String snippet=arg0.getSnippet();
				String[] s=snippet.split(" ");
				new SubscribePlace(MapActivity.this,1).execute(s[4]);
			}
	    	
	    });
	   
	    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;

        // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
               
            }
            return false;
        }
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
    
    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * GeofenceRemover and GeofenceRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     * calls
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Choose what to do based on the request code
    	 switch (requestCode) {

         // If the request code matches the code sent in onConnectionFailed
         case GeofenceUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

             switch (resultCode) {
                 // If Google Play services resolved the problem
                 case Activity.RESULT_OK:

                     // If the request was to add geofences
                     if (GeofenceUtils.REQUEST_TYPE.ADD == mRequestType) {

                         // Toggle the request flag and send a new request
                         mGeofenceRequester.setInProgressFlag(false);

                         // Restart the process of adding the current geofences
                         mGeofenceRequester.addGeofences(mCurrentGeofences);

                     // If the request was to remove geofences
                     }
                 break;

                 // If any other result was returned by Google Play services
                 default:

                     // Report that Google Play services was unable to resolve the problem.
                     Log.d(GeofenceUtils.APPTAG, getString(R.string.no_resolution));
             }

         // If any other request code was received
         default:
            // Report that this Activity received an unknown requestCode
            Log.d(GeofenceUtils.APPTAG,
                    getString(R.string.unknown_activity_request_code, requestCode));

            break;
     }
   
    	 
    	 
        
    }
    
    /**
     * Define a Broadcast receiver that receives updates from connection listeners and
     * the geofence transition service.
     */
    public class GeofenceSampleReceiver extends BroadcastReceiver {
        /*
         * Define the required method for broadcast receivers
         * This method is invoked when a broadcast Intent triggers the receiver
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

            // Intent contains information about successful addition or removal of geofences
            } else if (
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                    ||
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

            // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

            // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing geofences, put it here.
         *
         * @param context A Context for this component
         * @param intent The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context A Context for this component
         * @param intent The Intent containing the transition
         */
        private void handleGeofenceTransition(Context context, Intent intent) {
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
    public void goback(View v){
    	super.onBackPressed();
    }
}


