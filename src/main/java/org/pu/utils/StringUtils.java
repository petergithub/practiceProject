package org.pu.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper Class to provide functions to perform String Manipulations.
 */
public class StringUtils {
	private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

	public static final char SURROGATE_MIN = 55296;
	public static final char SURROGATE_MAX = 57343;

	/**
	 * @return true if val == null || val.trim().length() == 0
	 */
	public static boolean isEmpty(String val) {
		return val == null || val.trim().length() == 0;
	}

	/**
	 * @return true if val != null && val.trim().length() > 0
	 */
	public static boolean isNotEmpty(String val) {
		return val != null && val.trim().length() > 0;
	}

	/**
	 * Helper Method for generation of Padded String from given number and length.
	 * 
	 * @param paddinglength (String) - is the padding length
	 * @param next_seq (String) - is the next Available Sequence
	 * @return String -returns the padded String
	 */
	public static String createPadding(String paddinglength, String next_seq) {
		String auto_number = "";
		String padding_str = "";
		int padLength = Integer.parseInt(paddinglength);
		if (next_seq.length() <= padLength) {
			int length = padLength - next_seq.length();
			for (int i = 0; i < length; i++) {
				padding_str = padding_str + "0";
			}
		}
		auto_number = padding_str + next_seq;
		return auto_number;
	}

	public static String createPadding(int paddingLength, int seq) {
		return createPadding(Integer.toString(paddingLength), Integer.toString(seq));
	}

