package util;


import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.Reporter;
import util.listeners.TestListener;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelFileUtil {
    private static final String excelFilePath = System.getProperty("excelPath");
    private static final int sourceRow = Integer.parseInt(System.getProperty("sourceRow"));
    private static final int destinationRow = Integer.parseInt(System.getProperty("destinationRow"));
    private static FileInputStream fip;
    //writes data in result set from sql query to an excel file
    public static void generateExcel(TestDataHandler testDataHandler, String fileName) throws IOException {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);
        //create a copy of the data saved from testcases/test suite
        Map<String, ResultSet> dataMap = testDataHandler.getTestDataInMap();
        //create a map containing resultSet per test case
        Map<String, List<LinkedHashMap<String, String>>> resultSetMap = getResultSetMap(dataMap);
        Set<String> testNames = dataMap.keySet();
        XSSFWorkbook wb = null;
        //append data to existing workbook, if workbook doesn't exist create a new one
        if (file.isFile() && file.exists()) {
            fip = new FileInputStream(file);
            wb = new XSSFWorkbook(fip);
            System.out.println(fileName + " open");
        } else {
            wb = new XSSFWorkbook();
            System.out.println(fileName + " either does not exist" + " or can't open, creating new file");
        }
        
        for (String name : testNames) {
            try {
                XSSFSheet sheet = makeSheet(wb, name, resultSetMap);
                System.out.println("Finished writing to " + sheet.getSheetName());
            } catch (Exception e) {
                TestListener.logExceptionDetails("Error writing data to Excel file: " + e.getLocalizedMessage());
                e.printStackTrace();
            }

        }

        if (fip != null) {
            fip.close();
        }
        autoSizeColumns(wb);
        writeFile(file, wb);
        Reporter.log(fileName + " closed.", true);
    }

    //if duplicate data is found in existing file, create a new file without any duplicate data.
    public static void writeWithoutDuplicates(String fileName) {
        String excelFile = excelFilePath + fileName;
        File file = new File(excelFile);
        try {
            if (file.isFile() && file.exists() && duplicateRowsExist(fileName)) {
                String newName = System.getProperty("newExcelFileName");
                Reporter.log("Duplicate data exists", true);
                Reporter.log("Creating new file without duplicates: " + newName, true);
                writeToFileWithoutDuplicates(newName);
            }
        } catch (Exception e) {
            Reporter.log("Could not access duplicate data", true);
            TestListener.logExceptionDetails("Unable access duplicate data " + e.getLocalizedMessage());
        }
    }
    //format the testcase data
    private static Map<String, List<LinkedHashMap<String, String>>> getResultSetMap (Map<String, ResultSet> dataMap) {
        return DbUtil.processResultSet(dataMap);
    }
    private static void writeFile (File file, XSSFWorkbook wb) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }
    private static List<String> getSheetNames(XSSFWorkbook wb) {
      return IntStream.range(0, wb.getNumberOfSheets())
              .boxed()
              .map(wb::getSheetName).toList();
    }

    private static boolean sheetExists(XSSFWorkbook wb, String sheetName) {
        List<String> sheetNames = getSheetNames(wb);
        return sheetNames.contains(sheetName);
    }

    private static XSSFSheet makeSheet(XSSFWorkbook wb, String sheetName, Map<String, List<LinkedHashMap<String, String>>> resultSetMap) {

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

    private static void createHeader(XSSFSheet sheet, Map<String, List<LinkedHashMap<String, String>>> resultSetMap, XSSFWorkbook wb, int rowCount) {
        XSSFCellStyle headerStyle = createHeaderStyle(wb);
        XSSFRow row = sheet.createRow(rowCount);
        List<LinkedHashMap<String, String>> resultSets = resultSetMap.get(sheet.getSheetName());
        Map<String, String> columnDetails = resultSets.get(0);
        Set<String> columnNames = columnDetails.keySet();

        int cellNo = 0;
        for (String s1 : columnNames) {
            XSSFCell cell0 = row.createCell(cellNo);
            cell0.setCellStyle(headerStyle);
            cell0.setCellValue(s1);
            cellNo++;
        }
    }

    private static void createRows(XSSFSheet sheet,  Map<String, List<LinkedHashMap<String, String>>> resultSetMap, int rowCount) {
        List<LinkedHashMap<String, String>> resultSets = resultSetMap.get(sheet.getSheetName());
        Map<String, String> columnDetails = new LinkedHashMap<>();

        for (int i = 1; i <= resultSets.size(); i++) {
            columnDetails = resultSets.get(i-1);
            Set<String> s = columnDetails.keySet();
            XSSFRow nextRow = sheet.createRow(rowCount + i);

            int cellNum = 0;
            for (String str: s) {
                nextRow.createCell(cellNum).setCellValue(columnDetails.get(str));
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
              return !workSheets.stream()
                      .map(wb::getSheet)
                      .filter(s -> getRowMap(s).size() > checkDuplicate(s).size())  // duplicate rows will be greater than unique rows
                      .toList()
                      .isEmpty();
   }
   //read each row from excel sheet
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

    //create a map of unique rows, so we can write to a new file without having duplicate data
    private static Map<String, List<List<String>>> getUniqueRows(XSSFWorkbook wb) {
        List<String> sheetNames = getSheetNames(wb);
        Map<String, List<List<String>>> uniqueRowsMap = new HashMap<>();
        for(String sh : sheetNames) {
            uniqueRowsMap.put(sh, checkDuplicate(wb.getSheet(sh)));
        }
        return uniqueRowsMap;
    }
    //gather only distinct rows of data from current sheet, then delete file that contains duplicates
    private static List<List<String>> checkDuplicate(XSSFSheet spreadSheet) {
        Map<Integer, List<String>> rowMap = getRowMap(spreadSheet);
        return rowMap.keySet().stream().map(rowMap::get).distinct().toList();
    }
    private static void writeToFileWithoutDuplicates(String newFileName) throws IOException{
        String newExcelFile = excelFilePath + newFileName;
        String oldExcelFile = excelFilePath + "dbResults.xlsx";

        File file = new File(newExcelFile);
        File delFile = new File(oldExcelFile);
        FileInputStream fileInputStream = new FileInputStream(oldExcelFile);

        XSSFWorkbook newWb = new XSSFWorkbook();
        XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);

        List<String> sheetNames = getSheetNames(wb);
        Map<String, List<List<String>>> rowVals = getUniqueRows(wb);

        for (String sh: sheetNames) {
            try {
                XSSFSheet sheet = newWb.createSheet(sh);
                int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
                List<List<String>> rowData = rowVals.get(sh);
                copyRow(wb, newWb, sheet,  wb.getSheet(sh), sourceRow, destinationRow);

                for (int i = 1; i <= rowData.size(); i++) {
                    XSSFRow nextRow = sheet.createRow(rowCount + i);
                    List<String> singleRow = rowData.get(i-1);
                    int cellNum = 0;
                    for (String s : singleRow) {
                        nextRow.createCell(cellNum).setCellValue(s);
                        cellNum++;
                    }

                }
                autoSizeColumns(newWb);
                fileInputStream.close();
                writeFile(file, newWb);
                Reporter.log("Finished writing new data to " + sheet.getSheetName(), true);
            } catch (IOException e) {
                TestListener.logExceptionDetails("Error creating worksheet when editing duplicate data: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        deleteFile(delFile);
        Reporter.log("Finished copying unique data from " + oldExcelFile + " to " + newExcelFile, true);
    }

    //copy (header) row from source workbook to new workbook
    private static void copyRow(XSSFWorkbook workbook, XSSFWorkbook newWb, XSSFSheet destWorksheet, XSSFSheet srcWorksheet, int sourceRowNum, int destinationRowNum) {
        XSSFCellStyle headerStyle = createHeaderStyle(newWb);
        XSSFRow newRow = destWorksheet.getRow(destinationRowNum);
        XSSFRow sourceRow = srcWorksheet.getRow(sourceRowNum);
        try {
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
        } catch (Exception e) {
            Reporter.log("Error copying row to new file", true);
            TestListener.logExceptionDetails("Error copying row to new file: " + e.getMessage());
        }
    }
    private static void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }
    private static void deleteFile(File file) {
        if (file.delete()) {
            System.out.println("Deleted the file: " + file.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}
