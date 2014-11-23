package demo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.comparator.SizeFileComparator;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.pu.test.base.TestBase;



/**
 * @author Shang Pu
 * @version Date: Apr 15, 2012 11:08:53 AM
 */
public class ApacheCommonsDemo extends TestBase {

	public void testEscape() throws Exception {
		// escape a String for Java
		String str = FileUtils.readFileToString(new File("input.txt"));
		String results = StringEscapeUtils.escapeJava(str);
		System.out.println(results);

		// escape a String for HTML4
		String escapeHtmlResults = StringEscapeUtils.escapeHtml4(str);
		System.out.println(escapeHtmlResults);

		// escape a String for XML
		String escapeXmlResults = StringEscapeUtils.escapeXml(str);
		System.out.println(escapeXmlResults);

		// escape a String for JavaScript
		// String escapeJavaScriptResults =
		// StringEscapeUtils.escapeJavaScript(str);
		// System.out.println(escapeJavaScriptResults);
	}

	/**
	 * output the fields of an object
	 */
	public static String objToString(Object obj) {
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * sort an array of files
	 */
	public void testSortArrayFiles() {
		File directory = new File(".");
		// get just files, not directories
		File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);

		// Last Modified Ascending Order (LASTMODIFIED_COMPARATOR)
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

		// Last Modified Descending Order (LASTMODIFIED_REVERSE)
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

		// Names, case sensitive ascending order (NAME_COMPARATOR)
		Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

		// list Files beginning with 'Test' (case sensitive)
		files = directory.listFiles((FileFilter) new PrefixFileFilter("Test", IOCase.SENSITIVE));

		// list Files that match regular expression: pattern
		String pattern = "[tT]est[1-3].txt";
		FileFilter filter = new RegexFileFilter(pattern);
		files = directory.listFiles(filter);

		// SizeFileComparator.SIZE_COMPARATOR (Ascending, directories treated as
		// 0)
		Arrays.sort(files, SizeFileComparator.SIZE_COMPARATOR);
		System.out
				.println("\nSizeFileComparator.SIZE_COMPARATOR (Ascending, directories treated as 0)");

		// Ascending, directory size used
		Arrays.sort(files, SizeFileComparator.SIZE_SUMDIR_COMPARATOR);
		System.out
				.println("\nSizeFileComparator.SIZE_SUMDIR_COMPARATOR (Ascending, directory size used)");

		System.out.println("\nList Files ending with '.txt' (case sensitive):");
		files = directory.listFiles((FileFilter) new SuffixFileFilter(".txt", IOCase.SENSITIVE));
	}

	public void testGetAllFilesInDirectory() throws IOException {
		File dir = new File("dir");
		// get all files in a directory including subdirectories
		System.out.println("Getting all files in " + dir.getCanonicalPath()
				+ " including those in subdirectories");
		List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);

		// get all files with certain extensions in a directory including
		// subdirectories
		String[] extensions = new String[] { "txt", "jsp" };
		System.out.println("Getting all .txt and .jsp files in " + dir.getCanonicalPath()
				+ " including those in subdirectories");
		FileUtils.listFiles(dir, extensions, true);

		// get the directories in a directory using a filter
		File directory = new File(".");
		directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

		// get the hidden files in a directory using a filter
		directory.listFiles((FileFilter) HiddenFileFilter.HIDDEN);

		// get the visible files in a directory using a filter
		directory.listFiles((FileFilter) HiddenFileFilter.VISIBLE);

		for (File file : files) {
			System.out.println("file: " + file.getCanonicalPath());
		}
	}

	/**
	 * A SequenceInputStream can act as a single InputStream that can represent the streams of several
	 * other InputStreams in sequence. As an example, the SequenceInputStreamTest class below creates
	 * 3 FileInputStreams to files and then adds these to a Vector of InputStreams. It gets an
	 * Enumeration to the Vector and passes the Enumeration to the constructor of the
	 * SequenceInputStream. SequenceInputStreamTest reads all of the content from the
	 * SequenceInputStream and outputs it to the console via System.out.
	 */
	public void testSequenceInputStream() throws Exception {

		FileInputStream fis1 = new FileInputStream("testfile1.txt");
		FileInputStream fis2 = new FileInputStream("testfile2.txt");
		FileInputStream fis3 = new FileInputStream("testfile3.txt");

		Vector<InputStream> inputStreams = new Vector<InputStream>();
		inputStreams.add(fis1);
		inputStreams.add(fis2);
		inputStreams.add(fis3);

		Enumeration<InputStream> enu = inputStreams.elements();
		SequenceInputStream sis = new SequenceInputStream(enu);

		try {
			int oneByte;
			while ((oneByte = sis.read()) != -1) {
				System.out.write(oneByte);
			}
		} finally {
			org.pu.utils.IoUtils.close(sis);
		}

		System.out.flush();
	}

	public void testRedirectSystemOut() throws FileNotFoundException {
		System.out.println("This goes to the console");
		PrintStream console = System.out;

		File file = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		// System.setErr(ps);

		System.out.println("This goes to out.txt");

		System.setOut(console);
		System.out.println("This also goes to the console");
	}

	public void testCompareFileContents() throws IOException {
		File file1 = new File("test1.txt");
		File file2 = new File("test2.txt");
		File file3 = new File("test3.txt");

		boolean compare1and2 = FileUtils.contentEquals(file1, file2);
		boolean compare2and3 = FileUtils.contentEquals(file2, file3);
		boolean compare1and3 = FileUtils.contentEquals(file1, file3);

		System.out.println("Are test1.txt and test2.txt the same? " + compare1and2);
		System.out.println("Are test2.txt and test3.txt the same? " + compare2and3);
		System.out.println("Are test1.txt and test3.txt the same? " + compare1and3);
	}

	public void testCopyFileToDirectoryTest() throws IOException {
		File file = new File("test1.txt");
		File destinationDir = new File("test-directory");
		FileUtils.copyFileToDirectory(file, destinationDir);
	}

	public void testGetDriveFreeSpace() throws IOException {
		long freeSpaceKb = FileSystemUtils.freeSpaceKb("C:");
		System.out.println("Free space on C (in KB): " + freeSpaceKb);
	}

}
