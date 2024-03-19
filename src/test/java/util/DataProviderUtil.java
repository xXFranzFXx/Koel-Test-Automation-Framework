package util;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviderUtil {
    @DataProvider(name="LoginData")
    public Object[][] getLoginData(Method method){
        if(method.getName().equalsIgnoreCase("loginWithWrongCredentials")) {
             return new Object[][]{
                {"", ""},
                {"", System.getProperty("koelPassword")},
                {System.getProperty("koelUser"), ""},
                {"badEmail@email.com", "badPassword"}
            };
        } else {
            return new Object[][] {
                    {System.getProperty("koelUser"), System.getProperty("koelPassword")},
            };
        }
    }
}
