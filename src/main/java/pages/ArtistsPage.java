package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ArtistsPage extends BasePage{
    @FindBy(xpath = "//li[text()='Play All']")
    private WebElement playAllLocator;
    @FindBy(css = "li[data-test='shuffle']")
    private WebElement shuffleLocator;
    @FindBy(xpath = "//img[@alt='Sound bars']")
    private WebElement soundBarLocator;
    @FindBy(xpath = "//nav[@id='sidebar']/section[1]/ul/li[5]/a")
    private By artistsSideMenuLocator;
    @FindBy(xpath = "//h1[text()[normalize-space()='Artists']]")
    private WebElement artistsPageTitleLocator;
    //finds the link to any album on the artists page
    @FindBy(xpath = "//section[@id='artistsWrapper']/div/article[1]/span/span/a")
    private WebElement firstArtistAlbumLocator;
    @FindBy(css = ".artists .thumbnail-wrapper .cover > a")
    private List<WebElement> artistPlayBtns;
    @FindBy(css = "#artistsWrapper article.item.full")
    private List<WebElement> artistTiles;
    public ArtistsPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public boolean checkHeaderTitle() {
        return artistsPageTitleLocator.isDisplayed();
    }
    public boolean soundbarIsDisplayed() {
        return soundBarLocator.isDisplayed();
    }
    public ArtistsPage rightClickAlbum() {
        contextClick(firstArtistAlbumLocator);
        return this;
    }
    public void selectPlayAll() {
        findElement(playAllLocator);
        playAllLocator.click();
    }
    public boolean artistsArePresent() {
        return !artistTiles.isEmpty();
    }
    public void invokeSearchFromKeybd() {
        wait.until(ExpectedConditions.urlContains("https://qa.koel.app/#!"));
        actions.sendKeys("f").perform();
    }
    public ArtistsPage searchArtist(String artist) {
        invokeSearchFromKeybd();
        actions.pause(2).sendKeys(artist).perform();
        return this;
    }
    public ArtistsPage clickArtistPlay() {
        findElement(artistPlayBtns.get(0)).click();
        return this;
    }
    public boolean songIsPlaying() {
        return findElement(soundBarLocator).isDisplayed();
    }
    public List<String> getArtistsNames() {
        List<String> names = new ArrayList<>();
        pause(4);
        for(WebElement artist: artistTiles) {
            String artistName = findElement(artist).getAttribute("title");
            names.add(artistName);
        }
        //return a single string instead of List
        //return names.stream().map(String::valueOf).collect(Collectors.joining(", ", "(", ")"));
        return names;
    }
}
