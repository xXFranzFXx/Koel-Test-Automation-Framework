package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.List;


public class HomePage extends BasePage {
    RecentlyPlayedPage recentlyPlayedPage;
    AlbumsPage albumsPage;
    CurrentQueuePage currentQueuePage;
    ArtistsPage artistsPage;
    ProfilePage profilePage;

    //user avatar icon element
    @FindBy(css = "img.avatar")
    private WebElement userAvatarIcon;
    //side menu first playlist
    @FindBy(xpath = "//section[@id='playlists']/ul/li[3]/a")
    private WebElement playlistsMenuFirstPl;
    @FindBy(xpath = "//span[@id='userBadge']//span[@class='name']")
    private By userBadgeLocator;
    //first created playlist
    @FindBy(css = ".playlist:nth-child(3)")
    private By firstPlaylist;
    //locates the first playlist listed on menu after clicking "add to" button
    @FindBy(css = "#songResultsWrapper li:nth-child(5)")
    private WebElement addToPlaylistMenuSelection;
    //playlist name input field, accessed when creating a new playlist
    @FindBy(css = "input[name='name']")
    private WebElement playlistInputField;
    @FindBy(css = ".song-menu li.has-sub")
    private WebElement addToDropDown;
    @FindBy(css = "li.has-sub ul li:nth-child(7)")
    private WebElement chooseFirstPlylstFrmDD;

    //"plus" icon for creating new playlist
    @FindBy(css = ".fa-plus-circle")
    private WebElement addNewPlaylistIcon;
    //delete playlist button
    @FindBy(className = "btn-delete-playlist")
    private By deletePlaylistBtn;
    @FindBy(css = ".search-results .virtual-scroller tr:nth-child(1)  .title")
    private WebElement firstSearchSong;
    @FindBy(css = "div.success.show")
    private WebElement successNotification;
    //block containing all songs
    @FindBy(css = ".songs")
    private WebElement allSongs;
    @FindBy(css = "[data-testid='playlist-context-menu-create-simple']")
    private WebElement selectCreateNewPlaylist;
    @FindBy(xpath = "//div[@id='searchForm']/input[@type='search']")
    private WebElement searchSongInput;

    //play button used for hoverplay method
    @FindBy(css = "[data-testid='play-btn']")
    private WebElement play;

    @FindBy(xpath = "//section[@class='songs']//button[contains(.,'View All')]")
    private WebElement viewAllBtnLocator;

    @FindBy(xpath = "//section[@class=\"recent\"]//h1")
    private WebElement recentlyPlayedViewAllBtn;
    private By rPPlayedViewAllBtn = By.xpath("//section[@class=\"recent\"]//h1/button");
    //AddTo dropdown menu choice in the context menu when right-clicking a song

    //first playlist on "AddTo" dropdown menu
    @FindBy(xpath = "//nav[@class='menu song-menu']//li[@class='playlist'][1]")
    private WebElement playlistLocator;

    @FindBy(css =".btn-add-to")
    private WebElement greenAddToBtn;


    /**
     * Search results components start
     */
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//article[1]//span[@class='details']/text()")
    private WebElement searchResultSongText;
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//article[1]//span[@class='main']")
    private WebElement searchResultSongLocator;

    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//article[1]//span[@class='by text-secondary']/a/text()")
    private WebElement searchResultArtistText;
    /**
     * Search results components end
     */

    /**
     * INFO panel components start
     */
    @FindBy(css = "[data-testid='extra-panel']")
    private WebElement infoPanel;

    @FindBy(css = "#extra.text-secondary.showing")
    private WebElement infoPanelShowing;

