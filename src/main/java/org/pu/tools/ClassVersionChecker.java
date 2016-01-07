package org.pu.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * major  minor Java platform version 
 * 45       3           1.0
 * 45       3           1.1
 * 46       0           1.2
 * 47       0           1.3
 * 48       0           1.4
 * 49       0           5.0
 * 50       0           6.0
 * 51       0           7
 * 52       0           8
 * </pre>
 * another way is javap -verbose <classfilename>
 * 
 * @author Shang Pu
 * @version Date: Sep 4, 2013 4:31:00 PM
 */
public class ClassVersionChecker {
	public static Map<Integer, Integer> versionMap = new HashMap<Integer, Integer>();
	
	static {
		versionMap.put(49, 5);
		versionMap.put(50, 6);
		versionMap.put(51, 7);
	}
	
	public static void main(String[] args) throws IOException {
//		args = new String[] { "C:/sp/work/eclipseWorkspace/gdms/gdmsr5/webapp/WEB-INF/classes/com/generiscorp/cara/web/dctm/common/server/content/DownloadServletG.class" };
		args = new String[] { "C:/cache/Alert.class" };
		args = new String[] { "C:/cache/GdmsSecurityLinkProcessor.class" };
		args = new String[] { "C:/cache/SplExportServlet.class" };
		args = new String[] { "C:/cache/GTCBusinessObject.class" };
		for (int i = 0; i < args.length; i++)
			checkClassVersion(args[i]);
	}

	private static void checkClassVersion(String filename) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(filename));

		int magic = in.readInt();
		if (magic != 0xcafebabe) {
			System.out.println(filename + " is not a valid class!");
		}
		int minor = in.readUnsignedShort();
		int major = in.readUnsignedShort();
		System.out.println(filename + ": " + major + "." + minor + ", JDK " + versionMap.get(major));
		System.out.println("Version : " + major + "." + minor + ", JDK " + versionMap.get(major));
		in.close();
	}
}
