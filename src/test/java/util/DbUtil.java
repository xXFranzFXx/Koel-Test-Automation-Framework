package util;

import util.listeners.TestListener;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DbUtil {
    public static Map<String, List<LinkedHashMap<String, String>>> processResultSet(Map<String, ResultSet> dataMap) {
        Map<String, List<LinkedHashMap<String, String>>> resultListMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> rowDetails = new LinkedHashMap<>();
        List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
        Set<String> s = dataMap.keySet();
        ResultSet rs = null;
        ResultSetMetaData rsm = null;

            for (String str : s) {
                rs = dataMap.get(str);

                try {
                    //Total number of rows in each resultset
                    int numRows = getRowCount(rs);
                    //bring the iterator back to the start
                    rs.beforeFirst();
                    rsm = rs.getMetaData();

                    while (rs.next()) {
                        for (int j = 1; j<= numRows; j++) {
                            rowDetails = new LinkedHashMap<>();
                            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                                rowDetails.put(rsm.getColumnName(i), rs.getString(i));
                            }
                        }

                        resultList.add(rowDetails);
                        resultListMap.put(str, resultList);
                    }
                    //must reset this to avoid saving the row data from first ResultSet as each testCase Resultsets row data.
                    resultList = new ArrayList<>();

                } catch (SQLException e) {
                    TestListener.logExceptionDetails("Error processing sql Resultset, could not write to Excel file: " + e.getMessage());
                    e.printStackTrace();
                }
        }

        return resultListMap;
    }
    private static int getRowCount(ResultSet resultSet) throws SQLException {
        resultSet.last();
        return resultSet.getRow();
    }
}

