package com.example.demo.services.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * String Utility Class This is used to encode passwords programmatically
 * <p>
 * <a h ref="StringUtil.java.html"> <i>View Source </i> </a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible </a>
 */
public class StringUtil {
    //~ Static fields/initializers =============================================



    public static final char MIDNAME_CHAR_HYPHEN = '-';

    public static final char MIDNAME_CHAR_WHITESPACE = ' ';
    
    public static final char MIDNAME_CHAR_PERIOD = '.';

    public static final String NON_ALPHANUMERIC = "[^0-9A-Za-z]";

    public static final String ALPHANUMERIC_ONLY = "[a-z0-9]*";

    public static String CHARSET_HEX = "0123456789abcdef";
    
    public static String CHARSET_NUM = "0123456789";

    public static String CHARSET_ALPHALOWER = "abcdefghijklmnopqrstuvwxyz";

    public static String CHARSET_ALPHAUPPER = CHARSET_ALPHALOWER.toUpperCase();
    
    public static String CHARSET_ALPHA = CHARSET_ALPHAUPPER + CHARSET_ALPHALOWER;
    
    public static String CHARSET_NAME = StringUtil.CHARSET_ALPHA + StringUtil.MIDNAME_CHAR_HYPHEN + StringUtil.MIDNAME_CHAR_PERIOD + StringUtil.MIDNAME_CHAR_WHITESPACE + "'";
    
    public static String CHARSET_ALPHA_NUMERIC_NO_VOWELS = "0123456789BCDFGHJKLMNPQRSTWXZ";

    public static String CHARSET_ALPHANUM = CHARSET_ALPHALOWER
            + CHARSET_ALPHAUPPER + CHARSET_NUM;    
    
    //~ Methods ================================================================



	/** returns a string of exactly len characters,
	 * truncating s or padding on left with zeros */
	public static String fixedWidthZeros(String s, int len) {
		if (s == null) {
			s = "";
		}
		if (s.length() > len) {
			s = s.substring(0, len);
		}
		while (s.length() < len) {
			s = "0" + s;
		}
		return s;
	}

	/** convert a double to BCD 123456.78 -> '12345678F'
	 * fieldWidth is the width of the database field, so there are actually fieldWidth*2 -1 digits.
	 * val can only have two decimal places */
	public static String doubleToBcd(double val, int fieldWidth) {
	    String signSuffix = "F";
	    if( val < 0 ) {
	        signSuffix = "D";
	        val = -val;
	    }
	    String ret = fixedWidthZeros("" + (int)(val * 100), fieldWidth*2 - 1) + signSuffix;
	    return ret;
	}

//    /**
//     * Encode a string using Base64 encoding. Used when storing passwords as cookies. This is weak
//     * encoding in that anyone can use the decodeString routine to reverse the encoding.
//     *
//     * @param str
//     * @return String
//     */
//    public static String encodeString(String str) {
//        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//        return encoder.encodeBuffer(str.getBytes()).trim();
//    }
//
//    /**
//     * Decode a string using Base64 encoding.
//     *
//     * @param str
//     * @return String
//     */
//    public static String decodeString(String str) {
//        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
//        try {
//            return new String(dec.decodeBuffer(str));
//        } catch (IOException io) {
//            throw new RuntimeException(io.getMessage(), io.getCause());
//        }
//    }

    public static boolean isEmpty(String str) {
        return (null == str) || (0 == str.length());
    }

    public static boolean isBlank(String str) {
        return (null == str) || (0 == str.trim().length());
    }

    /**
     * Returns null if the string is empty
     *
     * @param str
     * @return
     */
    public static String nullString(String str) {
        if (isEmpty(str))
            return null;
        return str;
    }

    public static boolean isEmpty(Character str) {
        return (null == str);
    }

    /**
     * lef-pad a number with 0's to length length
     */
    public static String padNumber(Integer num, int length) {
        return padNumber(num.toString(), length);
    }

    /**
     * lef-pad a number with 0's to length length
     */
    public static String padNumber(String inum, int length) {
        while (inum.length() < length) {
            inum = "0" + inum;
        }
        return inum;
    }

