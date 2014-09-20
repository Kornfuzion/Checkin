package com.checkin.utils;

import android.graphics.Bitmap;

public class User {
	private String phoneNumber;
	private String name;
	private String password;
	private String aboutMe;
	private Bitmap profilePic;
	
	//might be public void setProfilePic(bytearray)
	public void setProfilePic(String byteArray){
		//TODO convert database string to bitmap
	}
	public String getProfilPicByteArrayString(){
		//TODO
		return profilePic.toString();
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Bitmap getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}
	public String getAboutMe() {
		return aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	
}
