package org.pu.app.tip;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.pu.utils.DateUtils;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 创建闪动的托盘图像
 */
public class BickerTray extends JFrame {
	private static final long serialVersionUID = -3115128552716619277L;
	private static final Logger log = LoggerFactory.getLogger(BickerTray.class);

	private TrayIcon trayIcon;// 当前对象的托盘
	private TextArea textArea;
	private boolean isPlayingSound = true; // play sound when get message the first time
	private boolean getNewMsgFlag = false; // 是否有新消息
	private static int times = 1; // 接收消息次数
	/**
	 * how often the tray icon will be bicker
	 */
	private static final int interval = 2000;

	public BickerTray() {
		Image imageTray = getToolkit().getImage(IoUtils.getResource("img/f17.gif"));// 托盘图标
		Image imageWindow = getToolkit().getImage(IoUtils.getResource("img/f32.gif"));// big icon

		trayIcon = createTrayIcon(imageTray);// 创建托盘对象
		addTrayIcon(trayIcon);

		initWindow(imageWindow);
	}

	private void initWindow(Image imageWindow) {
		// 初始化窗体
		setTitle("Message box");
		setSize(400, 400);
		setVisible(true); // 使得当前的窗口显示
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(imageWindow);
		textArea = new TextArea("");
		textArea.setEditable(false);
		add(textArea);

		// 添加窗口最小化事件,将托盘添加到操作系统的托盘
		addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				addTrayIcon(trayIcon);
				setVisible(false); // 使得当前的窗口隐藏
			}

			public void windowActivated(WindowEvent e) {
				if (getNewMsgFlag) {
					displayContent();
				}
			}
		});
	}

	/**
	 * 控制闪动
	 */
	public void bicker() {
		Image image = trayIcon.getImage();
		while (true) {
			if (getNewMsgFlag) { // 有新消息
				// if the window is focused, display the content without bicker
				if (this.isFocused()) {
					displayContent();
					continue;
				}
				if (isPlayingSound) {
					// 播放消息提示音
					playSound();
					isPlayingSound = false;
				}
				// 闪动消息的空白时间
				trayIcon.setImage(new ImageIcon("").getImage());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					log.error("InterruptedException in BickerTray.bicker()", e);
				}
				// 闪动消息的提示图片
				trayIcon.setImage(image);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					log.error("InterruptedException in BickerTray.bicker()", e);
				}
			} else { // 无消息或是消息已经打开过
				trayIcon.setImage(image);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					log.error("InterruptedException in BickerTray.bicker()", e);
				}
				getNewMsgFlag = true;
			}
		}
	}

	/**
	 * set getNewMsgFlag = false, times++, isPlayingSound = true
	 */
	private void displayContent() {
		textArea.setText(textArea.getText()
				+ "\n==============================================\n 《通知》 今天下午4:00到大礼堂开会。 \n 第" + times
				+ "次接收时间：" + DateUtils.getLocaleDate()); // 设置通知消息内容
		getNewMsgFlag = false; // 消息打开了
		isPlayingSound = true;
		times++;
	}

	/**
	 * display normal window
	 */
	private void displayWindow() {
		setExtendedState(JFrame.NORMAL);
		setVisible(true); // 显示窗口
		toFront(); // 显示窗口到最前端
	}

	/**
	 * 创建系统托盘的对象 步骤: 1,获得当前操作系统的托盘对象 2,创建弹出菜单popupMenu 3,创建托盘图标icon 4,创建系统的托盘对象trayIcon
	 */
	public TrayIcon createTrayIcon(Image image) {
		PopupMenu popupMenu = new PopupMenu();// 弹出菜单
		MenuItem openMenu = new MenuItem("open");
		MenuItem exitMenu = new MenuItem("exit");
		popupMenu.add(openMenu);
		popupMenu.add(exitMenu);
		// 为弹出菜单项添加事件
		openMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayWindow();
			}
		});
		exitMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		TrayIcon trayIcon = new TrayIcon(image, "message box", popupMenu);
		// 添加鼠标监听器，当鼠标在托盘图标上双击时，默认显示窗口
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (isDoubleClick(e)) { // 鼠标双击
					displayWindow();
				}
			}
		});
		return trayIcon;
	}

	/**
	 * 添加到当前操作系统托盘
	 */
	public void addTrayIcon(TrayIcon trayIcon) {
		SystemTray sysTray = SystemTray.getSystemTray();// 当前操作系统的托盘对象
		// if did not add tray icon to system tray, add it将托盘添加到操作系统的托盘
		if (!Arrays.asList(sysTray.getTrayIcons()).contains(trayIcon)) {
			try {
				sysTray.add(trayIcon);
			} catch (AWTException e) {
				log.error("Exception in BickerTray.addTrayIcon()", e);
			}
		}
	}

	private void playSound() {
		try {
			AudioClip p = Applet.newAudioClip(IoUtils.getResource("sound/msg.wav"));
			log.debug("playing Sound");
			p.play();
		} catch (Exception e) {
			log.error("Exception in BickerTray.playSound()", e);
		}
	}

	private boolean isDoubleClick(MouseEvent e) {
		return e.getClickCount() == 2;
	}

	public static void main(String[] args) {
		BickerTray tray = new BickerTray();
		tray.bicker();
	}

}
