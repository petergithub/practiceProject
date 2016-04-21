package demo.file.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class testExcel {
	private static final Logger log = LoggerFactory.getLogger(testExcel.class);


	@Test
	public void checkEffectiveVersion() {
		try {
			String fileName = "c:/cache/lc4 effective report.xls";
			FileInputStream fileInputStream = new FileInputStream(fileName);
			HSSFWorkbook book = new HSSFWorkbook(fileInputStream);
			HSSFSheet sheet = book.getSheet("Sheet1");
			int column_chronicle_id = 1;
			int column_version_label = 4;
			String r_version_label = "";
			String chronicle_id_current = "";
			int max = 0;
			int maxRow = 0; 
			int rowCount = sheet.getLastRowNum();
			for (int i = 1; i <= rowCount; i++) {
				HSSFRow row = sheet.getRow(i);
				r_version_label = row.getCell(column_chronicle_id).getStringCellValue();
				log.debug("r_version_label = {}", r_version_label);
				r_version_label = r_version_label.replace(",CURRENT", "").replace("CURRENT,", "");
				log.debug("after replace current r_version_label = {}", r_version_label);
				int version = Integer.valueOf(r_version_label);
				String chronicle_id = row.getCell(column_chronicle_id).getStringCellValue();
				if (chronicle_id_current.length() == 0 || !chronicle_id_current.equals(chronicle_id)) {
					chronicle_id_current = chronicle_id;
					r_version_label = row.getCell(column_chronicle_id).getStringCellValue();
					
					
					max = 0;
					maxRow = i;
				} else {
						if (version > max) {
							max = version;
							maxRow = i;
						}
				}
			}
			log.debug("maxRow = {}", maxRow);
			log.debug("column_version_label = {}", column_version_label);

			// index from 0,0... cell A1 is cell(0,0)
			HSSFRow row1 = sheet.createRow(0);

			HSSFCell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Hello");
			HSSFCellStyle cellStyle = book.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellA1.setCellStyle(cellStyle);

			HSSFCell cellB1 = row1.createCell(1);
			cellB1.setCellValue("Goodbye");
			cellStyle = book.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellB1.setCellStyle(cellStyle);

			HSSFCell cellC1 = row1.createCell(2);
			cellC1.setCellValue(true);

			HSSFCell cellD1 = row1.createCell(3);
			cellD1.setCellValue(new Date());
			cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			cellD1.setCellStyle(cellStyle);
			
			FileOutputStream fileOut = new FileOutputStream("test.xls");
			book.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * The Apache Jakarta POI project, located at http://jakarta.apache.org/poi/ is a Java library
	 * that allow you to manipulate Microsoft document formats. Within the POI project, POI-HSSF
	 * allows you to read, modify, and write Excel documents. The HSSF Quick Guide at
	 * http://jakarta.apache.org/poi/hssf/quick-guide.html is a great resouce for quickly getting up
	 * to speed with POI-HSSF.
	 */
	public void testPoiWriteExcelFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream("poi-test.xls");
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("POI Worksheet");

			// index from 0,0... cell A1 is cell(0,0)
			HSSFRow row1 = sheet.createRow(0);

			HSSFCell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Hello");
			HSSFCellStyle cellStyle = book.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellA1.setCellStyle(cellStyle);

			HSSFCell cellB1 = row1.createCell(1);
			cellB1.setCellValue("Goodbye");
			cellStyle = book.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellB1.setCellStyle(cellStyle);

			HSSFCell cellC1 = row1.createCell(2);
			cellC1.setCellValue(true);

			HSSFCell cellD1 = row1.createCell(3);
			cellD1.setCellValue(new Date());
			cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			cellD1.setCellStyle(cellStyle);

			book.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testPoiReadExcelXlsFile() {
		try {
			FileInputStream fileInputStream = new FileInputStream(
					"C:\\language_iso.xls");
			HSSFWorkbook book = new HSSFWorkbook(fileInputStream);
			HSSFSheet sheet = book.getSheet("POI Worksheet");
			HSSFRow row1 = sheet.getRow(0);
			HSSFCell cellA1 = row1.getCell(0);
			String a1Val = cellA1.getStringCellValue();
			HSSFCell cellB1 = row1.getCell(1);
			String b1Val = cellB1.getStringCellValue();
			HSSFCell cellC1 = row1.getCell(2);
			boolean c1Val = cellC1.getBooleanCellValue();
			HSSFCell cellD1 = row1.getCell(3);
			Date d1Val = cellD1.getDateCellValue();

			System.out.println("A1: " + a1Val);
			System.out.println("B1: " + b1Val);
			System.out.println("C1: " + c1Val);
			System.out.println("D1: " + d1Val);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testPoiGetXlsAllSheetNames() {
		List<String> sheetsList = new ArrayList<String>();
		try {
			FileInputStream fileInputStream = new FileInputStream(
					"C:\\Attribute mapping.xls");
			HSSFWorkbook book = new HSSFWorkbook(fileInputStream);
			int size = book.getNumberOfSheets();
			for (int i = 0; i < size; i++) {
				String name = book.getSheetAt(i).getSheetName();
				sheetsList.add(name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("sheetList = {}", sheetsList);
	}

	public void testPoiReadExcelXlsxFile() {
		try {
			FileInputStream fileInputStream = new FileInputStream(
					"C:\\language_iso.xlsx");
			XSSFWorkbook book = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = book.getSheet("Sheet1");
			String dql = "insert into dm_dbo.languages (language_key,language_text,applicable_to_pim,active_flag,language_abbv,language_iso,source) values(''{0}'', ''{1}'', ''{2}'',''{3}'', ''{4}'', ''{5}'', ''{6}'')\r\ngo";
			int rowCount = sheet.getLastRowNum();
			for (int i = 1; i <= rowCount; i++) {
				XSSFRow row = sheet.getRow(i);
				XSSFCell language_text = row.getCell(0);
				XSSFCell applicable_to_pim = row.getCell(1);
				XSSFCell active_flag = row.getCell(2);
				XSSFCell language_abbv = row.getCell(3);
				XSSFCell language_iso = row.getCell(4);
				XSSFCell source = row.getCell(5);

				String result = MessageFormat.format(dql, new Object[] { i, language_text,
						applicable_to_pim, active_flag, language_abbv, language_iso, source });
				System.out.println(result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
