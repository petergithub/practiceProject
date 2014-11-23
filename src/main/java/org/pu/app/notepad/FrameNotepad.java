package org.pu.app.notepad;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * <p>
 * Title: 流星絮语记事本
 * </p>
 * <p>
 * Description: 记事本
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: 西昌学院
 * </p>
 * 
 * @author 冯立彬
 * @version 1.0
 */
public class FrameNotepad extends JFrame {
	private static final long serialVersionUID = 7262175296824848982L;
	private JPanel contentPane;
	private JMenuBar jMenuBar1 = new JMenuBar(); // 生成菜单栏母体
	private JMenu jMenuFile = new JMenu(); // 生成第一个菜单(File)
	private JMenuItem jMenuFileCreat = new JMenuItem();
	private JMenuItem jMenuFileSave = new JMenuItem();
	private JMenuItem jMenuFileOpen = new JMenuItem();
	private JMenuItem jMenuFileSaveAs = new JMenuItem();
	private JMenuItem jMenuFileExit = new JMenuItem();
	private JMenu jMenuEdit = new JMenu(); // 生成第二个菜单(Edit)
	private JMenuItem jMenuEditReDo = new JMenuItem();
	private JMenuItem jMenuEditCut = new JMenuItem();
	private JMenuItem jMenuEditCopy = new JMenuItem();
	private JMenuItem jMenuEditPaste = new JMenuItem();
	private JMenuItem jMenuEditDel = new JMenuItem();
	private JMenuItem jMenuEditSelectAll = new JMenuItem();
	private JMenu jMenuFormat = new JMenu(); // 生成第三个菜单(format)
	private JMenuItem jMenuFormatColor = new JMenuItem();
	private JMenu jMenuHelp = new JMenu(); // 生成第四个菜单(about)
	private JMenuItem jMenuHelpDocument = new JMenuItem();
	private JMenuItem jMenuHelpAbout = new JMenuItem();
	private JToolBar jToolBar = new JToolBar();
	private JButton jButton1 = new JButton();
	private JButton jButton2 = new JButton();
	private JButton jButton3 = new JButton();
	private ImageIcon image1 = new ImageIcon(
			org.pu.app.notepad.FrameNotepad.class.getResource("openFile.png"));
	private ImageIcon image2 = new ImageIcon(
			org.pu.app.notepad.FrameNotepad.class.getResource("closeFile.png"));
	private ImageIcon image3 = new ImageIcon(
			org.pu.app.notepad.FrameNotepad.class.getResource("help.png"));
	private JLabel statusBar = new JLabel();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextArea jTxtArea = new JTextArea();
	private JMenuItem jMenuEditAutoWrap = new JMenuItem();
	// 生成打开文件对话框
	// Frame_FileDialog fdilog=new Frame()
	// JMenuItem jMenuFileOpen = new JMenuItem();
	private FileDialog fd;
	private String File_Name_withPath = ""; // 全局变量名，文件名
	private String File_Name = ""; // 全局变量名，文件名
	private boolean textIsChanged = false;
	private boolean textIsSaved = false;
	private FileReader fr; // 文件读取器
	private FileWriter fw; // 文件写入器
	private BufferedReader br; // 文件缓冲读取器
	private BufferedWriter bw; // 文件缓冲写入器
	private Clipboard clipBoard;
	private StringSelection text; // 用于存放获得的字符

