package demo.javabasics.demo.awt.swing;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MyFrame {

	public static void main(String[] args) {
		Frame f = new Frame("mybole");
		f.setSize(600, 400);
		f.setLocation(100, 100);
		f.setBackground(Color.blue);
		f.setLayout(new GridLayout(3, 2, 10, 20));
		// f.setLayout(new FlowLayout(FlowLayout.LEADING));
		// f.setLayout(new BorderLayout(10,10));
		Button b1 = new Button("north");
		Button b2 = new Button("east");
		Button b3 = new Button("west");

		b1.setSize(20, 10);
		b1.setBackground(Color.red);
		f.add(b1, "South");
		f.add(b2, "North");
		f.add(b3, "West");
		// f.addWindowListener(new MyWindowListener());
		// f.addWindowListener(new YourWindowListener());
		// f.addWindowListener(new HisWindowListener());
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});

		// f.show();
		f.setVisible(true);

	}

}

class MyWindowListener implements WindowListener {
	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}
}

class YourWindowListener implements WindowListener {

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}

}

class HisWindowListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
}
