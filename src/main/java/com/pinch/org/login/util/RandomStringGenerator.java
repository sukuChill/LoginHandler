package com.pinch.org.login.util;

import java.util.Random;

public class RandomStringGenerator {
	static String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~()'!*:@,;";

	public static String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder builder = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomValue = random.nextInt(allowedCharacters.length());// get random number from 0 to string length
			builder.append(allowedCharacters.charAt(randomValue));// get the character at the random generated value and append
		}

		return builder.toString();
	}

}
