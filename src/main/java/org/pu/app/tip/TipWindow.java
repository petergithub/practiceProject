package org.pu.app.tip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * pop up window
 * 
 * @author Shang Pu
 * @version Date: Feb 3, 2013 3:38:01 PM
 */
public class TipWindow extends JDialog {
	private static final Logger log = LoggerFactory.getLogger(TipWindow.class);
	
	private static final long serialVersionUID = 1L;
	private int x, y;
	private int width, height;

	public TipWindow() {
	}

	public TipWindow(Map<String, String> feaMap) {
		this(300, 220);
		setTitle(feaMap.get("name"));
		setResizable(false);
		setAlwaysOnTop(true);

		JLabel nameLabel = new JLabel(feaMap.get("name"));
		JTextArea featureTextArea = new JTextArea(feaMap.get("feature"));
		featureTextArea.setEditable(false);
		featureTextArea.setForeground(Color.red);
		featureTextArea.setFont(new Font("宋体", Font.PLAIN, 13));

		JScrollPane featureScrollPane = new JScrollPane(featureTextArea);
		featureScrollPane.setPreferredSize(new Dimension(283, 80));

		JLabel releaseLabel = new JLabel(feaMap.get("release"));
		JPanel featurePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		// featurePanel.setBorder(BorderFactory.createMatteBorder(1, 2, 3, 0,
		// Color.gray));
		featurePanel.add(featureScrollPane);
		featurePanel.add(releaseLabel);

		JPanel headPanel = new JPanel();
		headPanel.add(nameLabel);

		JPanel btnPanel = new JPanel();
		JButton confirmBtn = new JButton("确定");
		btnPanel.add(confirmBtn);

		add(headPanel, BorderLayout.NORTH);
		add(featurePanel, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);

		confirmBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				close();
			}
		});

		setVisible(true);
	}

	public TipWindow(int width, int height) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		this.width = width;
		this.height = height;
		x = (int) (dim.getWidth() - width - 3);
		y = (int) (dim.getHeight() - screenInsets.bottom - 3);
		initComponents();
	}

	private void initComponents() {
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	/**
	 * 开启渐入效果 开启后3秒，窗口自动渐出 若不需要渐出，注释掉，sleep(3000)和close()方法
	 */
	public void popup() {
		for (int i = 0; i <= height; i += 10) {
			this.setLocation(x, y - i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				log.error("InterruptedException in TipWindow.popup()", e);
			}
		}
		// Utils.sleep(3000);
		// close();
	}

	private void close() {
		for (int i = 0; i <= height; i += 10) {
			setLocation(x, y - height + i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				log.error("InterruptedException in TipWindow.close()", e);
			}
		}
		dispose();
	}

	public static void main(String args[]) {
		Map<String, String> feaMap = new HashMap<String, String>();
		feaMap.put("name", "java气泡提醒");
		feaMap.put("release", "发布日期: 2010-08-20 11:33:00");
		feaMap.put("feature", "java气泡提醒含以下功能:\n1.含动画渐入与渐出效果\n2.3秒后启动动画渐出效果");
		TipWindow window = new TipWindow(feaMap);
		window.popup();
	}
}
