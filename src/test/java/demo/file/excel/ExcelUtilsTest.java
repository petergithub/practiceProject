package demo.file.excel;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Jun 21, 2013 7:19:02 PM
 * @author Shang Pu
 */
public class ExcelUtilsTest {
	private static final Logger log = LoggerFactory.getLogger(ExcelUtilsTest.class);

	public void testPrintCell() {
		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheetDif = book.createSheet("only in DIF");
		ExcelUtils.printCell(sheetDif, false, "CLINICAL_PROTOCOL_CODE", "PROTOCOL_TITLE",
				"STUDY_TYPE_ID", "STUDY_PHASE_ID");

		try {
			FileOutputStream fileOut = new FileOutputStream("test.xlsx");
			book.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			log.error("IOException in ExcelUtilsTest.testPrintCell()", e);
		}
	}
}