    @FindAll({
            @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist smart']/a"),
            @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist']/a")
    })
    private List<WebElement> allPlaylists;
    @FindBy(xpath = "//footer[@id='mainFooter']//button[@data-testid='toggle-extra-panel-btn']")
    private WebElement infoButton;
    @FindBy(xpath = "//footer[@id='mainFooter']//button[@data-testid='toggle-extra-panel-btn active']")
    private WebElement infoButtonActive;
    @FindBy(xpath = "//button[contains(text(), 'Lyrics')]")
    private WebElement lyricsTab;
    @FindBy(xpath = "//button[@id='extraTabArtist']")
    private WebElement artistTab;
    @FindBy(xpath = "//button[@id=extraTabAlbum']")
    private WebElement albumTab;
    @FindBy(xpath = "//article[@class='album-info sidebar']//span[@class='cover']/a")
    private WebElement albumTabCoverFinder;
    @FindBy(xpath = "//article[@class='artist-info sidebar']//h1/span/text()")
    private WebElement albumTabCoverPlayBtnText;
    @FindBy(xpath = "//article[@class='artist-info sidebar']//h1/span/text()")
    private WebElement artistTabInfoText;
    @FindBy(xpath = "//button[@class='shuffle control']//i[@class='fa fa-random']")
    private WebElement artistTabShuffleBtn;
    @FindBy(xpath = "//p[@class='none text-secondary']/span/text()")
    private WebElement lyricsTabInfoText;
    @FindBy(xpath = "//h1[text()[normalize-space()=' Current Queue ']]")
    private WebElement currentQueueText;
    @FindBy(css = "section#extra .tabs")
    private WebElement infoPanelTabsGroupLocator;
    @FindBy(xpath = "//section[@id=\"homeWrapper\"]//div[@class='heading-wrapper']/h1")
    private WebElement welcomeMsg;
    @FindBy(css = "section.recent .text-secondary")
    private WebElement emptyListPlaceHolderText;
    @FindBy(xpath = "//span[@class='right']/a[@class='shuffle-album']")
    private WebElement rAShuffleBtn;
    @FindBy(xpath = "//span[@class='right']/a[@class='download-album']")
    private WebElement rADownloadBtn;
    @FindBy(xpath = "//span[@class='album-thumb-wrapper']//span[@class='album-thumb']")
    private WebElement playBtnBefore;
    @FindBy(xpath = "//i[@class='fa-play']")
    private WebElement hoverPlayBtnLocator;
    @FindBy(xpath = "//section[@id='playlists']//h1")
    private WebElement createNewPlaylistBtnLocator;
    @FindBy(xpath = "//section[@id='playlists']//i[@data-testid='sidebar-create-playlist-btn']")
    private WebElement createNewPlaylistBtn;
    @FindBy(xpath = "//nav[@class='menu playlist-menu']//li[@data-testid='playlist-context-menu-create-simple']")
    private WebElement contextMenuNewPlaylst;
    @FindBy(xpath = "//nav[@class='menu playlist-menu']//li[@data-testid='playlist-context-menu-create-smart']")
    private WebElement contextMenuNewSmartlst;
    @FindBy(css = "input[name='name']")
    private WebElement newPlaylistInput;
    @FindBy(xpath = "//form[@data-testid='create-smart-playlist-form']")
    private WebElement smartListModal;
    @FindBy(xpath = "//span[@class=\"value-wrapper\"]/input[@type=\"text\"]")
    private WebElement smartListCriteriaInput;
    @FindBy(xpath = "//form[@data-testid='create-smart-playlist-form']//input[@name='name']")
    private WebElement smartListFormNameInput;
    @FindBy(xpath = "//button[text()=\"Save\"]")
    private WebElement smartListSaveButton;
    @FindBy(xpath = "//article/footer/div/span[@class=\"sep text-secondary\"]")
    private WebElement rAThumbnailTitle;
    @FindBy(css = "section.recent p.text-secondary")
    private WebElement rPEmptyText;
    @FindBy(xpath = "//section[@id='playlistWrapper']//table[@class='items']/tr")
    private List<WebElement> playlistSongs;
    @FindBy(xpath = "//a[@href=\"/#!/profile\"]")
    private WebElement profilePageLink;
    private final By songTitle = By.cssSelector("section#playlistWrapper td.title");
    private final By searchResultThumbnail = By.cssSelector("section[data-testid=\"song-excerpts\"] span.cover:nth-child(1)");
    private final By lyricsTabLocator = By.id("extraTabLyrics");
    private final By lyricsTabInfo = By.cssSelector(".none span");
    private final By artisTabLocator = By.id("extraTabArtist");
    private final By artistTabInfo = By.cssSelector("[data-test='artist-info'] h1.name span");
    private final By albumTabLocator = By.id("extraTabAlbum");
    private final By albumTabInfo = By.cssSelector("main span a.control.control-play");
    private final By albumTabShuffleBtn = By.cssSelector("article[data-test=\"album-info\"] .fa-random");
    private final By currentQueueHeader = By.cssSelector("#queueWrapper .heading-wrapper h1");
    private final By currentQueueLocator = By.xpath("//h1[text()[normalize-space()=' Current Queue ']]");

