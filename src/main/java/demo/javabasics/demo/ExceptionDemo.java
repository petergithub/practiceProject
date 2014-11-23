package demo.javabasics.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionDemo {
	public static final Logger log = LoggerFactory
			.getLogger(ExceptionDemo.class);
	public static void main(String[] args) {
		ExceptTestFrame frame = new ExceptTestFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	@Test
	public void testExceptionStack() {
		try {
			try {
				try {
					log.info("Init Exception");
					throw new Exception("Init Exception");
				} catch (Exception e) {
					log.error("Exception 1 in ExceptionDemo.testExceptionStack()", e);
					throw e;
				}
			} catch (Exception e) {
				log.error("Exception 2 in ExceptionDemo.testExceptionStack()", e);
				throw new Exception(e);
			}
		} catch (Exception e) {
			log.error("Exception 3 in ExceptionDemo.testExceptionStack()", e);
		}
	}
}

/**
 * A frame with a panel for testing various exceptions
 */
class ExceptTestFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public ExceptTestFrame() {
		setTitle("ExceptTest");
		ExceptTestPanel Panel = new ExceptTestPanel();
		add(Panel);
		pack();
		setLocation(434, 223);
	}

/**
 * A panel with radio buttons for running code snippets and studying their
 * exception behavior
 */
class ExceptTestPanel extends Box {
	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(ExceptTestPanel.class);

	public ExceptTestPanel() {
		super(BoxLayout.Y_AXIS);
		group = new ButtonGroup();

		// add radio buttons for code snippets
		addRadioButton("Integer divide by zero", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a[1] = 1 / (a.length - a.length);
			}
		});

		addRadioButton("Floating point divide by zero", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a[1] = a[2] / (a[3] - a[3]);
			}
		});

		addRadioButton("Array bounds", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a[1] = a[10];
			}
		});

		addRadioButton("Bad cast", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a = (double[]) event.getSource();
			}
		});

		addRadioButton("Null pointer", new ActionListener() {
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent event) {
				event = null;
				event.getSource();
			}
		});

		addRadioButton("sqrt(-1)", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a[1] = Math.sqrt(-1);
			}
		});

		addRadioButton("Overflow", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				a[1] = 1000 * 1000 * 1000 * 1000;
				int n = (int) a[1];
				log.info("n = " + n + " in method actionPerformed()");
			}
		});

		addRadioButton("No such file", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					new FileInputStream("woozle.txt");
				} catch (IOException e) {
					textField.setText(e.toString());
				}
			}
		});

		addRadioButton("Throw unknown", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				throw new UnknownError();
			}
		});

		// add the text field for exception display
		textField = new JTextField(30);
		add(textField);
	}

	/**
	 * Adds a radio button with a given listener to the panel. Traps any
	 * exceptions in the actionPerformed method of the listener.
	 * 
	 * @parm s the label of the button
	 * @parm listener the action listener for the radio button
	 */
	private void addRadioButton(String s, ActionListener listener) {
		JRadioButton button = new JRadioButton(s, false) {
			private static final long serialVersionUID = 1L;

			// the button calls this method to fire an
			// action event. We override it to trap exceptions
			protected void fireActionPerformed(ActionEvent event) {
				try {
					textField.setText("No exception");
					super.fireActionPerformed(event);
				} catch (Exception e) {
					textField.setText(e.toString());
				}
			}
		};

		button.addActionListener(listener);
		add(button);
		group.add(button);
	}

	private ButtonGroup group;
	private JTextField textField;
	private double[] a = new double[10];
}

}