package util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public static Map<String, LinkedHashMap<String, String>> processResultSet(String name, ResultSet rs){

        LinkedHashMap<String, String> rowDetails = new LinkedHashMap<String, String>();
        Map<String, LinkedHashMap<String, String>> resultMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        ResultSetMetaData rsm = null;
        if (rs != null)
        {
            try
            {
                rsm = rs.getMetaData();
                ArrayList<String> columnNames = new ArrayList<String>();
                for (int i = 1; i <= rsm.getColumnCount(); i++)
                {
                    System.out.println(i + " -> " + rsm.getColumnName(i));
                    columnNames.add(rsm.getColumnName(i));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            int rowCount = 1;
            while (rs.next())
            {
                for (int i = 1; i <= rsm.getColumnCount(); i++)
                {
                    rowDetails.put(rsm.getColumnName(i), rs.getString(i));
                }
                resultMap.put(Integer.valueOf(rowCount).toString(), rowDetails);
                rowCount++;
                rowDetails = new LinkedHashMap<>();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultMap;
    }
}
