package com.example.utils;

import com.google.android.gms.maps.model.LatLng;

public class Place {
	private String name;
	private LatLng location;
	private int place_id;
	private double radius;
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
}
