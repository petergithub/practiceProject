package org.pu.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.pu.utils.IoUtils;


/**
 * Description: compare xml files
 * 
 * @author Pu Shang Created on: May 26, 2011
 */
public class CompareFiles {
	private static String newLine = "\n";
	private static String blankLine = "\n\n";
	private static String indent = "\t";
	private static Logger logger = Logger.getLogger(CompareFiles.class);
	public static Map<String, Integer> differentFileList = new HashMap<String, Integer>();
	private List<Record> recordList = new ArrayList<Record>();

	public static void main(String args[]) {
		logger.info("CompareFiles start ...");
		String argus[] = {"C:\\compareA", "C:\\compareB", "C:\\"};
		String fileA = argus[0];
		String fileB = argus[1];
		String result = argus[2] + "results.txt";
		CompareFiles compare = new CompareFiles();
		boolean isIdentical = false;
		try {
			isIdentical = compare.compareFolder(fileA, fileB, result);
			// isIdentical = compare.compareFile(fileA, fileB, result);
			if (isIdentical) {
				logger.info(fileA + " and " + fileB + " are the same as each other!");
			} else {
				logger.info(fileA + " and " + fileB + " are Different with each other! Different total: "
						+ differentFileList.size() + "\nResults: " + result);
			}
			String reportPath = "C:/Shang/Work/Eclipse3.6Workspace/DctmTools/doc/";
			compare.recordToCsv(reportPath + "compareResult.csv", compare.recordList);
		} catch (IOException e) {
			logger.error("Exception in main()", e);
		}
		// compare.recordToTxt(result, compare.recordList);
		logger.info("CompareFiles stop ...");
	}

	/**
	 * @param folderA
	 * @param folderB
	 * @param resultFilePath
	 * @return
	 * @throws IOException
	 */
	public boolean compareFolder(String folderA, String folderB, String resultFilePath)
			throws IOException {
		boolean isIdentical = true;
		IoUtils
				.appendFile(resultFilePath, blankLine + "compare folder: " + new File(folderA).getName());
		File[] filesA = new File(folderA).listFiles();
		File[] filesB = new File(folderB).listFiles();
		File[] filesMore = null;
		File[] filesLess = null;
		// if (filesA.length > filesB.length) {
		// filesMore = filesA;
		// filesLess = filesB;
		// isIdentical = false;
		// } else if (filesA.length < filesB.length) {
		// filesMore = filesB;
		// filesLess = filesA;
		// isIdentical = false;
		// } else {
		// filesMore = filesA;
		// filesLess = filesB;
		// }
		filesMore = filesA;
		filesLess = filesB;

		for (File fileA : filesMore) {
			boolean isInFilesLess = false;
			// for (File fileB : filesLess) {
			int i = 0;
			for (; i < filesLess.length; i++) {
				File fileB = filesLess[i];
				if (fileA.getName().equals(fileB.getName())) {
					isInFilesLess = true;
					if (fileA.isFile()) {
						if (!compareFile(fileA.getAbsolutePath(), fileB.getAbsolutePath(), resultFilePath)) {
							isIdentical = false;
						}
					} else {
						if (!compareFolder(fileA.getAbsolutePath(), fileB.getAbsolutePath(), resultFilePath)) {
							isIdentical = false;
						}
					}
				}
			}
			if (!isInFilesLess) {
				Record record = new Record(fileA.getAbsolutePath());
				recordList.add(record);
				differentFileList.put(fileA.getName(), 0);
			}
		}
		return isIdentical;
	}

	/**
	 * @param filePathA
	 * @param filePathB
	 * @param resultFilePath
	 * @return
	 * @throws IOException
	 */
	public boolean compareFile(String filePathA, String filePathB, String resultFilePath)
			throws IOException {
		boolean isIdentical = true;
		File fileA = new File(filePathA);
		String fileName = fileA.getName();
		IoUtils.appendFile(resultFilePath, blankLine + indent + "compare files: " + fileName);
		int lineNum = 1;
		try {
			FileReader readerA = new FileReader(filePathA);
			BufferedReader brA = new BufferedReader(readerA);
			FileReader readerB = new FileReader(filePathB);
			BufferedReader brB = new BufferedReader(readerB);
			String stringA = brA.readLine().trim().replaceAll("\t", "");
			String stringB = brB.readLine().trim().replaceAll("\t", "");
			while ((stringA != null) || (stringB != null)) {
				if (stringA == null) {
					stringA = "";
				}
				if (stringB == null) {
					stringB = "";
				}
				stringA = stringA.replaceAll("\t", "");
				stringB = stringB.replaceAll("\t", "");
				if (stringA.replaceAll(" ", "").equals(stringB.replaceAll(" ", ""))) {
					stringA = brA.readLine();
					stringB = brB.readLine();
				} else {
					if (fileName.equals("gtc_xm_artwork.xml")
							|| (fileName.equals("pfe_submission_comp.xml") && !stringA.replaceAll(" ", "")
									.startsWith("<UserQuery>"))) {
						stringA = brA.readLine();
						stringB = brB.readLine();
						continue;
					}
					isIdentical = false;

					Record record = new Record(getDoctype(fileA), getSubtype(fileA), lineNum, filePathA,
							stringA, filePathB, stringB);
					recordList.add(record);
					IoUtils.appendFile(resultFilePath, newLine);
					IoUtils.appendFile(resultFilePath, filePathA + " line: " + lineNum + indent + stringA
							+ newLine);
					IoUtils.appendFile(resultFilePath, filePathB + " line: " + lineNum + indent + stringB
							+ newLine);
					stringA = brA.readLine();
					stringB = brB.readLine();
				}
				lineNum++;
			}
			brA.close();
			brB.close();
			readerA.close();
			readerB.close();
		} catch (Exception e) {
			logger.error("Exception: " + e + ", when comparing file " + fileName);
			isIdentical = false;
		}
		if (isIdentical) {
			IoUtils.appendFile(resultFilePath, newLine + fileName + " are the same!");
		} else {
			differentFileList.put(fileName, lineNum);
			IoUtils.appendFile(resultFilePath, newLine + fileName + " are different!");
		}
		return isIdentical;
	}

