package util;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviderUtil {

    @DataProvider(name="LoginData")
    public Object[][] getDataFromDataProvider(){
        return new Object[][]{
                {"demo@class.com", "te$t$tudent"},
                {"invalidemail@class.com", "te$t$tudent"},
                {"demo@class.com", "InvalidPassword"},
                {"",""}
        };
    }

    @DataProvider(name="SearchData")
    public Object[][] getSearchData(Method method){
        if(method.getName().equalsIgnoreCase("checkWhiteSpace")) {
            return new Object[][]{
                    {"chillsong"},
                    {"   chillsong"},
                    {"chillsong    "},
                    {"    chillsong    "}
            };
        } else {
            return new Object[][] {
                    {"a", "d"}
            };
        }
    }
}
