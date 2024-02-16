package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.List;
import java.util.NoSuchElementException;


public class HomePage extends BasePage {
    RecentlyPlayedPage recentlyPlayedPage;
    AlbumsPage albumsPage;
    CurrentQueuePage currentQueuePage;
    ArtistsPage artistsPage;
    ProfilePage profilePage;

    //user avatar icon element
    @CacheLookup
    @FindBy(css = "img.avatar")
    private WebElement userAvatarIcon;
    //side menu first playlist
    @FindBy(xpath = "//section[@id='playlists']/ul/li[3]/a")
    private WebElement playlistsMenuFirstPl;
    //locates the first playlist listed on menu after clicking "add to" button
    @FindBy(css = "#songResultsWrapper li:nth-child(5)")
    private WebElement addToPlaylistMenuSelection;
    //playlist name input field, accessed when creating a new playlist
    @FindBy(css = ".song-menu li.has-sub")
    private WebElement addToDropDown;
    @FindBy(css = "li.has-sub ul li:nth-child(7)")
    private WebElement chooseFirstPlylstFrmDD;

    //delete playlist button
    @FindBy(className = "btn-delete-playlist")
    private By deletePlaylistBtn;
    @FindBy(css = ".search-results .virtual-scroller tr:nth-child(1)  .title")
    private WebElement firstSearchSong;
    @FindBy(css = "div.success.show")
    private WebElement successNotification;
    //block containing all songs
    @FindBy(xpath = "//div[@id='searchForm']/input[@type='search']")
    private WebElement searchSongInput;

    //play button used for hoverplay method
    @FindBy(css = "span[data-testid='play-btn']")
    private WebElement play;

    @FindBy(xpath = "//section[@class='songs']//button[contains(.,'View All')]")
    private WebElement viewAllBtnLocator;

    @FindBy(xpath = "//section[@class='recent']//h1")
    private WebElement recentlyPlayedViewAllBtn;
    private By rPPlayedViewAllBtn = By.xpath("//section[@class='recent']//h1/button");
    //AddTo dropdown menu choice in the context menu when right-clicking a song

    @FindBy(xpath = "//section[@id='songResultsWrapper']//button[@class='btn-add-to']")
    private WebElement greenAddToBtn;

    /**
     * Search results components start
     */
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//article[1]//span[@class='details']/text()")
    private WebElement searchResultSongText;
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//article[1]//span[@class='main']")
    private WebElement searchResultSongLocator;

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
    @FindBy(xpath = "//section[@id='playlists']/ul/li")
    private List<WebElement> playlistsSection;
    @FindAll({
            @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist smart']/a"),
            @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist']/a")
    })
    private List<WebElement> allPlaylists;
    @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist']/a")
    private List<WebElement> regularPlaylists;
    @FindBy(xpath = "//section[@id='playlists']/ul/li[@class='playlist playlist smart']/a")
    private List<WebElement> smartLists;
    @FindBy(xpath = "//footer[@id='mainFooter']//button[@data-testid='toggle-extra-panel-btn']")
    private WebElement infoButton;
    @FindBy(xpath = "//article[@class='album-info sidebar']//span[@class='cover']/a")
    private WebElement albumTabCoverFinder;

