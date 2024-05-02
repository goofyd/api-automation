package test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.annotations.DataProvider;

import core.ExcelDataSource;
import utility.ExcelUtils;

public class TestSetup {

	@DataProvider(name="DataProvider", parallel = true)
	public Object[][] getDataFromExcel(Method m) {
		if (m.isAnnotationPresent(ExcelDataSource.class)) {
			ExcelDataSource ds = m.getAnnotation(ExcelDataSource.class);
			ExcelUtils excel = new ExcelUtils(ds.path());
			var a = excel.getAllData(ds.sheetName());
			List<Map<String, String>> rows = a.stream().filter(x->x.get(ds.columnName()).equalsIgnoreCase(ds.name()))
			.collect(Collectors.toList());
			Object[][] str = new Object[rows.size()][1];
			for(int i=0; i<str.length; i++)
				str[i][0] = rows.get(i);
			return str;
		} else {
			throw new IllegalArgumentException("There is no Test Name defined for the test");
		}
	}
	
	
	/*
	 * public static void main(String[] args) { ExcelUtils excel = new
	 * ExcelUtils("testData/Sample.xlsx"); List<Map<String, String>> rows =
	 * excel.getAllData("Sheet1").stream().filter(x->x.get("Name").equalsIgnoreCase(
	 * "sample")) .collect(Collectors.toList()); Object[][] str = new
	 * Object[rows.size()][1]; }
	 */

}
