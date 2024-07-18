package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchPage;
import util.DataProviderUtil;
import util.listeners.TestListener;

/**
 * Story:
 * As a user, I want to perform a search to find an existing song
 * Acceptance Criteria:
 * After searching for existing songs, results if matched should be populated on the Search results page.
 * Results should be displayed on each section: Songs, Artist, Album. Also, data should be displayed in each section, no matter what we are looking for: a song, artist or album.
 * Show empty list if searched song\artist\album doesn't exist in the Koel app with the message "no results".
 * Ignore trailing\heading white spaces (Examples: "  chill song " should be searched as "chill song" or "  chill song", "chill song ").
 * BUG >>Search should be case sensitive
 * User can clear the search query with keyboard and 'x' button. Search results should be cleared on the search field and the search page in each section: song, artist or album.
 */
public class SearchTests extends BaseTest {
    HomePage homePage;
    LoginPage loginPage;
    SearchPage searchPage;
    public void setupSearch() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
        searchPage = new SearchPage(getDriver());
        loginPage.loginValidCredentials();
    }

    @Test(description = "Verify info is displayed in Songs, Artists, Albums section when searching")
    @Parameters({"searchString"})
    public void checkSearchResults(String searchString) {
        setupSearch();
        homePage.searchSong(searchString);
        TestListener.logInfoDetails("Search input: " + searchString);
        TestListener.logAssertionDetails("Results for songs are empty: " + searchPage.isSearchResultEmpty("song"));
        TestListener.logAssertionDetails("Results for artist are empty: " + searchPage.isSearchResultEmpty("artist"));
        TestListener.logAssertionDetails("Results for album are empty: " + searchPage.isSearchResultEmpty("album"));
        Assert.assertFalse(searchPage.isSearchResultEmpty("song"));
        Assert.assertFalse(searchPage.isSearchResultEmpty("artist"));
        Assert.assertFalse(searchPage.isSearchResultEmpty("album"));
    }
    @Test(description = "Verify no info is displayed when search returns no matches")
    @Parameters({"searchString2"})
    public void checkEmptyResults(String searchString2) {
        setupSearch();
        homePage.searchSong(searchString2);
        TestListener.logInfoDetails("Search input: " + searchString2);

        TestListener.logAssertionDetails("Results for songs are empty: " + searchPage.noneFoundTextExists("song"));
        TestListener.logAssertionDetails("Results for artist are empty: " + searchPage.noneFoundTextExists("artist"));
        TestListener.logAssertionDetails("Results for album are empty: " + searchPage.noneFoundTextExists("album"));

        Assert.assertTrue(searchPage.noneFoundTextExists("song"));
        Assert.assertTrue(searchPage.noneFoundTextExists("artist"));
        Assert.assertTrue(searchPage.noneFoundTextExists("album"));
    }
    @Test(description="Test 'x' button in search field: type a letter, click 'x', then enter a different letter", dataProvider = "SearchData", dataProviderClass = DataProviderUtil.class)
    public void cancelButton(String firstSearch, String secondSearch) {
        setupSearch();
        homePage.searchSong(firstSearch);
        searchPage.clickCancelBtn();
        homePage.searchSong(secondSearch);
        String searchText = searchPage.getSearchResultHeaderText();
        TestListener.logInfoDetails("Search string used before cancelling: " + firstSearch);
        TestListener.logRsDetails("Search string that appears on search page results after clicking cancel button: " + searchText);
        Assert.assertEquals(secondSearch, searchText);
    }
    @Test(description="Use keyboard to clear the search field: type a letter, clear input by keyboard, then enter a different letter",  dataProvider = "SearchData", dataProviderClass = DataProviderUtil.class)
    public void keyboardClear(String firstSearch, String secondSearch) {
        setupSearch();
        homePage.searchSong(firstSearch);
        searchPage.useKeyBoardClear();
        homePage.searchSong(secondSearch);
        String searchText = searchPage.getSearchResultHeaderText();
        TestListener.logInfoDetails("Search string used before cancelling: " + firstSearch);
        TestListener.logRsDetails("Search string that appears on search page results after clearing by keyboard: " + searchText);
        Assert.assertEquals(secondSearch, searchText);
    }
    @Test(description = "Verify trailing/heading whitespaces are ignored for search input", dataProvider = "SearchData", dataProviderClass = DataProviderUtil.class)
    public void checkWhiteSpace(String str) {
        setupSearch();
        homePage.searchSong(str);
        String searchText = searchPage.getSearchResultHeaderText();
        TestListener.logInfoDetails("Search string: " + str);
        TestListener.logRsDetails("Search string that appears on search page results: " + searchText);
        Assert.assertEquals(str.trim(), searchText);
    }
    @Test(description = "Verify search page can be invoked with keyboard shortcut, by pressing 'F' key")
    public void invokeSearchByKeybd() {
        setupSearch();
        TestListener.logInfoDetails("URL before invoking search feature: " + getDriver().getCurrentUrl());
        searchPage.invokeSearchFromKeybd();
        String expectedUrl = "https://qa.koel.app/#!/search";
        TestListener.logRsDetails("URL after invoking search feature: " + getDriver().getCurrentUrl());
        Assert.assertEquals(expectedUrl, getDriver().getCurrentUrl());
    }
}
