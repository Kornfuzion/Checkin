package com.checkin.utils;

import java.util.Vector;

public class SharedObjects {
	
	public final static String TAG = "CheckIn";
	public final static String DB = "http://ec2-54-84-102-132.compute-1.amazonaws.com/checkin/";
	
	public final static String MOXTRA_CLIENT_ID = "gYHvRfT9j38";
	public final static String MOXTRA_CLIENT_SECRET = "s5IbMUkqDQo";
	
	public final static String PACKAGE_NAME = "com.checkin.";
	public final static String DISPLAYED_PACKAGE_NAME = "com.checkin";
	public final static String PLACE_ID = PACKAGE_NAME + "PLACE_ID";
	public final static String PLACE_NAME = PACKAGE_NAME + "PLACE_NAME";
	
	
	public static String phoneNumber = null;
	public static String name = "default";
	public static Vector<User> friends;
	public static Vector<Place> places;
}
