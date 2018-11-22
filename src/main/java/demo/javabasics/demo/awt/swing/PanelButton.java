package demo.javabasics.demo.awt.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PanelButton extends Frame {
	private static final long serialVersionUID = 1L;
	private Panel borderPanel;
	private Panel flowPanel;
	private Panel gridPanel;
	private Panel cardPanel;

	public PanelButton(String title) {
		super(title);
		setSize(600, 400);
		setLocation(100, 100);

		setBorderLayoutPanel();
		setFlowLayoutPanel();
		setGridLayoutPanel();
		setCardLayoutPanel();

		setLayout(new GridLayout(2, 2));
		add(borderPanel);
		add(flowPanel);
		add(gridPanel);
		add(cardPanel);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void setBorderLayoutPanel() {
		borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());
		Button btn1 = new Button("North");
		Button btn2 = new Button("South");
		Button btn3 = new Button("West");
		Button btn4 = new Button("East");
		Button btn5 = new Button("Center");
		borderPanel.add(btn1, BorderLayout.NORTH);
		borderPanel.add(btn2, BorderLayout.SOUTH);
		borderPanel.add(btn3, BorderLayout.WEST);
		borderPanel.add(btn4, BorderLayout.EAST);
		borderPanel.add(btn5, BorderLayout.CENTER);
	}

	public void setFlowLayoutPanel() {
		flowPanel = new Panel();
		Button btn1 = new Button("my");

		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Button) e.getSource()).setLabel("wei");
			}
		});
		Button btn2 = new Button("win");
		flowPanel.add(btn1);
		flowPanel.add(btn2);
	}

	public void setGridLayoutPanel() {
		gridPanel = new Panel();
		gridPanel.setLayout(new GridLayout(2, 2));
		Button btn1 = new Button("b1");
		Button btn2 = new Button("b2");
		Button btn3 = new Button("b3");
		Button btn4 = new Button("b4");
		gridPanel.add(btn1);
		gridPanel.add(btn2);
		gridPanel.add(btn3);
		gridPanel.add(btn4);
	}

	public void setCardLayoutPanel() {
		cardPanel = new Panel();
		cardPanel.setLayout(new CardLayout());
		Button btn1 = new Button("黑桃A");
		Button btn2 = new Button("红桃K");
		cardPanel.add(btn1, "1");
		cardPanel.add(btn2, "2");
	}

	public static void main(String[] args) {
		YourFrame yf = new YourFrame("http://www.mybole.com.cn");
		yf.setVisible(true);
	}
}
