package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class AllSongsPage extends BasePage{

    @FindBy(xpath = "//h1[text()[normalize-space()='All Songs']]")
    private WebElement allSongsPageTitleLocator;
    @FindBy(css = ".all-songs tr.song-item:nth-child(1)")
    private WebElement firstSongElementLocator;
    @FindBy(css = "li.playback")
    private WebElement choosePlayAllLocator;

    @FindBy(xpath = "//nav[@id='sidebar']/section[@class='music']/ul/li[3]/a")
    private WebElement allSongsLocator;

    public AllSongsPage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public boolean checkSongPlaying() {
        return isSongPlaying();
    }
    public AllSongsPage navigateToAllSongs() {
        allSongsLocator.click();
        return this;
    }
    public AllSongsPage checkHeaderTitle() {
        findElement(allSongsPageTitleLocator);
        Assert.assertTrue(allSongsPageTitleLocator.isDisplayed());
        return this;
    }
    public AllSongsPage contextClickFirstSong() {
        contextClick(firstSongElementLocator);
        return this;
    }

    public void choosePlayOption() {
       choosePlayAllLocator.click();
    }

}
