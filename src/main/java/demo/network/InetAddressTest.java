package demo.network;

/**
 @version 1.01 2001-06-26
 @author Cay Horstmann
 */

import java.net.InetAddress;

/**
 * This program demonstrates the InetAddress class. Supply a host name as
 * command line argument, or run without command line arguments to see the
 * address of the local host.
 */
public class InetAddressTest {
	public static void main(String[] args) {
		args = new String[15];
		args[0] = "www.google.com";
		args[1] = "google.com";
		args[2] = "www.baidu.com";
		args[3] = "baidu.com";
		args[4] = "dropbox.com";
		try {
			if (args.length > 0) {
				for (String arg : args) {
					String host = arg;
					InetAddress[] addresses = InetAddress.getAllByName(host);
					for (InetAddress a : addresses) {
						System.out.println(a);
					}
				}
			} else {
				InetAddress localHostAddress = InetAddress.getLocalHost();
				System.out.println(localHostAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
