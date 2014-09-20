package com.checkin.utils;

import java.util.Vector;

public class SharedObjects {
	
	public final static String TAG = "CheckIn";
	public final static String DB = "http://ec2-54-84-102-132.compute-1.amazonaws.com/checkin/";
	public static Vector<User> friends;
	public static Vector<Place> places;
}
