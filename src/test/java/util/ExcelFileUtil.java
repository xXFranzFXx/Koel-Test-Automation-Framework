package util;

import com.beust.ah.A;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import util.listeners.TestListener;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelFileUtil {
    private static final String excelFilePath = System.getProperty("excelPath");

    //writes data in result set from sql query to an excel file
    public static void generateExcel(Map<String, ResultSet> dataMap, String fileName) throws SQLException, IOException {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);


        Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap = getResultSetMap(dataMap);
        Set<String> testNames = dataMap.keySet();
        XSSFWorkbook wb = null;
        if (file.isFile() && file.exists()) {
            FileInputStream fip = new FileInputStream(file);
            wb = new XSSFWorkbook(fip);
            System.out.println(fileName + " open");
        } else {
            wb = new XSSFWorkbook();
            System.out.println(fileName + " either does not exist" + " or can't open, creating new file");
        }
        for (String name : testNames) {
            try {

                Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(name);
                XSSFSheet sheet = makeSheet(wb, name, resultSetMap);

                for (int i = 0; i < resultSets.get("1").size(); i++) {
                    sheet.autoSizeColumn(i);
                }
                writeFile(file, wb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private static void writeWithoutDuplicates() throws SQLException, IOException {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);
        if (file.isFile() && file.exists() && duplicateRowsExist(fileName)) {
            String newName = System.getProperty("newExcelFileName");
            Reporter.log("Duplicate data exists", true);
            Reporter.log("Creating new file without duplicates: " + newName, true);
            writeToFileWithoutDuplicates(newName);
        }
    }
    private static Map<String, Map<String, LinkedHashMap<String, String>>> getResultSetMap (Map<String, ResultSet> dataMap) throws SQLException {
        return DbUtil.createProcessedResultSetMap(dataMap);
    }
    private static void writeFile (File file, XSSFWorkbook wb) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }
    private static List<String> getSheetNames(XSSFWorkbook wb) {
        List<String> sheetNames = new ArrayList<String>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            sheetNames.add(wb.getSheetName(i));
        }
        return sheetNames;
    }

    private static boolean sheetExists(XSSFWorkbook wb, String sheetName) {
        List<String> sheetNames = getSheetNames(wb);
        return sheetNames.contains(sheetName);
    }

    private static XSSFSheet makeSheet(XSSFWorkbook wb, String sheetName, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap) {

        if (sheetExists(wb, sheetName)) {
            XSSFSheet sheet = wb.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            createRows(sheet, resultSetMap, rowCount);
            return sheet;
        } else {
            XSSFSheet sheet = wb.createSheet(sheetName);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            createHeader(sheet, resultSetMap, wb, rowCount);
            createRows(sheet, resultSetMap, rowCount);
            return sheet;
        }
    }
    private static XSSFCellStyle createHeaderStyle(XSSFWorkbook wb) {
        XSSFCellStyle headerStyle = wb.createCellStyle();
        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
        headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private static void createHeader(XSSFSheet sheet, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap, XSSFWorkbook wb, int rowCount) {
        XSSFCellStyle headerStyle = createHeaderStyle(wb);
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

    private static void createRows(XSSFSheet sheet, Map<String, Map<String, LinkedHashMap<String, String>>> resultSetMap, int rowCount) {
        Map<String, LinkedHashMap<String, String>> resultSets = resultSetMap.get(sheet.getSheetName());
        Map<String, String> columnDetails = new HashMap<>();
        for (int i = 1; i <= resultSets.size(); i++) {
            columnDetails = resultSets.get(Integer.valueOf(i).toString());
            XSSFRow nextRow = sheet.createRow(rowCount + i);
            Set<String> set = columnDetails.keySet();
            int cellNum = 0;
            for (String s2 : set) {
                nextRow.createCell(cellNum).setCellValue(columnDetails.get(s2));
                cellNum++;
            }
        }
    }
   public static boolean duplicateRowsExist(String fileName) throws IOException {
       String excelFile = excelFilePath + fileName;
       FileInputStream fip = new FileInputStream(excelFile);
       XSSFWorkbook wb = new XSSFWorkbook(fip);
       List<String> workSheets = getSheetNames(wb);
       for(String worksheet: workSheets) {
           XSSFSheet ws = wb.getSheet(worksheet);
           if (getRowMap(ws).size() > checkDuplicate(ws).size()) {
               Reporter.log("There are " + (getRowMap(ws).size() - checkDuplicate(ws).size()) + " duplicate rows of data in spreadsheet " + ws.getSheetName() + ".", true);
           }
       }
              return !workSheets.stream().map(wb::getSheet).filter(s -> getRowMap(s).size() > checkDuplicate(s).size()).toList().isEmpty();
   }
   private static Map<Integer, List<String>> getRowMap (XSSFSheet spreadSheet) {
       Map<Integer, List<String>> rowMap = new HashMap<>();
       List<String> recordsSet = new ArrayList<>();
       XSSFRow row = spreadSheet.getRow(1);
       int numOfRows = spreadSheet.getPhysicalNumberOfRows();
       int numOfColumns = row.getLastCellNum();

       for (int j = 1; j < numOfRows; j++) {
           XSSFRow nextRow = spreadSheet.getRow(j);
           Iterator<Cell> cellIterator = nextRow.cellIterator();
           Cell cell;

           for (int i = 1; i <= numOfColumns; i++) {
               cell = cellIterator.next();
               if (cell.getRowIndex() == 0)
                   continue;
               if (Objects.requireNonNull(cell.getCellType()) == CellType.STRING) {
                   recordsSet.add(cell.getStringCellValue());
               } else if (cell.getCellType() == CellType.BLANK) {
                   recordsSet.add(null);
               }
               rowMap.put(j, recordsSet);
           }
           recordsSet = new ArrayList<>();
       }

       return rowMap;
   }

    private static List<List<String>> checkDuplicate(XSSFSheet spreadSheet) {
        Map<Integer, List<String>> rowMap = getRowMap(spreadSheet);
        //create a list of unique rows, so we can write to a new file without having duplicate data
        return rowMap.keySet().stream().map(rowMap::get).distinct().toList();
    }
private static Map<String, List<List<String>>> getUniqueRows(XSSFWorkbook wb) {
    List<String> sheetNames = getSheetNames(wb);
    Map<String, List<List<String>>> uniqueRowsMap = new HashMap<>();
    for(String sh : sheetNames) {
        uniqueRowsMap.put(sh, checkDuplicate(wb.getSheet(sh)));
    }
    return uniqueRowsMap;
  }
  private static void writeToFileWithoutDuplicates(String newFileName) throws IOException, SQLException {
      String newExcelFile = excelFilePath + newFileName;
      String oldExcelFile = excelFilePath + "dbResults.xlsx";

      File file = new File(newExcelFile);
      FileInputStream fip = new FileInputStream(oldExcelFile);

      XSSFWorkbook newWb = new XSSFWorkbook();
      XSSFWorkbook wb = new XSSFWorkbook(fip);

      List<String> sheetNames = getSheetNames(wb);
      Map<String, List<List<String>>> rowVals = getUniqueRows(wb);

      for (String sh: sheetNames) {
          try {
              XSSFSheet sheet = newWb.createSheet(sh);
              int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
              List<List<String>> rowData = rowVals.get(sh);
              copyRow(wb, newWb, sheet,  wb.getSheet(sh), 0, 0);

              for (int i = 1; i <= rowData.size(); i++) {
                  XSSFRow nextRow = sheet.createRow(rowCount + i);
                  List<String> singleRow = rowData.get(i-1);
                  int cellNum = 0;
                  for (String s : singleRow) {
                      nextRow.createCell(cellNum).setCellValue(s);
                      cellNum++;
                  }
                  sheet.autoSizeColumn(i-1);
              }
              writeFile(file, newWb);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
    private static void copyRow(XSSFWorkbook workbook, XSSFWorkbook newWb, XSSFSheet destWorksheet, XSSFSheet srcWorksheet, int sourceRowNum, int destinationRowNum) {
        XSSFCellStyle headerStyle = createHeaderStyle(newWb);
        XSSFRow newRow = destWorksheet.getRow(destinationRowNum);
        XSSFRow sourceRow = srcWorksheet.getRow(sourceRowNum);

        if (newRow != null) {
            destWorksheet.shiftRows(destinationRowNum, destWorksheet.getLastRowNum(), 1);
        } else {
            newRow = destWorksheet.createRow(destinationRowNum);
        }

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);
            newCell.setCellStyle(headerStyle);

            if (oldCell == null) {
                newCell = null;
                continue;
            }
            newCell.setCellType(oldCell.getCellType());

            switch (oldCell.getCellType()) {
                case CellType.BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case CellType.BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case CellType.ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case CellType.FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case CellType.NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case CellType.STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }
    }
}
