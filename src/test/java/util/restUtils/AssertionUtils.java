package util.restUtils;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.response.Response;
import util.listeners.TestListener;

import java.util.*;

public class AssertionUtils {
    public static void assertExpectedValuesWithJsonPath(Response response, Map<String, Object> expectedValuesMap) {
        List<AssertionKeys> actualValuesMap = new ArrayList<>();
        // Table headers
        actualValuesMap.add(new AssertionKeys("JSON_PATH", "EXPECTED_VALUE", "ACTUAL_VALUE", "RESULT"));
        boolean allMatched = true;
        // Iterate to extract value from response using jsonpath
        Set<String> jsonPaths =  expectedValuesMap.keySet();
        for(String jsonPath : jsonPaths) {
            Optional<Object> actualValue = Optional.ofNullable(response.jsonPath().get(jsonPath));

            if(actualValue.isPresent()) {
                Object value = actualValue.get();
                // Assert actual and expected values
                if(value.equals(expectedValuesMap.get(jsonPath))) {
                    // if value is matched then add details
                    actualValuesMap.add(new AssertionKeys(jsonPath, expectedValuesMap.get(jsonPath), value, "MATCHED ✅"));

                } else if(response.jsonPath().get(jsonPath).toString().contains(expectedValuesMap.get(jsonPath).toString())){
                    actualValuesMap.add(new AssertionKeys(jsonPath, expectedValuesMap.get(jsonPath), value, "FOUND ✅"));

                } else {
                    // if single assertion is failed then update final result as failure
                    allMatched = false;
                    actualValuesMap.add(new AssertionKeys(jsonPath, expectedValuesMap.get(jsonPath), value, "NOT_MATCHED ❌"));
                }
            }
            // if jsonpath does not exist in the response
            else {
                allMatched = false;
                actualValuesMap.add(new AssertionKeys(jsonPath, expectedValuesMap.get(jsonPath), "VALUE_NOT_FOUND", "NOT_MATCHED ❌"));
            }
        }
        // To decide final result
        if(allMatched)
            TestListener.logPassDetails("All assertions have passed. 😊😊😊😊😊");
        else
            TestListener.logFailureDetails("All assertions have not passed. 😒😒😒😒😒");

        // To log the details in a tabular format in extent report
        String[][] finalAssertionsMap = actualValuesMap.stream().map(assertions -> new String[] {assertions.getJsonPath(),
                        String.valueOf(assertions.getExpectedValue()), String.valueOf(assertions.getActualValue()), assertions.getResult()})
                .toArray(String[][] :: new);
        TestListener.test.get().info(MarkupHelper.createTable(finalAssertionsMap));
    }
}
