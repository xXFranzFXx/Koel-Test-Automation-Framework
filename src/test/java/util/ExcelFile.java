package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelFile {
    private String excelFilePath = "./src/test/resources/testData/";
    @DataProvider(name="excel-data")
    public Object[][] excelDP() throws IOException {
        Object [][] arrObj;
        arrObj = getExcelData("test.xlsx", "Sheet1");
        return arrObj;
    }
    public String [][] getExcelData(String fileName, String sheetName) {
        String [][] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath+fileName);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = wb.getSheet(sheetName);
            XSSFRow row = sheet.getRow(0);

            int numOfRows = sheet.getPhysicalNumberOfRows();
            int numOfColumns = row.getLastCellNum();

            XSSFCell cell;
            data = new String[numOfRows-1][numOfColumns];

            for(int i = 1; i < numOfRows; i++) {
                for(int j = 0; j < numOfColumns; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);
                    data  [i-1][j] =  cell.getStringCellValue();
                }
            }
        } catch (Exception e) {
            System.out.println("Something went terribly wrong." + e);
        }
        return data;
    }
    public void writeToExcel(String fileName, String sheetName, String[] dataToWrite) throws IOException {
        try {
            String excelFile = excelFilePath + fileName;
            File file = new File(excelFile);
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = null;
            String fileExtensionName = fileName.substring((fileName.indexOf(".")));
            if (fileExtensionName.equals(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
                workbook.createSheet(sheetName);
            } else if (fileExtensionName.equals(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
                workbook.createSheet(sheetName);
            }
            //Read excel sheet by sheet name
            assert workbook != null;
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            Row row = sheet.getRow(0);
            Row newRow = sheet.createRow(rowCount + 1);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = newRow.createCell(j);
                cell.setCellValue(dataToWrite[j]);
            }
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e){
            Reporter.log("Error writing to excel file " + e, true);
        }
    }
}
