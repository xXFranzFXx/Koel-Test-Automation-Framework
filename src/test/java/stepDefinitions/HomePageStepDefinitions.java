package stepDefinitions;

import base.BaseDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RecentlyPlayedPage;

public class HomePageStepDefinitions extends BaseDefinitions {
     LoginPage loginPage;
     HomePage homePage;
     ProfilePage profilePage;
     String search = "Grav";
    static String newPlaylist = "newPlaylist";
    static String newSmartList = "newSmartList";
    static String searchSong;
    static RecentlyPlayedPage recentlyPlayedPage;
    private String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    @Given("User logs in as new user")
    public void loginAsNewUser() {
        loginPage = new LoginPage(getDriver());
        loginPage.loginAsNewUser();
    }

    @Given("User is on homepage")
    public void homePage() {
        String url = getDriver().getCurrentUrl();
        if (!url.equals(homeUrl)) {
            getDriver().get(homeUrl);
        }
    }
    @And("Close browser")
    public void closeBrowserWindow() {
        closeBrowser();
    }
    @Then("User should see not see recently played songs")
    public void userShouldNotSeeRecentlyPlayedSongs() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.noRecentlyPlayedList());
    }

    @Then("User should see recently played songs")
    public void userShouldSeeRecentlyPlayedSongs() {
        recentlyPlayedPage = new RecentlyPlayedPage(getDriver());
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.recentlyPlayedListExists());

    }

    @Then("welcome message should contain user's name")
    public void welcomeMessageShouldContainUserSName() {
    }

    @Then("User should see welcome message {string}")
    public void userShouldSeeWelcomeMessage(String welcomeMsg) {
        homePage = new HomePage(getDriver());
        Assert.assertEquals(homePage.getWelcomMsg(), welcomeMsg);
    }

    @Then("User will not see same welcome message as new user")
    public void userWillNotSeeSameWelcomeMessageAsNewUser() {
        homePage = new HomePage(getDriver());
        Assert.assertNotEquals(homePage.getWelcomMsg(), "student");
    }

    @When("User plays a song")
    public void userPlaysASong() {
        homePage = new HomePage(getDriver());
        homePage.searchSong(search)
                .clickSearchResultThumbnail();
        searchSong = homePage.getSearchedTitleFromResults();
    }

    //for new user only- checks that profile badge contains text "student"
    @Then("User profile name should be {string}")
    public void userProfileNameShouldBe(String name) {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.getUserBadgeText(name));
    }

    @When("User plays a song for first time")
    public void firstPlay() {
        homePage = new HomePage(getDriver());
        homePage.clickFooterPlayBtn();
    }

    @When("User clicks View All button")
    public void userClicksViewAllButton() {
        homePage = new HomePage(getDriver());
        homePage.clickRPViewAllBtn();
    }

    @Then("User will navigate to Recently Played page")
    public void userWillNavigateToRecentlyPlayedPage() {
        Assert.assertEquals(getDriver().getCurrentUrl(), recentlyPlayedUrl);
    }

    @Given("Recently Added songlist exists")
    public void recentlyAddedSonglistExists() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.recentlyPlayedListExists());
    }

    @Then("Album name should be displayed for each song")
    public void albumNameShouldBeDisplayedForEachSong() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.recentlyAddedListHasAlbumTitles());
    }

    @And("Download and Shuffle icons will be visible on hovered song")
    public void downloadAndShuffleIconsWillBeVisibleOnHoveredSong() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.checkRAListButtonsOnHover());
    }
    @When("User clicks the Create a new playlist button next to the PLAYLISTS menu header in the side menu")
    public void userClicksTheCreateANewPlaylistButton() {
        homePage = new HomePage(getDriver());
        homePage.clickCreateNewPlaylist();
    }

    @And("User selects New Playlist in the context menu")
    public void userSelectsNewPlaylistInTheContextMenu() {
        homePage = new HomePage(getDriver());
        homePage.contextMenuNewPlaylist();
    }



