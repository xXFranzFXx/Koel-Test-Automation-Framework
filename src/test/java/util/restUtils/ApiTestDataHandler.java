package util.restUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiTestDataHandler {
    Map<String, Object> testDataInMap=new HashMap<String, Object>();

    public String createPayload(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject.toString();
    }
    public void clearApiTestData() {
        testDataInMap.clear();
        setApiTestDataInMap(testDataInMap);
    }
    public Map<String, Object> getApiTestDataInMap() {
        return testDataInMap;
    }
    public void addApiTestData(String key, Object obj) {
        testDataInMap.put(key, obj);
        setApiTestDataInMap(testDataInMap);
    }
    public String getApiTestValue(String key) {
        Object value = testDataInMap.get(key);
        return value.toString();
    }
    public void setApiTestDataInMap(Map<String, Object> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }



}