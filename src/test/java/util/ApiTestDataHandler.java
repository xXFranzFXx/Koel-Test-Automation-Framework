package util;

import java.util.HashMap;
import java.util.Map;

public class ApiTestDataHandler {
    Map<String, Object> testDataInMap=new HashMap<String, Object>();


    public Map<String, Object> getApiTestDataInMap() {
        return testDataInMap;
    }

    public void setApiTestDataInMap(Map<String, Object> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }


}
