package demo.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * @author Shang Pu
 * @version Date: May 14, 2012 10:02:07 AM
 */
public class SocketDemo {
	protected static final Logger logger = Logger
			.getLogger(SocketDemo.class);

	/**
	 * After run the testEchoServer method, connection this server with telnet
	 * telnet localhost port This program implements a simple server that
	 * listens to port and 8189 echoes back all client input.
	 */
	public void testEchoServer() {
		try {
			// establish server socket
			java.net.ServerSocket s = new java.net.ServerSocket(8189);

			// wait for client connection
			java.net.Socket incoming = s.accept();
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				java.util.Scanner in = new java.util.Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);

				out.println("Hello! Enter BYE to exit.");

				// echo client input
				boolean done = false;
				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Echo: " + line);
					if (line.trim().equals("BYE")) {
						done = true;
					}
				}
				in.close();
			} finally {
				incoming.close();
				s.close();
			}
		} catch (IOException e) {
			logger.error("error in testEchoServer", e);
		}
	}

	/**
	 * This program makes a socket connection to the atomic clock in Boulder,
	 * Colorado, and prints the time that the server sends.
	 */
	public void testSocket() {
		try {
			java.net.Socket s = new java.net.Socket(
					"time-A.timefreq.bldrdoc.gov", 13);
			try {
				InputStream inStream = s.getInputStream();
				java.util.Scanner in = new java.util.Scanner(inStream);

				while (in.hasNextLine()) {
					String line = in.nextLine();
					logger.info(line);
				}
				in.close();
			} finally {
				s.close();
			}
		} catch (IOException e) {
			logger.error("error in testSocket", e);
		}
	}

}
