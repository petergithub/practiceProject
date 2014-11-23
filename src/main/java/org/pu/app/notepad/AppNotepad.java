package org.pu.app.notepad;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
public class AppNotepad {
	private boolean packFrame = false;

	/**
	 * Construct and show the application.
	 */
	public AppNotepad() {
		FrameNotepad frame = new FrameNotepad();
		frame.setSize(420, 360);
		frame.setResizable(false);
		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}

	/**
	 * put png file under class folder Application entry point.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				new AppNotepad();
			}
		});
	}
}
