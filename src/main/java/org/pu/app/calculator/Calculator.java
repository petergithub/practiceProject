package org.pu.app.calculator;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Calculator extends Frame {
	private static final long serialVersionUID = 1L;
	double result = 0, tmp = 0;
	int opId = 0; // 0:无,1:+,2:-,3:*,4:/
	String keyin = "0"; // 要在 T1 内显示的内容
	// ============================================
	JTextField T1;
	Button B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12, B13, B14, B15, B16, B17, B18, B19;

	public Calculator() {
		this.setLayout(null); // 手动指定各组件的位置
		T1 = new JTextField(keyin);
		T1.setBounds(20, 30, 158, 28);
		T1.setHorizontalAlignment(JTextField.RIGHT);// 字靠右
		T1.setEditable(false);// 不许在 T1 内编辑
		T1.setBackground(Color.WHITE); // 设定背景颜色
		this.add(T1);

		B1 = new Button("1");
		B1.setBounds(20, 65, 35, 25);
		B1.setForeground(Color.BLUE);
		B1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“1”
				setKeyin("1");
			}
		});
		this.add(B1);

		B2 = new Button("2");
		B2.setBounds(60, 65, 35, 25);
		B2.setForeground(Color.BLUE);
		B2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“2”
				setKeyin("2");
			}
		});
		this.add(B2);

		B3 = new Button("3");
		B3.setBounds(100, 65, 35, 25);
		B3.setForeground(Color.BLUE);
		B3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“3”
				setKeyin("3");
			}
		});
		this.add(B3);

		B4 = new Button("4");
		B4.setBounds(20, 95, 35, 25);
		B4.setForeground(Color.BLUE);
		B4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“4”
				setKeyin("4");
			}
		});
		this.add(B4);

		B5 = new Button("5");
		B5.setBounds(60, 95, 35, 25);
		B5.setForeground(Color.BLUE);
		B5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“5”
				setKeyin("5");
			}
		});
		this.add(B5);

		B6 = new Button("6");
		B6.setBounds(100, 95, 35, 25);
		B6.setForeground(Color.BLUE);
		B6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“6”
				setKeyin("6");
			}
		});
		this.add(B6);

		B7 = new Button("7");
		B7.setBounds(20, 125, 35, 25);
		B7.setForeground(Color.BLUE);
		B7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“7”
				setKeyin("7");
			}
		});
		this.add(B7);

		B8 = new Button("8");
		B8.setBounds(60, 125, 35, 25);
		B8.setForeground(Color.BLUE);
		B8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“8”
				setKeyin("8");
			}
		});
		this.add(B8);

		B9 = new Button("9");
		B9.setBounds(100, 125, 35, 25);
		B9.setForeground(Color.BLUE);
		B9.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“9”
				setKeyin("9");
			}
		});
		this.add(B9);

		B10 = new Button("0");
		B10.setBounds(20, 155, 35, 25);
		B10.setForeground(Color.BLUE);
		B10.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“0”
				setKeyin("0");
			}
		});
		this.add(B10);

		B11 = new Button("+");
		B11.setBounds(140, 65, 35, 25);
		B11.setForeground(Color.RED);
		B11.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“+”
				count();
				opId = 1;
				keyin = "";
				setTitle("传统计算器 +");
			}
		});
		this.add(B11);

		B12 = new Button("-");
		B12.setBounds(140, 95, 35, 25);
		B12.setForeground(Color.RED);
		B12.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“-”
				count();
				opId = 2;
				keyin = "";
				setTitle("传统计算器 -");
			}
		});
		this.add(B12);

		B13 = new Button("*");
		B13.setBounds(140, 125, 35, 25);
		B13.setForeground(Color.RED);
		B13.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“*”
				count();
				opId = 3;
				keyin = "";
				setTitle("传统计算器 *");
			}
		});
		this.add(B13);

		B14 = new Button("/");
		B14.setBounds(140, 155, 35, 25);
		B14.setForeground(Color.RED);
		B14.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“/”
				count();
				opId = 4;
				keyin = "";
				setTitle("传统计算器 /");
			}
		});
		this.add(B14);

		B15 = new Button(".");
		B15.setBounds(60, 155, 35, 25);
		B15.setForeground(Color.BLUE);
		B15.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“.”
				setKeyin(".");
			}
		});
		this.add(B15);

		B16 = new Button("C");
		B16.setBounds(100, 155, 35, 25);
		B16.setForeground(Color.RED);
		B16.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“C”清除
				opId = 0;
				tmp = 0;
				result = 0;
				T1.setText(keyin = "0");
				setTitle("传统计算器");
			}
		});
		this.add(B16);

		B17 = new Button("+/-");
		B17.setBounds(20, 185, 35, 25);
		B17.setForeground(Color.BLUE);
		B17.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“+/-”，对结果值、或正在输入的加(减、乘、除)数都有效
				if (keyin.equals("")) { // 若单击了“+”、“-”、“*”、“/”按钮后尚未输入数字就单击“+/-”按钮
					keyin = "0";
				} else {
					if (keyin.length() > 0 && keyin.charAt(0) == '-')
						keyin = keyin.substring(1); // 去头的 - 号
					else if (!keyin.equals("0")) keyin = "-" + keyin; // 头加 -
					// 号，"0"
					// 除外
				}
				T1.setText(keyin);
			}// void mouseClicked(MouseEvent e) end
		});
		this.add(B17);

		B18 = new Button("←");
		B18.setBounds(60, 185, 35, 25);
		B18.setForeground(Color.RED);
		B18.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 单击按钮“←”倒退
				if (keyin.equals("")) return; // 此情况不提供倒退功能
				// 单击了按钮“+”、“-”、“*”、“/”其中之一，但还未开始输入数字
				// =============================================
				keyin = T1.getText();
				if (!keyin.equals("0")) {
					if (keyin.length() > 2) {
						if (keyin.charAt(keyin.length() - 2) == 'E')
							// 倒数第二字符是'E'，表示指数会被删完
							keyin = keyin.substring(0, keyin.length() - 2);
						// 'E' 也要删掉才行，故删2字符
						else
							keyin = keyin.substring(0, keyin.length() - 1);
					} else if (keyin.length() == 2) // 剩2个字
					{
						if (keyin.length() > 0 && keyin.charAt(0) == '-')
							keyin = "0"; // 是个位负数，如: -5
						else
							keyin = keyin.substring(0, keyin.length() - 1);
						// 删后面一个字
					} else
						// 剩1个字
						keyin = "0";

					T1.setText(keyin); // 显示删除后的结果
				}
			} // void mouseClicked(MouseEvent e) end
		});
		this.add(B18);

		B19 = new Button("=");
		B19.setBounds(100, 185, 75, 25);
		B19.setForeground(Color.RED);
		B19.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 按[=]
				keyin = count();
				opId = 0;
				setTitle("传统计算器");
			}
		});
		this.add(B19);
		// ===========================================
		this.setBounds(200, 100, 200, 230);
		this.setTitle("传统计算器");
		// this.setBackground(Color.PINK); //设背景颜色
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0); // 结束系统
			}
		});
		this.setVisible(true);
	}

	public void setKeyin(String s) {
		if (s.equals(".")) {
			if (keyin.indexOf('.') != -1) return; // 已经有小数点
			if (keyin.equals("")) keyin = "0";
		}

		if (keyin.equals("0")) { // 现在 keyin 为 0
			if (s.equals("0"))
				return; // 原来的 keyin 已是 "0" 后面不能再加 "0"
			else if (!s.equals(".")) keyin = ""; // 现在输入的若不是"."，不能加在"0"之后
		}
		keyin = keyin + s; // 将现在输入的数字加在 keyin 之后
		T1.setText(keyin);
	}// void setKeyin(String s) end

	public String count() {
		String resultStr;
		System.out.println("opId = " + opId + " keyin =" + keyin); // 用于测试
		if (!keyin.equals("")) // 有输入一个加、减、乘或除数
			tmp = Double.parseDouble(keyin); // 若无输入则仍tmp=0
		switch (opId) { // 作 + - * / 那一种运算 val op val action
			case 1: // +
				result = result + tmp; // 加法运算
				break;
			case 2: // -
				result = result - tmp; // 减法运算
				break;
			case 3: // *
				if (!keyin.equals("")) // 有输入乘数
					result = result * tmp; // 乘法运算
				break;
			case 4: // /
				if (!keyin.equals("")) { // 有输入除数
					if (tmp == 0) // 不可除 0
						JOptionPane.showMessageDialog(null, "无法除零");
					else
						result = result / tmp; // 除法运算
				}
				break;
			case 0: // val action
				result = tmp;
				// 把目前的 keyin 记在 result
		} // end of switch(opId)
			// ========================================
		if (result == (long) result)
			resultStr = String.valueOf((long) result); // 结果是整数
		else
			resultStr = String.valueOf(result);
		T1.setText(resultStr); // 将计算后的结果显示在 T1 组件
		tmp = 0; // tmp 归零
		return resultStr;
	}// String count() end

	public static void main(String[] para) {
		new Calculator();
	}
}
