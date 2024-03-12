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
        FileInputStream fip = new FileInputStream(file);

        Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap = DbUtil.createProcessedResultSetMap(dataMap);
        Set<String> testNames = dataMap.keySet();
        XSSFWorkbook wb = null;
        if (file.isFile() && file.exists()) {
            wb = new XSSFWorkbook(fip);
            System.out.println(fileName + ".xlsx open");
        } else {
            wb = new XSSFWorkbook();
            System.out.println(fileName + ".xlsx either not exist" + " or can't open");
        }
        for (String name : testNames) {
            try {

                Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(name);
                XSSFSheet sheet = makeSheet(wb, name, resultSetMap);

                for (int i = 0; i < resultSets.get("1").size(); i++) {
                    sheet.autoSizeColumn(i);
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
    public static void checkDuplicate(XSSFSheet spreadSheet) {
        Set<String> recordsSet =new TreeSet<>();
        XSSFRow row;
        for (Row cells : spreadSheet) {
            row = (XSSFRow) cells;
            //System.out.println("----->"+spreadsheet.get);
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell.getRowIndex() == 0)
                    continue;
                if (Objects.requireNonNull(cell.getCellType()) == CellType.STRING) {
                    recordsSet.add(cell.getStringCellValue());
                }
            }
        }

    }
}