    public static String rpadStringWithSpaces(String istr, int length) {
        while (istr.length() < length) {
            istr = istr + " ";
        }
        return istr;
    }

    /**
     * @param toFormat -
     *            name part to format
     * @return formatted name part
     */
    public static String initcapFormat(String toFormat) {

        // characters likely to be found in the middle of a name
        char[] delimiters = { MIDNAME_CHAR_HYPHEN, MIDNAME_CHAR_WHITESPACE };

        // trim trailing/leading whitespace before formatting
        String formatted = toFormat != null ? toFormat.trim() : "";

        // capitalize first letter, lower case all others, but also capitalize any initial
        // character following any character in the delimiter list above.
        return WordUtils.capitalizeFully(StringUtils.capitalize(formatted), delimiters);
    }

    /**
     * Parse string, inserting the specified string at the specified character interval.
     * @param  int character interval
     * @param  String - string to parse
     * @param  String - string to insert
     * @return String - string with string fragment inserted at the specified interval
     * @author TWuolle
     *
     */
    public static String insertStringAtInterval(int interval, String source,
            String fragment) {

        StringBuffer buf = new StringBuffer();
        char[] chars = source.toCharArray();

        for (int j = 0; j < chars.length; j++) {

            if (j <= 0) {

                buf.append(chars[j]);

            } else if ((j % interval) == 0) {

                buf.append(fragment);
                buf.append(chars[j]);

            } else {
                buf.append(chars[j]);

            }
        }

        return buf.toString();
    }

    public static String stripToAlphaNumeric(String toStrip){
    	return toStrip.replaceAll(NON_ALPHANUMERIC, "");
    }

//    public static String[] parseCsvString(String s) {
//        try {
//        	return CsvLine.getTokens(s);
//        } catch (ParseException e) {
//        	throw new RuntimeException(e);
//        }
//    }

	public static String arrayToCSV(List<String> inArray){
		Iterator<String> it = inArray.iterator();
		StringBuffer buf = new StringBuffer();
		while(it.hasNext()){
			String str = (String)it.next();
			buf.append(str);
			buf.append(",");
		}
		int lastComma = buf.lastIndexOf(",");
		buf.replace(lastComma, lastComma + 1, "");

		return buf.toString();
	}

	public static String arrayToCSV(String[] inArray){
		if (inArray == null || inArray.length == 0)
			return "";

		StringBuffer buf = new StringBuffer();
		for (int i=0;i<inArray.length;i++) {
			buf.append(inArray[i]);
			buf.append(",");
		}
		int lastComma = buf.lastIndexOf(",");
		buf.replace(lastComma, lastComma + 1, "");

		return buf.toString();
	}

    public static String getUnqualifiedClassName(Class<?> clazz){
        String name = clazz.getName();
        int lastIndex = name.lastIndexOf('.');
        if(lastIndex>0){
            return name.substring(lastIndex + 1);
        }
        return name;
    }

	/**
	 * Convert a binary-coded decimal number, represented as a string of hexadecimal digits,
	 * into a double value. For example "42C" yields 42.0, and "42D" yields -42.0.
	 *
	 * The special case of the string being zero or more EBCDIC spaces (i.e. the hex bytes are "40",
	 * which is not a valid BCD sequence) is interpreted as a value of zero, because some OBTS tables
	 * appear to use spaces for an absent BCD value (CQ defect 4418).
	 *
	 * A RunTimeException is thrown if the string is not a valid BCD number or all blanks
	 *
	 * @param hex - the BCD number as a hex string
	 * @return - the double value
	 */
	static public double BCDHexToDouble(String hex) {
		if (hex.matches("^(40)*$")) return 0; // all EBCDIC spaces map to zero
		double x;
		String s = hex.substring(0, hex.length() - 1);
		try {
		    x = Double.parseDouble(s);
		}
		catch(NumberFormatException e) {
		    throw new NumberFormatException("Bad BCD string: " + hex);
		    // return 0;
		}

		final char signNibble = hex.substring(hex.length() - 1, hex.length()).charAt(0);
		switch (signNibble) {
		    case 'D': return -x; // negative value
		    case 'C': case 'F': return x; // positive or unsigned value
		    default: {
		        throw new NumberFormatException("Bad BCD string, invalid sign: " + hex);
		        // return 0;
		    }
		}
	}

