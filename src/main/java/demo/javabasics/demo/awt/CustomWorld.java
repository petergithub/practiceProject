package demo.javabasics.demo.awt;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * This program demonstrates how to customize a "Hello, World" program with a
 * properties file.
 */
public class CustomWorld {
	public static void main(String[] args) {
		CustomWorldFrame frame = new CustomWorldFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

/**
 * This frame displays a message. The frame size, message text, font, and color
 * are set in a properties file.
 */
class CustomWorldFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public CustomWorldFrame() {
		Properties defaultSettings = new Properties();
		defaultSettings.put("font", "Monospaced");
		defaultSettings.put("width", "300");
		defaultSettings.put("height", "200");
		defaultSettings.put("message", "Hello, World");
		defaultSettings.put("color.red", "0");
		defaultSettings.put("color.green", "50");
		defaultSettings.put("color.blue", "50");
		defaultSettings.put("ptsize", "12");

		Properties settings = new Properties(defaultSettings);
		try {
			// FileOutputStream out = new
			// FileOutputStream("doc\\CustomWorld.properties");
			// defaultSettings.store(out, "CustomWorld.properties");
			String filePath = new File("doc\\CustomWorld.properties")
					.getAbsolutePath();
			System.out.println(filePath);
			FileInputStream in = new FileInputStream(filePath);
			settings.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int red = Integer.parseInt(settings.getProperty("color.red"));
		int green = Integer.parseInt(settings.getProperty("color.green"));
		int blue = Integer.parseInt(settings.getProperty("color.blue"));

		Color foreground = new Color(red, green, blue);

		String name = settings.getProperty("font");
		int ptsize = Integer.parseInt(settings.getProperty("ptsize"));
		Font f = new Font(name, Font.BOLD, ptsize);

		int hsize = Integer.parseInt(settings.getProperty("width"));
		int vsize = Integer.parseInt(settings.getProperty("height"));
		setSize(hsize, vsize);
		setTitle(settings.getProperty("message"));

		JLabel label = new JLabel(settings.getProperty("message"),
				SwingConstants.CENTER);
		label.setFont(f);
		label.setForeground(foreground);
		add(label);
	}
}
