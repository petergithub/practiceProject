package org.pu.utils;

import static org.pu.utils.Regex.HTML_TAG;
import static org.pu.utils.Regex.SCRIPT;
import static org.pu.utils.Regex.STYLE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shang Pu
 * @version Date: Sep 28, 2012 4:55:20 PM
 */
public class HtmlUtils {
	
	/** 
	 * %2F /
	 * %2B +
	 * %3D =
	 * 
	 */
    /*
        Filename Reserved Characters
           '!', '/', '\', '$',
           '&', '*', ':', '<', 
           '>', '|', '?', '"',
           '^', ''', '#', '~'
           
        Reserved Characters
           '$', '&', '+', ',',
           '/', ':', ';', '=',
           '?', '@', '!', '*'
           
        Unsafe Characters: 
           ' ', ''', '<', '>',
           '#', '%', '{', '}',
           '|', '\', '^', '~',
           '[', ']', '`'
    */    
    
    private static final char[] FILE_NAME_RESERVED_CHARACTERS = 
    {   0x21, 0x2F, 0x5C, 0x24,
    	0x26, 0x2A, 0x3A, 0x3C, 
    	0x3E, 0x7C, 0x3F, 0x22,
    	0x5E, 0x27, 0x23, 0x7E    	
    };

    private static final String[] FILE_NAME_RESERVED_CHARACTER_REPLACING_STRINGS = 
    {   "%21","%2F","%5C","%24",  
        "%26","%2A","%3A","%3C",
        "%3E","%7C","%3F","%22",
        "%5E","%27","%23","7E"
    }; 
    
    private static final char[] UNSAFE_CHARACTERS = 
        {   0x20, 0x22, 0x3C, 0x3E,
            0x23, 0x25, 0x7B, 0x7D,
            0x7C, 0x5C, 0x5E, 0x7E,
            0x5B, 0x5D, 0x60
        };
    
    private static final String[] UNSAFE_CHARACTER_REPLACING_STRINGS = 
        {   "%20","%22","%3C","%3E",  
            "%23","%25","%7B","%7D",
            "%7C","%5C","%5E","%7E",
            "%5B","%5D","%60"
        };
    
    private static final char[] RESERVED_CHARACTERS = 
        {   0x24, 0x26, 0x2B, 0x2C,
            0x2F, 0x3A, 0x3B, 0x3D,
            0x3F, 0x40, 0x21, 0x2A
        };
    
    private static final String[] RESERVED_CHARACTER_REPLACING_STRINGS = 
        {   "%24","%26","%2B","%2C",  
            "%2F","%3A","%3B","%3D",
            "%3F","%40","%21","%2A"
        };

	private static final String UNSAFE = "unsafe";
	private static final String RESERVED = "reserved";
	private static final String FILENAME = "filename";

	private static Map<String, char[]> encodingCharArrayMap = new HashMap<String, char[]>(
			2);
	static {
		encodingCharArrayMap.put(UNSAFE, UNSAFE_CHARACTERS);
		encodingCharArrayMap.put(RESERVED, RESERVED_CHARACTERS);
		encodingCharArrayMap.put(FILENAME, FILE_NAME_RESERVED_CHARACTERS);
	}

	private static Map<String, String[]> replacingStringArrayMap = new HashMap<String, String[]>(
			2);
	static {
		replacingStringArrayMap.put(UNSAFE, UNSAFE_CHARACTER_REPLACING_STRINGS);
		replacingStringArrayMap.put(RESERVED,
				RESERVED_CHARACTER_REPLACING_STRINGS);
		replacingStringArrayMap.put(FILENAME,
				FILE_NAME_RESERVED_CHARACTER_REPLACING_STRINGS);
	}

	private static String iEncodeCharacters(String str, String encodingType) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}

		char[] charArray = (char[]) encodingCharArrayMap.get(encodingType);
		String[] replacingStringArray = (String[]) replacingStringArrayMap
				.get(encodingType);

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int position = getCharPosition(c, charArray);

			// If not in the char array, just append it
			if (position < 0) {
				buffer.append(c);
			} else {
				buffer.append(replacingStringArray[position]);
			}
		}
		return buffer.toString();
	}

	private static int getCharPosition(char charToCheck, char[] charArray) {
		int index = -1;
		for (int i = 0; i < charArray.length; i++) {
			if (charToCheck == charArray[i]) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static String encodeStringForUnsafeCharacters(String toBeEncodedStr) {
		return iEncodeCharacters(toBeEncodedStr, UNSAFE);
	}

	public static String encodeStringForReservedCharacters(String toBeEncodedStr) {
		return iEncodeCharacters(toBeEncodedStr, RESERVED);
	}

	public static String encodeString(String toBeEncodedStr) {
		toBeEncodedStr = iEncodeCharacters(toBeEncodedStr, UNSAFE);
		return iEncodeCharacters(toBeEncodedStr, RESERVED);
	}

	public static String encodeFileName(String toBeEncodedStr) {
		return iEncodeCharacters(toBeEncodedStr, FILENAME);
	}

	public static URL escapeSpaces(URL url) throws MalformedURLException {
		String strUrl = url.toExternalForm();
		if (strUrl.indexOf(' ') == -1) return url;
		int len = strUrl.length();
		StringBuffer buf = new StringBuffer(len + 3);
		for (int i = 0; i < len; i++) {
			char c = strUrl.charAt(i);
			if (c == ' ')
				buf.append("%20");
			else
				buf.append(c);
		}

		return new URL(buf.toString());
	}

  public static String encodeURL(String url) {
    url = url.replaceAll("\\/", "%2F");
    url = url.replaceAll("\\?", "%3F");
    url = url.replaceAll("\\:", "%3A");
    url = url.replaceAll("\\&", "%26");
    url = url.replaceAll("\\=", "%3D");
    url = url.replaceAll("\\#", "%23");
    url = url.replaceAll(" ", "%20");
    return url;
  }

	/**
	 * 移除html标签
	 */
	public static String removeHtmlTag(String content) {
		if (content == null || content.isEmpty()) {
			return "";
		}
		String temp = content;
		// 去除js
		temp = temp.replaceAll(SCRIPT, "");
		// 去除style
		temp = temp.replaceAll(STYLE, "");
		// 去除html
		temp = temp.replaceAll(HTML_TAG, "");
		// 合并空格
		temp = temp.replaceAll("\\s+", " ");

		return temp.trim();
	}
}
