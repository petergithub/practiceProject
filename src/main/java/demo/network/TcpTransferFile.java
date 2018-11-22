package demo.network;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * 接收的文件保存在当前文件夹下
 */
public class TcpTransferFile extends Frame implements Runnable {
	private static final long serialVersionUID = 1L;
	String recFile; // 欲接收的文件
	File recFileDir; // 欲接收的文件所含上层目录
	boolean canRecFile; // 是否可接收文件
	// ====================
	String sendFile; // 欲发送的文件
	boolean canSendFile; // 是否可送出文件
	// =====================================
	Label label1, label2, label3, label4, label5;
	TextField textField1, textField2, textField3, textField4;
	List list1;
	Button button1;

	public TcpTransferFile() {
		this.setLayout(null);
		this.setBounds(200, 200, 370, 400);
		this.setTitle("TCP 点对点文件传输");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// ---------------------------------------------
		label1 = new Label("对方 IP: ");
		label1.setBounds(10, 25, 55, 25);
		label1.setBackground(new Color(255, 193, 255));
		this.add(label1);

		textField1 = new TextField("127.0.0.1");
		textField1.setBounds(70, 25, 110, 25);
		this.add(textField1);

		label2 = new Label("对方接收的Port: ");
		label2.setBounds(10, 60, 120, 25);
		label2.setBackground(new Color(255, 193, 255));
		this.add(label2);

		textField2 = new TextField("2222");
		textField2.setBounds(135, 60, 55, 25);
		this.add(textField2);

		label3 = new Label("欲传送的文件: ");
		label3.setBounds(10, 95, 95, 25);
		label3.setBackground(new Color(255, 193, 255));
		this.add(label3);

		textField3 = new TextField("testDir\\readme.txt");
		textField3.setBounds(105, 95, 200, 25);
		this.add(textField3);

		button1 = new Button("送出");
		button1.setBounds(310, 95, 50, 25);
		button1.addActionListener(new myActionListener());
		this.add(button1);

		label4 = new Label("本地收的Port: ");
		label4.setBounds(10, 150, 120, 25);
		label4.setBackground(new Color(255, 193, 255));
		this.add(label4);

		textField4 = new TextField("2222");
		textField4.setBounds(135, 150, 55, 25);
		textField4.setEnabled(false);
		this.add(textField4);

		label5 = new Label("接收文件存储至: ");
		label5.setBounds(10, 185, 120, 25);
		label5.setBackground(new Color(255, 193, 255));
		this.add(label5);

		list1 = new List();
		list1.setBounds(10, 220, 350, 160);
		this.add(list1);
		// -----------------------------------------------
		this.setBackground(new Color(220, 255, 255));
		this.setVisible(true);
	} // public TCP() end

	public static void main(String para[]) {
		new Thread(new TcpTransferFile()).start();// 启动负责接收文件的线程
	}

	// =========================================================
	private boolean recFileName(ServerSocket serverTcp) throws IOException { // 接收文件名，若有需要接收的文件，则返回
																				// true
		Socket connSocket = serverTcp.accept();
		// 接受 client 请求，建立联机
		InputStreamReader serverInput = new InputStreamReader(
				connSocket.getInputStream());
		// 将 byte 流转成 char 流
		int data;
		StringBuffer sb = new StringBuffer();// “可变动”的字符串
		while ((data = serverInput.read()) != -1) { // 逐字读入文件存储在 sb
			sb.append(String.valueOf((char) data));
		}
		recFile = sb.toString(); // 回传成 String
		serverInput.close(); // 关闭输入流
		connSocket.close(); // 关闭 Socket

		return !(recFile == null || recFile.equals("")); // 有需要接收的文件
	}

	private void checkFile() { // 确定欲接收的文件的所在目录已经建立
		recFileDir = new File(recFile).getParentFile();
		if ((recFileDir != null) && (!recFileDir.exists())) { // 有上层目录，且还不存在
			canRecFile = recFileDir.mkdirs();
			// 若目录成功建立，就返回 true，否则返回 false
		} else { // 无上层目录，或是已经存在
			canRecFile = true;
		}
	}