	public FrameNotepad() {
		try {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			jbInit();
			// getToolkit()获得一个Toolkit对象，该对象再调用getSystemClipboard()方法
			// 获得系统的剪贴板
			clipBoard = getToolkit().getSystemClipboard();
			// 为文本框增加键盘监听事件
			// addKeyListener(this);
			// jTxtArea.addKeyListener(KeyPressed);
			// addKeyListener(jTxtArea);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Component initialization.
	 * 
	 * @throws java.lang.Exception
	 */
	private void jbInit() throws Exception {
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		setSize(new Dimension(400, 300));
		setTitle("记事本");
		statusBar.setText("程序开始");
		statusBar.setBounds(new Rectangle(0, 284, 400, 17));
		jMenuFile.setText("文件");
		jMenuFileCreat.setText("新建");
		jMenuFileCreat.addActionListener(new FrameNotepad_jMenuFileCreat_ActionAdapter(this));
		jMenuFileOpen.setText("打开..");
		jMenuFileOpen.addActionListener(new FrameNotepad_jMenuFileOpen_ActionAdapter(this));
		jMenuFileSave.setText("保存");
		jMenuFileSave.addActionListener(new FrameNotepad_jMenuFileSave_ActionAdapter(this));
		// jMenuFileSave.setEnabled(false);
		jMenuFileSaveAs.setText("另存为..");
		jMenuFileSaveAs.addActionListener(new FrameNotepad_jMenuFileSaveAs_ActionAdapter(this));
		// jMenuFileSaveAs.setEnabled(false);
		jMenuFileExit.setText("Exit");
		jMenuFileExit.addActionListener(new FrameNotepad_jMenuFileExit_ActionAdapter(this));
		jMenuEdit.setText("编辑");

		jMenuEditReDo.setText("撤消");
		jMenuEditCut.setText("剪切");
		jMenuEditCut.addActionListener(new FrameNotepad_jMenuEditCut_ActionAdapter(this));
		jMenuEditCopy.setText("拷贝");
		jMenuEditCopy.addActionListener(new FrameNotepad_jMenuEditCopy_ActionAdapter(this));
		jMenuEditPaste.setText("粘贴");
		jMenuEditPaste.addActionListener(new FrameNotepad_jMenuEditPaste_ActionAdapter(this));
		jMenuEditDel.setText("删除");
		jMenuEditDel.addActionListener(new FrameNotepad_jMenuEditDel_ActionAdapter(this));
		jMenuEditSelectAll.setText("全选");
		jMenuEditSelectAll.addActionListener(new FrameNotepad_jMenuEditSelectAll_ActionAdapter(this));
		jMenuEditAutoWrap.setText("自动换行");
		jMenuEditAutoWrap.addActionListener(new FrameNotepad_jMenuEditAutoWrap_ActionAdapter(this));
		jMenuFormat.setText("格式");
		jMenuFormatColor.setText("字体..");
		jMenuFormatColor.addActionListener(new FrameNotepad_jMenuFormatColor_ActionAdapter(this));
		jMenuHelp.setText("帮助");
		jMenuHelpDocument.setText("帮助文档");
		jMenuHelpDocument.addActionListener(new FrameNotepad_jMenuHelpDocument_ActionAdapter(this));
		jMenuHelpAbout.setText("关于软件");
		jMenuHelpAbout.addActionListener(new FrameNotepad_jMenuHelpAbout_ActionAdapter(this));
		jToolBar.setBounds(new Rectangle(0, 0, 400, 25));
		jScrollPane1.setBounds(new Rectangle(4, 27, 396, 260));
		jTxtArea.setText("");
		jTxtArea.setLineWrap(true);

		jMenuBar1.add(jMenuFile);
		jMenuFile.add(jMenuFileCreat);
		jMenuFile.add(jMenuFileOpen);
		jMenuFile.add(jMenuFileSave);
		jMenuFile.add(jMenuFileSaveAs);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileExit);

		jMenuBar1.add(jMenuEdit);
		jMenuEdit.add(jMenuEditReDo);
		jMenuEdit.addSeparator();
		jMenuEdit.add(jMenuEditCopy);
		jMenuEdit.add(jMenuEditCut);
		jMenuEdit.add(jMenuEditPaste);
		jMenuEdit.add(jMenuEditDel);
		jMenuEdit.addSeparator();
		jMenuEdit.add(jMenuEditSelectAll);
		jMenuEdit.add(jMenuEditAutoWrap);
		jMenuBar1.add(jMenuHelp);
		jMenuHelp.add(jMenuHelpDocument);
		jMenuHelp.add(jMenuHelpAbout);
		setJMenuBar(jMenuBar1);
		jButton1.setIcon(image1);
		jButton1.setToolTipText("Open File");
		jButton2.setIcon(image2);
		jButton2.setToolTipText("Close File");
		jButton3.setIcon(image3);
		jButton3.setToolTipText("Help");
		jToolBar.add(jButton1);
		jToolBar.add(jButton2);
		jToolBar.add(jButton3);
		contentPane.add(jToolBar, null);
		contentPane.add(statusBar, null);
		contentPane.add(jScrollPane1);
		jScrollPane1.getViewport().add(jTxtArea);
	}

	// 下面是键盘事件
	// KeyEvent类提供了一个方法: public void getKeyCode()用于判断哪个键被按下或释放
	// 键盘按下
	public void keyPressed(KeyEvent e) {
		textIsChanged = true;
	}

	// 释放按键

	public void keyRelease(KeyEvent e) {
		textIsChanged = true;
	}

	//
	public void keyTyped(KeyEvent e) {
		textIsChanged = true;
	}

	/**
	 * File | Exit action performed.
	 * 
	 * @param actionEvent ActionEvent
	 */
	void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
		System.exit(0);
	}

