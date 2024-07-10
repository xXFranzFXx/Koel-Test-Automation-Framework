package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import java.util.List;

public class FavoritesPage extends BasePage{
    @FindBy(xpath = "//section[@id='favoritesWrapper']//td[@class='favorite']//button[@class='text-secondary' and contains(@title, 'Unlike')]")
    private List<WebElement> likedHeartIcon;
    @FindBy(xpath = "//nav[@data-testid='song-context-menu']//li[@class='download']")
    private WebElement contextMenuDownload;
    @FindBy(xpath = "//section[@id='favoritesWrapper']//table[@class='items']/tr")
    private List<WebElement> favoritesSongList;
    @FindBy(xpath = "//section[@id='favoritesWrapper']//i[@class='fa fa-frown-o']")
    private WebElement frownIcon;
    public FavoritesPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public int getLikedSongsCount() {
        return likedHeartIcon.size();
    }
    public FavoritesPage unlikeAllSongs()  throws StaleElementReferenceException {
        try {
            if(likedHeartIcon.isEmpty()) return this;
            likedHeartIcon.forEach(WebElement::click);
            return this;
        } catch (StaleElementReferenceException e) {
            Reporter.log("Error removing songs from favorites playlist:  " + e, true);
            unlikeAllSongs();
        }
        return this;
    }
    public FavoritesPage contextClickFirstSong() {
        contextClick(favoritesSongList.get(0));
        return this;
    }
    public void selectDownloadFromCM() {
        WebElement selection = locateByVisibility(contextMenuDownload);
        actions.moveToElement(selection).click().perform();
        pause(1);
        actions.sendKeys(Keys.ENTER).perform();
    }
    public boolean checkPlaylistEmptyIcon() {
        List<WebElement> icon = findElements(By.xpath("//section[@id='favoritesWrapper']//i[@class='fa fa-frown-o']"));
        return !icon.isEmpty();
    }
    public boolean isFavoritesEmpty() {
        return favoritesSongList.isEmpty();
    }
}