	@SuppressWarnings("unused")
	public void recFile(ServerSocket serverTcp, byte[] theData)
			throws IOException { // 接收文件
		if (!(this.recFileName(serverTcp))) return; // 没有需要接收的文件，不必再继续

		checkFile();
		if (!canRecFile) { // 欲接收的文件的目录未建立
			JOptionPane.showMessageDialog(button1, "无法建立目录");
			return;
		}
		// ===================================================
		Socket connSocket = serverTcp.accept();
		// 接受 client 请求，建立联机
		BufferedInputStream serverInput = new BufferedInputStream(
				connSocket.getInputStream());
		// 取得输入流
		BufferedOutputStream FileObj = new BufferedOutputStream(
				new FileOutputStream(recFile));

		while (serverInput.read(theData) != -1) { // 读入数据
			FileObj.write(theData); // 写到本地的文件
		}
		serverInput.close(); // 关闭输入流
		FileObj.flush();
		FileObj.close();
		connSocket.close(); // 关闭 Socket
		// serverTcp.close();
		/*
		 * 若关闭，则第二次接收文件会引发异常。 因为本例的 ServerSocket 要一直使用，所以不要关闭它。
		 */

		list1.add(new File(recFile).getCanonicalPath());
		Runtime.getRuntime().exec("notepad " + recFile);
		// 以记事本尝试开启刚才复制的文件
		boolean canRecFile = false; // 文件已收，状态要回复
		String recFile = "";
		File recFileDir = null;
	}

	public void run() { // 以 TCP 接收文件数据
		byte[] theData = new byte[1];
		try { // 产生 ServerSocket
			ServerSocket serverTcp = new ServerSocket(
					Integer.parseInt(textField4.getText().trim()));
			// 用某一个 Port 接收数据
			while (true) {
				this.recFile(serverTcp, theData);// 接收文件
				Thread.sleep(200); // 停 0.2 秒
			}
		} catch (Exception ecp) {
			JOptionPane.showMessageDialog(button1, ecp.getMessage());
		}
	} // void run() end

	// *************************************************************
	class myActionListener implements ActionListener { // “送出文件”的部分
		public void sendFileName() throws UnknownHostException, IOException { // 送出文件名，嵌套类
																				// myActionListener
																				// 的方法
			sendFile = textField3.getText().trim();
			File sourFile = new File(sendFile);
			if (!sourFile.exists()) {
				canSendFile = false;
				JOptionPane.showMessageDialog(null, "欲送出的文件不存在！");
				return;
			}
			// Socket(String host,int port)，建立 Socket 并向 Server 联机
			Socket tcpSocket = new Socket(textField1.getText().trim(),
					Integer.parseInt(textField2.getText().trim()));
			// 若联机不成功则产生异常
			BufferedOutputStream opStream = new BufferedOutputStream(
					tcpSocket.getOutputStream());
			// 取得对远程主机作“输出”的流
			opStream.write(sendFile.getBytes(), 0, sendFile.getBytes().length);

			opStream.flush();
			opStream.close(); // 关闭输出流
			tcpSocket.close(); // 关闭 Socket
			canSendFile = true; // 能执行到此，表示已送出文件名
		}

		public void sendFile() throws UnknownHostException,
				FileNotFoundException, IOException { // 送出文件，嵌套类
														// myActionListener
														// 的方法
			sendFileName(); // 先送出文件名
			if (!canSendFile) return; // 没有要送出文件，不必继续
			// ==================================
			byte[] tmpData = new byte[1]; // 以 byte 为单位
			Socket tcpSocket = new Socket(textField1.getText().trim(),
					Integer.parseInt(textField2.getText().trim()));
			// 若联机不成功则产生异常
			BufferedInputStream fileObj = new BufferedInputStream(
					new FileInputStream(sendFile));
			// 使用 byte 流，能够传送各类型的文件
			BufferedOutputStream opStream = new BufferedOutputStream(
					tcpSocket.getOutputStream());
			// 取得对远程主机作“输出”的流
			while (fileObj.read(tmpData) != -1) {
				opStream.write(tmpData); // 送出文件数据
			}
			fileObj.close();
			opStream.flush();
			opStream.close(); // 关闭输出流
			tcpSocket.close();// 关闭 Socket

			canSendFile = false; // 文件已送，状态要回复
			sendFile = "";
			JOptionPane.showMessageDialog(null, "文件送毕！");
		}

		public void actionPerformed(ActionEvent e) { // 以 TCP 送出文件数据
			try {
				this.sendFile(); // 送出文件
			} catch (IOException ecp) {
				JOptionPane.showMessageDialog(button1,
						"aa=>" + ecp.getMessage());
			}
		}
	} // inner class myActionListener end
} // class TCP end