	/**
	 * Help | About action performed.
	 * 
	 * @param actionEvent ActionEvent
	 */
	void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
		FrameNotepad_AboutBox dlg = new FrameNotepad_AboutBox(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height)
				/ 2 + loc.y);
		dlg.setModal(true);
		dlg.pack();
		dlg.setVisible(true);
	}

	// 新建文件
	void jMenuFileCreat_actionPerformed(ActionEvent actionEvent) {

	}

	// 打开文件
	void jMenuFileOpen_actionPerformed(ActionEvent actionEvent) {
		try {
			if (textIsChanged == true) {// 如果文本已经发生了更改，提示保存
				// 这里应该提示用户是否保存
				jMenuFileSaveAs_actionPerformed(actionEvent);// 调开另存为话框
			} else {
				fd = new FileDialog(this, "文件打开对话框", FileDialog.LOAD);
				fd.setVisible(true);
				// 怎么确定没有选定文件，而不进行下面的操作
				// File_Name = fd.getFile().toString();
				File_Name = fd.getFile();
				File_Name_withPath = fd.getDirectory();
				// File_Name_withPath = fd.getDirectory().toString() + File_Name;
				if (File_Name.length() > 0) {
					jTxtArea.setText("");
				}
				this.setTitle(File_Name); // 将文件名设为标题
				// 生成一个对应于该文件的文件对象
				File file = new File(fd.getDirectory(), fd.getFile());
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String tString; // 临时字符串变量
				// 加上换行符，在输出的时候才会格式不变化
				String lineSep = System.getProperty("line.separator");
				while ((tString = br.readLine()) != null) {
					tString = tString + lineSep;
					jTxtArea.append(tString);
				}
				br.close();
				fr.close();
				fd.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 保存文件
	void jMenuFileSave_actionPerformed(ActionEvent actionEvent) {
		try {
			if (textIsSaved == false) {
				jMenuFileSaveAs_actionPerformed(actionEvent);
			} else {
				fd = new FileDialog(this, "文件保存对话框", FileDialog.SAVE);
				// fd.setVisible(true);//因为是保存所有不用显示出来
				// fd.set
				// if(File_Name.length()>0)//如果是已经打开的文件就把文件名显示出来
				// fd.setFile(File_Name);
				// fd.setFilenameFilter(txt);
				File file = new File(File_Name_withPath, File_Name);
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				// String lineString;
				// lineString=jTxtArea.getText();
				// lineString=lineString.replace("/n");
				bw.write(jTxtArea.getText(), 0, jTxtArea.getText().length());
				this.setTitle(fd.getFile());
				bw.close();
				fw.close();
				fd.dispose();
				textIsSaved = true;
				textIsChanged = false;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	void jMenuFileSaveAs_actionPerformed(ActionEvent actionEvent) {
		try {
			// 这里还有错误，就是如果文件不是打开而是新建的点击这里会出错
			// 因为只是一下就保存了，假设已经取得原来的文件名和路径
			fd = new FileDialog(this, "文件另存为对话框", FileDialog.SAVE);
			fd.setVisible(true);
			// fd.set
			// if(File_Name.length()>0)//如果是已经打开的文件就把文件名显示出来
			// fd.setFile(File_Name);
			// fd.setFilenameFilter(txt);
			File file = new File(fd.getDirectory(), fd.getFile());
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			// String lineString;
			// lineString=jTxtArea.getText();
			// lineString=lineString.replace("/n");
			bw.write(jTxtArea.getText(), 0, jTxtArea.getText().length());
			this.setTitle(fd.getFile());
			bw.close();
			fw.close();
			fd.dispose();
			textIsSaved = true;
			textIsChanged = false;
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// 数据的拷贝，送入剪贴板
	void copySelectedData() {
		String S;
		S = jTxtArea.getSelectedText();
		text = new StringSelection(S); // 将选中的文本放入text全局变量中
		clipBoard.setContents(text, null);
	}

	// 数据的拷贝
	void jMenuEditCopy_actionPerformed(ActionEvent actionEvent) {
		copySelectedData();
	}

	// 数据的粘贴
	void jMenuEditPaste_actionPerformed(ActionEvent actionEvent) {
		Transferable contents = clipBoard.getContents(new Button());
		DataFlavor flavor = DataFlavor.stringFlavor;
		contents.isDataFlavorSupported(flavor);
		try {
			// 取得剪贴板中的内容
			String tText = (String) contents.getTransferData(flavor);
			// 用替换选中文字的方式来粘贴，如果没有选中文件，那么选择的开始和结束都是一样的
			// 取得选中点前面的文字和后面的文字，再将从剪贴板取得的文字三都结合起来就起到了剪
			// 贴的目的
			int selStart, selEnd;
			selStart = jTxtArea.getSelectionStart();
			selEnd = jTxtArea.getSelectionEnd();
			if (selStart == selEnd) {
				// 采用insert方法可以文本可以自动获得光标，而不用手工去获得光标
				jTxtArea.insert(tText, selStart);
			} else {

				String allText, beforeText, afterText;
				allText = jTxtArea.getText();
				beforeText = allText.substring(0, selStart);
				afterText = allText.substring(selEnd, allText.length());
				allText = beforeText + tText + afterText;
				jTxtArea.setText(allText);
				// 失去光标，手工获得光标，但是这时的光标位置是在文本尾
				jTxtArea.setFocusable(true);
				// jTxtArea.insert(tText,selStart);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 全选
	void jMenuEditSelectAll_actionPerformed(ActionEvent actionEvent) {
		jTxtArea.selectAll();
	}

	// 删除选中
	void jMenuEditDel_actionPerformed(ActionEvent actionEvent) {
		int selStart, selEnd;
		selStart = jTxtArea.getSelectionStart();
		selEnd = jTxtArea.getSelectionEnd();
		String allText, beforeText, afterText;
		allText = jTxtArea.getText();
		beforeText = allText.substring(0, selStart);
		afterText = allText.substring(selEnd, allText.length());
		allText = beforeText + afterText;
		jTxtArea.setText(allText);
		// 失去光标，手工获得光标，但是这时的光标位置是在文本尾
		jTxtArea.setFocusable(true);

	}

	void jMenuEditCut_actionPerformed(ActionEvent actionEvent) {
		int selStart, selEnd;
		String beforeText, afterText, allText, selText;
		// 取得选中的文字
		selText = jTxtArea.getSelectedText();
		allText = jTxtArea.getText();
		// 取得选中文字的开始点
		selStart = jTxtArea.getSelectionStart();
		// 取得选中文字的结束点
		selEnd = jTxtArea.getSelectionEnd();
		// 取得选中文字前面的文字
		beforeText = allText.substring(0, selStart);
		// 取得选中文字后面的文字
		afterText = allText.substring(selEnd, allText.length());

		text = new StringSelection(selText); // 将选中的文本放入text全局变量中
		clipBoard.setContents(text, null);

		allText = beforeText + afterText;
		jTxtArea.setText(allText);
	}

	// 自动换行
	void jMenuEditAutoWrap_actionPerformed(ActionEvent actionEvent) {
		if (jTxtArea.getLineWrap()) {
			jTxtArea.setLineWrap(false);
		} else {
			jTxtArea.setLineWrap(true);
		}
	}

	// 格式化文本的字体
	void jMenuFormatColor_actionPerformed(ActionEvent actionEvent) {
		// FontDialog fd1=new FontD
	}

	// 调用帮助文档
	void jMenuHelpDocument_actionPerformed(ActionEvent actionEvent) {
		Runtime ec = Runtime.getRuntime();
		try {
			ec.exec("notepad.chm");
		} catch (IOException ex) {
		}
	}
}

class FrameNotepad_jMenuFileExit_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFileExit_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileExit_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuHelpAbout_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuHelpAbout_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuHelpAbout_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuFileCreat_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFileCreat_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileCreat_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuFileOpen_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFileOpen_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileOpen_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuFileSave_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFileSave_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileSave_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuFileSaveAs_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFileSaveAs_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileSaveAs_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditCopy_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditCopy_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditCopy_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditPaste_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditPaste_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditPaste_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditSelectAll_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditSelectAll_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditSelectAll_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditDel_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditDel_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditDel_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditCut_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditCut_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditCut_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuEditAutoWrap_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuEditAutoWrap_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuEditAutoWrap_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuFormatColor_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuFormatColor_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFormatColor_actionPerformed(actionEvent);
	}
}

class FrameNotepad_jMenuHelpDocument_ActionAdapter implements ActionListener {
	private FrameNotepad adaptee;

	FrameNotepad_jMenuHelpDocument_ActionAdapter(FrameNotepad adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuHelpDocument_actionPerformed(actionEvent);
	}
}
