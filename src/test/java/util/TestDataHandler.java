package util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TestDataHandler {
    Map<String, ResultSet> testDataInMap = new HashMap<String, ResultSet>();

    public Map<String, ResultSet> getTestDataInMap() {
        return testDataInMap;
    }
    public void addDataFromMap(Map<String, ResultSet> dataMap){
        testDataInMap.putAll(dataMap);
    }
    public void setTestDataInMap(Map<String, ResultSet> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }
}


