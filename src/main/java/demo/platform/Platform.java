package demo.platform;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get current system and desktop
 */
public class Platform {
	private final static Logger log = LoggerFactory.getLogger(Platform.class);

	private static final String OS_NAME = getSystemProperty("os.name");
	private static final boolean IS_OS_LINUX = getOSMatches("linux");
	private static final boolean IS_OS_MAC = getOSMatches("mac");
	private static final boolean IS_OS_WINDOWS = getOSMatches("windows");
	private Desktop desktop = null;

	public Desktop getDesktop() {
		if (IS_OS_LINUX) {
			desktop = getLinuxDesktop();
		}
		return desktop;
	}

	public OS getOS() {
		if (IS_OS_LINUX) {
			return OS.LINUX;
		}
		if (IS_OS_WINDOWS) {
			return OS.WINDOWS;
		}
		if (IS_OS_MAC) {
			return OS.MAC;
		}
		return OS.UNKNOWN;
	}

	private Desktop getLinuxDesktop() {
		Process process;
		BufferedReader r = null;
		try {
			process = Runtime.getRuntime().exec("ps ax");
			process.waitFor();
			InputStream inputStream = process.getInputStream();

			BufferedInputStream in = new BufferedInputStream(inputStream);
			r = new BufferedReader(new InputStreamReader(in));

			String l = r.readLine();
			while (l != null) {
				if (l.contains("kwin")) {
					return Desktop.KDE;
				}
				if (l.contains("gnome-settings-daemon")) {
					return Desktop.GNOME;
				}
				l = r.readLine();
			}
		} catch (Throwable e) {
			log.error("Exception in Platform.getLinuxDesktop()", e);
		} finally {
			if (r != null) try {
				r.close();
			} catch (IOException e) {
			}
		}
		return Desktop.UNKNOWN;
	}

	private static String getSystemProperty(String property) {
		try {
			return System.getProperty(property).toLowerCase();
		} catch (SecurityException ex) {
			// we are not allowed to look at this property
			String msg = "Caught a SecurityException reading the system property '"
					+ property + "'; the property value will default to null.";
			log.info("msg = {}", msg);
			return null;
		}
	}

	/**
	 * Decides if the operating system matches.
	 * 
	 * @param osNamePrefix the prefix for the os name
	 * @return true if matches, or false if not or can't determine
	 */
	private static boolean getOSMatches(String osNamePrefix) {
		if (OS_NAME == null) {
			return false;
		}
		return OS_NAME.startsWith(osNamePrefix);
	}
	enum OS {
		WINDOWS, LINUX, MAC, UNKNOWN
	}
	enum Desktop {
		GNOME, KDE, UNKNOWN
	}
}
