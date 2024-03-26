package util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DbTestDataHandler {
    Map<String, ResultSet> testDataInMap = new HashMap<>();

    public Map<String, ResultSet> getTestDataInMap() {
        return testDataInMap;
    }
    public void addDataFromMap(Map<String, ResultSet> dataMap){
        testDataInMap.putAll(dataMap);
    }
    public void addTestData(String key, ResultSet rs) {
        testDataInMap.put(key, rs);
    }
    public void clearData() {
        testDataInMap.clear();
        setTestDataInMap(testDataInMap);
    }
    public void setTestDataInMap(Map<String, ResultSet> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }
}
