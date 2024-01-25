package util;

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
    public Map<String, Object> getApiTestDataInMap() {
        return testDataInMap;
    }
    public void setApiTestDataInMap(Map<String, Object> testDataInMap) {
        this.testDataInMap = testDataInMap;
    }


}
