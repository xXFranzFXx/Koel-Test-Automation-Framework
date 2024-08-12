package util;

import java.util.HashMap;
import java.util.Map;

public class TestDataHandler {
    Map<String, Object> testDataInMap = new HashMap<String, Object>();
    public Map<String, Object> getTestDataInMap() {
        return testDataInMap;
    }
    public void setTestDataInMap(Map<String, Object> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }
}