	/**
	 * Equivalent to BCDHexToDouble, except the result is scaled to be a
	 * dollars and cents value.
	 */
	static public double BCDHexToMoney(String hex) {
		return BCDHexToDouble(hex)/100.0;
	}

    public static String randString(int minLen, int maxLen) {
        String name = randString(minLen, maxLen, CHARSET_ALPHANUM);
        return name;
    }	
	
    public static String randString(int minLen, int maxLen, String charSet) {
        int len = randInt(minLen, maxLen);
        String s = "";
        for (int i = 0; i < len; i++) {
            s += charSet.charAt(new Random().nextInt(charSet.length()));
        }
        return s;
    }
    
    public static Random getRandom() {
        return new Random();
    }    
	
    /** Returns a random integer between min and max (inclusive) */
    public static int randInt(int min, int max) {
        return (int) randLong(min, max);
    }

    /** Returns a random long between min and max (inclusive) */
    public static long randLong(long min, long max) {

        long number = (Math.abs(getRandom().nextLong()) % (max - min + 1))
                + min;
        return number;
    }    
    
    //-------------------------------------------------------------------------
    /**
	 * @return true if input is alphanumeric
	 */

    public static boolean isAlphaNumeric (String s) {
    	Pattern p = Pattern.compile(ALPHANUMERIC_ONLY, Pattern.CASE_INSENSITIVE);
    	Matcher m = p.matcher(s);
    	boolean b = m.matches();
    	//System.err.println("s= " + s + " a-z0-9= " + b);
    	return b;
    }

    /**
     * Changes a string array into a string with a delimiter.
     * @param array
     * @param delimiter Default is a space.
     * @return
     */
    public static String arrayToString (String[] array, String delimiter) {
    	String retVal = "";

    	if (array == null) {
    		return "";
    	}

    	for (int x = 0; x < array.length; x++) {
    		retVal = retVal + array[x];
    		if (x+1 < array.length && !isEmpty(delimiter)) {
    			retVal = retVal + ",";
    		} else {
    			retVal = retVal + " ";
    		}
    	}
    	return retVal;
    }


    /**********************************************************************
     * 	HEX, String, and Character conversion / translation
     **********************************************************************/


    /**
     * Convert lowest Nibble of integer to a HEX char.
     *
     * @param nib - integer with nibble to convert to HEX char.
     * @return char - HEX character.
     */
    public static char encodeHexNibble(int nib) {
		final char[] hexDigit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	   	return hexDigit[nib & 0x000F];
    }


    /**
     * Convert a HEX char to Nibble value.
     *
     * @param chr HEX character to convert to nibble value (case insensitive).
     * @return int - value of HEX Nibble, -1 if invalid HEX character.
     */
    public static int decodeHexNibble(char chr) {
	   	final String hexDigit = "0123456789ABCDEFabcdef";
	   	int ndx = hexDigit.indexOf(chr);
	   	if (ndx > 0x0F) ndx -= 6;
	   	return ndx;
    }


    /**
     * Convert a string to a HEX String representation.  Encode lower unicode byte only.
     *
     * @param str string to convert to HEX
     * @return String - HEX encoding
     */
    public static String encodeHexString(String str) {
	   	char[] charBuf = str.toCharArray();
	   	StringBuffer strBuf = new StringBuffer(str.length() * 2);
	   	for (int i = 0; i < str.length(); i++) {
	   		int foo = charBuf[i];
	   		strBuf.append(encodeHexNibble((foo >> 4)));
	   		strBuf.append(encodeHexNibble(foo));
	   	}
	   	return strBuf.toString();
    }


