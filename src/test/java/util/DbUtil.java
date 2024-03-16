package util;

import util.listeners.TestListener;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DbUtil {
    public static  Map<String, Map<String, LinkedHashMap<String, String>>>createProcessedResultSetMap(Map<String, ResultSet> dataMap) throws SQLException {
        Map<String, Map<String, LinkedHashMap<String, String>>> multiRsMap = new LinkedHashMap<>();
        Set<String> s = dataMap.keySet();
        for (String str: s) {
            multiRsMap.put(str, processResultSet(str, dataMap.get(str)));
        }
        return multiRsMap;
    }
    public static Map<String, LinkedHashMap<String, String>> processResultSet(String name, ResultSet rs) {
        LinkedHashMap<String, String> rowDetails = new LinkedHashMap<String, String>();
        Map<String, LinkedHashMap<String, String>> resultMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        ResultSetMetaData rsm = null;
        if (rs != null) {
            try {
                int rowCount = 1;
                rsm = rs.getMetaData();
                while (rs.next()) {
                    for (int i = 1; i <= rsm.getColumnCount(); i++) {
                        rowDetails.put(rsm.getColumnName(i), rs.getString(i));
                    }
                    resultMap.put(Integer.valueOf(rowCount).toString(), rowDetails);
                    rowCount++;
                    rowDetails = new LinkedHashMap<>();
                }
            } catch (SQLException e) {
                TestListener.logExceptionDetails("Error processing sql Resultset, could not write to Excel file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}
