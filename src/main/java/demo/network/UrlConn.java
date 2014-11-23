package demo.network;

import java.awt.Button;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class UrlConn extends JFrame {
	private static final long serialVersionUID = 1L;
	JPanel contentPane;
	Label label1;
	TextField textField1;
	Button button1;
	TextArea textArea1;

	public UrlConn() {
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(null);
		// ================================================
		label1 = new Label("URL Name");
		label1.setBounds(2, 6, 65, 22);
		contentPane.add(label1);
		textField1 = new TextField("http://www.baidu.com/");
		textField1.setBounds(72, 4, 247, 24);
		contentPane.add(textField1);
		button1 = new Button("Read");
		button1.setBounds(323, 4, 65, 24);
		button1.addMouseListener(new myMouseAdapter());
		contentPane.add(button1);
		textArea1 = new TextArea();
		textArea1.setBounds(2, 32, 440, 201);
		contentPane.add(textArea1);
		// ================================================
		this.setSize(450, 260);
		this.setTitle("使用 URLConnection 对象读取网站内容");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	}

	public void paint(Graphics g) { // 窗口改变大小时，textArea1 也跟着改变大小
		textArea1.setBounds(2, 32, this.getWidth() - 10, this.getHeight() - 59);
	}

	class myMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			textArea1.setText(""); // 清掉目前 textArea1 内的文字
			String lineStr = null; // 记录由此 URL 读取的一行
			try {
				URL myURL = new URL(textField1.getText());
				// 建立所指定的 myURL 对象
				URLConnection urlConn = myURL.openConnection();
				// 开启对服务器网站的实际联机
				String inform = "内容长度 = " + urlConn.getContentLength()
						+ " bytes\n内容型态 = " + urlConn.getContentType()
						+ "\n从服务器送来的日期 = " + new Date(urlConn.getDate())
						+ "\n内容: \n";

				textArea1.append(inform);
				// ===================================================
				BufferedReader myConnReader = new BufferedReader(
						new InputStreamReader(urlConn.getInputStream()));
				/*
				 * 由此 URLConnection 取得读取此 URL 所指网站的 InputStream， 并且以
				 * InputStreamReader 转换 byte 流为 char 流
				 */

				while ((lineStr = myConnReader.readLine()) != null) { // 读一行
					textArea1.append(lineStr + "\n");
					// 将现在读到这一行加入 textArea1 组件
				}
				myConnReader.close(); // 关闭输入流
			} catch (Exception ecp) {
				System.err.println(ecp);
			}
		}
	} // inner class myMouseAdapter end

	public static void main(String arg[]) {
		new UrlConn();
	}
} // class URLConn end
