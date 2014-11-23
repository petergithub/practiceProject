package org.pu.utils;

public class Constants {
	public static final String EOL = System.getProperty("line.separator");
	public static final int EOF = -1;
	public static final String userdir = System.getProperties().getProperty("user.dir");

	/**
	 * \{Dropbox}\base\testProject\bin\
	 */
	public static final String classpath = Constants.class.getResource("/").getPath();
	
	/** Value delimiter: � */
  public static final String VALUE_DELIMITER                    = "�";
}
