package demo.web.webutils;

public final class SafeHTMLString {
	public static String escapeText(String strText) {
		String escText = strText;
		if (strText != null && strText.length() > 0) {
			StringBuffer strSafe = new StringBuffer(strText.length() + 16);
			for (int i = 0; i < strText.length(); i++) {
				char ch = strText.charAt(i);
				if (ch == '&') {
					strSafe.append("&amp;");
					continue;
				}
				if (ch == '>') {
					strSafe.append("&gt;");
					continue;
				}
				if (ch == '<') {
					String strTag = strText.substring(i);
					if (strTag.toLowerCase().startsWith("<br>")) {
						strSafe.append("<br>");
						i += 3;
					} else {
						strSafe.append("&lt;");
					}
					continue;
				}
				if (ch == '\r') continue;
				if (ch == '\n') {
					strSafe.append("<br>");
					continue;
				}
				if (ch == '"')
					strSafe.append("&quot;");
				else
					strSafe = escapeMoreChars(ch, strSafe);
			}

			escText = strSafe.toString();
		}
		return escText;
	}

	public static String escapeAttribute(String strAttr) {
		if (strAttr == null || strAttr.length() == 0) return "";
		StringBuffer strSafe = new StringBuffer(strAttr.length() + 16);
		for (int i = 0; i < strAttr.length(); i++) {
			char ch = strAttr.charAt(i);
			if (ch == '&') {
				if (i + 1 < strAttr.length() && strAttr.charAt(i + 1) == '{')
					strSafe.append(ch);
				else
					strSafe.append("&amp;");
				continue;
			}
			if (ch == '>') {
				strSafe.append("&gt;");
				continue;
			}
			if (ch == '<') {
				strSafe.append("&lt;");
				continue;
			}
			if (ch == '"')
				strSafe.append("&#34;");
			else
				strSafe = escapeMoreChars(ch, strSafe);
		}

		return strSafe.toString();
	}

	public static String escapeScriptLiteral(String strText) {
		String strString = strText;
		if (strText != null && strText.length() != 0) {
			StringBuffer strSafe = new StringBuffer(strText.length() + 8);
			int len = strText.length();
			for (int i = 0; i < len; i++) {
				char c = strText.charAt(i);
				switch (c) {
					case 10: // '\n'
						strSafe.append("\\n");
						break;

					case 13: // '\r'
						strSafe.append("\\r");
						break;

					case 9: // '\t'
						strSafe.append("\\t");
						break;

					case 34: // '"'
					case 39: // '\''
					case 92: // '\\'
						strSafe.append('\\').append(c);
						break;

					case 60: // '<'
						strSafe.append(c);
						if (i + 1 < len && strText.charAt(i + 1) == '/') {
							strSafe.append('\\').append('/');
							i++;
						}
						break;

					default:
						strSafe.append(c);
						break;
				}
			}

			strString = strSafe.toString();
		}
		return strString;
	}

	private static StringBuffer escapeMoreChars(char ch, StringBuffer buf) {
		if (ch == '\'')
			buf.append("&#39;");
		else if (ch == '%')
			buf.append("&#37;");
		else if (ch == '(')
			buf.append("&#40;");
		else if (ch == ')')
			buf.append("&#41;");
		else if (ch == '+')
			buf.append("&#43;");
		else if (ch == ';')
			buf.append("&#59;");
		else
			buf.append(ch);
		return buf;
	}

	public static String unescape(String strEscaped) {
		String strUnescaped = unescapeNumEntities(strEscaped);
		return unescapeCharEntities(strUnescaped);
	}

	private static String unescapeCharEntities(String strAttr) {
		if (strAttr == null || strAttr.length() == 0) {
			return "";
		} else {
			String strUnescaped = strAttr;
			strUnescaped = strUnescaped.replaceAll("&amp;", "&");
			strUnescaped = strUnescaped.replaceAll("&lt;", "<");
			strUnescaped = strUnescaped.replaceAll("&gt;", ">");
			strUnescaped = strUnescaped.replaceAll("&quot;", "\"");
			return strUnescaped;
		}
	}

	private static String unescapeNumEntities(String strEscaped) {
		if (strEscaped == null || strEscaped.length() == 0) {
			return "";
		} else {
			String strUnescaped = strEscaped;
			strUnescaped = strUnescaped.replaceAll("&#39;", "'");
			strUnescaped = strUnescaped.replaceAll("&#37;", "%");
			strUnescaped = strUnescaped.replaceAll("&#40;", "(");
			strUnescaped = strUnescaped.replaceAll("&#41;", ")");
			strUnescaped = strUnescaped.replaceAll("&#43;", "+");
			strUnescaped = strUnescaped.replaceAll("&#59;", ";");
			return strUnescaped;
		}
	}
}
