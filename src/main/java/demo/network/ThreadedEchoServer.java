package demo.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program implements a multithreaded server that listens to port 8189 and
 * echoes client input.
 */
public class ThreadedEchoServer {
	/**
	 * After run this main method, connection this server with telnet telnet
	 * localhost port
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket s = null;
		try {
			int i = 1;
			s = new ServerSocket(8189);

			while (true) {
				Socket incoming = s.accept();
				System.out.println("Spawning " + i);
				Runnable r = new ThreadedEchoHandler(incoming, i);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
			}
		}
	}
}

/**
 * This class handles the client input for one server socket connection.
 */
class ThreadedEchoHandler implements Runnable {
	/**
	 * Constructs a handler.
	 * 
	 * @param i the incoming socket
	 * @param c the counter for the handlers (used in prompts)
	 */
	public ThreadedEchoHandler(Socket i, int c) {
		incoming = i;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);

				// print a welcome message
				out.println("Hello, you've contacted the Echo Server");
				out.println("\tWhatever you type, I will type back to you ...");
				out.println("Enter BYE to exit.");
				out.println("");
				out.println("");
				out.flush();

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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Socket incoming;
}
