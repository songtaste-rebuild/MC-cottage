package com.mccottage.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;


public class MD5Utils {

	private static ThreadLocal<MessageDigest> messageDigestHolder = new ThreadLocal<MessageDigest>();
	private static final Logger log = Logger.getLogger(MD5Utils.class);

	static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	static {
		try {
			MessageDigest message = java.security.MessageDigest.getInstance("MD5");
			messageDigestHolder.set(message);
		} catch (NoSuchAlgorithmException e) {
			log.error("加密异常:" + messageDigestHolder);
		}
	}

	public static String getMD5Format(String data) {
		try {
			MessageDigest message = messageDigestHolder.get();
			if (message == null) {
				message = java.security.MessageDigest.getInstance("MD5");
				messageDigestHolder.set(message);
			}
			message.update(data.getBytes());
			byte[] b = message.digest();

			String digestHexStr = "";
			for (int i = 0; i < 16; i++) {
				digestHexStr += byteHEX(b[i]);
			}

			return digestHexStr;
		} catch (Exception e) {
			log.error("MD5加密异常" + data);
			return null;
		}
	}
	
	public static String getMD5Format(byte[] data) {
		try {
			MessageDigest message = messageDigestHolder.get();
			if (message == null) {
				message = java.security.MessageDigest.getInstance("MD5");
				messageDigestHolder.set(message);
			}
			
			message.update(data);
			byte[] b = message.digest();

			String digestHexStr = "";
			for (int i = 0; i < 16; i++) {
				digestHexStr += byteHEX(b[i]);
			}

			return digestHexStr;
		} catch (Exception e) {
			return null;
		}
	}

	private static String byteHEX(byte ib) {
		char[] ob = new char[2];
		ob[0] = hexDigits[(ib >>> 4) & 0X0F];
		ob[1] = hexDigits[ib & 0X0F];
		String s = new String(ob);
		return s;
	}
}