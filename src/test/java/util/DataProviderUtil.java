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
    @DataProvider(name="PlaylistData")
    public Object[][] playlistData(Method method){
        if(method.getName().equalsIgnoreCase("createOneCharacterPlaylistName")) {
            return new Object[][]{
                    {"a"}
            };
        } else {
            return new Object[][] {
                    {"playlist"}
            };
        }
    }
    @DataProvider(name="ApiData")
    public Object[][] apiData(Method method){
        if(method.getName().equalsIgnoreCase("increasePlayCount") || method.getName().equalsIgnoreCase("getSongExtraInfo")) {
            return new Object[][]{
                    {"06cd19b77127f1e7f889ecad54376b30"},
                    {"08116cdc269c9f19964369e4bb1ab343"},
                    {"0aedf0c1c02188e8b73483d1bf2eacab"},
                    {"0b794968a26cd03bb533762affc8c0ca"},
                    {"0c3d784a530e74f9e5dbcad9c2711cd2"}
            };
        } else {
            return new Object[][] {
                    {"Airbit"}
            };
        }
    }
}
