package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class AllSongsPage extends BasePage{

    @FindBy(xpath = "//h1[text()[normalize-space()='All Songs']]")
    private WebElement allSongsPageTitleLocator;
    @FindBy(css = ".all-songs tr.song-item:nth-child(1)")
    private WebElement firstSongElementLocator;
    @FindBy(css = "li.playback")
    private WebElement choosePlayAllLocator;
    @FindBy(xpath = "//nav[@id='sidebar']/section[@class='music']/ul/li[3]/a")
    private WebElement allSongsLocator;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Like')]")
    private WebElement singleLikeButton;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary']")
    private List<WebElement> likeButtons;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Like')]")
    private List<WebElement> unLikedButton;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Unlike')]")
    private List<WebElement> likedSongsButton;

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
    //unlikes every liked song
    public AllSongsPage unlikeSongs() {
        if(likedSongsButton.isEmpty()) return this;
        for(WebElement l: likedSongsButton) {
                l.click();
        }
        return this;
    }
    public AllSongsPage likeOneSong() {
        findElement(singleLikeButton).click();
        return this;
    }
    //likes every song
    public AllSongsPage likeSongs() {
        if(unLikedButton.isEmpty()) return this;
        for(WebElement l: unLikedButton) {
            l.click();
        }
        return this;
    }

    //checks if there are any songs that are marked as "liked"
    public boolean checkUnliked() {
        return likedSongsButton.isEmpty();
    }

    public void choosePlayOption() {
       choosePlayAllLocator.click();
    }

}