    private final By badgeNameTextLocator = By.xpath("//span[@id=\"userBadge\"]//span[@class='name']");
    private final By playPauseBtn = By.xpath("//span[@title='Play or resume']//i[@class='fa fa-play']");
    private final By recentlyAddedlistItems = By.xpath("//ol[@class=\"recently-added-album-list\"][1]/li");
    private final By searchResultsGroup = By.cssSelector("#searchExcerptsWrapper .results section");
    private final By selectNewSmartList = By.cssSelector("li[data-testid=\"playlist-context-menu-create-smart\"]");
    private final String strUrl = driver.getCurrentUrl();
    private final By infoBtnLocator = RelativeLocator.with(By.tagName("button")).toLeftOf(By.id("equalizer"));
    @FindBy(xpath = "//div[@class='alertify']//nav/button[@class='ok']")
    private WebElement ok;
    /**
     * INFO panel components end
     */
    public HomePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public HomePage contextClickFirstPlDelete() {
       By delete = By.xpath("//section[@id='playlists']/ul/li[3]/nav/ul/li[2]");
       contextClick(playlistsMenuFirstPl);
       click(delete);
       return this;
    }
    public HomePage deleteAllPlaylists() {
        List<WebElement> list = allPlaylists;
        if(list.isEmpty()) return this;
        for (WebElement l: list) {
            l.click();
            Reporter.log("playlists song total " + playlistSongs.size(), true);
            if(!playlistSongs.isEmpty()){
                contextClickFirstPlDelete();
                wait.until(ExpectedConditions.elementToBeClickable(ok)).click();
            } else {
                contextClickFirstPlDelete();
            }
        }
        return this;
    }
    public HomePage hoverAddToTHenSelectPlaylist() {
     actions.moveToElement(addToDropDown).perform();
     findElement(chooseFirstPlylstFrmDD).click();
     return this;

    }
    public boolean getUserAvatar() {
        return userAvatarIcon.isEnabled();
    }


    public HomePage deletePlaylist() {
        click(deletePlaylistBtn);
        return this;
    }

    public HomePage searchSong(String song) {

        searchSongInput.clear();
        searchSongInput.sendKeys(song);
        return this;
    }

    public boolean notificationMsg() {
        findElement(successNotification);
        return successNotification.isDisplayed();
    }

