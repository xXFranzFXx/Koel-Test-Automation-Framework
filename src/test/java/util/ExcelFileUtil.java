package util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ExcelFileUtil {
    private static String excelFilePath = "./src/test/resources/testData/";

    @DataProvider(name = "excel-data")
    public Object[][] excelDP() throws IOException {
        Object[][] arrObj;
        arrObj = getExcelData("test.xlsx", "Sheet1");
        return arrObj;
    }

    // Data providers end
    public String[][] getExcelData(String fileName, String sheetName) {
        String[][] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath + fileName);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = wb.getSheet(sheetName);
            XSSFRow row = sheet.getRow(0);

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
            System.out.println("Something went terribly wrong." + e);
        }
        return data;
    }

    //writes data in result set from sql query to an excel file

    public static void generateExcel(Map<String, ResultSet> dataMap, String fileName) throws SQLException, IOException {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);
        FileOutputStream fileOut = null;
        Map<String, Map<String, LinkedHashMap<String, String>>> rMap = TestUtil.createProcessedResultSetMap(dataMap);
        Set<String> names = dataMap.keySet();
        try {
           XSSFWorkbook  wb = new XSSFWorkbook();
                for (String name: names) {
                    System.out.println(name);
                    XSSFSheet sheet3 = wb.createSheet(name);
                    XSSFCellStyle headerStyle = wb.createCellStyle();
                    XSSFFont headerFont = wb.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
                    headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                    headerStyle.setFont(headerFont);
                    try {
                        fileOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    int rowCount = sheet3.getLastRowNum() - sheet3.getFirstRowNum();
                    XSSFRow sessionname = sheet3.createRow(0);
                    XSSFCell title = sessionname.createCell(1);
                    title.setCellStyle(headerStyle);
                    title.setCellValue(name);
                    XSSFRow row = sheet3.createRow(rowCount);
                    Map<String, LinkedHashMap<String, String>> rsMap = rMap.get(name);
                    Map<String, String> columnDetails = rsMap.get("1");
                    Set<String> s = columnDetails.keySet();
                    int cellNo = 0;
                    for (String s1 : s) {
                        XSSFCell cell0 = row.createCell(cellNo);
                        cell0.setCellStyle(headerStyle);
                        cell0.setCellValue(s1);
                        cellNo++;
                    }
                    for (int i = 1; i <= rsMap.size(); i++) {
                        columnDetails = rsMap.get(Integer.valueOf(i).toString());
                        XSSFRow nextrow = sheet3.createRow(rowCount + i);
                        Set<String> set = columnDetails.keySet();
                        int cellNum = 0;
                        for (String s2 : set) {
                            nextrow.createCell(cellNum).setCellValue(columnDetails.get(s2));
                            cellNum++;
                        }
                    }
                    for (int i = 0; i < columnDetails.size(); i++) {
                        sheet3.autoSizeColumn(i);
                    }
                    wb.write(fileOut);
                }
            } finally {
                try {
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
}