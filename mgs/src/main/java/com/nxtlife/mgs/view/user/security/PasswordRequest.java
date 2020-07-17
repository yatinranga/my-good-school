package com.nxtlife.mgs.view.user.security;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.nxtlife.mgs.ex.ValidationException;

public class PasswordRequest {

	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username not valid")
	private String username;

	@NotEmpty(message = "password can't be null/empty")
	private String password;

	private String generatedPassword;

	private String oldPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void checkPassword() {
		if (this.password.equals(this.oldPassword)) {
			throw new ValidationException("old password and password can't be same");
		}
	}

	public void checkGeneratedPassword() {
		if (generatedPassword == null) {
			throw new ValidationException("Generated password can't be empty");
		} else if (this.password.equals(this.generatedPassword)) {
			throw new ValidationException("generated password and password can't be same");
		}
	}
}