	/**
	 * @return true if string is in the list
	 */
	public static boolean stringInList(List<String> list, String str) {
		for (int i = 0; i < list.size(); i++) {
			String strList = list.get(i);
			if (strList.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * concatenate 2 arrays of type String (replace the String in the code with your class name)
	 */
	public static String[] concatArrays(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	/**
	 * convert a String array to a string with the delimiter
	 * 
	 * @param array String
	 * @param delimiter
	 * @return
	 */
	public static String convertArrayToString(String array[], String delimiter) {
		if (array == null || array.length == 0) return null;
		String csString = null;
		if (array.length == 1) {
			csString = array[0];
		} else {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				buf.append(delimiter);
				buf.append(array[i]);
			}
			csString = buf.substring(1);
		}
		return csString;
	}

	/**
	 * convert a list to a string with the delimiter
	 * 
	 * @param list
	 * @param delimiter
	 * @return
	 */
	public static String convertListToString(List<String> list, String delimiter) {
		StringBuffer sb = new StringBuffer();
		if (list != null) {
			int size = list.size();
			if (size > 0) {
				for (int i = 0; i < size - 1; i++) {
					sb.append(list.get(i));
					sb.append(delimiter);
				}
				sb.append(list.get(size - 1));
			}
		}
		return sb.toString();
	}

	public static boolean isLong(String val) {
		try {
			Long.parseLong(val);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * truncate value if its length longer than maxLength
	 */
	public static String truncateValue(String value, int maxLength) {
		if (value != null && value.length() > maxLength) {
			log.warn("long value = {}", value);
			value = value.substring(0, maxLength);
		}
		log.debug("Exit truncateValue() value = {}", value);
		return value;
	}

	/**
	 * convert decimal to heximal
	 * 
	 * @param decString
	 * @return heximal value of decimal string
	 */
	public static String dec2hex(String decString) {
		if (isEmpty(decString)) {
			throw new IllegalArgumentException("NULL string[" + decString + "]");
		}
		long value = -1;

		try {
			value = Long.parseLong(decString.trim());
		} catch (NumberFormatException e) {
			log.warn("not valid long string[" + decString + "]");
			throw e;
		}

		String hexId = Long.toHexString(value);

		if (log.isDebugEnabled()) {
			log.debug("hex value[" + hexId + "] of dec[" + decString + "]");
		}
		return hexId;
	}

	public String toHex(String arg) {
		return String.format("%040x", new BigInteger(arg.getBytes()));
	}

	/**
	 * @return the index of string in the array
	 */
	public static int getArrayIndex(String s, String[] array) {
		if (array == null || array.length == 0) {
			return -1;
		}
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(s)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static String escapeXml(String s) {
		StringBuffer str = new StringBuffer();
		int len = (s != null) ? s.length() : 0;
		for (int i = 0; i < len; i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case '<':
					str.append("&lt;");
					break;
				case '>':
					str.append("&gt;");
					break;
				case '&':
					str.append("&amp;");
					break;
				case '"':
					str.append("&quot;");
					break;
				case '\'':
					str.append("&apos;");
					break;
				default:
					str.append(ch);
			}
		}
		return str.toString();
	}

	public static String decodeXml(String toReplace) {
		return toReplace.replaceAll("&amp;", "&").replaceAll("&apos;", "'").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
	}

	public static String escapeHtmlTags(String URL) {
		return URL.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		// URL = escapeChar(URL, '&', "&amp;");
		// URL = escapeChar(URL, '<', "&lt;");
		// URL = escapeChar(URL, '>', "&gt;");
		// return URL;
	}

	public static String escapeChar(String str, char c, String replaceVal) {
		int i = str.indexOf(c);
		while (i >= 0) {
			str = replace(str, i, i + 1, replaceVal);
			i = str.indexOf(c, i + replaceVal.length());
		}
		return str;
	}

	/**
	 * @param url
	 * @param encode
	 * @return null if fails
	 * @throws UnsupportedEncodingException
	 */
	public static String escapeUrl(String url, String encode) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, encode).replace("+", "%20").replace("*", "%2A")
				.replace("%7E", "~");
	}

	public static String replace(String str, int beginPos, int endPos, String replaceStr) {
		if ((str == null) || (beginPos > endPos) || (endPos > str.length()) || (beginPos < 0))
			return str;
		String retValue = str.substring(0, beginPos);
		if (replaceStr != null) retValue = retValue + replaceStr;
		retValue = retValue + str.substring(endPos);
		return retValue;
	}

	/**
	 * escape char one by one from listCharFind to listCharReplace
	 * @param input
	 * @param listCharFind
	 * @param listCharReplace
	 * @return new string
	 */
	public static String replaceCharByAnotherChar(String input, String listCharFind,
			String listCharReplace) {
		StringBuffer result = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		int iLength = listCharFind.length();
		for (int i = 0; i < iLength; i++)
			map.put(listCharFind.substring(i, i + 1), listCharReplace.substring(i, i + 1));

		iLength = input.length();
		for (int i = 0; i < iLength; i++) {
			String character = input.substring(i, i + 1);
			if (map.containsKey(character))
				result.append(map.get(character));
			else
				result.append(character);
		}

		return result.toString();
	}

	public static String supplement(int value, char supp, int num) {
		String v = String.valueOf(value);
		if (v.length() >= num) {
			return v;
		}

		StringBuffer buf = new StringBuffer(v);
		while (buf.length() < num) {
			buf.insert(0, supp);
		}
		return buf.toString();
	}

	/**
	 * make the first character of words capitalize
	 * 
	 * @see org.apache.commons.lang3.text.WordUtils.capitalizeFully(String)
	 */
	public static String capitalizeWord(String string, String delimiter) {
		String[] array = string.split(delimiter);
		for (int i = 0; i < array.length; i++) {
			if (array[i].length() <= 1) {
				array[i] = array[i].toUpperCase();
			} else {
				array[i] = array[i].substring(0, 1).toUpperCase() + array[i].substring(1).toLowerCase();
			}
		}
		return convertArrayToString(array, delimiter);
	}

	/**
	 * converts given byte array to a hex string
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) buffer.append('0');
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}

	/**
	 * converts given hex string to a byte array (ex: "0D0A" => {0x0D, 0x0A,})
	 */
	public static final byte[] hexStringToByteArray(String str) {
		int i = 0;
		byte[] results = new byte[str.length() / 2];
		for (int k = 0; k < str.length();) {
			results[i] = (byte) (Character.digit(str.charAt(k++), 16) << 4);
			results[i] += (byte) (Character.digit(str.charAt(k++), 16));
			i++;
		}
		return results;
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains(".")) return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * override Collection.toString
	 */
	public static String toString(Collection<?> coll) {
		StringBuffer sb = new StringBuffer("{");

		if (coll != null) {
			for (Iterator<?> iter = coll.iterator(); iter.hasNext();) {
				sb.append('[').append(iter.next()).append(']');

				if (iter.hasNext()) sb.append(", ");
			}
		}
		sb.append('}');

		return sb.toString();
	}

	public static boolean isSurrogate(int c) {
		return 55296 <= c && c <= 57343;
	}

	public static int calculateMaximumAllowedChars(String value, int maximumAllowedBytes) {
		if (value == null) return 0;
		int lengthInBytes = 0;
		int lengthInChars = 0;
		int i = 0;
		int limit = value.length();
		do {
			if (i >= limit) break;
			int charNum = value.charAt(i);
			if (charNum <= 127)
				lengthInBytes++;
			else if (charNum <= 2047)
				lengthInBytes += 2;
			else
				lengthInBytes += 3;
			if (lengthInBytes > maximumAllowedBytes) break;
			lengthInChars++;
			i++;
		} while (true);
		return lengthInChars;
	}

	public static int calculateEncodedLength(String value) {
		if (value == null) return 0;
		int lengthInBytes = 0;
		int i = 0;
		for (int limit = value.length(); i < limit; i++) {
			int charNum = value.charAt(i);
			if (charNum <= 127) {
				lengthInBytes++;
				continue;
			}
			if (charNum <= 2047) {
				lengthInBytes += 2;
				continue;
			}
			if (isSurrogate(charNum))
				lengthInBytes += 2;
			else
				lengthInBytes += 3;
		}

		return lengthInBytes;
	}

	/**
	 * @deprecated used String.split() is better This method is used to convert an String to a List
	 *             based on the delimiter
	 * @param str - String to be converted in List
	 * @param delimeter - delimiter
	 */
	public static List<String> splitString(String str, String delimeter) {
		List<String> resultList = new ArrayList<String>();
		String strToken = "";
		// Get String Tokenizer
		StringTokenizer strTokenizer = new StringTokenizer(str, delimeter);
		// Store Tokens in a List
		while (strTokenizer.hasMoreTokens()) {
			strToken = strTokenizer.nextToken().trim();
			if (strToken.length() > 0) {
				resultList.add(strToken);
			}
		}
		return resultList;
	}
}
