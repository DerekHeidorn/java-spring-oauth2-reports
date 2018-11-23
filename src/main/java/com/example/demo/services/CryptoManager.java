package com.example.demo.services;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.example.demo.services.exceptions.CryptoException;
import com.example.demo.services.utils.MD5Util;

@Service
public final class CryptoManager implements ApplicationContextAware {


	private final static String PARAM_KEY_PAYLOAD = "p";
	private final static String PARAM_KEY_MD5SUM = "m";
	
	public final static String WEB_ENCRYPTION_ALGORITHM_AES = "AES";

	private Log log = LogFactory.getLog(this.getClass());
	
	private ApplicationContext applicationContext; 
	private SecretKey key = null;
	private static String WEB_ENCRYPTION_ALGORITHM = WEB_ENCRYPTION_ALGORITHM_AES;
	private Base64 base64 = new Base64(0, new byte[]{}, true); // non-chunking, web safe base64				
	
	@Autowired
	private CommonManager commonManager;


	public CryptoManager() {
		super();
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
		try {
			setupApplicationKeys(applicationContext);
		} catch(Exception e) {
			e.printStackTrace();
			throw new FatalBeanException("Unable to create and load encryption keys for the application:" + e.getMessage());
		}
	}	
	

	private void setupApplicationKeys(ApplicationContext applicationContext) throws InvalidKeyException, 
							    						NoSuchAlgorithmException, 
													    InvalidKeySpecException,
													    NoSuchPaddingException, 
													    IllegalBlockSizeException, 
													    BadPaddingException, IOException {
		

		CommonManager commonManager = getCommonManager(applicationContext);
        
        String appSecretKey = commonManager.getConfigParamValue("app.secret_key");
        log.debug("appSecretKey=" + appSecretKey);
        
    	byte[] rawPassword = appSecretKey.getBytes();
    	key = new SecretKeySpec(rawPassword, "AES");
		
		if(key != null) {
			log.info("Web layer encryption is enabled. Using " + WEB_ENCRYPTION_ALGORITHM + " algorithm");
		} else {
			log.fatal("WEB LAYER ENCRYPTION IS DISABLED. Key = null");
			throw new InvalidKeyException("Key is null");
		}

	}

	
	/**
	 * @param s
	 * @return
	 * @throws CryptoException
	 */
	public String encryptStringAndUrlEncoding(String s) throws CryptoException  {
		String urlEncodeString = null;
		
		String base64EncodedValue = encryptString(s); 

		if(base64EncodedValue != null) {
			try {
				urlEncodeString = URLEncoder.encode(base64EncodedValue, "UTF-8");
			} catch (UnsupportedEncodingException unsupportedEncodingException) {
				throw new CryptoException(unsupportedEncodingException.getMessage());
			}
		}

		return urlEncodeString;
	}
	
	/**
	 * @param i
	 * @return
	 * @throws CryptoException
	 */
	public String encryptIntegerAndUrlEncoding(Integer i) throws CryptoException  {
		String urlEncodeString = null;
		
		String base64EncodedValue = encryptInteger(i); 

		if(base64EncodedValue != null) {
			try {
				urlEncodeString = URLEncoder.encode(base64EncodedValue, "UTF-8");
			} catch (UnsupportedEncodingException unsupportedEncodingException) {
				throw new CryptoException(unsupportedEncodingException.getMessage());
			}
		}

		return urlEncodeString;
	}	
	
    /**
     * Encrypts a String
     * 
     * @param s
     * @return
     * @throws CryptoException
     */
    public String encryptString(String s) throws CryptoException  {
    	
    	if(s == null) {
    		return null;
    	}    	
    	
    	return encrypt(s.getBytes());
    	
    }
	

    /**
     * Decrypts a String 
     * 
     * @param encryptedString
     * @return
     * @throws CryptoException
     */
    public String decryptString(String encryptedString) throws CryptoException {
    	
    	byte[] output = decrypt(encryptedString);
    	
    	return new String(output);
    }
    