	public void recordToCsv(String path, List<Record> recordList) throws IOException {
		StringBuffer csvContentBuffer = new StringBuffer();
		csvContentBuffer.append("doctype").append(',').append("subtype").append(',').append("line")
				.append(',').append("fileA").append(',').append("fileA line").append(',').append("fileB")
				.append(',').append("fileB line").append("\r\n");

		for (Record r : recordList) {
			csvContentBuffer.append(r.doctype).append(',').append(escapeComma(r.subtype)).append(',')
					.append(r.line).append(',').append(escapeComma(r.filePathA)).append(',')
					.append(escapeComma(r.lineA)).append(',').append(escapeComma(r.filePathB)).append(',')
					.append(escapeComma(r.lineB)).append("\r\n");
		}
		IoUtils.write(path, csvContentBuffer.toString());
		logger.info("Report path: " + path);
		logger.info("Different file count: " + differentFileList.size());
		logger.info("Different record count: " + recordList.size());
		logger.info("Uninclude file: gtc_xm_artwork.xml");
		logger.info("More difference (uninclude record) file: pfe_submission_comp.xml");
	}

	public void recordToTxt(String path, List<Record> recordList) throws IOException {
		for (Record r : recordList) {
			IoUtils.appendFile(path, newLine);
			IoUtils.appendFile(path, r.filePathA + " line: " + r.line + indent + r.lineA + newLine);
			IoUtils.appendFile(path, r.filePathB + " line: " + r.line + indent + r.lineB + newLine);
		}
	}

	private String getSubtype(File file) {
		String filePath = file.getAbsolutePath();
		String fileName = file.getName();
		String subtype = "Not Available";

		if (filePath.indexOf("artifacts") == -1) {
			subtype = "Not Available";
		} else if (fileName.indexOf(".xml") != -1) {
			subtype = fileName.substring(0, fileName.indexOf(".xml"));
			subtype = standardSubtypeName(subtype);
		}
		return subtype;
	}

	private String getDoctype(File file) {
		String filePath = file.getAbsolutePath();
		String folderPath = filePath.substring(0, filePath.indexOf(file.getName()));
		return folderPath.substring(folderPath.lastIndexOf("(") + 1, folderPath.lastIndexOf(")"));
	}

	private String standardSubtypeName(String objectName) {
		if (objectName.equals("TMF CompoundDrug Level Documentation - Lifecycle 2")) {
			return "TMF Compound/Drug Level Documentation - Lifecycle 2";
		} else if (objectName.equals("TMF CompoundDrug Level Documentation - Lifecycle 3")) {
			return "TMF Compound/Drug Level Documentation - Lifecycle 3";
		} else if (objectName.equals("Answer Keys - GLPGCP.xml")) {
			return "Answer Keys - GLP/GCP.xml";
		} else {
			return objectName;
		}
	}

	private String escapeComma(String str) {
		return "\"" + str + "\"";
	}

	private class Record {
		public Record(String doctype, String subtype, int line, String filePathA, String lineA,
				String filePathB, String lineB) {
			this.doctype = doctype;
			this.subtype = subtype;
			this.line = line;
			this.filePathA = filePathA;
			this.lineA = lineA;
			this.filePathB = filePathB;
			this.lineB = lineB;
		}

		public Record(String filePathA) {
			this.line = 0;
			this.filePathA = filePathA;
			this.filePathB = "Not Available";
		}

		private String doctype;
		private String subtype;
		private int line;
		private String filePathA;
		private String lineA;
		private String filePathB;
		private String lineB;
	}
}
