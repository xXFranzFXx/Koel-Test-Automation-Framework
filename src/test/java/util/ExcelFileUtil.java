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
    private static final String excelFilePath = System.getProperty("excelPath");

    //writes data in result set from sql query to an excel file
    public static void generateExcel(Map<String, ResultSet> dataMap, String fileName) throws SQLException, IOException {

        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);

        Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap = DbUtil.createProcessedResultSetMap(dataMap);
        Set<String> testNames = dataMap.keySet();
        for (String name : testNames) {
            try {
                FileInputStream fip = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(fip);

                if (file.isFile() && file.exists()) {
                    System.out.println(fileName + ".xlsx open");
                } else {
                    System.out.println(fileName + ".xlsx either not exist" + " or can't open");
                }
                Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(name);
                XSSFSheet sheet3 = makeSheet(wb, name, resultSetMap);
//                XSSFCellStyle headerStyle = wb.createCellStyle();
//                XSSFFont headerFont = wb.createFont();
//                headerFont.setBold(true);
//                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
//                headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
//                headerStyle.setFont(headerFont);
//
//                int rowCount = sheet3.getLastRowNum() - sheet3.getFirstRowNum();
//                XSSFRow row = sheet3.createRow(rowCount);
//                Map<String, String> columnDetails = resultSets.get("1");
//                Set<String> columnNames = columnDetails.keySet();
//                int cellNo = 0;
//                for (String s1 : columnNames) {
//                    XSSFCell cell0 = row.createCell(cellNo);
//                    cell0.setCellStyle(headerStyle);
//                    cell0.setCellValue(s1);
//                    cellNo++;
//                }
//                for (int i = 1; i <= resultSets.size(); i++) {
//                    columnDetails = resultSets.get(Integer.valueOf(i).toString());
//                    XSSFRow nextrow = sheet3.createRow(rowCount + i);
//                    Set<String> set = columnDetails.keySet();
//                    int cellNum = 0;
//                    for (String s2 : set) {
//                        nextrow.createCell(cellNum).setCellValue(columnDetails.get(s2));
//                        cellNum++;
//                    }
//                }
                for (int i = 0; i < resultSets.get("1").size(); i++) {
                    sheet3.autoSizeColumn(i);
                }

                FileOutputStream fileOut = new FileOutputStream(file);
                wb.write(fileOut);
                fip.close();
                fileOut.flush();
                fileOut.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static List<String> getSheetNames (XSSFWorkbook wb) {
        List<String> sheetNames = new ArrayList<String>();
        for (int i=0; i<wb.getNumberOfSheets(); i++) {
            sheetNames.add( wb.getSheetName(i) );
        }
        return sheetNames;
    }

    private static boolean sheetExists (XSSFWorkbook wb, String sheetName) {
        List<String> sheetNames = getSheetNames(wb);
        return sheetNames.contains(sheetName);
    }
    private static XSSFSheet makeSheet (XSSFWorkbook wb, String sheetName, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap) {
        if (sheetExists(wb, sheetName)) {
             XSSFSheet sheet = wb.getSheet(sheetName);
             int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
             createRows(sheet, resultSetMap, wb, rowCount);
             return sheet;
        } else {
            XSSFSheet sheet  =  wb.createSheet(sheetName);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            createHeader(sheet, resultSetMap, wb, rowCount);
            createRows(sheet, resultSetMap, wb, rowCount);
            return sheet;
        }
    }
    private static void createHeader(XSSFSheet sheet, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap, XSSFWorkbook wb, int rowCount) {

        XSSFCellStyle headerStyle = wb.createCellStyle();
        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
        headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFont(headerFont);

//        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        XSSFRow row = sheet.createRow(rowCount);
        Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(sheet.getSheetName());
        Map<String, String> columnDetails = resultSets.get("1");
        Set<String> columnNames = columnDetails.keySet();
        int cellNo = 0;
        for (String s1 : columnNames) {
            XSSFCell cell0 = row.createCell(cellNo);
            cell0.setCellStyle(headerStyle);
            cell0.setCellValue(s1);
            cellNo++;
         }
      }

      private static void createRows(XSSFSheet sheet, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap, XSSFWorkbook wb, int rowCount) {
          Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(sheet.getSheetName());
          for (int i = 1; i <= resultSets.size(); i++) {
              Map<String, String> columnDetails = resultSets.get(Integer.valueOf(i).toString());
              XSSFRow nextrow = sheet.createRow(rowCount + i);
              Set<String> set = columnDetails.keySet();
              int cellNum = 0;
              for (String s2 : set) {
                  nextrow.createCell(cellNum).setCellValue(columnDetails.get(s2));
                  cellNum++;
              }
          }
      }
    }