    public boolean hoverPlay() throws InterruptedException {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(playPauseBtn));
        actions.moveToElement(btn).perform();
        return playBtnBefore.isEnabled();
    }


    public HomePage clickViewAllButton() {
        findElement(viewAllBtnLocator).click();
        return this;
    }
    public HomePage clickRPViewAllBtn() {
        actions.moveToElement(recentlyPlayedViewAllBtn).perform();
        click(rPPlayedViewAllBtn);

        return this;
    }


    public HomePage clickFirstSearchResult() {
        findElement(firstSearchSong).click();
        return this;
    }

    public HomePage clickGreenAddToBtn() {
       findElement(greenAddToBtn).click();
        return this;
    }
    public HomePage selectPlaylistToAddTo() {
        findElement(addToPlaylistMenuSelection).click();
        return this;
    }

    public void clickInfoButton() {
       findElement(infoButton).click();
    }
    //click the info button and check for info panel visibility, click again if invisible this verifies it disappears when clicked
    //if the first click turns on visibility, click again to make it invisible, and negative assert visibility of info panel this verifies that the info button toggles the visibility.

    public boolean checkVisibility() {
        return infoPanel.isDisplayed();
    }


    public String clickLyricsTab() {
        click(lyricsTabLocator);
        WebElement lyricsInfoText = wait.until(ExpectedConditions.presenceOfElementLocated(lyricsTabInfo));
        return lyricsInfoText.getText();
    }

    public String clickArtistTab() {
        click(artisTabLocator);
        WebElement artistInfoText = wait.until(ExpectedConditions.presenceOfElementLocated(artistTabInfo));
        return artistInfoText.getText();
    }

    public String clickAlbumTab() {
        if (isInfoPanelVisible().isDisplayed()) {
            click(albumTabLocator);
        } else {
            clickInfoButton();
        }
//        click(albumTabLocator);
        WebElement albumInfoText = wait.until(ExpectedConditions.presenceOfElementLocated(albumTabInfo));
        return albumInfoText.getText();
    }

    public HomePage checkHeaderTitle() {
        WebElement headerTitle = wait.until(ExpectedConditions.presenceOfElementLocated(currentQueueHeader));
        Assert.assertTrue(headerTitle.isDisplayed());
        return this;
    }

    public void clickAlbumTabShuffleBtn() {
        click(albumTabShuffleBtn);

    }

    public boolean checkQueueTitle() {
        String url = "https://qa.koel.app/#!/queue";
        String currentUrl = driver.getCurrentUrl();
        return url.equals(currentUrl);
    }

    public HomePage doubleClickFirstSearchResult() {
        actions.doubleClick(searchResultSongText).perform();
        return this;
    }

    public WebElement isInfoPanelVisible() {
        return findElement(infoPanelShowing);
    }
