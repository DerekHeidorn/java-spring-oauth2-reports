package com.example.demo.services.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.Base64Utils;

/**
 * @author CRRS
 *
 */
public final class MD5Util {

	/**
	 * Changes bytes to hex String.
	 * 
	 * @param data
	 * @return hex string representation of the byte[] array
	 */
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	        	if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	        	halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        return buf.toString();
    }
 
	/**
	 * @param password
	 * @param text
	 * @return MD5Sum as Byte[] array
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] MD5Raw(String password, String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		return MD5Raw(password + text);
	}	
	
	/**
	 * @param text
	 * @return MD5Sum as Byte[] array
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] MD5Raw(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		byte[] md5hash = md.digest();
		return md5hash;
	}	
	
	/**
	 * @param text
	 * @return hex encoded MD5sum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */	
	public static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		return convertToHex(MD5Raw(text));
	}
	
	/**
	 * @param password
	 * @param text
	 * @return hex encoded MD5sum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String MD5(String password, String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return MD5(password + text);
	}

	/**
	 * @param text
	 * @return WebSafe Base64 encoded MD5sum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String MD5_Base64(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
		return Base64Utils.encodeToString(MD5Raw(text));
	}
	
	
	/**
	 * @param password
	 * @param text
	 * @return WebSafe Base64 encoded MD5sum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String MD5_Base64(String password, String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return MD5_Base64(password + text);
	}	
}
