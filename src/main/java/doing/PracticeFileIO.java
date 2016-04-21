package doing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.pu.test.base.TestBase;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeFileIO extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeFileIO.class);
	
	private static StringBuffer stringBufferOfData = new StringBuffer();
	private static String filename = null;
	private static Scanner sc = new Scanner(System.in);// initialize scanner to get user input
	private String pathname = "testing.txt";
	
	
	public void fileToArrayOfBytes() throws IOException {
		//jdk7
		Path path = Paths.get(pathname);
		byte[] dataJdk7 = Files.readAllBytes(path);
		log.info("dataJdk7[{}]", dataJdk7);
		
		//ApacheFileUtil
		File file = new File(pathname);
		byte[] dataApacheFileUtil = FileUtils.readFileToByteArray(file);
		log.info("dataApacheFileUtil[{}]", dataApacheFileUtil);

		// jdk6
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();

			for (int i = 0; i < bFile.length; i++) {
				System.out.print((char) bFile[i]);
			}

			System.out.println("Done");
	}
	
	public void testTempFolder() throws IOException {
		File temp = File.createTempFile("test", "");
		temp.delete();
		temp.mkdir();
		log.info("temp.getAbsolutePath() = {}", temp.getAbsolutePath());
	}

	public void testAudio() throws IOException, LineUnavailableException,
			UnsupportedAudioFileException {
		URL url = new URL("http://pscode.org/media/leftright.wav");
		Clip clip = AudioSystem.getClip();
		// getAudioInputStream() also accepts a File or InputStream
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// A GUI element to prevent the Clip's daemon Thread
				// from terminating at the end of the main()
				JOptionPane.showMessageDialog(null, "Close to exit!");
			}
		});
	}

	public void testCreateFile() {
		String date = "date";
		open(date);
		open(date);
	}

	private void open(String date) {
		String directory = "log";
		String prefix = "catalina.";
		String suffix = ".log";
		System.setProperty("catalina.base", "c:/cache");
		// Create the directory if necessary
		File dir = new File(directory);
		if (!dir.isAbsolute())
			dir = new File(System.getProperty("catalina.base"), directory);
		dir.mkdirs();

		PrintWriter writer;
		// Open the current log file
		try {
			String pathname = dir.getAbsolutePath() + File.separator + prefix + date + suffix;
			writer = new PrintWriter(new FileWriter(pathname, true), true);
		} catch (IOException e) {
			writer = null;
		}
		writer.append(date);
		IoUtils.close(writer);
	}

	public void testFilenameFilter() throws Exception {
		File file = new File("a");
		String[] names = file.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		});
		log.info("names = " + names);
	}
	
	public static void main(String[] args) {
		boolean fileRead = readFile();// call the method to read the file with the files name
		if (fileRead) {// if the read file was successfull
			replacement();// call method to get text to replace, replacement text and output replaced
										// String buffer
			writeToFile();
		}
		System.exit(0);// exit once app is done
	}

	private static boolean readFile() {
		System.out.println("Please enter your files name and path i.e C:\\test.txt: ");// prompt for
																																										// file name
		filename = sc.nextLine();// read in the file name
		Scanner fileToRead = null;
		try {
			fileToRead = new Scanner(new File(filename)); // point the scanner method to a file
			// check if there is a next line and it is not null and then read it in
			for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
				System.out.println(line);// print each line as its read
				stringBufferOfData.append(line).append("\r\n");// this small line here is to appened all
																												// text read in from the file to a string
																												// buffer which will be used to edit the
																												// contents of the file
			}
			fileToRead.close();// this is used to release the scanner from file
			return true;
		} catch (FileNotFoundException ex) {// if the file cannot be found an exception will be thrown
			System.out.println("The file " + filename + " could not be found! " + ex.getMessage());
			return false;
		} finally {// if an error occurs now we close the file to exit gracefully
			fileToRead.close();
		}
	}

	private static void writeToFile() {
		try {
			BufferedWriter bufwriter = new BufferedWriter(new FileWriter(filename));
			bufwriter.write(stringBufferOfData.toString());// writes the edited string buffer to the new
																											// file
			bufwriter.close();// closes the file
		} catch (Exception e) {// if an exception occurs
			System.out.println("Error occured while attempting to write to file: " + e.getMessage());
		}
	}

	private static void replacement() {
		System.out.println("Please enter the contents of a line you would like to edit: ");// prompt for
																																												// a line in
																																												// file to
																																												// edit
		String lineToEdit = sc.nextLine();// read the line to edit
		System.out.println("Please enter the the replacement text: ");// prompt for a line in file to
																																	// replace
		String replacementText = sc.nextLine();// read the line to replace
		// System.out.println(sb);//used for debugging to check that my stringbuffer has correct
		// contents and spacing
		int startIndex = stringBufferOfData.indexOf(lineToEdit);// now we get the starting point of the
																														// text we want to edit
		int endIndex = startIndex + lineToEdit.length();// now we add the staring index of the text with
																										// text length to get the end index
		stringBufferOfData.replace(startIndex, endIndex, replacementText);// this is where the actual
																																			// replacement of the text
																																			// happens
		System.out.println("Here is the new edited text:\n" + stringBufferOfData); // used to debug and
																																								// check the string
																																								// was replaced
	}
}
