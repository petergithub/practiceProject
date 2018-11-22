package doing.search;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 功能 根据输入的网址、起止字符串，采集改网址页面的超链接和链接文字 get all URL and title recurse in the given URL
 */
public class ExtractURLs extends JFrame {
	private static final long serialVersionUID = 1L;
	JPanel contentPane;
	Label label1;
	TextField textField1;
	Button button1, button2;
	TextArea textArea1;
	StringBuffer URLTextBuffer = new StringBuffer();
	String URLText;

	public ExtractURLs() {
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

		button2 = new Button("ExtractURL");
		button2.setBounds(423, 4, 65, 24);
		button2.addMouseListener(new ExtractUrlListener());
		contentPane.add(button2);

		textArea1 = new TextArea();
		textArea1.setBounds(2, 32, 440, 201);
		contentPane.add(textArea1);
		// ================================================
		this.setSize(550, 460);
		this.setTitle("Search");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	} // public URLConn() end

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
				String inform = "url = " + textField1.getText() + "\nprotocalType = " + myURL.getProtocol()
						+ "\nhost = " + myURL.getHost() + "\nport = " + myURL.getPort() + " \nContentType = "
						+ urlConn.getContentType() + "\n";

				textArea1.append(inform);
				// ===================================================
				BufferedReader myConnReader = new BufferedReader(new InputStreamReader(
						urlConn.getInputStream()));
				/*
				 * 由此 URLConnection 取得读取此 URL 所指网站的 InputStream， 并且以 InputStreamReader 转换 byte 流为 char 流
				 */

				while ((lineStr = myConnReader.readLine()) != null) { // 读一行
					textArea1.append(lineStr + "\n"); // 换行时，pt.matcher不能实现作用
					URLTextBuffer.append(lineStr);
					// 将现在读到这一行加入 textArea1 组件
				}
				URLText = URLTextBuffer.toString();
				System.out.println(URLText);
				myConnReader.close(); // 关闭输入流
			} catch (Exception ecp) {
				System.err.println(ecp);
			}
		}
	} // inner class myMouseAdapter end

	/**
	 * 功能 根据输入的网址、起止字符串，采集改网址页面的超链接和链接文字 get all URL and title recurse in the given URL
	 */
	class ExtractUrlListener extends MouseAdapter {
		String sourceURL;// 需要采集的网页网址
		String sourceContent;// 网页页面内容
		// String URLs; //采集到的超链接
		// String title;//采集到的链接文字
		String beginStr;// 网页内容匹配区域开始字符串
		String endStr;// 网页内容匹配区域结束字符串
		String matchContent;// 网页内容匹配区域

		public void mouseClicked(MouseEvent e) {
			ExtractUrlListener extract = new ExtractUrlListener(URLText, "<body", "</body>");
			System.out.println(URLText);
			// extractURLs.getSourceContent(extractURLs.sourceURL);
			extract.matchContent = extract.getMatchContent(extract.beginStr, extract.endStr);
			extract.getString(extract.matchContent);
		}

		public ExtractUrlListener() {
		}

		// 根据传来的网页网址、匹配区域起止字符串初始化
		public ExtractUrlListener(String text, String beginStr1, String endStr1) {
			sourceContent = text;
			beginStr = beginStr1;
			endStr = endStr1;
		}

		// 获取匹配区域
		public String getMatchContent(String beginStr, String endStr) {
			String regex = beginStr + ".*?" + endStr;
			Pattern pt = Pattern.compile(regex);
			Matcher mt = pt.matcher(sourceContent);
			if (mt.find()) {
				return matchContent = mt.group();
			} else
				return null;
		}

		// 获取需要的部分:超链接和标题
		public void getString(String s) {
			int counter = 0;// 计算器 计算匹配的个数
			String regexURL = "(http|https|ftp)://([^/:]+)(:\\d*)?([^#\\s]*)";
			Pattern pt = Pattern.compile(regexURL);
			Matcher mt = pt.matcher(s);
			while (mt.find()) {
				String s2 = mt.group();
				counter++;
				System.out.println(mt.group());
				textArea1.append(mt.group() + "\n");

				// 获取并打印标题
				String titleRegex = ">.*?</a>";
				String title;
				Matcher mt1 = Pattern.compile(titleRegex).matcher(s2);
				while (mt1.find()) {
					title = mt1.group().replaceAll(">|</a>|<font.*?>|</font>|&nbsp;", "");
					System.out.println("标题: " + title);
					textArea1.append("标题: " + title + "\n");
				}

				// 获取并打印网址
				// String urlsRegex = "http://.*?\.html";
				String urlsRegex = "(http|https|ftp)://([^/:]+)(:\\d*)?([^#\\s]*)";
				String urls;
				Matcher mt2 = Pattern.compile(urlsRegex).matcher(s2);
				while (mt2.find()) {
					urls = mt2.group().replaceAll("<a href=|>", "");
					System.out.println("网址: " + urls);
				}

				System.out.println();// 空行
			}

			System.out.println("共有" + counter + "个符合结果");
		}
	}

	public static void main(String arg[]) {
		new ExtractURLs();
	}
} // class URLConn end
