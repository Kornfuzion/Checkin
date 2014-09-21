package com.checkin.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class User {
	public static final String PHONE_NUMBER = "phone_number";
	public static final String USERNAME = "username";
	public static final String NAME = "name";
	public static final String ABOUT_ME = "about_me";
	public static final String PROFILE_PIC = "profile_pic";
	
	private static String[] tableSchemaMapOrder = {NAME,PHONE_NUMBER,USERNAME,ABOUT_ME};
	
	private String phoneNumber;
	private String username;
	private String realName;
	private String aboutMe;
	private Bitmap profilePic;
	
	public static User createUser(String[] columns){
		User tmp = new User();
		try{
			for (int i = 0; i < columns.length; i++){
				if (tableSchemaMapOrder[i].equals(NAME)){
					tmp.realName = columns[i];
				}else if  (tableSchemaMapOrder[i].equals(PHONE_NUMBER)){
					tmp.phoneNumber = columns[i];
				}
				else if  (tableSchemaMapOrder[i].equals(USERNAME)){
					tmp.username = columns[i];
				}
				else if  (tableSchemaMapOrder[i].equals(ABOUT_ME)){
					tmp.aboutMe = columns[i];
				}
				else{
					//not supported table column
					Log.e(SharedObjects.TAG, "Unsupported table column");
					return null;
				}
			}
		}catch(NumberFormatException e){
			Log.e(SharedObjects.TAG, e.toString());
			return null;
		}
		return tmp;
	}
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
