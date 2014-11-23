package demo.file.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @version Date: Jun 21, 2013 7:12:41 PM
 * @author Shang Pu
 */
public class ExcelUtils {

	/**
	 * @param isNewRow  create a new row, if it is true; set it false when write into the first row
	 * @param values
	 */
	public static void printCell(Sheet sheet, boolean isNewRow, String... values) {
		Row row = null;
		int maxRow = sheet.getLastRowNum();
		if (isNewRow) {
			row = sheet.createRow(maxRow + 1);
		} else {
			row = sheet.getRow(maxRow);
		}
		if (row == null) row = sheet.createRow(maxRow);
		int size = values.length;
		int index = row.getLastCellNum()==-1 ? 0 : row.getLastCellNum();
		for (int i = 0; i < size; i++) {
			row.createCell(index++).setCellValue(values[i]);
		}
	}
}
