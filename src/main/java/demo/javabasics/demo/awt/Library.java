package demo.javabasics.demo.awt;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class Library extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = Logger.getLogger(Library.class);

	static String DRIVER = "com.mysql.jdbc.Driver";
	static String URL = "jdbc:mysql://localhost:3306/Library?&characterEncoding=GBK";
	static String username = "root";
	static String password = null;
	Connection ConnObj;
	Statement SQLStatement;
	ResultSet RS;
	int CurrentRow = 1;
	static final int BROWSE = 0; // 浏览
	static final int PREINSERT = 1; // 准备新增
	static final int TOINSERT = 2; // 新增
	static final int TOUPDATE = 3; // 修改
	static final int TOSELECT = 4; // 查询
	static final int SELECTED = 5; // 已查询
	int workingMode = Library.BROWSE;
	// ==========================
	Library frame;
	JPanel contentPane;
	Panel panel1;
	Label L1, L2, L3, L4, L5;
	TextField T1, T2, T3, T4;
	Button B1, B2, B3, B4, B5, B6, B7, B8, B9, B10;

	public Library() {
		frame = this;
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(null);
		// ===================================
		panel1 = new Panel();
		panel1.setBounds(0, 0, 380, 160);
		panel1.setLayout(null);
		contentPane.add(panel1, null);
		L1 = new Label("状态:   浏览模式");
		L1.setBounds(20, 10, 350, 25);
		panel1.add(L1, null);
		L2 = new Label("ISBN: ");
		L2.setBounds(20, 40, 50, 25);
		panel1.add(L2, null);
		T1 = new TextField();
		T1.setBounds(70, 40, 300, 25);
		T1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 在 T1 按 [Enter]
															// 键会触发此事件
				T1_actionPerformed(e);
			}
		});
		panel1.add(T1, null);
		L3 = new Label("书名: ");
		L3.setBounds(20, 70, 50, 25);
		panel1.add(L3, null);
		L4 = new Label("作者: ");
		L4.setBounds(20, 100, 50, 25);
		panel1.add(L4, null);
		L5 = new Label("价格: ");
		L5.setBounds(20, 130, 50, 25);
		panel1.add(L5, null);
		T2 = new TextField();
		T2.setBounds(70, 70, 300, 25);
		T2.addActionListener(new myActionListener()); // 按 [Enter] 键
		panel1.add(T2, null);
		T3 = new TextField();
		T3.setBounds(70, 100, 300, 25);
		T3.addActionListener(new myActionListener()); // 按 [Enter] 键
		panel1.add(T3, null);
		T4 = new TextField();
		T4.setBounds(70, 130, 300, 25);
		T4.addActionListener(new myActionListener()); // 按 [Enter] 键
		panel1.add(T4, null);
		// =============================================
		B1 = new Button("第一条");
		B1.setBounds(30, 180, 65, 20);
		B1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B1_mouseClicked(e);
			}
		});
		contentPane.add(B1);
		B2 = new Button("上一条");
		B2.setBounds(100, 180, 65, 20);
		B2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B2_mouseClicked(e);
			}
		});
		contentPane.add(B2);
		B3 = new Button("下一条");
		B3.setBounds(170, 180, 65, 20);
		B3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B3_mouseClicked(e);
			}
		});
		contentPane.add(B3);
		B4 = new Button("最末条");
		B4.setBounds(240, 180, 65, 20);
		B4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B4_mouseClicked(e);
			}
		});
		contentPane.add(B4);
		B5 = new Button("新增");
		B5.setBounds(30, 210, 65, 20);
		B5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B5_mouseClicked(e);
			}
		});
		contentPane.add(B5);
		B6 = new Button("修改");
		B6.setBounds(100, 210, 65, 20);
		B6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B6_mouseClicked(e);
			}
		});
		contentPane.add(B6);
		B7 = new Button("删除");
		B7.setBounds(170, 210, 65, 20);
		B7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B7_mouseClicked(e);
			}
		});
		contentPane.add(B7);
		B8 = new Button("查询");
		B8.setBounds(240, 210, 65, 20);
		B8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B8_mouseClicked(e);
			}
		});
		contentPane.add(B8);
		B9 = new Button("确定");
		B9.setBounds(310, 180, 65, 20);
		B9.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B9_mouseClicked(e);
			}
		});
		contentPane.add(B9);
		B10 = new Button("离开");
		B10.setBounds(310, 210, 65, 20);
		B10.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				B10_mouseClicked(e);
			}
		});
		contentPane.add(B10);
		// ==================
		// 控制画面操作过程
		panel1.setEnabled(false); // 光标无法进入其内的 T1~T4
		B9.setEnabled(false); // [确定]
		B10.setEnabled(false); // [离开]
		// ==================
		this.setBounds(200, 100, 415, 280);
		this.setTitle("新增、修改、删除、查询");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0); // 结束
			}
		});
		this.setVisible(true);
		// ============== 连接数据库，并浏览 ==============

		try {
			Class.forName(DRIVER);
			// 载入 JDBC Driver
			ConnObj = DriverManager.getConnection(URL);
			SQLStatement = ConnObj.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			RS = SQLStatement.executeQuery("SELECT * FROM books");
			if (RS.next()) { // RS 内至少有一条记录，才会返回 true
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
			} else
				JOptionPane.showMessageDialog(this, "myBook数据库内,无任何数据");
		} catch (Exception O) {
			JOptionPane.showMessageDialog(frame, O.getMessage());
		}
	} // 构造函数 public Library() end
		// ===============================================

	class myActionListener implements ActionListener { // T2、T3、T4 都加入此
														// ActionListener
		public void actionPerformed(ActionEvent e) {
			((TextField) e.getSource()).transferFocus();
			// 按 [Enter] 键, focus 往下一个组件
		}
	}// class myActionListener end
		// ===============================================

	void B1_mouseClicked(MouseEvent e) { // 第一条
		try {
			RS.first();
			T1.setText(RS.getString("ISBN"));
			T2.setText(RS.getString("BookName"));
			T3.setText(RS.getString("Author"));
			T4.setText(String.valueOf(RS.getInt("Price")));
		} catch (Exception O) {
		}
	} // B1_mouseClicked(MouseEvent e) end
		// ===============================================

	void B2_mouseClicked(MouseEvent e) { // 上一条
		try {
			if (RS.previous()) {
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
			} else {
				RS.first();
				JOptionPane.showMessageDialog(B2, "已到第一条");
			}
		} catch (SQLException O) {
		}
	} // B2_mouseClicked(MouseEvent e) end
		// ===============================================

	void B3_mouseClicked(MouseEvent e) { // 下一条
		try {
			if (RS.next()) {
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
			} else {
				RS.last();
				JOptionPane.showMessageDialog(B3, "已到最末条");
			}
		} catch (SQLException O) {
		}
	} // B3_mouseClicked(MouseEvent e) end
		// ===============================================

	void B4_mouseClicked(MouseEvent e) { // 最末条
		try {
			RS.last();
			T1.setText(RS.getString("ISBN"));
			T2.setText(RS.getString("BookName"));
			T3.setText(RS.getString("Author"));
			T4.setText(String.valueOf(RS.getInt("Price")));
		} catch (SQLException O) {
		}
	} // B4_mouseClicked(MouseEvent e) end
		// ===============================================

	void B5_mouseClicked(MouseEvent e) { // 新增一条数据
		try {
			CurrentRow = RS.getRow(); // 进行新增之前指到的这一条
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B5, O.getMessage());
		}

		workingMode = Library.PREINSERT;
		paintView();
		T1.setText("");// 清空所有 TextFiled
		T2.setText("");
		T3.setText("");
		T4.setText("");
		T1.requestFocus();// 光标会进到 T1
	} // B5_mouseClicked(MouseEvent e) end
		// ===============================================

	void B6_mouseClicked(MouseEvent e) { // 修改一条数据
		workingMode = Library.TOUPDATE;
		paintView();
		try {
			CurrentRow = RS.getRow(); // 现在更新这条的位置
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B6, O.getMessage());
		}
		T2.requestFocus();
	} // B6_mouseClicked(MouseEvent e) end
		// ===============================================

	void B7_mouseClicked(MouseEvent e) { // 删除一条数据
		try {
			// 记删除一条后要指到的位置
			if (RS.isLast() && (!RS.isFirst())) { // 若删除是: 最末条且非第一条
				CurrentRow = RS.getRow() - 1; // 删除后要移到它的前一条
			} else
				CurrentRow = RS.getRow();
			// 包括: 不是最末条、是最末条又正是是第一条

			int choice = JOptionPane.showConfirmDialog(frame, "确定要删除现在这一条吗？");
			if (choice == JOptionPane.YES_OPTION) { // 若用户单击 “是(Y)” 按钮
				String deleteSQL = "DELETE FROM books WHERE ISBN='"
						+ T1.getText().trim() + "'";
				logger.info(deleteSQL); // 可用于测试之时
				SQLStatement.executeUpdate(deleteSQL);// 对数据库进行删除
				RS = SQLStatement.executeQuery("SELECT * FROM books");
				if (RS.absolute(CurrentRow)) { // 移到刚才指的位置，若是删除最末条，会移到它的前一条
					T1.setText(RS.getString("ISBN"));
					T2.setText(RS.getString("BookName"));
					T3.setText(RS.getString("Author"));
					T4.setText(String.valueOf(RS.getInt("Price")));
				} else {
					T1.setText("");
					T2.setText("");
					T3.setText("");
					T4.setText("");
				}
			}
		} catch (Exception O) {
			JOptionPane.showMessageDialog(B6, O.getMessage());
		}
	} // B7_mouseClicked(MouseEvent e) end
		// ===============================================

	void B8_mouseClicked(MouseEvent e) { // 查询数据
		workingMode = Library.TOSELECT;
		paintView();
		try {
			CurrentRow = RS.getRow(); // 目前指到的这一条
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B6, O.getMessage());
		}
	} // B8_mouseClicked(MouseEvent e) end
		// ===============================================

	/*
	 * 用户单击“确定”按钮的情况:  1. 进入新增模式，确定要新增 2. 进入修改模式，确定要修改 3. 进入查询模式，确定要查询
	 */
	void B9_mouseClicked(MouseEvent e) { // 确定操作，用于新增、修改、查询
		switch (workingMode) {
			case Library.TOINSERT:
				ToInsert(); // 确定要进行新增的操作
				break;
			case Library.TOUPDATE:
				ToUpdate(); // 确定要进行修改的操作
				break;
			case Library.TOSELECT:
				ToSelect(); // 确定要进行查询的操作
				break;
			default:
				JOptionPane.showMessageDialog(B9, "目的不明！");
		}
		paintView(); // 控制画面
	} // B9_mouseClicked(MouseEvent e) end
		// ===============================================

	/*
	 * 用户单击“离开”按钮的情况:  1. 进入新增模式，但不要新增 2. 进入修改模式，但不要修改 3. 进入查询模式，但不要查询，或是已经查询过了
	 */
	void B10_mouseClicked(MouseEvent e) { // 离开 新增、修改、查询 模式
		try {
			RS = SQLStatement.executeQuery("SELECT * FROM books");
			if (RS.absolute(CurrentRow)) {
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
			}
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B10, O.getMessage());
		}

		// 无论对数据库的新增、修改、查询操作是否成功
		workingMode = Library.BROWSE;
		CurrentRow = 1;
		paintView(); // 控制画面
	} // B10_mouseClicked(MouseEvent e) end
		// ===============================================

	void T1_actionPerformed(ActionEvent e) { // 在 T1 内按 [Enter] 键会调用此方法
		if (workingMode != Library.PREINSERT) { // TOINSERT、TOUPDATE、TOSELECT
												// 状态才有可能
			T1.transferFocus(); // Focus 移到下一个组件
			return;
		} else { // PREINSERT 状态
			if (T1.getText().trim().equals("")) {
				T1.requestFocus(); // 令 T1 取得 Focus
				return;
			}
		}
		String newISBN = T1.getText().trim();
		if (newISBN.length() > 13) { // 数据不要超过在数据表设定的字段长度
			JOptionPane.showMessageDialog(T1, "ISBN 字段最大长度为 13！");
			return;
		}
		// 新增时先检查该条是否已存在
		try {
			RS = SQLStatement.executeQuery("SELECT * FROM books WHERE ISBN='"
					+ newISBN + "'");
			if (RS.next()) { // 显示已存在的条笔数据
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
				JOptionPane.showMessageDialog(T1, "此条数据已存在");
			} else {
				workingMode = Library.TOINSERT;// 不得再更改新的一条 ISBN
				T1.setText(newISBN);
			}
		} catch (SQLException t) {
			logger.error("Exception in T1_actionPerformed()", t);
		}
		paintView(); // 控制画面
		T1.transferFocus(); // Focus 移到下个组件
	} // T1_actionPerformed(ActionEvent e) end

	// ====================================
	void ToInsert() { // 实际进行新增
		int checkPrice; // 用来检查价格
		int insertCount = 0; //
		if (T2.getText().trim().equals("") || T3.getText().trim().equals("")
				|| T4.getText().trim().equals("")
				|| T2.getText().trim().length() > 50
				|| T3.getText().trim().length() > 30) { // 数据不要超过数据表设定的字段长度
			JOptionPane.showMessageDialog(B9, "有数据尚未填正确的值！");
			return;
		}

		try { // 检查价格
			checkPrice = Integer.parseInt(T4.getText().trim());
			logger.info("checkPrice = " + checkPrice + " in method ToUpdate()");
		} catch (NumberFormatException O) {
			JOptionPane.showMessageDialog(B9, "价格请填入整数，最大为 2147483647！");
			return;
		}
		// ===============================
		String insertSQL = "INSERT INTO books(ISBN,BookName,Author,Price) "
				+ "VALUES('" + T1.getText().trim() + "','"
				+ T2.getText().trim() + "','" + T3.getText().trim() + "',"
				+ T4.getText().trim() + ")";
		try { // 对数据库进行新增
			logger.info(insertSQL); // 可用于测试时
			insertCount = SQLStatement.executeUpdate(insertSQL);
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B9, O.getMessage());
		}

		try {
			RS = SQLStatement.executeQuery("SELECT * FROM books");
			if (insertCount != 0) { // 若新增成功
				logger.info("Insert OK"); // 可用于测试时
				RS.last(); // 若新增成功，就移到最末条
			} else
				RS.absolute(CurrentRow);// 若失败则移到刚才浏览时指的那条

			T1.setText(RS.getString("ISBN"));
			T2.setText(RS.getString("BookName"));
			T3.setText(RS.getString("Author"));
			T4.setText(String.valueOf(RS.getInt("Price")));
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B9, O.getMessage());
		}

		// 无论对数据库的新增操作是否成功
		workingMode = Library.BROWSE;
		CurrentRow = 1;
	} // ToInsert() end
		// =====================================================

	void ToUpdate() { // 实际进行修改
		int checkPrice; // 用来检查价格
		if (T2.getText().trim().equals("") || T3.getText().trim().equals("")
				|| T4.getText().trim().equals("")
				|| T2.getText().trim().length() > 50
				|| T3.getText().trim().length() > 30) { // 数据不可超过字段设定的长度
			JOptionPane.showMessageDialog(null, "有数据尚未填正确的值！");
			return; // 不要继续下去
		}
		try { // 检查价格
			checkPrice = Integer.parseInt(T4.getText().trim());
			logger.info("checkPrice = " + checkPrice + " in method ToUpdate()");
		} catch (NumberFormatException ecp) { // 所填的文字，字面意义若非 int 整数，转型会失败
			JOptionPane.showMessageDialog(null, "价格请填入 0~2147483647 的整数！");
			return; // 不要继续下去
		}
		// ===============================
		String updateSQL = "UPDATE books SET BookName='" + T2.getText().trim()
				+ "',Author='" + T3.getText().trim() + "',Price="
				+ T4.getText().trim() + " WHERE ISBN='" + T1.getText().trim()
				+ "'";
		logger.info(updateSQL); // 可用于测试时
		try { // 对数据库进行修改
			SQLStatement.executeUpdate(updateSQL);
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B9, O.getMessage());
		}

		try {
			RS = SQLStatement.executeQuery("SELECT * FROM books");
			RS.absolute(CurrentRow);// 移到修改的那条
			T1.setText(RS.getString("ISBN"));
			T2.setText(RS.getString("BookName"));
			T3.setText(RS.getString("Author"));
			T4.setText(String.valueOf(RS.getInt("Price")));
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B9, O.getMessage());
		}

		// 无论对数据库的修改操作是否成功
		workingMode = Library.BROWSE;
		CurrentRow = 1;
	} // ToUpdate() end
		// ======================================================

	void ToSelect() { // 实际进行查询
		String condi = "";
		if (T1.getText().trim().equals("") && T2.getText().trim().equals("")
				&& T3.getText().trim().equals("")
				&& T4.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(B8, "至少要填一个字段当条件");
			return;
		}
		// ===================================
		if (!T1.getText().trim().equals(""))
			condi = " ISBN='" + T1.getText().trim() + "'";
		// 若用户有填 ISBN 当条件
		if (!T2.getText().trim().equals("")) { // 若用户有填 BookName 当条件
			if (condi.equals(""))
				condi = " BookName='" + T2.getText().trim() + "'";
			else
				condi = condi + " AND BookName='" + T2.getText().trim() + "'";
		}
		if (!T3.getText().trim().equals("")) { // 若用户有填 Author 当条件
			if (condi.equals(""))
				condi = " Author='" + T3.getText().trim() + "'";
			else
				condi = condi + " AND Author='" + T3.getText().trim() + "'";
		}
		if (!T4.getText().trim().equals("")) { // 若用户有填 Price 当条件
			if (condi.equals(""))
				condi = " Price=" + T4.getText().trim();
			else
				condi = condi + " AND Price=" + T4.getText().trim();
		}

		String selectSQL = "SELECT * FROM books WHERE" + condi;
		logger.info(selectSQL); // 可用于测试时
		try {
			RS = SQLStatement.executeQuery(selectSQL);
			if (RS.next()) { // 有查到一条以上符合的记录
				T1.setText(RS.getString("ISBN"));
				T2.setText(RS.getString("BookName"));
				T3.setText(RS.getString("Author"));
				T4.setText(String.valueOf(RS.getInt("Price")));
			} else { // 没查到符合的记录
				T1.setText("");
				T2.setText("");
				T3.setText("");
				T4.setText("");
			}
		} catch (SQLException O) {
			JOptionPane.showMessageDialog(B9, O.getMessage());
		}

		// 无论对数据库的查询是否成功
		workingMode = Library.SELECTED;// 已查询
		// 查完还要回到之前记录的 CurrentRow
	} // ToSelect() end
		// ===============================================

	void paintView() { // 控制画面，即控制用户的操作过程
		switch (workingMode) {
			case Library.BROWSE:
				L1.setText("状态:   浏览模式");
				break;
			case Library.PREINSERT:
				L1.setText("状态:   新增模式 － 输入 ISBN 后按键盘 [Enter] 键");
				break;
			case Library.TOINSERT:
				L1.setText("状态:   新增模式");
				break;
			case Library.TOUPDATE:
				L1.setText("状态:   修改模式");
				break;
			case Library.TOSELECT:
				L1.setText("状态:   查询模式");
				break;
			case Library.SELECTED:
				L1.setText("状态:   查询结果");
				break;
		}
		// =======================================
		if ((workingMode == Library.TOINSERT)
				|| (workingMode == Library.TOUPDATE))
			T1.setEditable(false); //
		else
			T1.setEditable(true);
		// =======================================
		if (workingMode == Library.PREINSERT) {
			T2.setEnabled(false);
			T3.setEnabled(false);
			T4.setEnabled(false);
		} else {
			T2.setEnabled(true);
			T3.setEnabled(true);
			T4.setEnabled(true);
		}
		// =======================================
		if ((workingMode == Library.BROWSE)
				|| (workingMode == Library.SELECTED)) {
			panel1.setEnabled(false); // 此时光标不可进其内的 T1~T4
			B1.setEnabled(true); // [第一条]
			B2.setEnabled(true); // [上一条]
			B3.setEnabled(true); // [下一条]
			B4.setEnabled(true); // [最末条]
		} else {
			panel1.setEnabled(true); // 此时光标可进其内的 T1~T4
			B1.setEnabled(false); // [第一条]
			B2.setEnabled(false); // [上一条]
			B3.setEnabled(false); // [下一条]
			B4.setEnabled(false); // [最末条]
		}
		// =======================================
		if (workingMode == Library.BROWSE) {
			B5.setEnabled(true); // [新增]
			B6.setEnabled(true); // [修改]
			B7.setEnabled(true); // [删除]
			B8.setEnabled(true); // [查询]
		} else {
			B5.setEnabled(false); // [新增]
			B6.setEnabled(false); // [修改]
			B7.setEnabled(false); // [删除]
			B8.setEnabled(false); // [查询]
		}
		// =======================================
		if ((workingMode == Library.TOINSERT)
				|| (workingMode == Library.TOUPDATE)
				|| (workingMode == Library.TOSELECT))
			B9.setEnabled(true); // [确定]
		else
			B9.setEnabled(false); // [确定]
		// =======================================
		if (workingMode != Library.BROWSE)
			B10.setEnabled(true); // [离开]
		else
			B10.setEnabled(false); // [离开]
	}// paintView() end
		// ===============================================

	public static void main(String[] args) {
		new Library();
	}
}// public class Library end
