package com.checkin.utils;


import java.util.Vector;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	public static final String PLACE_ID = "place_id";
	public static final String NAME = "name";
	public static final String LONG = "long";
	public static final String LAT = "lat";
	public static final String RADIUS = "radius";
	public static final String ICONNUMBER = "icon_number";
	public static final String NUMFRIENDS = "num_friends";
	
	private String name;
	private Double longitude = null;
	private Double latitude = null;
	private LatLng location;
	private int place_id;
	private double radius;
	private int iconNumber;
	private Vector<User> currentUsers;
	private Integer numFriends = Integer.valueOf(0);
	
	private static String[] tableSchemaMapOrder = {NAME,LONG,LAT,RADIUS,NUMFRIENDS,ICONNUMBER, PLACE_ID};
	
	public static Place createPlace(String[] columns){
		Place tmp = new Place();
		try{
			for (int i = 0; i < columns.length; i++){
				if (tableSchemaMapOrder[i].equals(PLACE_ID)){
					tmp.place_id = Integer.parseInt(columns[i]);
				}else if  (tableSchemaMapOrder[i].equals(NAME)){
					tmp.name = columns[i];
				}
				else if  (tableSchemaMapOrder[i].equals(LONG)){
					tmp.longitude = Double.valueOf(columns[i]);
				}
				else if  (tableSchemaMapOrder[i].equals(LAT)){
					tmp.latitude = Double.valueOf(columns[i]);
				}
				else if  (tableSchemaMapOrder[i].equals(RADIUS)){
					tmp.radius = Double.valueOf(columns[i]);
				}
				else if  (tableSchemaMapOrder[i].equals(NUMFRIENDS)){
					tmp.setNumFriends(Integer.valueOf(columns[i]));
				}
				else if  (tableSchemaMapOrder[i].equals(ICONNUMBER)){
					tmp.setIconNumber(Integer.valueOf(columns[i]));
				}
				else{
					//not supported table column
					Log.e(SharedObjects.TAG, "Unsupported table column");
					return null;
				}
			}
			
			if (tmp.longitude != null && tmp.latitude != null){
				tmp.location = new LatLng(tmp.longitude.doubleValue(), tmp.latitude.doubleValue());
			}
			else{
				Log.e(SharedObjects.TAG, "Missing Longitude or Latitude");
				return null;
			}
		}catch(NumberFormatException e){
			Log.e(SharedObjects.TAG, e.toString());
			return null;
		}
		return tmp;
	}
	
	public void setLatLng(double latitude, double longitude){
		location = new LatLng(latitude, longitude);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LatLng getLocation() {
		return location;
	}
	
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	public int getPlace_id() {
		return place_id;
	}
	
	public void setPlace_id(int place_id) {
		this.place_id = place_id;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Vector<User> getCurrentUsers() {
		return currentUsers;
	}

	public void setCurrentUsers(Vector<User> currentUsers) {
		this.currentUsers = currentUsers;
	}

	public Integer getNumFriends() {
		return numFriends;
	}

	public void setNumFriends(Integer numFriends) {
		this.numFriends = numFriends;
	}

	public int getIconNumber() {
		return iconNumber;
	}

	public void setIconNumber(int iconNumber) {
		this.iconNumber = iconNumber;
	}


	

}
