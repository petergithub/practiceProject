package demo.javabasics.demo.awt.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonTest1 {
	public static void main(String[] args) {
		ButtonFrame frame = new ButtonFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

// Frame
class ButtonFrame1 extends JFrame {
	private static final long serialVersionUID = 1L;

	public ButtonFrame1() {
		setTitle("ButtonTest");
		setLocation(434, 223);
		setSize(300, 200);

		// add panel to frame
		ButtonPanel panel = new ButtonPanel();
		add(panel);
	}
}

// Panel
class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public ButtonPanel() {
		JButton yellowButton = new JButton("Yellow");
		JButton blueButton = new JButton("Blue");
		JButton redButton = new JButton("Red");

		// add button to panel
		add(yellowButton);
		add(blueButton);
		add(redButton);

		// create button actions
		ColorAction yellowAction = new ColorAction(Color.YELLOW);
		ColorAction blueAction = new ColorAction(Color.BLUE);
		ColorAction redAction = new ColorAction(Color.RED);

		// associate actions with buttons
		yellowButton.addActionListener(yellowAction);
		blueButton.addActionListener(blueAction);
		redButton.addActionListener(redAction);
	}

	private class ColorAction implements ActionListener {
		public ColorAction(Color c) {
			backgroundColor = c;
		}

		public void actionPerformed(ActionEvent event) {
			setBackground(backgroundColor);
		}

		private Color backgroundColor;
	}
}
