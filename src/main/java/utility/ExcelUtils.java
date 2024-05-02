package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private String path;
	private FileInputStream fis;
	private XSSFWorkbook wb;
	private XSSFSheet ws;
	private XSSFRow row;
	private XSSFCell cell;

	public ExcelUtils(String fileName) {
		this.path = String.format(Data.RESOURCES_FOLDER_PATH, fileName);
	}

	private void init() {
		try {
			fis = new FileInputStream(this.path);
			wb = new XSSFWorkbook(fis);
		} catch (IOException e) {
			throw new RuntimeException(String.format("The File is not found at %s", this.path));
		}
	}

	public int getRowCount(String sheetName) {
		init();
		ws = wb.getSheet(sheetName);
		int rowCount = ws.getLastRowNum();
		try {
			wb.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rowCount;
	}

	public int getCellCount(String sheetName, int rowNum) {
		init();
		ws = wb.getSheet(sheetName);
		row = ws.getRow(rowNum);
		int cellCount = row.getLastCellNum();
		try {
			wb.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cellCount;
	}

	public String getCellData(String sheetName, int rowNum, int colNum) {
		init();
		ws = wb.getSheet(sheetName);
		row = ws.getRow(rowNum);
		cell = row.getCell(colNum);
		DataFormatter df = new DataFormatter();
		String data = "";
		try {
			data = df.formatCellValue(cell);
			wb.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<Map<String, String>> getAllData(String sheetName) {
		init();
		ws = wb.getSheet(sheetName);
		int rowCount = ws.getLastRowNum();
		List<String> keys = new ArrayList<>();
		List<Map<String, String>> data = new ArrayList<>();
		row = ws.getRow(0);
		int cellCount = row.getLastCellNum();
		for (int i = 0; i < cellCount; i++) {
			cell = row.getCell(i);
			DataFormatter df = new DataFormatter();
			try {
				keys.add(df.formatCellValue(cell));
			} catch (Exception e) {
				keys.add("");
			}
		}
		for (int i=1; i<=rowCount; i++) {
			row = ws.getRow(i);
			cellCount = row.getLastCellNum();
			Map<String, String> rowData = new HashMap<>();
			for (int j = 0; j < cellCount; j++) {
				cell = row.getCell(j);
				DataFormatter df = new DataFormatter();
				try {
					rowData.put(keys.get(j), df.formatCellValue(cell));
				} catch (Exception e) {
					rowData.put(keys.get(j),"");
				}
			}
			data.add(rowData);
		}
		try {
			wb.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