    /**
     * Convert a string representation of Binary to a HEX String. eg. "10101110" => "AE"
     *
     * @param str string to convert to HEX
     * @return String - HEX encoding
     */
    public static String encodeBinStrToHexStr(String bStr) {
	  	bStr = bStr.replaceAll("[^01]","");		// leave only binary digits
	  	char[] charBuf = bStr.toCharArray();
	  	StringBuffer strBuf = new StringBuffer((bStr.length()+3) / 4);
	  	int foo = 0;
	  	for (int i = 0; i < bStr.length(); ) {
	  		foo *= 2;
	  		foo += charBuf[i++] & 0x01;
	  		if (i%4 == 0) strBuf.append(encodeHexNibble(foo));
	  	}
	  	return strBuf.toString();
    }


    /**
     * Convert a HEX String to character String. eg. "30313233" => "0123"
     *
     * @param str HEX String to decode.
     * @return String - decoding result, null if invalid input.
     */
    public static String decodeHexString(String str) {
    	if (str.length()%2 != 0) return null;
    	int resLen = str.length()/2;
    	char[] charBuf = str.toCharArray();
    	StringBuffer strBuf = new StringBuffer(resLen);
    	for (int i = 0, j=0; i < resLen; i++) {
    		int foo = decodeHexNibble(charBuf[j++]) << 4;
    		foo += decodeHexNibble(charBuf[j++]);
    		strBuf.append((char)foo);
    	}
    	return strBuf.toString();
    }

    /**
     * Translate character set of a HEX String. eg. "30313233" => "F0F1F2F3"
     *
     * @param str HEX String to translate.
     * @param transTable A translation table of 256 chars to re-map characters. ie. EBCDIC to ASCII
     * @return String - translation result in HEX, null if invalid input.
     */
    public static String translateHexString(String str, char[] transTable) {
	   	if (transTable == null) return str;		// no translation
	   	if (str.length()%2 != 0) return null;
	   	char[] charBuf = str.toCharArray();
	   	StringBuffer strBuf = new StringBuffer(str.length());
	   	for (int i = 0; i < str.length(); i++) {
	   		int foo = decodeHexNibble(charBuf[i++]) << 4;
	   		foo += decodeHexNibble(charBuf[i]);
	   		foo = transTable[foo];
	   		strBuf.append(encodeHexNibble((foo >> 4)));
	   		strBuf.append(encodeHexNibble(foo));
	   	}
	   	return strBuf.toString();
    }

    /**
     * Translate character set of a String.
     *
     * @param str String to translate.
     * @param transTable A translation table of 256 chars to re-map characters. ie. EBCDIC to ASCII
     * @return String - translation result.
     */
    public static String translateString(String str, char[] transTable) {
	   	if (transTable == null) return str;		// no translation
	  	char[] charBuf = str.toCharArray();
	  	StringBuffer strBuf = new StringBuffer(str.length());
	  	for (int i = 0; i < str.length(); i++) {
	  		strBuf.append(transTable[charBuf[i]]);
	  	}
	  	return strBuf.toString();
    }

    /**
     * Translate character set of a 'char'.
     *
     * @param chr character to translate.
     * @param transTable A translation table of 256 chars to re-map characters. ie. EBCDIC to ASCII
     * @return char - translation result.
     */
    public static char translateChar(char chr, char[] transTable) {
    	return transTable[chr];
    }



