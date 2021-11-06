package com.pinch.org.login.containers;

import java.util.Date;
import java.util.Map;

public class EditProfileDto {

	private String token;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String bio;
	private String location;
	private Map<String, String> socialSquare;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Map<String, String> getSocialSquare() {
		return socialSquare;
	}

	public void setSocialSquare(Map<String, String> socialSquare) {
		this.socialSquare = socialSquare;
	}
}