    @FindBy(xpath = "//article[@class='artist-info sidebar']//h1/span/text()")
    private WebElement artistTabInfoText;
    @FindBy(css = "section#extra .tabs")
    private WebElement infoPanelTabsGroupLocator;
    @FindBy(xpath = "//section[@id='homeWrapper']//div[@class='heading-wrapper']/h1")
    private WebElement welcomeMsg;
    @FindBy(css = "section.recent .text-secondary")
    private WebElement emptyListPlaceHolderText;
    @FindBy(xpath = "//span[@class='right']/a[@class='shuffle-album']")
    private WebElement rAShuffleBtn;
    @FindBy(xpath = "//span[@class='right']/a[@class='download-album']")
    private WebElement rADownloadBtn;
    @FindBy(xpath = "//span[@class='album-thumb-wrapper']//span[@class='album-thumb']")
    private WebElement playBtnBefore;
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
    @FindBy(css = ".rule-group .btn-add-rule .fa.fa-plus")
    private WebElement smartListAddRuleBtn;
    @FindBy(css = ".btn-add-group .fa.fa-plus")
    private WebElement smartListAddGroupBtn;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='text']")
    private WebElement smartListCriteriaInput;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='text']")
    private List<WebElement> smartListCriteriaInputGroup;
    @FindBy(xpath = "//form[@data-testid='create-smart-playlist-form']//input[@name='name']")
    private WebElement smartListFormNameInput;
    @FindBy(xpath = "//button[text()='Save']")
    private WebElement smartListSaveButton;
    @FindBy(xpath = "//article/footer/div/span[@class='sep text-secondary']")
    private WebElement rAThumbnailTitle;
    @FindBy(css = "section.recent p.text-secondary")
    private WebElement rPEmptyText;
    @FindBy(xpath = "//a[@href='/#!/profile']")
    private WebElement profilePageLink;
    @FindBy(xpath = "//section[@id='playlists']/ul/li[3]/nav/ul/li[2]")
    private WebElement plDeleteBtn;
    @FindBy(css = "#playlists li.smart a")
    private List<WebElement> sideMenuSmartPlaylistName;
    @FindBy(xpath = "//form[@data-testid='edit-smart-playlist-form']//input[@name='name']")
    private WebElement editSmartListFormNameInput;
    @FindBy(xpath = "//button[text()='Cancel']")
    private WebElement smartListCancelButton;
    @FindBy(xpath="//section[@id='playlists']//ul/li[@class='playlist playlist smart']//li[text()[contains(.,'Edit')]]")
    private WebElement plEditBtn;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='number']")
    private WebElement smartListCriteriaIntInput;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='number']")
    private List<WebElement> smartListCriteriaIntInputGrp;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='date']")
    private WebElement smartListCriteriaDateInput;
    @FindBy(xpath = "//div[@class='alertify']//nav/button[@class='ok']")
    private WebElement ok;
    @FindBy(xpath = "//span[@class='value-wrapper']/input[@type='text']")
    private WebElement smartListCriteriaTextInput;
    @FindBy(xpath = "//div[@data-test='smart-playlist-rule-row']//select[@name='model[]']")
    private WebElement smartListModelSelectMenu;
    @FindBy(xpath = "//div[@data-test='smart-playlist-rule-row']//select[@name='operator[]']")
    private WebElement smartListOperatorSelectMenu;
    @FindBy(css ="#playlistWrapper .items")
    private List<WebElement> smartPlaylistSongs;
    @FindBy(css = ".fa.fa-file-o")
    private WebElement emptySmartlistIcon;
    @FindBy(css = "#playlistWrapper .text")
    private WebElement emptySmartListText;

    private final By searchResultThumbnail = By.cssSelector("section[data-testid='song-excerpts'] span.cover:nth-child(1)");
    private final By lyricsTabLocator = By.id("extraTabLyrics");
    private final By lyricsTabInfo = By.cssSelector(".none span");
    private final By artisTabLocator = By.id("extraTabArtist");
    private final By artistTabInfo = By.cssSelector("[data-test='artist-info'] h1.name span");
    private final By albumTabLocator = By.id("extraTabAlbum");
    private final By albumTabInfo = By.cssSelector("main span a.control.control-play");
    private final By albumTabShuffleBtn = By.cssSelector("article[data-test='album-info'] .fa-random");
    private final By currentQueueHeader = By.cssSelector("#queueWrapper .heading-wrapper h1");
    private final By badgeNameTextLocator = By.xpath("//span[@id='userBadge']//span[@class='name']");
    private final By playPauseBtn = By.xpath("//span[@title='Play or resume']//i[@class='fa fa-play']");
    private final By recentlyAddedlistItems = By.xpath("//ol[@class='recently-added-album-list'][1]/li");
    private final By searchResultsGroup = By.cssSelector("#searchExcerptsWrapper .results section");
    private final By selectNewSmartList = By.cssSelector("li[data-testid='playlist-context-menu-create-smart']");
    private By playlistDelete = By.xpath("//section[@id='playlists']/ul/li[3]/nav/ul/li[2]");
    private By cmDelete= By.xpath("//li[text()[contains(.,'Delete')]]");

    private By okBtn = By.xpath( "//div[@class='alertify']//nav/button[@class='ok']");

    /**
     * INFO panel components end
     */
    public HomePage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public HomePage deleteRegularPlaylistWithSong(String playlistName) {
       WebElement regularPl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='playlist playlist']/a[text()='" + playlistName + "']")));
       contextClick(regularPl);
       pause(2);
       click(cmDelete);
        wait.until(ExpectedConditions.elementToBeClickable(findElement(ok)));
        actions.moveToElement(ok).click().perform();
       return this;
    }
    public HomePage contextClickFirstPlDelete() {
            wait.until(ExpectedConditions.elementToBeClickable(playlistsMenuFirstPl));
            contextClick(playlistsMenuFirstPl);
            click(playlistDelete);
            actions.sendKeys(Keys.ENTER).perform();
            return this;
    }
    public void deleteAllPlaylists() {
            for (WebElement l : allPlaylists) {
                try {
                    findElement(l).click();
                    contextClickFirstPlDelete();
                    pause(2);
                    actions.sendKeys(Keys.ENTER).perform();


                    Reporter.log("Deleted playlist: " + l, true);
                } catch (NoSuchElementException e) {
                    Reporter.log("cannot delete playlist" + e, true);
                }
                if(playlistsEmpty()) {
                        Reporter.log("There are currently no playlists to delete", true);
                        return;
                }
            }
        Reporter.log("Total Playlists remaining: " + allPlaylists.size(), true);
    }
    public HomePage deletePlaylists() {
        int count = 0;
        try {
            System.out.println(playlistsSection.size());
            for (int i = 2; i < playlistsSection.size(); i ++) {
                WebElement plSection = wait.until(ExpectedConditions.elementToBeClickable(playlistsSection.get(i)));
                actions.moveToElement(plSection).pause(1).perform();
                contextClick(plSection);
//                contextClick(playlistsSection.get(i));
                actions.moveToElement(plDeleteBtn).click().pause(2).perform();
                count++;
                checkOkModal();
            }
        } catch (StaleElementReferenceException e) {
            e.printStackTrace();
        }
        return this;
    }
    public boolean playlistsEmpty() {
        return (playlistsSection.size() == 2);
    }

    public boolean getUserAvatar() {
        return userAvatarIcon.isEnabled();
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


    public void checkOkModal() {
        List<WebElement> ele2 = driver.findElements(okBtn);
        if (!ele2.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(findElement(ok)));
            actions.moveToElement(ok).click().pause(2).perform();
        } else {
            pause(2);
            deletePlaylists();
        }
    }
    public HomePage clickFirstSearchResult() {
        findElement(firstSearchSong).click();
        return this;
    }

    public HomePage clickGreenAddToBtn() {
        wait.until(ExpectedConditions.visibilityOf(greenAddToBtn)).click();
        return this;
    }
    public HomePage selectPlaylistToAddTo(String playlist) {
        WebElement menuChoice = find(By.xpath("//section[@id='songResultsWrapper']//li[@class='playlist' and text()[contains(.,"+playlist+")]]"));
        findElement(menuChoice).click();
        return this;
    }

    public void clickInfoButton() {
       findElement(infoButton).click();
    }

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
        WebElement albumInfoText = wait.until(ExpectedConditions.presenceOfElementLocated(albumTabInfo));
        return albumInfoText.getText();
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
    public String getSearchInputValidationMsg() {
        return findElement(newPlaylistInput).getAttribute("validationMessage");
    }


    public WebElement isInfoPanelVisible() {
        return findElement(infoPanelShowing);
    }

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

    public HomePage enterSmartListName(String smartList) {
        WebElement input = findElement(smartListFormNameInput);
        input.sendKeys(smartList);
        return this;
    }
    public HomePage enterSmartListCriteria(String criteria) {
        WebElement input = findElement(smartListCriteriaInput);
        input.sendKeys(criteria);
        return this;
    }

    public HomePage enterSmartListTextCriteria(String criteria) {
        WebElement input = findElement(smartListCriteriaTextInput);
        input.sendKeys(criteria);
        return this;
    }
    public HomePage enterSmartListIntCriteria(int integer) {
        WebElement input = findElement(smartListCriteriaIntInput);
        input.sendKeys(String.valueOf(integer));
        return this;
    }
    public boolean smartlistAddedToMenu (String playlist){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='playlist playlist smart']/a[text()='" + playlist + "']"))).isDisplayed();
    }
    public HomePage clickSaveSmartList() {
        findElement(smartListSaveButton).click();
        pause(2);
        return this;
    }
    public HomePage clickGroupRuleBtn() {
        findElement(smartListAddGroupBtn).click();
        return this;
    }
    public HomePage enterGroupRulesText(String[] text){
        for (int i = 0; i < text.length; i ++) {
            smartListCriteriaInputGroup.get(i).sendKeys(text[i]);
        }
        return this;
    }
    public HomePage enterIntCriteria(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            smartListCriteriaIntInputGrp.get(i).sendKeys(String.valueOf(ints[i]));
        }
        return this;
    }
    public HomePage clickSmartListCancelBtn() {
        findElement(smartListCancelButton).click();
        return this;
    }
    public HomePage cmEditFirstSmartPl() {
        contextClick(sideMenuSmartPlaylistName.get(0));
        findElement(plEditBtn).click();
        return this;
    }

    public String getFirstSmartPlName() {
        return sideMenuSmartPlaylistName.get(0).getText();
    }
    public HomePage editSmartPlName(String playlist){
        if(!isEditModalVisible()) pause(2);
        WebElement input = findElement(editSmartListFormNameInput);
        actions.moveToElement(input).doubleClick(input).perform();
        input.sendKeys(Keys.SPACE);
        pause(2);
        input.sendKeys(playlist);
        return this;
    }
    public boolean isEditModalVisible() {
        WebElement editModal = find(By.cssSelector(".smart-playlist-form form"));
        return  editModal.isDisplayed();
    }
    public HomePage selectOperatorOption(String option){
        Select operatorSelectMenu = new Select(smartListOperatorSelectMenu);
        operatorSelectMenu.selectByVisibleText(option);
        return this;
    }
    public HomePage selectModelOption(String option){
        Select modelSelectMenu = new Select(smartListModelSelectMenu);
        modelSelectMenu.selectByVisibleText(option);
        return this;
    }
    public boolean checkSmartListEmpty() {
        return smartPlaylistSongs.isEmpty();
    }
    public HomePage enterSmartListDateCriteria(String date) {
        smartListCriteriaDateInput.sendKeys(date);
        return this;
    }
    public boolean checkEmptySmartListIcon() {
        return emptySmartlistIcon.isDisplayed();
    }
    public String emptySmartListMessage() {
        return emptySmartListText.getText();
    }
    public String getNameInputValidationMsg() {
        return findElement(smartListFormNameInput).getAttribute("validationMessage");
    }
}