//    @Then("A success notification will appear")
//    public void aSuccessNotificationWillAppear() {
//    }

    @Then("A new playlist will be listed in the side menu")
    public void aNewPlaylistWillBeListedInTheSideMenu() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.playlistAddedToMenu(newPlaylist));
    }

    @When("User clicks profile link")
    public void userClicksProfileLink() {
        homePage = new HomePage(getDriver());
        homePage.clickAvatar();
    }

    @Then("User will be on profile page")
    public void userWillBeOnProfilePage() {
        Assert.assertEquals(getDriver().getCurrentUrl(), profileUrl);
    }

    @When("User clicks About link")
    public void userClicksAboutLink() {
        homePage = new HomePage(getDriver());
        homePage.clickAboutLink();
    }

    @Then("An About Koel modal will appear")
    public void anAboutKoelModalWillAppear() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.aboutModalVisible());
    }

    @When("User clicks close")
    public void userClicksClose() {
        homePage = new HomePage(getDriver());
        homePage.closeModalAndLogOut();
    }

    @Then("The Modal will disappear")
    public void theModalWillDisappear() {
        loginPage = new LoginPage(getDriver());
       Assert.assertTrue(loginPage.getRegistrationLink());
    }



    @When("User clicks Home in the left side navigation panel")
    public void userClicksHomeInTheLeftSideNavigationPanel() {
        homePage = new HomePage(getDriver());
        homePage.clickHome();
    }

    @Then("User navigates to home page")
    public void userNavigatesToHomePage() {
        Assert.assertEquals(getCurrentUrl(), homeUrl);
    }

    @When("User clicks All Songs in the left side navigation panel")
    public void userClicksAllSongsInTheLeftSideNavigationPanel() {
        homePage = new HomePage(getDriver());
        homePage.clickAllSongs();
    }

    @Then("User navigates to All Songs page")
    public void userNavigatesToAllSongsPage() {
        Assert.assertEquals(getCurrentUrl(), allSongsUrl);
    }

    @When("User clicks Albums in the left side navigation panel")
    public void userClicksAlbumsInTheLeftSideNavigationPanel() {

        homePage = new HomePage(getDriver());
        homePage.clickAlbums();
    }

    @Then("User navigates to Albums page")
    public void userNavigatesToAlbumsPage() {
        Assert.assertEquals(getCurrentUrl(), albumsUrl);
    }

    @When("User clicks Artists in the left side navigation panel")
    public void userClicksArtistsInTheLeftSideNavigationPanel() {
        homePage = new HomePage(getDriver());
        homePage.clickArtists();
    }

    @Then("User navigates to Artists page")
    public void userNavigatesToArtistsPage() {
        Assert.assertEquals(getCurrentUrl(), artistUrl);
    }

    @When("User executes a search")
    public void userExecutesASearch() {
        homePage = new HomePage(getDriver());
        homePage.searchSong(search);
    }

    @Then("Search results will be displayed")
    public void searchResultsWillBeDisplayed() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.searchResultsExists());
    }



    @And("User selects New Smart Playlist in the context menu")
    public void userSelectsNewSmartPlaylistInTheContextMenu() {
        homePage = new HomePage(getDriver());
        homePage.contextMenuNewSmartlist();
    }

    @Then("A New Smart Playlist modal will appear")
    public void aNewSmartPlaylistModalWillAppear() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.smartListModalVisible());
    }

    @When("User enters smart playlist name in the Name input field")
    public void userEntersSmartPlaylistNameInTheNameInputField() {
        homePage = new HomePage(getDriver());
        homePage.enterSmartListName(newSmartList);
    }

    @And("User enters song criteria in the criteria input field")
    public void userEntersSongCriteriaInTheCriteriaInputField() {
        homePage = new HomePage(getDriver());
        homePage.enterSmartListCriteria("my criteria");
    }

    @And("User clicks Save button")
    public void userClicksSaveButton() {
        homePage = new HomePage(getDriver());
        homePage.clickSaveSmartList();
    }

//    @Then("A success notification appears")
//    public void aSuccessNotificationAppears() {
//    }

    @Then("A new smart list will be in the side menu")
    public void aNewSmartListWillBeInTheSideMenu() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.smartlistAddedToMenu(newSmartList));
    }

    @When("User clicks Recently Played in the left side navigation panel")
    public void userClicksRecentlyPlayedInTheLeftSideNavigationPanel() {
        homePage = new HomePage(getDriver());
        homePage.clickRecentlyPlayed();
    }

    @When("User clicks Favorites in the left side navigation panel")
    public void userClicksFavoritesInTheLeftSideNavigationPanel() {
        homePage = new HomePage(getDriver());
        homePage.clickFavorites();
    }

    @Then("User will navigate to Favorites page")
    public void userWillNavigateToFavoritesPage() {
        Assert.assertEquals(getCurrentUrl(), favoritesUrl);
    }

    @And("User enters playlist name in the input field followed by pushing ENTER key")
    public void userEntersPlaylistName() {
        homePage = new HomePage(getDriver());
        homePage.enterPlaylistName(newPlaylist);
    }
    @When("User clicks on logout link")
    public void userClicksOnLogoutLink() {
        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.checkForLogoutBtn());

        homePage.clickLogoutButton();
    }

    @Then("User will be logged out and navigate to login screen")
    public void userWillBeLoggedOutAndNavigateToLoginScreen() {
        loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.getRegistrationLink());
    }


}