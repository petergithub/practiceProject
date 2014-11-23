package org.pu.bean;

import java.awt.Toolkit;

import javax.swing.UIManager;

public class OS {

	public OS() {
	}

	public static boolean isMacOSX() {
		return osIsMacOsX;
	}

	public static boolean isWindows() {
		return osIsWindows;
	}

	public static boolean isWindowsXP() {
		return osIsWindowsXP;
	}

	public static boolean isWindows2003() {
		return osIsWindows2003;
	}

	public static boolean isWindowsVista() {
		return osIsWindowsVista;
	}

	public static boolean isLinux() {
		return osIsLinux;
	}

	public static boolean isUsingWindowsVisualStyles() {
		if (!isWindows()) return false;
		boolean xpthemeActive = Boolean.TRUE.equals(Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.xpstyle.themeActive"));
		if (!xpthemeActive) return false;
		try {
			return System.getProperty("swing.noxp") == null;
		} catch (RuntimeException e) {
			return true;
		}
	}

	public static String getWindowsVisualStyle() {
		String style = UIManager.getString("win.xpstyle.name");
		if (style == null)
			style = (String) Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.colorName");
		return style;
	}

	private static final boolean osIsMacOsX;
	private static final boolean osIsWindows;
	private static final boolean osIsWindowsXP;
	private static final boolean osIsWindows2003;
	private static final boolean osIsWindowsVista;
	private static final boolean osIsLinux;

	static {
		String os = System.getProperty("os.name").toLowerCase();
		osIsMacOsX = "mac os x".equals(os);
		osIsWindows = os != null && os.indexOf("windows") != -1;
		osIsWindowsXP = "windows xp".equals(os);
		osIsWindows2003 = "windows 2003".equals(os);
		osIsWindowsVista = "windows vista".equals(os);
		osIsLinux = os != null && os.indexOf("linux") != -1;
	}
}
