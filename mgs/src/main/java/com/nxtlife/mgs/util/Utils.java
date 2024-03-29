package com.nxtlife.mgs.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

public class Utils {

	private final static Random RANDOM = new SecureRandom();
	private final static String ALPHANUMERICSTRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private final static String NUMERICSTRING = "0123456789";

	public static String generateRandomAlphaNumString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHANUMERICSTRING.charAt(RANDOM.nextInt(ALPHANUMERICSTRING.length())));
		}

		return new String(returnValue);
	}

	public static String generateRandomNumString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(NUMERICSTRING.charAt(RANDOM.nextInt(NUMERICSTRING.length())));
		}

		return new String(returnValue);
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	// public static boolean hasTokenExpired(String token) {
	// boolean returnValue = false;
	//
	// try {
	// Claims claims =
	// Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
	// .getBody();
	//
	// Date tokenExpirationDate = claims.getExpiration();
	// Date todayDate = new Date();
	//
	// returnValue = tokenExpirationDate.before(todayDate);
	// } catch (ExpiredJwtException ex) {
	// returnValue = true;
	// }
	//
	// return returnValue;
	// }

	// public String generateEmailVerificationToken(String userId) {
	// String token = Jwts.builder()
	// .setSubject(userId)
	// .setExpiration(new Date(System.currentTimeMillis() +
	// SecurityConstants.getExpirationTime()))
	// .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
	// .compact();
	// return token;
	// }

	// public String generatePasswordResetToken(String userId)
	// {
	// String token = Jwts.builder()
	// .setSubject(userId)
	// .setExpiration(new Date(System.currentTimeMillis() +
	// SecurityConstants.getExpirationTime()))
	// .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
	// .compact();
	// return token;
	// }
}
