package com.models;

public class Not {
	public int userID ;
	public int checkinID ;
	public String type;
	
	public String getType () {
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
	public int getCheckinID (){
		return checkinID;
	}
	
	public void setCheckinID(int checkinID){
		this.checkinID = checkinID;
	}
}
