package demo.network;

/**
 @author Cay Horstmann
 @version 1.0 2004-08-03
 */



import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This program shows how to interrupt a socket channel.
 */
public class InterruptibleSocketTest {
	
	public static void main(String[] args) {
		JFrame frame = new InterruptibleSocketFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class InterruptibleSocketFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public InterruptibleSocketFrame() {
		setSize(WIDTH, HEIGHT);
		setTitle("InterruptibleSocketTest");

		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);

		messages = new JTextArea();
		add(new JScrollPane(messages));

		busyBox = new JCheckBox("Busy");
		northPanel.add(busyBox);

		startButton = new JButton("Start");
		northPanel.add(startButton);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				startButton.setEnabled(false);
				cancelButton.setEnabled(true);
				connectThread = new Thread(new Runnable() {
					public void run() {
						connect();
					}
				});
				connectThread.start();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		northPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connectThread.interrupt();
				startButton.setEnabled(true);
				cancelButton.setEnabled(false);
			}
		});
		server = new TestServer();
		new Thread(server).start();
	}

	/**
	 * Connects to the test server.
	 */
	public void connect() {
		try {
			SocketChannel channel = SocketChannel.open(new InetSocketAddress(
					"localhost", 8189));
			try {
				in = new Scanner(channel);
				while (true) {
					if (in.hasNextLine()) {
						String line = in.nextLine();
						messages.append(line);
						messages.append("\n");
					} else {
						Thread.sleep(100);
					}
				}
			} finally {
				channel.close();
				messages.append("Socket closed\n");
			}
		} catch (IOException e) {
			messages.append("\nInterruptibleSocketTest.connect: " + e);
		} catch (InterruptedException e) {
			messages.append("\nInterruptibleSocketTest.connect: " + e);
		}
	}

	/**
	 * A multithreaded server that listens to port 8189 and sends random numbers
	 * to the client.
	 */
	class TestServer implements Runnable {
		private final Logger log = LoggerFactory.getLogger(InterruptibleSocketTest.class);
		public void run() {
			ServerSocket s = null;
			try {
				s = new ServerSocket(8189);

				while (true) {
					Socket incoming = s.accept();
					Runnable r = new RandomNumberHandler(incoming);
					Thread t = new Thread(r);
					t.start();
				}
			} catch (IOException e) {
				messages.append("\nTestServer.run: " + e);
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					log.error("IOException in TestServer.run()", e);
				}
			}
		}
	}

	/**
	 * This class handles the client input for one server socket connection.
	 */
	class RandomNumberHandler implements Runnable {
		/**
		 * Constructs a handler.
		 * 
		 * @param i the incoming socket
		 */
		public RandomNumberHandler(Socket i) {
			incoming = i;
		}

		public void run() {
			try {
				OutputStream outStream = incoming.getOutputStream();
				PrintWriter out = new PrintWriter(outStream, true);
				Random generator = new Random();
				while (true) {
					if (!busyBox.isSelected()) {
						out.println(generator.nextInt());
					}
					Thread.sleep(100);
				}
			} catch (IOException e) {
				messages.append("\nRandomNumberHandler.run: " + e);
			} catch (InterruptedException e) {
				messages.append("\nRandomNumberHandler.run: " + e);
			}
		}

		private Socket incoming;
	}

	private Scanner in;
	private JButton startButton;
	private JButton cancelButton;
	private JCheckBox busyBox;
	private JTextArea messages;
	private TestServer server;
	private Thread connectThread;

	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
}
