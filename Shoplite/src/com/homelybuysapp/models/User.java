package com.homelybuysapp.models;

public class User {

	private int id;
	private String name;
	private String email;
	private String phno;
	private boolean isMale;
//	private HomelyBuysLocation HomelyBuysLocation;
	private String dob;
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhno() {
		return phno;
	}
	public void setPhno(String phno) {
		this.phno = phno;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
//	public HomelyBuysLocation getLocation() {
//		return HomelyBuysLocation;
//	}
//	public void setLocation(HomelyBuysLocation HomelyBuysLocation) {
//		this.location = HomelyBuysLocation;
//	}
	
}
