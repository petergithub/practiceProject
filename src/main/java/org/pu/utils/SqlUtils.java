package org.pu.utils;

import java.util.List;

/**
 * @author Shang Pu
 * @version Date: Feb 7, 2013 9:20:58 PM
 */
public class SqlUtils {
	public static final String WILD_CHAR = "%";
	public static final String STRING_CHAR = "'";
	public static final String STRING_CHAR_ESCAPE = "''";

	/**
	 * escape ' to '', _ to \\_, % to \\%
	 */
	public static String sqlLikeReplaceOra(String s) {
		return s.replaceAll("'", "''").replaceAll("_", "\\_").replaceAll("%", "\\%");
	}

	/**
	 * @return 'dms' if str is dms
	 */
	public static String singleQuote(String str) {
		return STRING_CHAR + str + STRING_CHAR;
	}

	/**
	 * quote str and escape quote in it
	 * 
	 * @return 'dms''home' if str is dms'home
	 */
	public static String quote(String str) {
		String strTrim = "";
		if (StringUtils.isNotEmpty(str)) strTrim = str.trim();
		return singleQuote(escapeQuote(strTrim));
	}

	/**
	 * append wild char % and apostrophe around char criteria
	 * 
	 * @param str not null
	 * @return '%dms%' if criteria is dms
	 */
	public static String quoteWildCriteria(String str) {
		String strTrim = WILD_CHAR;

		if (StringUtils.isNotEmpty(str)) {
			strTrim = WILD_CHAR + str.trim().toLowerCase() + WILD_CHAR;
		}
		return singleQuote(strTrim);
	}

	/**
	 * Duplicates each single quote in String <B>str</B>, making the result suitable for use as a
	 * quoted literal in a SQL query.
	 * 
	 * @return jerry''s home if criteria is jerry's home
	 */
	public static String escapeQuote(String str) {
		if (StringUtils.isEmpty(str)) return "";
		return str.trim().replaceAll(STRING_CHAR, STRING_CHAR_ESCAPE);
	}

	/**
	 * form in list in sql. return ('a', 'b', 'c')
	 */
	public static String formInArray(List<String> values) {
		if (values == null || values.size() == 0) return "";

		int startPos = 0;
		StringBuffer sb = new StringBuffer("(");
		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			if (StringUtils.isEmpty(value)) {
				continue;
			} else {
				if (startPos != 0) sb.append(", ");
				startPos++;
			}
			sb.append(quote(value.trim()));
		}
		String result = sb.append(')').toString();
		return (result.equals("()") ? "" : result);
	}

	/**
	 * form number in array in sql return (1, 2, 3)
	 */
	public static String formInArrayNumber(String[] values) {
		if (values == null || values.length == 0) return "";

		int startPos = 0;
		StringBuffer sb = new StringBuffer("(");
		for (int i = 0; i < values.length; i++) {
			if (StringUtils.isEmpty(values[i])) {
				continue;
			} else {
				if (startPos != 0) sb.append(", ");
				startPos++;
			}
			sb.append(values[i].trim());
		}
		String result = sb.append(')').toString();
		return (result.equals("()") ? "" : result);
	}
}
