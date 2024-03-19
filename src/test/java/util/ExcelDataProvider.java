package util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExcelDataProvider {
    private static final String excelFilePath = System.getProperty("excelPath");
    @DataProvider(name = "excel-data")
    public static  Object[][] excelDP()  {
        Object[][] arrObj;
        arrObj = getExcelData(System.getProperty("dataProviderFile"), System.getProperty("dataProviderSheet"));
        return arrObj;
    }
    @DataProvider(name = "excel-data-list")
    public static  Object[] excelDPList()  {
        Object[][] data = getExcelData(System.getProperty("dataProviderFile"), System.getProperty("dataProviderSheet"));
        return Arrays.stream(data)
                .flatMap(Arrays::stream)
                .toArray();
    }

    public static String[][] getExcelData(String fileName, String sheetName) {
        String[][] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath + fileName);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = wb.getSheet(sheetName);
            XSSFRow row = sheet.getRow(1);

            int numOfRows = sheet.getPhysicalNumberOfRows();
            int numOfColumns = row.getLastCellNum();

            XSSFCell cell;
            data = new String[numOfRows - 1][numOfColumns];

            for (int i = 1; i < numOfRows; i++) {
                for (int j = 0; j < numOfColumns; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);
                    data[i - 1][j] = cell.getStringCellValue();
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to open excel file." + e);
        }
        return data;
    }

}