    public static final char[] EBCDICtoASCII = {
//		 0      1      2      3      4      5      6      7      8      9      A      B      C      D      E      F
		0x00,  0x01,  0x02,  0x03,  0x00,  0x09,  0x00,  0x7F,  0x00,  0x00,  0x00,  0x0B,  0x0C,  0x0D,  0x0E,  0x0F,	// 0
		0x10,  0x11,  0x12,  0x13,  0x00,  0x00,  0x08,  0x00,  0x18,  0x19,  0x00,  0x00,  0x1C,  0x1D,  0x1E,  0x1F,	// 1
		0x00,  0x00,  0x00,  0x00,  0x00,  0x0A,  0x17,  0x1B,  0x00,  0x00,  0x00,  0x00,  0x00,  0x05,  0x06,  0x07,	// 2
		0x00,  0x00,  0x16,  0x00,  0x00,  0x00,  0x00,  0x04,  0x00,  0x00,  0x00,  0x00,  0x14,  0x15,  0x00,  0x1A,	// 3
		0x20,  0xA0,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x2E,  0x3C,  0x28,  0x2B,  0x7C,	// 4
		0x26,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x21,  0x24,  0x2A,  0x29,  0x3B,  0xAC,	// 5
		0x2D,  0x2F,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0xA6,  0x2C,  0x25,  0x5F,  0x3E,  0x3F,	// 6
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x60,  0x3A,  0x23,  0x40,  0x27,  0x3D,  0x22,	// 7
		0x00,  0x61,  0x62,  0x63,  0x64,  0x65,  0x66,  0x67,  0x68,  0x69,  0x00,  0x00,  0x00,  0x00,  0x00,  0xB1,	// 8
		0x00,  0x6A,  0x6B,  0x6C,  0x6D,  0x6E,  0x6F,  0x70,  0x71,  0x72,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// 9
		0x00,  0x7E,  0x73,  0x74,  0x75,  0x76,  0x77,  0x78,  0x79,  0x7A,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// A
		0x5E,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x5B,  0x5D,  0x00,  0x00,  0x00,  0x00,	// B
		0x7B,  0x41,  0x42,  0x43,  0x44,  0x45,  0x46,  0x47,  0x48,  0x49,  0xAD,  0x00,  0x00,  0x00,  0x00,  0x00,	// C
		0x7D,  0x4A,  0x4B,  0x4C,  0x4D,  0x4E,  0x4F,  0x50,  0x51,  0x52,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// D
		0x5C,  0x00,  0x53,  0x54,  0x55,  0x56,  0x57,  0x58,  0x59,  0x5A,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// E
		0x30,  0x31,  0x32,  0x33,  0x34,  0x35,  0x36,  0x37,  0x38,  0x39,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00	// F
    };


    public static final char[] ASCIItoEBCDIC = {
//		 0      1      2      3      4      5      6      7      8      9      A      B      C      D      E      F
		0x00,  0x01,  0x02,  0x03,  0x37,  0x2D,  0x2E,  0x2F,  0x16,  0x05,  0x25,  0x0B,  0x0C,  0x0D,  0x0E,  0x0F,	// 0
		0x10,  0x11,  0x12,  0x13,  0x3C,  0x3D,  0x32,  0x26,  0x18,  0x19,  0x3F,  0x27,  0x1C,  0x1D,  0x1E,  0x1F,	// 1
		0x40,  0x5A,  0x7F,  0x7B,  0x5B,  0x6C,  0x50,  0x7D,  0x4D,  0x5D,  0x5C,  0x4E,  0x6B,  0x60,  0x4B,  0x61,	// 2
		0xF0,  0xF1,  0xF2,  0xF3,  0xF4,  0xF5,  0xF6,  0xF7,  0xF8,  0xF9,  0x7A,  0x5E,  0x4C,  0x7E,  0x6E,  0x6F,	// 3
		0x7C,  0xC1,  0xC2,  0xC3,  0xC4,  0xC5,  0xC6,  0xC7,  0xC8,  0xC9,  0xD1,  0xD2,  0xD3,  0xD4,  0xD5,  0xD6,	// 4
		0xD7,  0xD8,  0xD9,  0xE2,  0xE3,  0xE4,  0xE5,  0xE6,  0xE7,  0xE8,  0xE9,  0xBA,  0xE0,  0xBB,  0xB0,  0x6D,	// 5
		0x79,  0x81,  0x82,  0x83,  0x84,  0x85,  0x86,  0x87,  0x88,  0x89,  0x91,  0x92,  0x93,  0x94,  0x95,  0x96,	// 6
		0x97,  0x98,  0x99,  0xA2,  0xA3,  0xA4,  0xA5,  0xA6,  0xA7,  0xA8,  0xA9,  0xC0,  0x4F,  0xD0,  0xA1,  0x07,	// 7
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// 8
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// 9
		0x41,  0x00,  0x00,  0x00,  0x00,  0x00,  0x6A,  0x00,  0x00,  0x00,  0x00,  0x00,  0x5F,  0xCA,  0x00,  0x00,	// A
		0x00,  0x8F,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// B
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// C
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// D
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,	// E
		0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00,  0x00	// F
    };

}