//    public HomePage waitForPanelToDisappear () {
//
//    }

    public boolean isInfoPanelTabsInvisible() {
        return wait.until(ExpectedConditions.invisibilityOf(infoPanelTabsGroupLocator));
    }

    public HomePage getSearchResultSongText() {
        searchResultSongLocator.getText();
        return this;
    }

    public HomePage getArtistTabText() {
        artistTabInfoText.getText();
        return this;
    }


    public boolean checkAlbumTabText() {
        findElement(albumTabCoverFinder);
        return albumTabCoverFinder.isDisplayed();
    }

    public void clickInfoBtnActive() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".control.text-uppercase.active"))).click();
    }

    public HomePage clickSearchResultThumbnail() {
        WebElement thumbnail = wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultThumbnail));
        thumbnail.click();
        return this;
    }

    public boolean checkForLogoutBtn() {
        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fa-sign-out")));
        return logoutButton.isDisplayed();
    }

    public String getWelcomMsg() {
        return findElement(welcomeMsg).getText();
    }

    public boolean noRecentlyPlayed() {
        return findElement(emptyListPlaceHolderText).isDisplayed();

    }

    public void clickFooterPlayBtn() {
        actions.moveToElement(play).perform();
        click(playPauseBtn);
    }

    public boolean getUserBadgeText(String name) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(badgeNameTextLocator, name));
    }

    public int searchResultsSize() {
        return findElements(searchResultsGroup).size();
    }

    public boolean searchResultsExists() {
        return (searchResultsSize() > 0);
    }

    public int recentlyPlayedListSize() {
        return findElements(By.className("recent-song-list")).size();
    }

    public boolean recentlyPlayedListExists() {
        int li = findElements(By.className("recent-song-list")).size();
        return (li > 0);
    }

    public boolean noRecentlyPlayedList() {
        String text = "Your recently played songs will be displayed here.";
        wait.until(ExpectedConditions.textToBePresentInElement(rPEmptyText, text));
        return findElement(rPEmptyText).isDisplayed();
    }

    //only checks first column since second column doesn't display correctly
    public boolean recentlyAddedListHasAlbumTitles() {
        List<WebElement> li = findElements(recentlyAddedlistItems);
        Reporter.log("Recently Added list items" + li, true);
        for (WebElement l : li) {
            Reporter.log("list item" + l, true);
            WebElement albumTitle = findElement(rAThumbnailTitle);
            Reporter.log("Checking for album titles" + albumTitle.getText(), true);
            return albumTitle.getText().equals("by");
        }
        return false;
    }
    //only checks first column since second column doesn't display correctly
    public boolean checkRAListButtonsOnHover() {
        List<WebElement> li = findElements(recentlyAddedlistItems);
        Reporter.log("Recently Added list items" + li, true);
        for (WebElement l : li) {
            Reporter.log("list item" + l, true);
            actions.moveToElement(l).perform();
            Reporter.log("Checking for buttons", true);
            if (rAShuffleBtn.isDisplayed()) {
                return rADownloadBtn.isDisplayed();
            }
            Reporter.log("both buttons present", true);
        }
        Reporter.log("Couldn't locate buttons", true);
        return false;
    }



    public String getSearchedTitleFromResults() {
        return getSearchedSongTitle();
    }

    public HomePage clickAboutLink() {
        about();
        return this;
    }

    public boolean aboutModalVisible() {
        return checkAboutModal();
    }

    public HomePage closeModalPopup() {
        closeModal();
        return this;
    }
    public boolean verifyModalClosed() {
        return modalIsClosed();
    }
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//button[@data-test='close-modal-btn']")
    private WebElement closeModalBtn;
    public HomePage closeModalAndLogOut() {
        closeModalAndLogout();
        return this;
    }

    /**
     * Side menu links actions
     */
    public void clickHome() {
        homePage();
        new HomePage(driver);
    }

    public CurrentQueuePage clickCurrentQueue() {
        currentQueuePage();
        return new CurrentQueuePage(driver);
    }
    public boolean checkSongPlaying() {
        return isSongPlaying();
    }
    public AllSongsPage clickAllSongs() {
        allSongsPage();
        return new AllSongsPage(driver);
    }
    public ProfilePage clickAvatar() {
        findElement(profilePageLink).click();
        return new ProfilePage(driver);
    }
    public AlbumsPage clickAlbums() {
        albumsPage();
        return new AlbumsPage(driver);
    }

    public ArtistsPage clickArtists() {
        artistsPage();
        return new ArtistsPage(driver);
    }

    public RecentlyPlayedPage clickRecentlyPlayed() {
        recentlyPlayedPage();
        return new RecentlyPlayedPage(driver);
    }

    public void clickFavorites() {
        favorites();
    }

    public HomePage clickCreateNewPlaylist() {
        actions.moveToElement(createNewPlaylistBtnLocator).perform();
        createNewPlaylistBtn.click();
        return this;
    }

    public HomePage contextMenuNewPlaylist() {
        findElement(contextMenuNewPlaylst);
        actions.moveToElement(contextMenuNewPlaylst).perform();
        clickElement(contextMenuNewPlaylst);
        return this;
    }

    public HomePage contextMenuNewSmartlist() {
       actions.moveToElement(contextMenuNewSmartlst).perform();
       click(selectNewSmartList);
       return this;
    }

    public HomePage enterPlaylistName(String playlist) {
        WebElement input = findElement(newPlaylistInput);
        input.sendKeys(playlist);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public boolean playlistAddedToMenu(String playlist) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class=\"playlist playlist\"]/a[text()='" + playlist + "']"))).isDisplayed();
    }

    public boolean smartListModalVisible() {
        return findElement(smartListModal).isDisplayed();
    }

    public void enterSmartListName(String smartList) {
        WebElement input = findElement(smartListFormNameInput);
        input.sendKeys("newSmartList");

    }
    public void enterSmartListCriteria(String criteria) {
        WebElement input = findElement(smartListCriteriaInput);
        input.sendKeys(criteria);


    }
    public boolean smartlistAddedToMenu(String playlist) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class=\"playlist playlist smart\"]/a[text()='" + playlist + "']"))).isDisplayed();
    }

    public void clickSaveSmartList() {
        findElement(smartListSaveButton).click();
    }
}

