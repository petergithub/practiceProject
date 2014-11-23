package demo.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.pu.utils.IoUtils;

import com.Ostermiller.util.ExcelCSVPrinter;

/**
 * @author Shang Pu
 * @version Date: Sep 19, 2012 11:37:38 AM
 */
public class CsvDemo {

	public void testParseCSV() throws IOException {
		String csv = "CR641.csv";
		StringBuffer fileContentBuffer = new StringBuffer();
		fileContentBuffer.append("docbroker_host").append(',').append("docbroker_port").append("\r\n");
		List<String[]> datas = IoUtils.getListFromFile(csv);
		for (String[] content : datas) {
			String docbroker_host = content[0].trim();
			if (!"".equals(docbroker_host)) {
				String docbroker_port = content[1].trim();
				fileContentBuffer.append(docbroker_host).append(',').append(docbroker_port).append("\r\n");
			}
		}
		IoUtils.write("CR641_result.csv", fileContentBuffer.toString());
	}

	public void testWriteCSV() throws IOException {
		FileOutputStream out = null;
		ExcelCSVPrinter csvPrinter = null;
		try {
			out = new FileOutputStream("GTCConfigDetails.csv");
			csvPrinter = new ExcelCSVPrinter(out);
			csvPrinter.println(new String[] { "Type", "Label" });
		} catch (Exception e) {
			
		} finally {
			csvPrinter.close();
			out.close();
		}
			
	}
}
