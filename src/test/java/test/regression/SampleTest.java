package test.regression;

import java.util.Map;

import org.testng.annotations.Test;

import core.ExcelDataSource;
import core.apis.ReqResAPI;
import test.TestSetup;
import utility.Data;

public class SampleTest extends TestSetup {

	@Test(dataProvider = "DataProvider")
	@ExcelDataSource(name = "sample", columnName = Data.TEST_NAME, path = "testdata/Sample.xlsx", sheetName = "Sheet1")
	public void test001(Map<String, String> data) {
		new ReqResAPI().createNewUser(data);
	}

}
