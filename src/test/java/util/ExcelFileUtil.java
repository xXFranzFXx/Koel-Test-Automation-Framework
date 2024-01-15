package util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ExcelFileUtil {
    private static  String excelFilePath = "./src/test/resources/testData/";
    @DataProvider(name="excel-data")
    public Object[][] excelDP() throws IOException {
        Object [][] arrObj;
        arrObj = getExcelData("test.xlsx", "Sheet1");
        return arrObj;
    }
    // Data providers end
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
    //writes data in result set from sql query to an excel file

    public static void generateExcel(Map<String, LinkedHashMap<String, String>> resultMap, String fileName, String name) {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);
        FileOutputStream fileOut = null;
        try
        {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFCellStyle headerStyle = wb.createCellStyle();
            HSSFSheet sheet3 = wb.createSheet(name);
            HSSFFont headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());


            headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            try
            {
                fileOut = new FileOutputStream(file);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            HSSFRow sessionname = sheet3.createRow(2);
            HSSFCell title = sessionname.createCell(3);
            title.setCellStyle(headerStyle);
            title.setCellValue(name);
            HSSFRow row = sheet3.createRow(5);
            Map<String, LinkedHashMap<String, String>> rMap = resultMap;
            Map<String, String> columnDetails = rMap.get("1");
            Set<String> s = columnDetails.keySet();

            int cellNo = 0;
            for (String s1 : s)
            {
                HSSFCell cell0 = row.createCell(cellNo);
                cell0.setCellStyle(headerStyle);
                cell0.setCellValue(s1);
                cellNo++;
            }
            for (int i = 1; i <= rMap.size(); i++)
            {
                columnDetails = rMap.get(Integer.valueOf(i).toString());
                System.out.println(i);
                HSSFRow nextrow = sheet3.createRow(5 + i);
                Set<String> set = columnDetails.keySet();
                int cellNum = 0;
                for (String s2 : set)
                {
                    nextrow.createCell(cellNum).setCellValue(columnDetails.get(s2));
                    cellNum++;
                }
            }
            sheet3.autoSizeColumn(0);
            sheet3.autoSizeColumn(1);
            sheet3.autoSizeColumn(2);
            sheet3.autoSizeColumn(3);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        }
        catch (FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileOut.flush();
                fileOut.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
