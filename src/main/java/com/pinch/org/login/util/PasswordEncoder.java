package com.pinch.org.login.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	static BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	public static String passwordEncode(String plainText) {
		return bcrypt.encode(plainText);
	}

	public static boolean passwordValidate(String plainText, String hash) {
		return bcrypt.matches(plainText, hash);
	}
}