    /**
     * Encrypts byte[] and Websafe Base64 encodes it.
     * 
     * @param input
     * @return
     * @throws CryptoException
     */
    public String encrypt(byte[] input) throws CryptoException  {

    	if(key == null) {
    		throw new CryptoException("application key is null");
    	}	
		
    	String encryptedString = null;
    	
    	try { 
    	
	        Cipher cipher = Cipher.getInstance(WEB_ENCRYPTION_ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	    	byte[] encrypt = cipher.doFinal(input);  
	    	
	    	// base64 encode the bytes
	    	encryptedString = base64.encodeAsString(encrypt);
    	
    	} catch(NoSuchAlgorithmException noSuchAlgorithmException) {
    		throw new CryptoException(noSuchAlgorithmException.getMessage());
    		
    	} catch(NoSuchPaddingException noSuchPaddingException) {
    		throw new CryptoException(noSuchPaddingException.getMessage());
    		
    	} catch(InvalidKeyException invalidKeyException) {
    		throw new CryptoException(invalidKeyException.getMessage());
    		
    	} catch(BadPaddingException badPaddingException) {
    		throw new CryptoException(badPaddingException.getMessage());
    		
    	} catch(IllegalBlockSizeException illegalBlockSizeException) {
    		throw new CryptoException(illegalBlockSizeException.getMessage());
    	}     	
    	
    	return encryptedString;
    }   	


    /**
     * Decrypts an encrypted string into a byte[] array
     * 
     * @param encryptedValue
     * @return
     * @throws CryptoException
     */
    public byte[] decrypt(String encryptedValue) throws CryptoException {

    	if(key == null) {
    		throw new CryptoException("application key is null");
    	}    	
    	
    	byte[] output = null;
    	
    	try { 
    	
	    	Cipher cipher = Cipher.getInstance(WEB_ENCRYPTION_ALGORITHM);
	    	cipher.init(Cipher.DECRYPT_MODE, this.key);
	    	
	    	//base64 decode
	    	byte[] decoded = base64.decode(encryptedValue);
	    	
	    	//decrypt message
	    	output =  cipher.doFinal(decoded);
	    	
    	
    	} catch(NoSuchAlgorithmException noSuchAlgorithmException) {
    		throw new CryptoException(noSuchAlgorithmException.getMessage());
    		
    	} catch(NoSuchPaddingException noSuchPaddingException) {
    		throw new CryptoException(noSuchPaddingException.getMessage());
    		
    	} catch(InvalidKeyException invalidKeyException) {
    		throw new CryptoException(invalidKeyException.getMessage());
    		
    	} catch(BadPaddingException badPaddingException) {
    		throw new CryptoException(badPaddingException.getMessage());
    		
    	} catch(IllegalBlockSizeException illegalBlockSizeException) {
    		throw new CryptoException(illegalBlockSizeException.getMessage());
    	}    	
    	
    	return output;
    }	
	

    /**
     * Encrypts Integer
     * 
     * @param i
     * @return
     * @throws CryptoException
     */
    public String encryptInteger(Integer i) throws CryptoException  {
    	
    	if(i == null) {
    		return null;
    	}
    	
    	return encryptString(i.toString());
    }   	
	

    /**
     * Decrypts Integer
     * 
     * @param encryptedInteger
     * @return
     * @throws CryptoException
     */
    public Integer decryptInteger(String encryptedInteger) throws CryptoException {
    	
    	String s = this.decryptString(encryptedInteger);
    	Integer i = null;

    	if(s != null && s.length() > 0) {
	    	// this should be an Integer
	    	try {
	    		i = new Integer(s);
	    	} catch(NumberFormatException nfe) {
	    		throw new CryptoException("invalid integer for string '" + s + "'");
	    	}
    	} 
    	
    	return i;
    }    
    

    /**
     * Encrypts a Long
     * 
     * @param l
     * @return
     * @throws CryptoException
     */
    public String encryptLong(Long l) throws CryptoException  {
    	
    	if(l == null) {
    		return null;
    	}
    	
    	return encrypt(CryptoManager.longToByteArray(l.longValue()));
    }   	
	
 
    /**
     * Decrypts a Long
     * 
     * @param encryptedLong
     * @return
     * @throws CryptoException
     */
    public Long decryptLong(String encryptedLong) throws CryptoException {
    	
    	byte[] raw = this.decrypt(encryptedLong);
    	Long l = null;

    	if(raw != null && raw.length > 0) {
    		l = CryptoManager.byteArrayToLong(raw);
    	} 
    	
    	return l;
    }    
    

    /**
     * Encrypts a Long that is protected by an MD5Sum
     * 
     * @param l
     * @param password
     * @return
     * @throws CryptoException
     */
    public String encryptLongWithMD5Sum(Long l, String password) throws CryptoException  {
    	
    	if(l == null) {
    		return null;
    	}
    	
		byte[] lngBytes = CryptoManager.longToByteArray(l.longValue());
		byte[] md5hash;
		try {
			md5hash = MD5Util.MD5Raw(password, l.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
		}
		
		byte[] combo = new byte[lngBytes.length + md5hash.length];
		int i = 0;
		for(int j = 0; j < lngBytes.length; j++, i++) {
			combo[i] = lngBytes[j];
		}
		for(int j = 0; j < md5hash.length; j++, i++) {
			combo[i] = md5hash[j];
		}		
    	
    	return encrypt(combo);
    }   	
	

    /**
     * Decrypts a string that contains a Long that is protected by an MD5Sum
     * 
     * @param encryptedString
     * @param password
     * @return
     * @throws CryptoException
     */
    public Long decryptLongWithMD5Sum(String encryptedString, String password) throws CryptoException {
    	
    	byte[] raw = this.decrypt(encryptedString);
    	Long l = null;
     	
    	byte[] rawLong = new byte[8];
    	byte[] rawMD5 = new byte[16];
    	
    	if(raw.length != (rawLong.length + rawMD5.length)) {
    		throw new CryptoException("byte lengths don't match.");
    	}
    	
		int i = 0;
		for(int j = 0; j < rawLong.length; j++, i++) {
			rawLong[j] = raw[i];
		}
		for(int j = 0; j < rawMD5.length; j++, i++) {
			rawMD5[j] = raw[i];
		}	    	
    	
		l = CryptoManager.byteArrayToLong(rawLong); 
    	
		byte[] md5hash;
		try {
			md5hash = MD5Util.MD5Raw(password, l.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
		}
		
		if(md5hash.length == rawMD5.length) {
		
			for(int j = 0; j < md5hash.length; j++) {
				if(md5hash[j] != rawMD5[j]) {
					throw new CryptoException("MD5 values don't match.");
				}
			}
		} else {
			throw new CryptoException("MD5 lengths don't match.");
		}
		
    	return l;
    }     
    
    /**
     * XOR bytes with a mask
     * 
     * @param value
     * @param mask
     * @return
     */
    public static byte[] xorBytes(byte[] value, byte[] mask) {    	
    	for(int i = 0, j = 0; i < value.length; i++) {
    		value[i] ^= mask[j]; 
    		if(j < mask.length - 1) {
    			j++;
    		} else {
    			j = 0;
    		}
    	}
    	return value;
    }
    
    /**
     * Converts a byte array into a Long
     * 
     * @param l
     * @return
     */
    public static byte[] longToByteArray(long l) {
		byte[] bArray = new byte[8];
		ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
		LongBuffer lBuffer = bBuffer.asLongBuffer();
		lBuffer.put(0, l);
		return bArray;
	}     
    
    /**
     * Converts a Long into a byte array
     * 
     * @param bytes
     * @return
     */
    public static long byteArrayToLong(byte[] bytes) {
    	ByteBuffer a = ByteBuffer.wrap(bytes);
         return a.getLong();
    }
    
    /**
     * Converts a byte array into an Integer
     * 
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
    	ByteBuffer a = ByteBuffer.wrap(bytes);
         return a.getInt();
    }      
    
    /**
     * Converts an Integer into a byte array
     * 
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] bArray = new byte[4];
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        IntBuffer iBuffer = bBuffer.asIntBuffer();
        iBuffer.put(0, i);
        return bArray;
    }    
    

    /**
     * Encrypts a Map as a String.
     * 
     * @param map
     * @return
     * @throws CryptoException
     */
    public String encryptMap(Map<String,String> map) throws CryptoException {
    	
    	String urlEncodedValues = CryptoManager.createUrlParameters(map);
    	return encryptString(urlEncodedValues);
   
    }    
    
    
    /**
     * Decrypts a Encoded String into a Map 
     * 
     * @param encryptedMap
     * @return
     * @throws CryptoException
     */
    public Map<String,String> decryptMap(String encryptedMap) throws CryptoException {

    	String urlEncodedString = decryptString(encryptedMap);
    	Map<String, String> map = CryptoManager.parseUrlParameters(urlEncodedString);
      	
    	return map;
    }    
    
    public String MD5(String s) throws CryptoException{
    	try {
			return MD5Util.MD5(SecurityConstants.MD5SUM_PASSWORD_GENERAL, s);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
		}
    }      
    
    public String MD5(String s, Class<?> clazz) throws CryptoException {
    	return MD5(SecurityConstants.MD5SUM_PASSWORD_GENERAL, clazz, s);
    }    
    
    public String MD5(String s, Class<?> clazz, String md5SumPassword) throws CryptoException {
    	try {
    		return MD5Util.MD5(md5SumPassword, clazz.getSimpleName() + s);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
		}    	
    	
    }
    
    public static String SHA_256(String s, String digestKey) throws CryptoException {
    	try {
	    	String combined = digestKey + s;
	    	
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			sha256.update(combined.getBytes("UTF-8"));
			byte[] digestSha256 = sha256.digest();
	
			return new String(Hex.encodeHex(digestSha256));
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
		} 
    }
    
    
    /**
     * Appends an MD5Sum to protected the data and then encrypts it.
     * 
     * @param s
     * @param md5SumPassword
     * @return
     * @throws CryptoException
     */
    public String encryptStringWithMD5Sum(String s, String md5SumPassword) throws CryptoException {

		String result = null;
		try {
			StringBuilder params = new StringBuilder();
	
			String md5Sum = MD5Util.MD5(md5SumPassword, s);
			
			params.append(PARAM_KEY_PAYLOAD + "=" + urlEncodeString(s));
			params.append("&" + PARAM_KEY_MD5SUM + "=" + urlEncodeString(md5Sum));
			
			result = encryptString(params.toString());
	
		} catch (UnsupportedEncodingException e) {
			throw new CryptoException(e.getMessage());
			
		} catch(NoSuchAlgorithmException e) {
			throw new CryptoException(e.getMessage());
		}
		return (result == null) ? "" : result;    
    }
    


    /**
     * Decrypts String that is protected with an MD5Sum
     * 
     * @param encryptedString
     * @param md5SumPassword
     * @return original string
     * @throws CryptoException
     */
    public String decryptStringWithMD5Sum(String encryptedString, String md5SumPassword) throws CryptoException  {
    	
    	String s = null;
    	

		String params = decryptString(encryptedString);
		StringTokenizer st = new StringTokenizer(params, "&");
		
		// must have 2 tokens
		if(st.countTokens() == 2) {
			String payloadParam = st.nextToken();
			String md5SumParam = st.nextToken();
			
			
			String md5Sum = null;
			try {
				s = getUrlDecodedParam(payloadParam, PARAM_KEY_PAYLOAD);
				md5Sum = getUrlDecodedParam(md5SumParam, PARAM_KEY_MD5SUM);
				String compareString = MD5Util.MD5(md5SumPassword, s);
				
				// MD5Sums must match
				if(!md5Sum.equals(compareString)) {
					throw new CryptoException("Unable to decrypt value");
				} 				
				
			} catch (UnsupportedEncodingException e) {
				throw new CryptoException(e.getMessage());
				
			} catch(NoSuchAlgorithmException e) {
				throw new CryptoException(e.getMessage());
			}


		} else {
			throw new CryptoException("Unable to decrypt value");
		}
    	
    	return s;
    }
    
    private String getUrlDecodedParam(String s, String keyMatch) throws UnsupportedEncodingException {
    	StringTokenizer st = new StringTokenizer(s, "=");
    	if(st.countTokens() == 2) {
    		String key = st.nextToken();
    		if(keyMatch.equals(key)) {
	    		String value = st.nextToken();
	    		return urlDecodeString(value);
    		}
    	}
    	return null;
    }  
    
    

	/**
	 * Url Decodes a String
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String urlDecodeString(String s) throws UnsupportedEncodingException  {
		return java.net.URLDecoder.decode(s, "UTF-8");
	}       
    
	/**
	 * Url Encodes a String
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String urlEncodeString(String s) throws UnsupportedEncodingException  {
		return java.net.URLEncoder.encode(s, "UTF-8");
	}      

	/**
	 * Creates a Url Encoded String from Map
	 * 
	 * @param parameters
	 * @return
	 */
	public static String createUrlParameters(Map<String,String> parameters) {
    	StringBuilder sb = new StringBuilder();
    	
    	Set<String> keySet = parameters.keySet();
    	int count = 0;
    	for (String key : keySet) {
    		String value = parameters.get(key);
			
			try {
				key = URLEncoder.encode(key, "UTF-8");
				value = URLEncoder.encode(value, "UTF-8");
				
	    		if(count > 0) {
	    			sb.append("&");
	    		}	
	    		
	    		sb.append(key + "=" + value);
	    		count++;
				
			} catch (UnsupportedEncodingException unsupportedEncodingException) {
			}     		
		}
    	return sb.toString();
    }	
	
	/**
	 * Creates Url Encoded String from key/value
	 * 
	 * @param unencodedKey
	 * @param unencodedValue
	 * @return
	 */
	public static String createUrlKeyValue(String unencodedKey, String unencodedValue) {
    	StringBuilder sb = new StringBuilder();

		try {
			sb.append(URLEncoder.encode(unencodedKey, "UTF-8"));
			sb.append("=");
			sb.append(URLEncoder.encode(unencodedValue, "UTF-8"));
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
		}     		
	
    	return sb.toString();
    }		
	
	/**
	 * @param urlEncodedString
	 * @return
	 */
	public static Map<String, String> parseUrlParameters(String urlEncodedString) {
		 
		Map<String, String> map = new HashMap<String, String>();
		if (urlEncodedString != null) {
			String[] params = urlEncodedString.split("&");
			
			for (String param : params) {
				String[] s = param.split("=");
				if (s.length == 2) {
					String name = param.split("=")[0];
					String value = param.split("=")[1];
					
					try {
						name = URLDecoder.decode(name, "UTF-8");
						value = URLDecoder.decode(value, "UTF-8");
						map.put(name, value);
					} catch (UnsupportedEncodingException unsupportedEncodingException) {
					}					
				}
			}
		}
		return map;
	} 		
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	private CommonManager getCommonManager(ApplicationContext applicationContext) {
		return (CommonManager) applicationContext.getBean("commonManager");
	}
	
	public CommonManager getCommonManager() {
		return commonManager;
	}

	public void setCommonManager(CommonManager commonManager) {
		this.commonManager = commonManager;
	}

}

